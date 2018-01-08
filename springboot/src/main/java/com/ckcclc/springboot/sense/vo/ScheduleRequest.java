/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 18-1-5
 */

package com.ckcclc.springboot.sense.vo;


import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

public class ScheduleRequest {

    @ApiModelProperty("调度事件类型")
    private Integer eventType;
    @ApiModelProperty("调度事件参数")
    private Map<String, Object> params;

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
