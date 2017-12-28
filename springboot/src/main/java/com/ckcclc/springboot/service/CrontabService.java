/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-28
 */

package com.ckcclc.springboot.service;

import com.ckcclc.springboot.common.cron.CronTimeTask;
import com.ckcclc.springboot.common.cron.Listener;
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
import static com.cronutils.model.field.expression.FieldExpressionFactory.*;

@Component
public class CrontabService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(CrontabService.class);

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

    public void scheduleTask(int taskId, int second, int minute) {
        Date start = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 30);
        Date end = calendar.getTime();
        Listener listener = new TaskListener(taskMap, taskId, end);
        CronTimeTask task = new CronTimeTask(start, end);
        task.setListener(listener);

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withDoW(on(Weekdays.THURSDAY.getWeekday()).and(on(Weekdays.FRIDAY.getWeekday())))
                .withMonth(always())
                .withDoM(questionMark())
                .withHour(always())
                .withMinute(on(minute))
                .withSecond(on(second))
                .instance();

        Set<ScheduledFuture> newFutures = new HashSet<>();
        ScheduledFuture newFuture = scheduler.schedule(task, new CronTrigger(cron.asString()));
        newFutures.add(newFuture);

        Set<ScheduledFuture> futures = taskMap.get(taskId);
        if (futures != null && !futures.isEmpty()) {
            synchronized (futures) {
                for (ScheduledFuture future : futures) {
                    future.cancel(true);
                }
                futures.clear();
            }
        }

        taskMap.put(taskId, newFutures);
    }

    private void execute() {
        logger.info("current time millis:{}", System.currentTimeMillis());
    }

}
