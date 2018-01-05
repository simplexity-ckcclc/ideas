/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.senseface.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class SenseTaskScheduler implements InitializingBean, FactoryBean<TaskScheduler> {

    private ThreadPoolTaskScheduler scheduler = null;

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

    @Override
    public TaskScheduler getObject() throws Exception {
        return scheduler;
    }

    @Override
    public Class<?> getObjectType() {
        return scheduler.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
