/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.sense.service;

import java.util.Map;

public interface ScheduleService {

    void schedule(Integer eventType, Map<String, Object> params) throws Exception;
}
