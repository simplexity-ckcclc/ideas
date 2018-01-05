/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-28
 */

package com.ckcclc.springboot.senseface.service.impl;

import com.ckcclc.springboot.senseface.common.Checkers;
import com.ckcclc.springboot.senseface.core.Event;
import com.ckcclc.springboot.senseface.service.ScheduleService;
import com.ckcclc.springboot.senseface.task.handler.ExampleTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ExampleTaskHandler handler;

    @Override
    public void schedule(int eventType, Map<String, Object> params) throws Exception {
        boolean isChecked = Checkers.newInstance()
                .add(eventType, Checkers.in(Event.types()))
                .check();
        if (!isChecked) {
            // TODO: 18-1-5
        }

        Event event = Event.fromType(eventType);
        switch (event) {
            case CREATE_EXAMPLE:
                handler.create(params);
                break;
            case CANCEL_EXAMPLE:
                handler.cancel(params);
                break;
            default:
                break;
        }
    }



}
