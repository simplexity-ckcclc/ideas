/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-28
 */

package com.ckcclc.springboot.sense.service.impl;

import com.ckcclc.springboot.sense.common.Checkers;
import com.ckcclc.springboot.sense.common.ErrorCode;
import com.ckcclc.springboot.sense.common.ServiceException;
import com.ckcclc.springboot.sense.core.Event;
import com.ckcclc.springboot.sense.service.ScheduleService;
import com.ckcclc.springboot.sense.task.handler.ExampleTaskHandler;
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
    public void schedule(Integer eventType, Map<String, Object> params) throws Exception {
        Checkers.Result result = Checkers.newInstance()
                .add(eventType, Checkers.in(Event.types()), "provided eventType not supported")
                .check();
        if (!result.isValid()) {
            logger.info("Params checking fail, error msg:{}", result.getErrorMsg());
            throw new ServiceException(ErrorCode.REQUEST_PARAMETER_ERROR, result.getErrorMsg());
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
                logger.info("Matched eventType handler not found, do nothing");
                break;
        }
    }

}
