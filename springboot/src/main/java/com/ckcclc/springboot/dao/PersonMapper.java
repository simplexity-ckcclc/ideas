package com.ckcclc.springboot.dao;

import com.ckcclc.springboot.entity.Person;

import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created by ckcclc on 22/10/2017.
 */
public interface PersonMapper {

    Person findByName(@Param("name") String name);

    void insertPersons(@Param("name") String name, @Param("country") String country, @Param("ages")List<Integer> ages);
}
