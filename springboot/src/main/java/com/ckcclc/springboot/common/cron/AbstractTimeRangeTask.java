/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.common.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstractTimeRangeTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTimeRangeTask .class);

    private Date start;
    private Date end;
    private Listener listener;

    AbstractTimeRangeTask(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        if (!isExecutable()) {
            listener.onUnExecutable();
            return;
        }

        execute();
        listener.onExecute();
    }

    abstract void execute();

    private boolean isExecutable() {
        Date now = new Date();
        logger.debug("NOW:[{}], START:[{}], END:[{}]", now, start, end);
        return now.after(start) && now.before(end);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }
}
