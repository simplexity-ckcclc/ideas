/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.common.cron;

import java.util.Date;

public abstract class AbstractTimeRangeTask implements Runnable {

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
        }

        execute();
        listener.onExecute();
    }

    abstract void execute();

    private boolean isExecutable() {
        Date now = new Date();
        return now.after(start) && now.before(end);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
