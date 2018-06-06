/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-29
 */

package com.ckcclc.anything.async;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
//        futureGetBlock();
//        futureThrowException();
//        futureGetLoop();
//        concurrentSet();
        asyncFuture();
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

    // exception throw in future task will be caught in Future::get
    private static void futureThrowException() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(() -> {throw new RuntimeException("Test Exception");});
        try {
            if (future.get(1, TimeUnit.SECONDS)) {
                logger.info("succeed");
            }
        } catch (Exception e) {
            logger.error("exception caught", e);
        }

        logger.info("finished");
    }

    // foreach loop block in future.get(). when async task done, will return async task thread to pool
    private static void futureGetLoop() throws Exception {
        int concurrency = 3;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(concurrency - 1);
        logger.debug("current time before async:{}", new Date());
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < concurrency; i++) {
            int finalI = i;
            Future<Integer> future = executor.submit(() -> {
                logger.debug("current time before async:{}, id:{}", new Date(), finalI);
                TimeUnit.SECONDS.sleep(3 - finalI);
//                TimeUnit.SECONDS.sleep(finalI + 1);
                logger.debug("before get, active count:{}, id:{}", executor.getActiveCount(), finalI);
                logger.debug("before get, task count:{}, id:{}", executor.getTaskCount(), finalI);
                logger.debug("before get, completed task count:{}, id:{}", executor.getCompletedTaskCount(), finalI);
                return finalI;
            });
            futures.add(future);
        }

        futures.forEach(future -> {
            try {
                int id = (int) future.get();
                logger.debug("current time after async:{}, id:{}", new Date(), id);
                logger.debug("after get, active count:{}, id:{}", executor.getActiveCount(), id);
                logger.debug("after get, task count:{}, id:{}", executor.getTaskCount(), id);
                logger.debug("after get, completed task count:{}, id:{}", executor.getCompletedTaskCount(), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        logger.debug("current time after async:{}", new Date());
    }

    // ConcurrentHashSet can avoid java.util.ConcurrentModificationException
    private static void concurrentSet() throws InterruptedException {
        int concurrency = 1024;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        int failure = 0;
        for (int round = 0; round < 100; round++) {
//            Set<Integer> successUserIds = Sets.newConcurrentHashSet();
            Set<Integer> successUserIds = Sets.newHashSet();
            long start = new Date().getTime();
            for (int i = 0; i < concurrency; i++) {
                int finalI = i;
                executor.submit(() -> {
                    TimeUnit.MILLISECONDS.sleep(1L);
                    successUserIds.add(finalI);
                    return finalI;
                });
            }
    //        Set<Integer> copy = new HashSet<>(successUserIds);
    //        Integer[] array = copy.toArray(new Integer[copy.size()]);
            Optional<Integer[]> array = Optional.empty();
            try {
                array = Optional.of(successUserIds.toArray(new Integer[successUserIds.size()]));
            } catch (Exception e) {
                logger.warn("Exception caught:{}", e);
                failure++;
            }
//            System.out.println("end:" + new Date().getTime());
            long end = new Date().getTime();
            logger.info(">>>>>>>>>>>> Round {}", round);
            logger.info("time elapsed im ms: {}", end - start);
            logger.info("array size: {}", (array.map(integers -> integers.length).orElse(0)));
            logger.info("failure: {}", failure);
            TimeUnit.SECONDS.sleep(3);
        }
    }

    private static void asyncFuture() throws InterruptedException {
        int count = 100;
        CountDownLatch latch = new CountDownLatch(count);
        Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
        for (int i = 0; i < count; i++) {
            int finalI = i;
            CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("index:{}", finalI);
                latch.countDown();
                return finalI;
            }, executor);
        }

        latch.await();
    }

}
