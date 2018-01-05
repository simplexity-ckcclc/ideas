/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.senseface.task.handler;

import com.ckcclc.springboot.senseface.common.Checkers;
import com.ckcclc.springboot.senseface.core.TaskFutureHolder;
import com.ckcclc.springboot.senseface.task.ExampleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Component
public class ExampleTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExampleTaskHandler.class);

    @Autowired
    private TaskScheduler scheduler;

    @Autowired
    private TaskFutureHolder futureHolder;

    public void create(Map<String, Object> params) throws Exception {
        String requestId = (String) params.get("requestId");
        String start = (String) params.get("start");
        String end = (String) params.get("end");
        String cron = (String) params.get("cron");
        Boolean cancel = (Boolean) params.get("cancel");

        String format = "yyyy-MM-dd HH:mm:ss";
        boolean isChecked = Checkers.newInstance()
                .add(requestId, Checkers.StringChecker.isNotBlank())
                .add(start, Checkers.DateChecker.isValid(format))
                .add(end, Checkers.DateChecker.isValid(format))
                .add(cron, Checkers.CronChecker.isValidQuartz())
                .add(cancel, Checkers.notNull())
                .check();
        if (!isChecked) {
            // TODO: 18-1-5
            logger.info("invalid params");
            return;
        }

        DateFormat df = new SimpleDateFormat(format);
        Date startDate = df.parse(start);
        Date endDate = df.parse(end);
        Date now = new Date();

        isChecked = Checkers.newInstance()
            .add(now, Checkers.DateChecker.before(endDate))
            .add(startDate, Checkers.DateChecker.before(endDate))
            .check();
        if (!isChecked) {
            // TODO: 18-1-5
            logger.info("invalid params");
            return;
        }

        // 1. create new service tasks
        ExampleTask task = new ExampleTask(startDate, endDate);
        String taskId = taskId(requestId);
        ExampleTaskListener listener = new ExampleTaskListener(taskId, futureHolder, endDate);
        task.setListener(listener);

        // 2. schedule new service tasks
        Set<ScheduledFuture> newFutures = new HashSet<>();
        ScheduledFuture newFuture = scheduler.schedule(task, new CronTrigger(cron));
        listener.setFuture(newFuture);
        newFutures.add(newFuture);

        // 3. cancel old service tasks if needed
        if (cancel) {
            futureHolder.cancelTasks(taskId);
            logger.info("Cancel all old tasks for taskId:{}", taskId);
        }

        // 4. update future holder
        futureHolder.updateTaskFutures(taskId, newFutures);
        logger.info("Create new tasks for taskId:{}", taskId);
    }

    public void cancel(Map<String, Object> params) throws Exception {
        String requestId = (String) params.get("requestId");

        boolean isChecked = Checkers.newInstance()
                .add(requestId, Checkers.StringChecker.isNotBlank())
                .check();
        if (!isChecked) {
            // TODO: 18-1-5
            logger.info("invalid params");
        }

        String taskId = taskId(requestId);
        futureHolder.cancelTasks(taskId);
        logger.info("Cancel all old tasks for taskId:{}", taskId);
    }

    private String taskId(String id) {
        return  "Example-" + id;
    }
}
