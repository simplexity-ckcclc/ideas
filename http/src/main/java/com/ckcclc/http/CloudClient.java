package com.ckcclc.http;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by admin on 2017/2/16.
 */
public class CloudClient {

    private static final Logger logger = LoggerFactory.getLogger(CloudClient.class);

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientManager.getHttpClient();

        HttpPost httpPost = new HttpPost("https://devinfo.tplinkcloud.com");
        JSONObject reqBody = new JSONObject()
                .put("method", "login")
                .put("params", new JSONObject()
                        .put("cloudUserName", "huangyucong@tp-link.net")
                        .put("cloudPassword", "141421")
                        .put("terminalUUID", UUID.randomUUID().toString())
                        .put("appType", "test"));

        StringEntity entity = new StringEntity(reqBody.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        try {
            // execute
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // get statusCode & body
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (logger.isTraceEnabled()) {
                logger.trace("Response for get token: [statusCode={}, body={}]", statusCode, body);
            }

            if (statusCode == HttpStatus.SC_OK) {
                // success request
                JSONObject bodyJSON = new JSONObject(body);
                int errorCode = bodyJSON.getInt("error_code");
                if (0 == errorCode) {
                    String token = bodyJSON.getJSONObject("result").getString("token");
                    logger.info("Get token: {}", token);
                }
            } else {
                logger.warn("Failed to get token: [statusCode={}, body={}]", statusCode, body);
            }
        } catch (IOException e) {
            logger.error("Failed to execute http request", e);
        }


    }

}
