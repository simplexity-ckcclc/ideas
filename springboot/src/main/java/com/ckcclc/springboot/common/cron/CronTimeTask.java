/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.common.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class CronTimeTask extends AbstractTimeRangeTask {

    private static final Logger logger = LoggerFactory.getLogger(CronTimeTask.class);

    public CronTimeTask(Date start, Date end) {
        super(start, end);
    }

    @Override
    void execute() {
        logger.info("Cron time task execute, current time millis:{}", System.currentTimeMillis());
    }
}
