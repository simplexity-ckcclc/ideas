/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.sense.task.handler;

import com.ckcclc.springboot.sense.core.Listener;
import com.ckcclc.springboot.sense.core.TaskFutureHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class ExampleTaskListener implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(ExampleTaskListener.class);

    private String taskId;
    private TaskFutureHolder futureHolder;
    private Date evictTime;
    private ScheduledFuture future;

    public ExampleTaskListener(String taskId, TaskFutureHolder futureHolder, Date evictTime) {
        this.taskId = taskId;
        this.futureHolder = futureHolder;
        this.evictTime = evictTime;
    }

    @Override
    public void onUnExecutable() {
        Date now = new Date();
        if (now.after(evictTime)) {
            if (future != null) {
                futureHolder.cancelTask(taskId, future);
                logger.info("Cancel evict task for taskId:{}, now:{}, evictTime:{}", taskId, now, evictTime);
            }
        }
    }

    public void setFuture(ScheduledFuture future) {
        this.future = future;
    }
}
