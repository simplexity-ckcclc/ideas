/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.senseface.service;

import java.util.Map;

public interface ScheduleService {

    void schedule(int eventType, Map<String, Object> params) throws Exception;
}
