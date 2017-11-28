/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-10-30
 */

package com.ckcclc.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    private static final Logger logger = LoggerFactory.getLogger(RetryService.class);

    @Retryable(value = RemoteAccessException.class, maxAttempts = 4, backoff = @Backoff(maxDelay = 1000L, random = true))
    public void remoteCall(String value) {
        logger.info("remote call, value:{}, time={}", value, System.nanoTime());
        throw new RemoteAccessException("not connected!");
    }

    @Recover
    public void recover(RemoteAccessException e) {
        logger.info("cannot connect, time={}", System.nanoTime());
    }
}
