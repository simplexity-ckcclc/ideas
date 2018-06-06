package com.ckcclc.javaspark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import spark.utils.IOUtils;

import static spark.Spark.*;

/**
 * Created by admin on 2017/1/18.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {


        before("/hello", (request, response) -> logger.info("request : {}", request.raw().getMethod()));

        get("/hello", (request, response) -> "helloworld");

        post("/log", ((request, response) -> {
            logger.info("[Receive] {}", request.body());
            return null;
        }));

        get("/download", (request, response) -> {
            String fileName = "aaa.txt";
            response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.type("application/force-download");

            FileInputStream inputStream = new FileInputStream("D:\\aaa.txt");
            OutputStream outputStream = new ByteArrayOutputStream();

            IOUtils.copy(inputStream, outputStream);
            return outputStream;
        });

        get("/preview-batch", (request, response) -> {

//                logger.debug("url param id:{}", request.params(":id"));
            logger.debug("url param name:{}", request.queryParams("name"));
            String[] books = request.queryParamsValues("book[]");
            for (String book : books) {
                logger.debug("url param book:{}", book);
            }
            logger.debug("url param book.size={}", books.length);

            FileInputStream inputStream = new FileInputStream("D:\\aaa.txt");
            OutputStream outputStream = new ByteArrayOutputStream();

//                try {
//                    response.raw().getOutputStream().write(.);
//                    raw.getOutputStream().flush();
//                    raw.getOutputStream().close();
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//                return raw;

            IOUtils.copy(inputStream, outputStream);
            return outputStream;
        });


        notFound((request, response) -> {
            response.type("application/com.ckcclc.anything.json");
            return "{\"message\":\"Custom 404\"}";
        });

    }

}
