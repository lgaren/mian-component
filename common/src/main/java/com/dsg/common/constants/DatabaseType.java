package com.dsg.common.constants;

import com.dsg.common.conf.DSGConfig;

/**
 * Created on 2018/3/142018 三月 星期三上午 11:17
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  dataTest, V 0.0  2018/3/14 上午 11:17 DSG Exp$$
 */
public enum DatabaseType {

    HIVE_SERVER2(
            "jdbc:hive2://",
            Constants.URL_PARAMS_SPLIT,
            "org.apache.hive.jdbc.HiveDriver",
            DSGConfig.ConfVars.HIVE_SERVER2_USER,
            DSGConfig.ConfVars.HIVE_SERVER2_PASSWORD,
            DSGConfig.ConfVars.HIVE_SERVER2_HOST,
            DSGConfig.ConfVars.HIVE_SERVER2_PORT
    ),

    MYSQL("jdbc:mysql://",
            Constants.URL_PARAMS_SPLIT,
            "com.mysql.jdbc.Driver",
            DSGConfig.ConfVars.DATABASE_USER,
            DSGConfig.ConfVars.DATABASE_PASSWORD,
            DSGConfig.ConfVars.DATABASE_HOST,
            DSGConfig.ConfVars.DATABASE_PORT
    ),
    SQL_SERVER(
            "jdbc:sqlserver://",
            ";dbname=",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            DSGConfig.ConfVars.DATABASE_USER,
            DSGConfig.ConfVars.DATABASE_PASSWORD,
            DSGConfig.ConfVars.DATABASE_HOST,
            DSGConfig.ConfVars.DATABASE_PORT
    ),
    ORACLE("jdbc:oracle:thin:@//",
            Constants.URL_PARAMS_SPLIT,
            "oracle.jdbc.driver.OracleDriver",
            DSGConfig.ConfVars.DATABASE_USER,
            DSGConfig.ConfVars.DATABASE_PASSWORD,
            DSGConfig.ConfVars.DATABASE_HOST,
            DSGConfig.ConfVars.DATABASE_PORT
    );

    public final String urlPrefix;
    public final String urlSplit;
    public final String driverName;
    public final DSGConfig.ConfVars userName;
    public final DSGConfig.ConfVars password;
    public final DSGConfig.ConfVars host;
    public final DSGConfig.ConfVars port;

    DatabaseType(String urlPrefix,String urlSplit, String driverName, DSGConfig.ConfVars userName, DSGConfig.ConfVars password, DSGConfig.ConfVars host, DSGConfig.ConfVars port) {
        this.urlPrefix = urlPrefix;
        this.urlSplit = urlSplit ;
        this.driverName = driverName;
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }
}
