/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-28
 */

package com.ckcclc.springboot.service;

import com.ckcclc.springboot.common.cron.CronTimeTask;
import com.ckcclc.springboot.common.cron.TaskListener;
import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.Weekdays;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

import static com.cronutils.model.field.expression.FieldExpression.questionMark;
import static com.cronutils.model.field.expression.FieldExpressionFactory.always;
import static com.cronutils.model.field.expression.FieldExpressionFactory.every;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;

@Component
public class CrontabService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(CrontabService.class);

    private static final Object lock = new Object();
    private ThreadPoolTaskScheduler scheduler = null;
    private ScheduledFuture<?> future = null;
    private Map<Integer, Set<ScheduledFuture>> taskMap = Maps.newConcurrentMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("cronScheduler-");
        scheduler.setPoolSize(10);
        // block spring context stopping to allow SI pollers to complete
        // (to graceful shutdown still running tasks, without destroying beans used in these tasks)
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(20);

        scheduler.initialize();
    }

    @Scheduled(cron = "${tick.crontab:*/1 * * * * *}")
    public void tick() {
        logger.info("Tick!");
    }

    public void schedule(String cron) {
        if (future != null) {
            future.cancel(true);
        }
        future =  scheduler.schedule(this::execute, new CronTrigger(cron));
    }

    public void scheduleTask(int requestId, int second, int minute) {
        Date start = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        Date end = calendar.getTime();
        TaskListener listener = new TaskListener(requestId, taskMap, end);
        CronTimeTask task = new CronTimeTask(start, end);
        task.setListener(listener);

        // 1. create cron expression
        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withDoW(on(Weekdays.THURSDAY.getWeekday())
                        .and(on(Weekdays.FRIDAY.getWeekday())))
                .withMonth(always())
                .withDoM(questionMark())
                .withHour(always())
                .withMinute(every(2))
                .withSecond(on(second))
                .instance();
        logger.info("cron expression:{}", cron.asString());

        // 2. create new cron tasks
        Set<ScheduledFuture> newFutures = new HashSet<>();
        ScheduledFuture newFuture = scheduler.schedule(task, new CronTrigger(cron.asString()));
        newFutures.add(newFuture);
        listener.setFuture(newFuture);

        // 3. cancel old tasks
        synchronized (lock) {
            Set<ScheduledFuture> futures = taskMap.get(requestId);
            if (futures != null && !futures.isEmpty()) {
                    for (ScheduledFuture future : futures) {
                        future.cancel(true);
                    }
                    futures.clear();
            }
            taskMap.put(requestId, newFutures);
            logger.info("Cancel all old tasks and recreate new tasks for requestId:{}", requestId);
        }
    }

    private void execute() {
        logger.info("current time:{}", new Date());
    }

}
