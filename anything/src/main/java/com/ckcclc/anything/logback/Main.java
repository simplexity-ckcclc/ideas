/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-19
 */

package com.ckcclc.anything.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log();
    }

    private static void log() {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }
}
