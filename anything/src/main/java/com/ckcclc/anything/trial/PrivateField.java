package com.ckcclc.anything.trial;

import java.lang.reflect.Field;

/**
 * Created by ckcclc on 02/09/2017.
 */
public class PrivateField {

    public static void main(String[] args) throws Exception {
        MyObject object = new MyObject(5);
        Field field = MyObject.class.getDeclaredField("number");
        field.setAccessible(true);
        System.out.println(field.get(object));
        field.set(object, 10);
        System.out.println(field.get(object));
    }

}


class MyObject {
    private int number;

    public MyObject(int number) {
        this.number = number;
    }

}
