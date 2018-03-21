/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-2-26
 */

package com.ckcclc.anything.trial;

import java.util.Optional;

public class OptionalTest {

    public static void main(String[] args) {
        School school = new School(Optional.empty());
//        School school = new School(); // NullPointerException
        Optional<School> schoolOptional = Optional.of(school);
        String name = schoolOptional
                .flatMap(School::getMyClass)
                .flatMap(MyClass::getStudent)
                .map(Student::getName)
                .orElse("unknown");
        System.out.println(schoolOptional.flatMap(School::getMyClass));
        System.out.println(schoolOptional.map(School::getMyClass));
        System.out.println(name);
    }

}

class School {
//    private MyClass myClass;
    private Optional<MyClass> myClass;

    public School() {
    }

    public School(Optional<MyClass> myClass) {
        this.myClass = myClass;
    }

    public Optional<MyClass> getMyClass() {
        return myClass;
    }
}

class MyClass {
//    private Student student;
    private Optional<Student> student;

    public Optional<Student> getStudent() {
        return student;
    }
}

class Student {
    private String name;

    public String getName() {
        return name;
    }
}
