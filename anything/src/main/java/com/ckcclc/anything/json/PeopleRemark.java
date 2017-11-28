/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-10
 */

package com.ckcclc.anything.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class PeopleRemark {

    private Integer credentialType;
    private String description;
    private String country;
    private Date bornTime;

    public Integer getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(Integer credentialType) {
        this.credentialType = credentialType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getBornTime() {
        return bornTime;
    }

    public void setBornTime(Date bornTime) {
        this.bornTime = bornTime;
    }

    public String toJSonString() {
        JSONObject remark = new JSONObject();
        remark.put("credentialType", credentialType);
        remark.put("description", description);
        remark.put("country", country);
        remark.put("bornTime", bornTime);
        return remark.toJSONString();
    }

    public static PeopleRemark fromJSonString(String jsonString) {
        return JSON.parseObject(jsonString, PeopleRemark.class);
    }

    @Override
    public String toString() {
        return toJSonString();
    }
}
