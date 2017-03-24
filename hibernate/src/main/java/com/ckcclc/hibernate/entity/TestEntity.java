/**
 * Author:  ckcclc <wchuang5900@163.com>
 * Created: 2017/3/23
 */

package com.ckcclc.hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name = "test",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "age"}))
public class TestEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 16, nullable = false)
    private String name = null;

    @Column(name = "age", length = 11, nullable = false)
    private Integer age = null;

    @Column(name = "city", length = 16, nullable = false)
    private String city = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
