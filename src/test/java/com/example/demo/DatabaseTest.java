package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;


@Testcontainers
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@Sql(scripts = {"/data.sql"})
public class DatabaseTest {

    @Container
    static final MySQLContainer<?> databaseContainer = new MySQLContainer<>("mysql:5.7")
        .withUsername("fungus")
        .withPassword("brungus");

    @DynamicPropertySource
    static void mySQLProperties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
            registry.add("spring.datasource.username", databaseContainer::getUsername);
            registry.add("spring.datasource.password", databaseContainer::getPassword);
    }

    @Autowired
    ThingMapper thingMapper;

    @Test
    void test() {
        List<Thing> actual = thingMapper.findAllThings();
        List<Thing> expected = List.of(thing(1, "foo"), thing(2, "bar"), thing(3, "baz"));

        Assertions.assertEquals(expected, actual);
    }

    private Thing thing(int id, String name) {
        Thing thing = new Thing();
        thing.setId(id);
        thing.setName(name);
        return thing;
    }
}
