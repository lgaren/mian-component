package com.dsg.common.utils;

import com.dsg.common.conf.DSGConfig;
import com.dsg.common.conf.DSGConfig.*;
import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;

import java.sql.*;
import java.util.Arrays;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
/**
 * Created on 2018/3/132018 三月 星期二下午 17:12
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  JDBCUtils, V 0.0  2018/3/13 下午 17:12 DSG Exp$$
 */
public class JDBC {

    private Connection con = null;
    private Statement stmt = null;

    public JDBC(DatabaseType serverName, String schema) throws ClassNotFoundException, SQLException {
//        Class clz = SQLServerDriver.class;
        Class.forName(DSGConfig.getVar(serverName.driverName));
        con = DriverManager.getConnection(serverName.urlPrefix + DSGConfig.getVar(serverName.host) + Constants.SPLIT +DSGConfig.getIntVar(serverName.port) + serverName.urlSplit +  schema,
                DSGConfig.getVar(serverName.userName), DSGConfig.getVar(serverName.password));
        stmt = con.createStatement();
    }

    public <T> ResultSet executeQuery(String sql , T replce) throws SQLException {
        return stmt.executeQuery(sql.replace(Constants.SQL_PH, replce.toString()));
    }

    public ResultSet executeQuery(String sql , Object[] replce) throws SQLException {
        Arrays.stream(replce).forEach( e -> sql.replace(Constants.SQL_PH,e.toString()));
        return stmt.executeQuery(sql);
    }

    public ResultSet executeQuery(String sql) throws SQLException { return stmt.executeQuery(sql); }

    public boolean execute(String sql) throws SQLException {
        return stmt.execute(sql);
    }

    public <T> boolean execute(String sql,T replce) throws SQLException {
        return stmt.execute(sql.replace(Constants.SQL_PH, replce.toString()));
    }

    public boolean execute(String sql,Object[] replce) throws SQLException {
        Arrays.stream(replce).forEach( e -> sql.replace(Constants.SQL_PH,e.toString()));
        return stmt.execute(sql);
    }

    public Statement getStatement(){return this.stmt;}

    public Connection getConnection(){return this.con;}

    public void close(){
        try {
            stmt.close();
        } catch (SQLException e) {
        }
        try {
            con.close();
        } catch (SQLException e) {
        }
    }

}
