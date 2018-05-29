package com.dsglyy.warehouse;

import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;
import com.dsg.common.utils.JDBC;
import com.dsg.common.utils.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import scala.tools.cmd.gen.AnyVals;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Created on 2018/3/142018 三月 星期三上午 11:17
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  dataTest, V 0.0  2018/3/14 上午 11:17 DSG Exp$$
 */
public class DataTest {

    public static ResultSet result = null;
    static XSSFWorkbook workbook;
    static XSSFSheet sheet;
    static XSSFRow row_title;

    static String sql_count ;
    static String sql_update_time ;
    static String sql_create_time ;
    static String sql_update_time_min ;
    static String sql_create_time_min ;
    static String sqlnullUpdateTime ;
    static String sqlnullCreateTime;
    //        String sql_get_table = "select table_name from TABLES where TABLE_SCHEMA='?' ";
    static String sql_get_table ;
    static String db;

    public static void main(String[] dsg) {
        //创建工作簿

//        String sql_test = "SELECT count(1) FROM ? where  update_time >= DATE_FORMAT('20150618','%Y-%m-%d')  and update_time < DATE_FORMAT('20180315','%Y-%m-%d');";

        workbook = new XSSFWorkbook();
        //新建工作表
        sheet = workbook.createSheet("data_test1");

        row_title = sheet.createRow(0);

        String col1 = "dupdate";
        String col2 = "dcreate";

        sql_count = "select count(1) from ? ";
        sql_update_time = "select max("+col1 + ") from ?  ";
        sql_create_time = "select max("+col2 + ") from ? ";
        sql_update_time_min = "select min("+col1 + ") from ?  ";
        sql_create_time_min = "select min("+col2 + ") from ? ";
        sqlnullUpdateTime = "select count(1) from ? where "+col1 + " is null ";
        sqlnullCreateTime = "select count(1) from ? where "+col2 +" is null ";
        sql_get_table = "select name from sysobjects where xtype='u' ";
//        多个db 用分号隔开；
        db = "joyu";
        getDataSum( col1,  col2);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("data_test.xlsx");
            workbook.write(fos);
            workbook.close();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getDataSum(String col1, String col2){
        //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
        XSSFCell cell0 = row_title.createCell(0);
        //给单元格赋值
        cell0.setCellValue("name");

        XSSFCell cell1 = row_title.createCell(1);
        //给单元格赋值
        cell1.setCellValue("count");

        XSSFCell cell2 = row_title.createCell(2);
        //给单元格赋值
        cell2.setCellValue("max_" + col1);

        XSSFCell cell3 = row_title.createCell(3);
        //给单元格赋值
        cell3.setCellValue("max_" +col2);
        XSSFCell cell4 = row_title.createCell(4);
        //给单元格赋值
        cell4.setCellValue("min_" + col1);
        XSSFCell cell5 = row_title.createCell(5);
        //给单元格赋值
        cell5.setCellValue("min_" +col2 );
        XSSFCell cell6 = row_title.createCell(6);
        //给单元格赋值
        cell6.setCellValue("null_"+col1 + "_count");
        XSSFCell cell7 = row_title.createCell(7);
        //给单元格赋值
        cell7.setCellValue("null_"+col2 + "_count");

        int i = 1;
        for ( String dbname: db.split(",")) {

            JDBC mysql_jdbc = null;
            JDBC mysql_jdbc2 = null;
//            JDBC table = null;
            try {
//                table = new JDBC(DatabaseType.SQL_SERVER, "information_schema");
                mysql_jdbc = new JDBC(DatabaseType.SQL_SERVER, dbname);
                ResultSet rstab = mysql_jdbc.executeQuery(sql_get_table, dbname);
                mysql_jdbc2 = new JDBC(DatabaseType.SQL_SERVER, dbname);
                while (rstab.next()) {
                    String table_name = rstab.getString(1);
                    if (StringUtils.startsWith(table_name, "temp_") )
                        continue;
                    //创建行,0表示第一行
                    XSSFRow row = sheet.createRow(i);
                    //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
                    XSSFCell cell0l = row.createCell(0);
                    //给单元格赋值
                    cell0l.setCellValue(dbname + "." + table_name);
                    //创建输出流
                    XSSFCell cell1l = row.createCell(1);

                    //给单元格赋值
//                cell1l.setCellValue(get(sql_count, table_name, mysql_jdbc));
                    cell1l.setCellValue(get(sql_count, table_name, mysql_jdbc2));
                    XSSFCell cell2l = row.createCell(2);
////                //给单元格赋值
//                cell2l.setCellValue(update_time);
                    cell2l.setCellValue(get(sql_update_time, table_name, mysql_jdbc2));
//
                    XSSFCell cell3l = row.createCell(3);

                    cell3l.setCellValue(get(sql_create_time, table_name, mysql_jdbc2));

                    XSSFCell cell4l = row.createCell(4);

                    cell4l.setCellValue(get(sql_update_time_min, table_name, mysql_jdbc2));

                    XSSFCell cell5l = row.createCell(5);

                    cell5l.setCellValue(get(sql_create_time_min, table_name, mysql_jdbc2));

                    XSSFCell cell6l = row.createCell(6);

                    cell6l.setCellValue(get(sqlnullUpdateTime, table_name, mysql_jdbc2));

                    XSSFCell cell7l = row.createCell(7);

                    cell7l.setCellValue(get(sqlnullCreateTime, table_name, mysql_jdbc2));
                    i++;
                }
//                rstab.close();
                mysql_jdbc.close();
                mysql_jdbc2.close();
//                table.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public static String get(String sql, String table, JDBC stm) {
        try {
            System.out.println(sql.replace(Constants.SQL_PH, table));
            result = stm.executeQuery(sql, table);

            if (result.next()) return result.getObject(1).toString();
        } catch (Exception e) {
        } finally {
            try {
                result.close();
            } catch (SQLException e) {
            }
        }
        return "";
    }

    public static void close() {
        try {
            result.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }


}
