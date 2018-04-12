package com.test;

import com.dsg.common.constants.DatabaseType;
import com.dsg.common.utils.JDBC;

import java.sql.SQLException;

/**
 * Created on 2018/3/132018 三月 星期二下午 20:15
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  Test, V   2018/3/13 下午 20:15 DSG Exp$$
 */
public class Test {

    public static void main(String[] dsg){
        try {
            JDBC jdbc = new JDBC(DatabaseType.HIVE_SERVER2,"ods");
            jdbc.execute("", 8);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
