/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017/2/23
 */

package com.ckcclc.springboot.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "/device")
public class DownloadController {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download() throws IOException {

        File file=new File("D:\\aaa.txt");
        String fileName=new String(file.getName().getBytes("utf-8"),"ISO-8859-1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName );
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

//    @RequestMapping(value = "/preview")
//    public StreamingResponseBody preview(@RequestParam(value = "book[]", required = false) String[] books) {
//        for (String book : books) {
//            logger.debug("url param book:{}", book);
//        }
//        logger.debug("url param book.size={}", books.length);
//        return new StreamingResponseBody() {
//            @Override
//            public void writeTo(OutputStream outputStream) throws IOException {
//                FileInputStream inputStream = new FileInputStream("D:\\aaa.txt");
//                IOUtils.copy(inputStream, outputStream);
//            }
//        };
//    }

}
