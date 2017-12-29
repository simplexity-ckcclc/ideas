/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-12-28
 */

package com.ckcclc.springboot.controller;

import com.ckcclc.springboot.service.CrontabService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Api(value="cron-controller", description="Operations of Testing")
public class CronController {

    @Autowired
    private CrontabService crontabService;

    @RequestMapping(value = "/cron", method = RequestMethod.POST)
    public ResponseEntity<String> cron(@RequestBody Map<String, Object> params) throws Exception {
        String cron = (String) params.get("cron");
        crontabService.schedule(cron);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/cronTask", method = RequestMethod.POST)
    public ResponseEntity<String> cronTask(@RequestBody Map<String, Object> params) throws Exception {
        Integer requestId = (Integer) params.get("requestId");
//        String cron = (String) params.get("cron");
        int minute = (Integer) params.get("minute");
        int second = (Integer) params.get("second");
        crontabService.scheduleTask(requestId, second, minute);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
