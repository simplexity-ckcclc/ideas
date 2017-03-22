/**
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  huangyucong <huangyucong@tp-link.com.cn>
 * Created: 2017/3/6
 */

package com.ckcclc.hibernate.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "device_validate_error_info",
        uniqueConstraints = @UniqueConstraint(columnNames = {"device_id", "device_mac", "device_hw_id"}))
//@SQLInsert( sql="INSERT INTO device_validate_error_info ON DUPLICATE KEY UPDATE")
public class DeviceValidateErrorEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "device_id", length = 40, nullable = false)
    private String deviceId = null;

    @Column(name = "device_mac", length = 16, nullable = false)
    private String deviceMac = null;

    @Column(name = "device_hw_id", length = 32, nullable = false)
    private String deviceHwId = null;

    @Column(name = "validate_timestamp", length = 19)
    private Date validateTimeStamp = null;

    @Column(name = "device_name", length = 64)
    private String deviceName = null;

    @Column(name = "device_alias", length = 64)
    private String deviceAlias = null;

    @Column(name = "device_model", length = 32)
    private String deviceModel = null;

    @Column(name = "device_hw_ver", length = 16)
    private String deviceHwVer = null;

    @Column(name = "fw_ver", length = 255)
    private String fwVer = null;

    @Column(name = "fw_id", length = 32)
    private String fwId = null;

    @Column(name = "ip_address", length = 64)
    private String ipAddress = null;

    @Column(name = "region", length = 32)
    private String region = null;

    @Column(name = "error_code", length = 64)
    private String errorCode = null;

    @Column(name = "cloud_mac", length = 16)
    private String cloudMac = null;

    @Column(name = "cloud_hw_id", length = 32)
    private String cloudHwId = null;

    @Column(name = "tcsp_ver", length = 64)
    private String tcspVer = null;

    @Column(name = "email_sent_time", length = 19)
    private Date emailSentTime = null;

    @Column(name = "is_sent")
    private boolean isSent = false;

    @Column(name = "first_validate_timestamp", length = 19)
    private Date firstValidateTimeStamp = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getDeviceHwId() {
        return deviceHwId;
    }

    public void setDeviceHwId(String deviceHwId) {
        this.deviceHwId = deviceHwId;
    }

    public Date getValidateTimeStamp() {
        return validateTimeStamp;
    }

    public void setValidateTimeStamp(Date validateTimeStamp) {
        this.validateTimeStamp = validateTimeStamp;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceHwVer() {
        return deviceHwVer;
    }

    public void setDeviceHwVer(String deviceHwVer) {
        this.deviceHwVer = deviceHwVer;
    }

    public String getFwVer() {
        return fwVer;
    }

    public void setFwVer(String fwVer) {
        this.fwVer = fwVer;
    }

    public String getFwId() {
        return fwId;
    }

    public void setFwId(String fwId) {
        this.fwId = fwId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCloudMac() {
        return cloudMac;
    }

    public void setCloudMac(String cloudMac) {
        this.cloudMac = cloudMac;
    }

    public String getCloudHwId() {
        return cloudHwId;
    }

    public void setCloudHwId(String cloudHwId) {
        this.cloudHwId = cloudHwId;
    }

    public String getTcspVer() {
        return tcspVer;
    }

    public void setTcspVer(String tcspVer) {
        this.tcspVer = tcspVer;
    }

    public Date getEmailSentTime() {
        return emailSentTime;
    }

    public void setEmailSentTime(Date emailSentTime) {
        this.emailSentTime = emailSentTime;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(boolean isSent) {
        this.isSent = isSent;
    }

    public Date getFirstValidateTimeStamp() {
        return firstValidateTimeStamp;
    }

    public void setFirstValidateTimeStamp(Date firstValidateTimeStamp) {
        this.firstValidateTimeStamp = firstValidateTimeStamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("errorCode", errorCode)
                .add("region", region)
                .add("deviceId", deviceId)
                .add("deviceMac", deviceMac)
                .add("cloudMac", cloudMac)
                .add("deviceHwId", deviceHwId)
                .add("cloudHwId", cloudHwId)
                .add("validateTimeStamp", validateTimeStamp)
                .add("ipAddress", ipAddress)
                .add("tcspVer", tcspVer)
                .add("deviceAlias", deviceAlias)
                .add("deviceHwVer", deviceHwVer)
                .add("fwId", fwId)
                .add("fwVer", fwVer)
                .add("deviceName", deviceName)
                .add("deviceModel", deviceModel)
                .toString();
    }

}