package com.ckcclc.anything.json;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ckcclc on 26/10/2017.
 */
public class FastJson {

    public static final Logger logger = LoggerFactory.getLogger(FastJson.class);
    public static void main(String[] args) {
//        Map<String, Object> data = Maps.newHashMap();
//        data.put("a", "b");
//        data.put("c", 1);
//
//        Test test = new Test(12, "foo", data);

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("age", test.getAge());
//        jsonObject.put("name", test.getName());
//        String jsonString = JSON.toJSONString(data);
//        jsonObject.put("value", jsonString);
//        String js = jsonObject.toJSONString();
//        logger.info("String {}", js);
//        System.out.println(js);
//
//        JSONObject jo = JSON.parseObject(js);
//        System.out.println(jo.get("name"));
//        JSONObject jom = jo.getJSONObject("value");
//        System.out.println(jom.get("a"));

        testList();
    }


    private static void testList() {
        Map<String, Object> data = Maps.newHashMap();
        data.put("a", "b");
        data.put("c", 1);

        Test test = new Test(12, "foo", data);
        Test test2 = new Test(13, "foo", data);
        Test test3 = new Test(14, "foo", data);

        ListEntity entity = new ListEntity();
        entity.setTests(Arrays.asList(test, test2, test3));
        entity.setRemark("for test");

        System.out.println(JSON.toJSONString(entity));
    }


}


class ListEntity {
    private String remark;
    private List<Test> tests;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}


class Test {
    private int age;
    private String name;
    private Object possession;

    public Test() {
    }

    Test(int age, String name, Object possession) {
        this.age = age;
        this.name = name;
        this.possession = possession;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPossession() {
        return possession;
    }

    public void setPossession(Object possession) {
        this.possession = possession;
    }
}
