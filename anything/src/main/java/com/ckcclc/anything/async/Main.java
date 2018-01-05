/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-29
 */

package com.ckcclc.anything.async;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        futureGetBlock();
    }

    private static void futureGetBlock() throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        logger.debug("current time before async:{}", new Date());
        Future<String> future = executor.submit(() -> {
            TimeUnit.SECONDS.sleep(5);
            return "Finished!";
        });
        future.get();
        logger.debug("current time after async:{}", new Date());
    }

    private static void concurrentHashMap() {
        Map<String, Object> map = Maps.newConcurrentMap();
    }

}
