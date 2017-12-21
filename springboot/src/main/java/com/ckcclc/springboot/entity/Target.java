package com.ckcclc.springboot.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by ckcclc on 21/10/2017.
 */
public class Target {

    @ApiModelProperty(notes = "target name")
    private String name;
    @ApiModelProperty(notes = "target age")
    private int age;
    @ApiModelProperty(notes = "target person properties")
    private Person person;


    public Target() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
