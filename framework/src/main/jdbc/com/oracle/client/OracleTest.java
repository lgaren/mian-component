package com.oracle.client;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Created on 2017/12/82017 十二月 星期五下午 13:52
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  OracleTest, V 0.0  2017/12/8 下午 13:52 DSG Exp$$
 */
public class OracleTest {


    static String url = "jdbc:oracle:thin:@//10.200.6.110:1521/PDBORCL";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
    static String user = "bi_user";// 用户名,系统默认的账户名
    static String password = "lvtu_oracle";// 你安装时选设置的密码


    static{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        testOracle();
    }

    /**
     * 一个非常标准的连接Oracle数据库的示例代码
     */
    public static void testOracle() {

//        insert into load_test(usr_name,come_date) values('dsg',TO_DATE('1997-12-15 23:50:26','YYYY-MM-DD HH24:MI:SS'))
        String sql = "insert into load_test(usr_name,come_date) values('dsg',TO_DATE('{}','YYYY-MM-DD HH24:MI:SS'))";
        for (int i = 0; i < 10; i++) {
            String date  =  long2Date(i);
            for (int j = 0; j <500 ; j++) {
                opt(sql.replace("{}",date));
            }
        }
    }

    public static void  opt(String sql){
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try {

            con = DriverManager.getConnection(url, user, password);// 获取连接
//            String sql = "select * from dataxt where user_id=430518";// 预编译语句，“？”代表参数


            pre = con.prepareStatement(sql);// 实例化预编译语句
//          pre.setString(1, "小茗同学");// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
            int i = pre.executeUpdate(sql);

//            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
//            while (result.next())
//                // 当结果集不为空时
//                System.out.println("学号:" + result.getInt("user_id") + "姓名:"
//                        + result.getString("mobile") + result.getString("province_name") +  result.getString("city_name"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null)
                    result.close();
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String long2Date(int ase) {
        long time = System.currentTimeMillis() + ase * 60 * 60 * 1000;
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date(time);
        String str = sdf.format(date);
        System.out.println(str);
        return sdf.format(date);
    }

}
