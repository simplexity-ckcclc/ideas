package com.ckcclc.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
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
        String serverUrl = "http://127.0.0.1:15080/log/uploadDeviceLog" +
                "?deviceToken=1ea939db55014b28b4fdd4d653716990&deviceId=deviceid&deviceMac=mac" +
                "&tcsp=1.2&deviceModel=model&deviceHwVer=hwVer&fwId=fwId&fwVer=fwVer";

        URI url = new URIBuilder(serverUrl)
                .build();

        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder postEntity = MultipartEntityBuilder.create();

        postEntity.addBinaryBody("test", new File("D:\\aaa.txt"), ContentType.APPLICATION_OCTET_STREAM, "test");

        HttpEntity reqEntity = postEntity.build();
        httpPost.setEntity(reqEntity);

        // execute the request
        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();

        logger.debug("Send request to service server: {}", httpPost);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        logger.debug("Receive response from service server: {}", httpResponse);
    }




}
