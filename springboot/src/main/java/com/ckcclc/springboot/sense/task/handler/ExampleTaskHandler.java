/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.sense.task.handler;

import com.ckcclc.springboot.sense.common.Checkers;
import com.ckcclc.springboot.sense.common.ErrorCode;
import com.ckcclc.springboot.sense.common.ServiceException;
import com.ckcclc.springboot.sense.core.TaskFutureHolder;
import com.ckcclc.springboot.sense.task.ExampleTask;
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
        Checkers.Result result = Checkers.newInstance()
                .add(requestId, Checkers.StringChecker.isNotBlank(), "requestId is blank")
                .add(start, Checkers.DateChecker.isValid(format), "start is not valid date format")
                .add(end, Checkers.DateChecker.isValid(format), "end is not valid date format")
                .add(cron, Checkers.CronChecker.isValidQuartz(), "cron is not valid quartz cron expression")
                .add(cancel, Checkers.notNull(), "cancel not provided")
                .check();
        if (!result.isValid()) {
            logger.info("Params checking fail, error msg:{}", result.getErrorMsg());
            throw new ServiceException(ErrorCode.REQUEST_PARAMETER_ERROR, result.getErrorMsg());
        }

        DateFormat df = new SimpleDateFormat(format);
        Date startDate = df.parse(start);
        Date endDate = df.parse(end);
        Date now = new Date();

        result = Checkers.newInstance()
            .add(now, Checkers.DateChecker.before(endDate), "end is before current time")
            .add(startDate, Checkers.DateChecker.before(endDate), "end is before start")
            .check();
        if (!result.isValid()) {
            logger.info("Params checking fail, error msg:{}", result.getErrorMsg());
            throw new ServiceException(ErrorCode.REQUEST_PARAMETER_ERROR, result.getErrorMsg());
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

        Checkers.Result result = Checkers.newInstance()
                .add(requestId, Checkers.StringChecker.isNotBlank(), "requestId is blank")
                .check();
        if (!result.isValid()) {
            logger.info("Params checking fail, error msg:{}", result.getErrorMsg());
            throw new ServiceException(ErrorCode.REQUEST_PARAMETER_ERROR, result.getErrorMsg());
        }

        String taskId = taskId(requestId);
        futureHolder.cancelTasks(taskId);
        logger.info("Cancel all old tasks for taskId:{}", taskId);
    }

    private String taskId(String id) {
        return  "Example-" + id;
    }
}
