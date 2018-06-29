package com.ckcclc.jersey;


import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 2018/6/29
 */
public class JerseyServer {

    private static final URI BASE_URI = URI.create("http://localhost:8080/base/");
    public static final String ROOT_PATH = "helloworld";

    public static void main(String[] args) {
        try {
            System.out.println("\"Hello World\" Jersey Example App");

            final ResourceConfig resourceConfig = new ResourceConfig(HelloWorldResource.class);
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig, false, null);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop()));
            server.start();

            System.out.println(String.format("Application started.\nTry out %s%s\nStop the application using CTRL+C",
                    BASE_URI, ROOT_PATH));
            Thread.currentThread().join();
        } catch (IOException | InterruptedException ex) {

        }
    }
}
