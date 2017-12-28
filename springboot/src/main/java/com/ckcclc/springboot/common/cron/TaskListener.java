/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.common.cron;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public class TaskListener implements Listener {

    private Map<Integer, Set<ScheduledFuture>> taskMap;
    private Integer taskId;
    private Date evictTime;

    public TaskListener(Map<Integer, Set<ScheduledFuture>> taskMap, Integer taskId, Date evictTime) {
        this.taskMap = taskMap;
        this.taskId = taskId;
        this.evictTime = evictTime;
    }

    @Override
    public void onUnExecutable() {
        Date now = new Date();
        if (now.after(evictTime)) {
            Set<ScheduledFuture> futures = taskMap.get(taskId);
            if (futures != null && !futures.isEmpty()) {
                synchronized (futures) {
                    for (ScheduledFuture future : futures) {
                        future.cancel(true);
                    }
                    futures.clear();
                }
            }
        }
    }
}
