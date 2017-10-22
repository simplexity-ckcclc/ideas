package com.ckcclc.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ckcclc on 22/10/2017.
 */

@Configuration
public class TestConfig {

    private static final Logger logger  = LoggerFactory.getLogger(TestConfig.class);

//    @Value("${file.system.client.path}")
//    private String clientPath;

//    @Bean
//    public FileSystemService getFileSystemService() {
//        logger.info("init file system service, clientPath:{}", clientPath);
//        return new FastDfsFileSystemService(clientPath);
//    }


}
