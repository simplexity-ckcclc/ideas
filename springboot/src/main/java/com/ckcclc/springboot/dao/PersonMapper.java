package com.ckcclc.springboot.dao;

import com.ckcclc.springboot.entity.Person;

import org.apache.ibatis.annotations.Param;


/**
 * Created by ckcclc on 22/10/2017.
 */
public interface PersonMapper {

    Person findByName(@Param("name") String name);
}
