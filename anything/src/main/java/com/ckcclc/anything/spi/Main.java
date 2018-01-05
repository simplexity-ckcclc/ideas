/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-14
 */

package com.ckcclc.anything.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.util.ServiceLoader;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SocketException {
        load();
    }

    private static void load() {

        ServiceLoader<Hello> loaders = ServiceLoader.load(Hello.class);
        for(Hello instance : loaders) {
            instance.sayHello();
        }
    }
}
