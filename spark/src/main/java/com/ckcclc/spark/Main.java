package com.ckcclc.spark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.notFound;

/**
 * Created by admin on 2017/1/18.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {


        before("/hello", new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                logger.info("request : {}", request.raw().getMethod());
            }
        });

//        get("/hello", (request, response) -> "Hello World!");
        get("/hello", new Route() {
            public String handle(Request request, Response response) throws Exception {
                return "helloworld";
            }
        });

        get("/download", new Route() {
            public OutputStream handle(Request request, Response response) throws Exception {
                String fileName = "aaa.txt";
                response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                response.type("application/force-download");

                FileInputStream inputStream = new FileInputStream("D:\\aaa.txt");
                OutputStream outputStream = new ByteArrayOutputStream();

                IOUtils.copy(inputStream, outputStream);
                return outputStream;
            }
        });

//        get("/preview-batch/:id", new Route() {
        get("/preview-batch", new Route() {
            public OutputStream handle(Request request, Response response) throws Exception {

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
            }
        });


        notFound(new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                response.type("application/com.ckcclc.anything.json");
                return "{\"message\":\"Custom 404\"}";
            }
        });

    }

}
