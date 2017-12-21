/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017/3/9
 */

package com.ckcclc.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class Person {

    // position has precedence over name
    @CsvBindByPosition(position = 0)
    @CsvBindByName(column = "姓名")
    private String name;

    @CsvBindByPosition(position = 1)
    @CsvBindByName(column = "年龄")
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
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
}
