/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-3
 */

package com.ckcclc.springboot.sense.core;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Component
public class TaskFutureHolder {

    private Map<String, Set<ScheduledFuture>> taskMap = Maps.newConcurrentMap();

    public void cancelTasks(String taskId) {
        synchronized (this) {
            Set<ScheduledFuture> futures = taskMap.get(taskId);
            if (futures != null) {
                for (ScheduledFuture future : futures) {
                    future.cancel(true);
                }
                futures.clear();
            }
        }
    }

    public void cancelTask(String taskId, ScheduledFuture future) {
        future.cancel(true);

        synchronized (this) {
            Set<ScheduledFuture> futures = taskMap.get(taskId);
            if (futures != null) {
                futures.remove(future);
            }
        }
    }

    public synchronized void updateTaskFutures(String taskId, Set<ScheduledFuture> futures) {
        Set<ScheduledFuture> origin = taskMap.get(taskId);
        if (origin != null) {
            origin.addAll(futures);
            return;
        }
        taskMap.put(taskId, futures);
    }

}
