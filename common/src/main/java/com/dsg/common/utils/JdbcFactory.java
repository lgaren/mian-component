package com.dsg.common.utils;

import com.dsg.common.conf.DSGConfig;
import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
//import com.microsoft.sqlserver.jdbc.SQLServerDriver;

/**
 * Created on 2018/3/132018 三月 星期二下午 17:12
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  JDBCUtils, V 0.0  2018/3/13 下午 17:12 DSG Exp$$
 */
public class JdbcFactory implements Serializable {

    private static JdbcFactory jdbcFactory = null;

    private Map<String, JDBC> jdbcPool = new HashMap<>();

    static {
//        new SQLServerDriver();
        jdbcFactory = new JdbcFactory();
    }

    private JdbcFactory() {
        this.jdbcPool = new HashMap<>();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                jdbcPool.forEach((key, jdbc) -> {
//                    jdbc.close0();
                });
            }
        });
    }

    public static synchronized JDBC get(DatabaseType serverName, String schema, String host, int port, String user, String pasword) throws Exception {
        String hashKey = ZnsjHash.toHash(Thread.currentThread().getName() + schema + host + port + user + pasword);
        JDBC jdbc = jdbcFactory.jdbcPool.get(hashKey);
        if (jdbc == null ||( jdbc.time - System.currentTimeMillis()) < 0) {
            if(jdbc != null  ){
                jdbc.close();
                jdbcFactory.jdbcPool.remove(hashKey);
            }
            JDBC jdbcTe = jdbcFactory.new JDBC(serverName, schema, host, port, user, pasword,hashKey);
            jdbcFactory.jdbcPool.put(hashKey, jdbcTe);
            return jdbcTe;
        }
        return jdbc;
    }

    public static JDBC get(DatabaseType serverName, String schema) throws Exception {
        return get(serverName, schema, DSGConfig.getVar(serverName.host), DSGConfig.getIntVar(serverName.port), DSGConfig.getVar(serverName.userName), DSGConfig.getVar(serverName.password));
    }

    public class JDBC implements Serializable {
        private Connection con = null;
        private Statement stmt = null;
        private String key = null;
        private Long time;

        private JDBC(DatabaseType serverName, String schema, String host, int port, String user, String pasword,String key) {
            this.key = key ;
            try {
//                System.out.println(serverName.driverName);
                Class.forName(serverName.driverName);
                con = DriverManager.getConnection(serverName.urlPrefix + host + Constants.SPLIT + port + serverName.urlSplit + schema + serverName.params, user, pasword);
                stmt = con.createStatement();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(4);
        }

        public <T> ResultSet executeQuery(String sql, T replce) throws SQLException {
//            System.out.println(sql.replace("?", replce.toString()));
            return stmt.executeQuery(sql.replaceFirst(Constants.SQL_PH, replce.toString()));
        }

        public ResultSet executeQuery(String sql, Object[] replce) throws SQLException {

            Arrays.stream(replce).forEach(e -> sql.replace("?", e.toString()));
            String sql1 = sql;
            for ( Object str : replce) {
                sql1 = sql1.replace("?", str.toString());
            }
            System.out.println(sql1);
            return stmt.executeQuery(sql1);
        }

        public ResultSet executeQuery(String sql) throws SQLException {
            return stmt.executeQuery(sql);
        }

        public boolean execute(String sql) throws SQLException {
            return stmt.execute(sql);
        }

        public <T> boolean execute(String sql, T replce) throws SQLException {
            return stmt.execute(sql.replace(Constants.SQL_PH, replce.toString()));
        }

        //  this.<String>getValue("");
        public Object getValue(String sql) throws SQLException {
            ResultSet res = stmt.executeQuery(sql);
            if (res.next()) {
                Object value = res.getObject(1);
                res.close();
                return value;
            }
            return 0;
        }

        public boolean isData(String sql ) throws SQLException {
            System.out.println(sql);
            ResultSet res = stmt.executeQuery(sql);
            boolean flag = res.next();
            res.close();
            return flag;
        }

        public boolean execute(String sql, Object[] replce) throws SQLException {
            Arrays.stream(replce).forEach(e -> sql.replace(Constants.SQL_PH, e.toString()));
            return stmt.execute(sql);
        }

        public Statement getStatement() {
            return this.stmt;
        }

        public Connection getConnection() {
            return this.con;
        }

        public void close0(){
            try {
                stmt.close();
                con.close();
            } catch (SQLException e) {
            }
        }

        public void close() {
            close0();
            jdbcPool.remove(this.key);
        }

    }


}
