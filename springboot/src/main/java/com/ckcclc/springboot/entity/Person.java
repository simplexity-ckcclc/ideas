package com.ckcclc.springboot.entity;

/**
 * Created by ckcclc on 22/10/2017.
 */
public class Person {

    private String name;
    private String country;
    private int age;

    public Person() {
    }

    public Person(String name, String country, int age) {
        this.name = name;
        this.country = country;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
