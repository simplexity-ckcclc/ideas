/**
 * Author:  ckcclc <wchuang5900@163.com>
 * Created: 2017/3/22
 */

package com.ckcclc.hibernate.dao;

import com.ckcclc.hibernate.config.DbConfig;
import com.ckcclc.hibernate.entity.DeviceValidateErrorEntity;
import com.ckcclc.hibernate.entity.TestEntity;
import com.tplink.cloud.common.database.CommonConfiguration;
import com.tplink.cloud.common.database.CommonDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class DeviceValidateErrorDao {

    private CommonDAO commonDAO;

    public DeviceValidateErrorDao(DbConfig dbConfig) {

        CommonConfiguration configuration = getCloudConfiguration(dbConfig);
        commonDAO = new CommonDAO(configuration);
    }

    private CommonConfiguration getCloudConfiguration(DbConfig config) {
        Class[] classes = new Class[]{
                DeviceValidateErrorEntity.class,
                TestEntity.class,
        };

        CommonConfiguration commonConfiguration = new CommonConfiguration(config.getJdbcCloudUrl(),
                config.getJdbcUser(), config.getJdbcPassword(), classes);

        commonConfiguration.setJdbcDriverClass(config.getJdbcDriverClass());
        commonConfiguration.setShowSQL(Boolean.valueOf(config.getHibernateShowSql()));
        commonConfiguration.setDialect(config.getHibernateDialect());
        commonConfiguration.setFormatSQL(Boolean.valueOf(config.getHibernateFormatSql()));
        commonConfiguration.setBatchSize(Integer.valueOf(config.getHibernateJdbcBatchSize()));
        commonConfiguration.setAutoCommit(Boolean.valueOf(config.getHibernateAutoCommit()));

        commonConfiguration.setMinPoolSize(Integer.valueOf(config.getMinPoolSize()));
        commonConfiguration.setMaxPoolSize(Integer.valueOf(config.getMaxPoolSize()));
        commonConfiguration.setAcquireIncrement(Integer.valueOf(config.getAcquireIncrement()));
        commonConfiguration.setIdleConnectionTestPeriod(Integer.valueOf(config.getIdleConnectionTestPeriod()));
        commonConfiguration.setMaxIdleTime(Integer.valueOf(config.getMaxIdleTime()));
        commonConfiguration.setAcquireIncrement(Integer.valueOf(config.getAcquireIncrement()));
        commonConfiguration.setInitialPoolSize(Integer.valueOf(config.getInitialPoolSize()));
        commonConfiguration.setMaxIdleTimeExcessConnections(
                Integer.valueOf(config.getMaxIdleTimeExcessConnections()));

        return commonConfiguration;
    }


    public CommonDAO getCommonDAO() {
        return commonDAO;
    }

    public DeviceValidateErrorEntity findByDevice(String deviceId, String deviceMac, String deviceHwId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DeviceValidateErrorEntity.class)
                .add(Restrictions.eq("device_id", deviceId))
                .add(Restrictions.eq("device_mac", deviceMac))
                .add(Restrictions.eq("device_hw_id", deviceHwId));
        return commonDAO.findByCriteriaUnique(criteria, DeviceValidateErrorEntity.class);
    }

//    public DeviceValidateErrorEntity saveOrUpdate(DeviceValidateErrorEvent event) {
//        checkNotNull(event, "DeviceValidateErrorEvent");
//        String deviceId = event.getDeviceId();
//        String deviceMac = event.getDeviceMac();
//        String deviceHwId = event.getHwId();
//        String cloudMac = event.getCloudMac();
//        String cloudHwId = event.getCloudHwId();
//        String deviceAlias = event.getDeviceAlias();
//        String deviceHwVer = event.getDeviceHwVer();
//        String deviceModel = event.getDeviceModel();
//        String deviceName = event.getDeviceName();
//        String errorCode = event.getErrorCode();
//        String fwId = event.getFwId();
//        String fwVer = event.getFwVer();
//        String ipAddress = event.getIpAddress();
//        String tcspVer = event.getTcspVer();
//        String region = event.getRegion();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date validateTime = sdf.parse(event.getValidateTimeStamp());
//
//        DeviceValidateErrorEntity entity = findByDevice(deviceId, deviceMac, deviceHwId);
//        if (entity == null) {
//            entity = new DeviceValidateErrorEntity();
//            entity.setDeviceId(deviceId);
//            entity.setDeviceMac(deviceMac);
//            entity.setCloudHwId(cloudHwId);
//        }
//
//        entity.setCloudMac(cloudMac);
//        entity.setCloudHwId(cloudHwId);
//        entity.setDeviceAlias(deviceAlias);
//        entity.setDeviceHwVer(deviceHwVer);
//        entity.setDeviceModel(deviceModel);
//        entity.setDeviceName(deviceName);
//        entity.setErrorCode(errorCode);
//        entity.setFwId(fwId);
//        entity.setFwVer(fwVer);
//        entity.setIpAddress(ipAddress);
//        entity.setTcspVer(tcspVer);
//        entity.setRegion(region);
//        entity.setValidateTimeStamp(validateTime);
//
//        commonDAO.saveOrUpdate(entity);
//
//        return entity;
//    }



}
