/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.sense.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;

public class ExampleTask extends AbstractTimeRangeTask {

    private static final Logger logger = LoggerFactory.getLogger(ExampleTask.class);

    public ExampleTask(Date start, Date end) {
        super(start, end);
    }

    @Override
    void execute() {
        logger.info("Cron time task execute, current time:{}", LocalDateTime.now());
    }
}
