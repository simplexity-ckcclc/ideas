/**
 * Author:  ckcclc <wchuang5900@163.com>
 * Created: 2017/3/22
 */

package com.ckcclc.hibernate;

import com.ckcclc.hibernate.config.DbConfig;
import com.ckcclc.hibernate.dao.DeviceValidateErrorDao;

import java.text.ParseException;

public class MysqlClient {

    public static final DbConfig dbConfig = new DbConfig() {

        @Override
        public String getHibernateAutoCommit() {
            return "true";
        }

        @Override
        public String getJdbcDriverClass() {
            return "com.mysql.jdbc.Driver";
        }

        @Override
        public String getJdbcCloudUrl() {
            return "jdbc:mysql://192.168.8.128:3306/test";
        }

        @Override
        public String getJdbcUser() {
            return "root";
        }

        @Override
        public String getJdbcPassword() {
            return "travis";
        }

        @Override
        public String getHibernateShowSql() {
            return "true";
        }

        @Override
        public String getHibernateDialect() {
            return "org.hibernate.dialect.MySQLDialect";
        }

        @Override
        public String getHibernateFormatSql() {
            return "false";
        }

        @Override
        public String getHibernateJdbcBatchSize() {
            return "20";
        }

        @Override
        public String getMinPoolSize() {
            return "3";
        }

        @Override
        public String getMaxPoolSize() {
            return "8";
        }

        @Override
        public String getMaxIdleTime() {
            return "3600";
        }

        @Override
        public String getAcquireIncrement() {
            return "1";
        }

        @Override
        public String getInitialPoolSize() {
            return "3";
        }

        @Override
        public String getIdleConnectionTestPeriod() {
            return "300";
        }

        @Override
        public String getAcquireRetryAttempts() {
            return "10";
        }

        @Override
        public String getMaxIdleTimeExcessConnections() {
            return "240";
        }

        @Override
        public String getTestConnectionOnCheckin() {
            return "false";
        }
    };


    public static void main(String[] args) throws ParseException {

        DeviceValidateErrorDao dao = new DeviceValidateErrorDao(dbConfig);
    }

}
