/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-28
 */

package com.ckcclc.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrontabTask {

    private static final Logger logger = LoggerFactory.getLogger(CrontabTask.class);

    @Scheduled(cron = "${tick.crontab:*/1 * * * * *}")
    public void tick() {
        logger.debug("Tick!");
    }

}
