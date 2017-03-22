/*
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  huming <huming@tp-link.net>
 * Created: 2015-12-24
 */
package com.ckcclc.hibernate.config;

public interface DbConfig {

    //config jdbc
    String getJdbcDriverClass();

    String getJdbcCloudUrl();

    String getJdbcUser();

    String getJdbcPassword();

    //config hibernate
    String getHibernateShowSql();

    String getHibernateDialect();

    String getHibernateFormatSql();

    String getHibernateJdbcBatchSize();

    String getHibernateAutoCommit();

    //C3P0
    String getMinPoolSize();

    String getMaxPoolSize();

    String getMaxIdleTime();

    String getAcquireIncrement();

    String getInitialPoolSize();

    String getIdleConnectionTestPeriod();

    String getAcquireRetryAttempts();

    String getMaxIdleTimeExcessConnections();

    String getTestConnectionOnCheckin();

}
