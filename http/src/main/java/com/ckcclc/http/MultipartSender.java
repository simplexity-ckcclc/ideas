package com.ckcclc.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;

public class MultipartSender {

    public static final Logger logger = LoggerFactory.getLogger(MultipartSender.class);

    public static void main(String[] args) throws Exception {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");

        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.conn", "DEBUG");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl.client", "DEBUG");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.client", "DEBUG");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");

        send();
    }

    public static void send() throws Exception {
//        String serverUrl = "http://127.0.0.1:15080/log/uploadDeviceLog" +
//                "?deviceToken=1ea939db55014b28b4fdd4d653716990&deviceId=deviceid&deviceMac=mac" +
//                "&tcsp=1.2&deviceModel=model&deviceHwVer=hwVer&fwId=fwId&fwVer=fwVer";
        String serverUrl = "http://172.18.0.109:8667/senseface/targets";

        URI url = new URIBuilder(serverUrl).build();

        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder postEntity = MultipartEntityBuilder.create();

        postEntity.addPart("file", new FileBody(new File("/home/sensetime/Pictures/test.jpg"),
                ContentType.MULTIPART_FORM_DATA, "police.jpg"));

        JSONObject person = new JSONObject()
                .put("name", "foo")
                .put("targetLibId", 14)
                .put("identityId", "123456200010010010");
        postEntity.addPart("target", new StringBody(person.toString(), ContentType.APPLICATION_JSON));

        HttpEntity reqEntity = postEntity.build();
        httpPost.setEntity(reqEntity);

        // execute the request
        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();

        logger.debug("Send request to service server: {}", httpPost);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        logger.debug("Receive response from service server: {}", httpResponse);
    }




}
