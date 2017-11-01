package com.ckcclc.http;

import com.google.common.base.Charsets;
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

/**
 * Created by admin on 2017/2/16.
 */
public class CloudClient {

    private static final Logger logger = LoggerFactory.getLogger(CloudClient.class);

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientManager.getHttpClient();

        HttpPost httpPost = new HttpPost("http://127.0.0.1:8667/senseface/test");
        JSONObject reqBody = new JSONObject()
                .put("name", new String("中文".getBytes(), Charsets.UTF_8))
                .put("age", 1);

        StringEntity entity = new StringEntity(reqBody.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        try {
            // execute
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // get statusCode & body
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            logger.info("Response for get token: [statusCode={}, body={}]", statusCode, body);
        } catch (IOException e) {
            logger.error("Failed to execute http request", e);
        }


    }

}
