package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;


@Testcontainers
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@ContextConfiguration(initializers = {DatabaseTest.Initializer.class})
@Sql(scripts = {"/data.sql"})
public class DatabaseTest {

    @Container
    public static final MySQLContainer databaseContainer = new MySQLContainer<>("mysql:5.7")
        // These values correspond with the values in application-test.properties
        .withDatabaseName("test_riker_db")
        .withUsername("brungus")
        .withPassword("brungus");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + databaseContainer.getJdbcUrl(),
                "spring.datasource.username=" + databaseContainer.getUsername(),
                "spring.datasource.password=" + databaseContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
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
