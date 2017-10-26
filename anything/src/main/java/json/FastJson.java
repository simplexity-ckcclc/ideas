package json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by ckcclc on 26/10/2017.
 */
public class FastJson {
    public static void main(String[] args) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("a", "b");
        data.put("c", 1);

        Test test = new Test(12, "foo", data);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("age", test.getAge());
        jsonObject.put("name", test.getName());
        String jsonString = JSON.toJSONString(data);
        jsonObject.put("value", jsonString);
        String js = jsonObject.toJSONString();
        System.out.println(js);

        JSONObject jo = JSON.parseObject(js);
        System.out.println(jo.get("name"));
        JSONObject jom = jo.getJSONObject("value");
        System.out.println(jom.get("a"));

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
