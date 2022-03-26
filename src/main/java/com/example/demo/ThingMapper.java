package com.example.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThingMapper {
    @Select("SELECT id, name FROM thing;")
    public List<Thing> findAllThings();
}
