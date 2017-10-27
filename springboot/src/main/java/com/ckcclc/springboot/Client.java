package com.ckcclc.springboot;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by admin on 2017/1/18.
 */
public class Client {

    public static void main(String[] args) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/com.ckcclc.anything.json");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);

        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> responseEntity = rest.exchange("http://localhost:10080/example", HttpMethod.GET, requestEntity, String.class);
        System.out.println(responseEntity.getBody());
    }
}
