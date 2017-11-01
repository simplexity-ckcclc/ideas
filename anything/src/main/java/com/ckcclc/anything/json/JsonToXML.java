/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-10-30
 */

package com.ckcclc.anything.json;

import org.json.JSONObject;
import org.json.XML;

public class JsonToXML {

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("age", 12);
        jsonObject.put("name", "foo");
        JSONObject value = new JSONObject();
        value.put("city", "bar");
        jsonObject.put("value", value);

        System.out.println(jsonObject.toString());

        System.out.println(XML.toString(jsonObject));

        xml();
    }

    public static void xml() {
        JSONObject json = new JSONObject("{name: JSON, integer: 1, double: 2.0, boolean: true, nested: { id: 42 }, array: [1, 2, 3]}");

        String xml = XML.toString(json);
        System.out.println(xml);
    }
}
