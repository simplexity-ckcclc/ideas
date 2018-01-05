/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.senseface.vo;


import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

public class ScheduleRequest {

    @ApiModelProperty("调度事件类型")
    private int eventType;
    @ApiModelProperty("调度事件参数")
    private Map<String, Object> params;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
