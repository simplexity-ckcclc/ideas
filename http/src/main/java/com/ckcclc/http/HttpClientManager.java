package com.ckcclc.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by admin on 2017/2/16.
 */
public class HttpClientManager {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientManager.class);

    private static final int MAX_TOTAL = 100;

    /* timeout configs */
    private static final int CONNECTION_REQUEST_TIMEOUT = 2000;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int SOCKET_TIMEOUT = 10000;

    private static volatile CloseableHttpClient httpClient;

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HttpClientManager.class) {
                if (httpClient == null) {
                    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                    cm.setMaxTotal(MAX_TOTAL);
                    cm.setDefaultMaxPerRoute(cm.getMaxTotal());

                    RequestConfig defaultRequestConfig = RequestConfig.custom()
                            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                            .setConnectTimeout(CONNECT_TIMEOUT)
                            .setSocketTimeout(SOCKET_TIMEOUT)
                            .build();

                    CloseableHttpClient client = HttpClients.custom()
//                            .setSSLSocketFactory(createSSLConnSocketFactory())
                            .setConnectionManager(cm)
                            .setDefaultRequestConfig(defaultRequestConfig)
                            .build();

                    httpClient = client;
                }
            }
        }

        return httpClient;
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }


}
