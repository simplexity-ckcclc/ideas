/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-3
 */

package com.ckcclc.springboot.sense.controller;

import com.alibaba.fastjson.JSONObject;
import com.ckcclc.springboot.sense.common.Response;
import com.ckcclc.springboot.sense.service.impl.ScheduleServiceImpl;
import com.ckcclc.springboot.sense.vo.ScheduleRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sense")
@Api(value = "/sense")
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    @ApiOperation(value = "调度任务", notes = "调度任务", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> schedule(@RequestBody ScheduleRequest request) throws Exception {
        logger.debug("[Request] schedule task of request params:{}", JSONObject.toJSONString(request));
        scheduleService.schedule(request.getEventType(), request.getParams());
        Response response = new Response();
        logger.info("[Response] schedule task of request params:{}, response:{}",
                JSONObject.toJSONString(request), response);
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
