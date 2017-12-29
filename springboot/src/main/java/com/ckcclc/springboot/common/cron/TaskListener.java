/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.common.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public class TaskListener implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(TaskListener.class);

    private Integer requestId;
    private Map<Integer, Set<ScheduledFuture>> taskMap;
    private ScheduledFuture future;
    private Date evictTime;

    public TaskListener(Integer requestId, Map<Integer, Set<ScheduledFuture>> taskMap, Date evictTime) {
        this.requestId = requestId;
        this.taskMap = taskMap;
        this.evictTime = evictTime;
    }

    @Override
    public void onUnExecutable() {
        Date now = new Date();
        if (now.after(evictTime)) {
            if (future != null) {
                future.cancel(true);
                Set<ScheduledFuture> futures = taskMap.get(requestId);
                if (futures != null) {
                    futures.remove(future);
                }
                logger.info("Cancel evict task for requestId:{}, now:{}, evictTime:{}", requestId, now, evictTime);
            }
        }
    }

    public void setFuture(ScheduledFuture future) {
        this.future = future;
    }
}
