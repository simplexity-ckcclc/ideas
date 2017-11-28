/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017/2/28
 */

package com.ckcclc.anything.json;

import com.google.gson.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

public class JsonUtil {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        Map<String, Serializable> data = new HashMap();
        data.put("key", "value");
        data.put("list", (Serializable) list);
        data.put("int", 1);

        MalMessage message = MalMessage.builder().msgLevel(MsgLevel.HIGH).msgType(MsgType.COMMON).data(data).build();

        String json = JsonUtil.toJson(message);
        System.out.println(json);

//        Map<String, Serializable> map = JsonUtil.fromJson(com.ckcclc.anything.json, Map.class);
//        System.out.println(map);
//        System.out.println(map);
//        for (Map.Entry entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }

        MalMessage msg = JsonUtil.fromJson(json, MalMessage.class);
        System.out.println(msg);


    }



    public static class MsgSerializer implements JsonSerializer<MalMessage> {

        @Override
        public JsonElement serialize(final MalMessage message, final Type typeOfSrc, final JsonSerializationContext context) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("msgLevel", message.getMsgLevel().getValue());
            jsonObject.addProperty("msgType", String.valueOf(message.getMsgType()));

            final JsonElement data = context.serialize(message.getData());
            jsonObject.add("dataMap", data);

//            final JsonArray jsonAuthorsArray = new JsonArray();
//            Map<String, Serializable> data = message.getData();
//            for (final String author : message.getAuthors()) {
//                final JsonPrimitive jsonAuthor = new JsonPrimitive(author);
//                jsonAuthorsArray.add(jsonAuthor);
//            }
//            jsonObject.add("authors", jsonAuthorsArray);

            return jsonObject;
        }
    }

    public static class MsgDeserializer implements JsonDeserializer {

        @Override
        public MalMessage deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();

            final MalMessage msg = MalMessage.builder()
                    .msgLevel(MsgLevel.fromValue(jsonObject.get("msgLevel").getAsString()))
                    .data(toMap(jsonObject.getAsJsonObject("dataMap")))
                    .build();

//            final Author author = new Author();
//            author.setId(jsonObject.get("id").getAsInt());
//            author.setName(jsonObject.get("name").getAsString());
//            return author;

            return msg;
        }
    }

    /**
     * convert java bean Seriable to com.ckcclc.anything.json string
     * @param object java bean
     * @return
     */
    public static String toJson(Object object) {
        Gson gson = new GsonBuilder().registerTypeAdapter(MalMessage.class, new MsgSerializer()).create();
        if (object == null) return null;
        try {
            if (object instanceof JsonElement) return object.toString();
            return gson.toJson(object);
        } catch (Exception e) {
            // TODO
        }
        return null;
//        return new Gson().toJson(obj);
    }

    /**
     * convert com.ckcclc.anything.json string to com.ckcclc.anything.json bean
     * @param json com.ckcclc.anything.json string
     * @param classOfT Class of com.ckcclc.anything.json bean
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        Gson gson = new GsonBuilder().registerTypeAdapter(MalMessage.class, new MsgDeserializer()).create();
        return gson.fromJson(json, classOfT);
//        return new Gson().fromJson(com.ckcclc.anything.json, classOfT);
    }

    public static Map toMap(Object object) {
        String json = toJson(object);
        return fromJson(json, Map.class);
    }


}
