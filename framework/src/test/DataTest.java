package com.dsglyy.warehouse;

import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;
import com.dsg.common.funct.Function10;
import com.dsg.common.funct.Function21;
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
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    static String sql_data_count =  "select count(1) from {table} ";
    static String sql_coln_max = "select max(?) from {table}  " ;
    static String sql_coln_min = "select min(?) from {table}  ";
    static String sql_coln_null_count = "select count(1) from {table} where ? is null " ;
    static int index = 0;
    //        String sql_get_table = "select table_name from TABLES where TABLE_SCHEMA='?' ";
    /**
     * 获取一个库的所有表格的语句
     */
    static String sql_get_table = "select name from sysobjects where xtype='u' ";

    //        多个db 用逗号隔开；
    static String db = "joyu" ;

    /**
     *支持一个正则表达式，被此正则表达式匹配到的表格，将不做统计
     */
    static String tableFilter  = "temp_" ;

    public static void main(String[] dsg) {
        //创建工作簿

        workbook = new XSSFWorkbook();
        //新建工作表
        sheet = workbook.createSheet("data_test1");

        String cols = "dupdate,dcreate";

        ((Function10<String>)  DataTest::getDataSum).apply(cols);
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

    public static void writeToExce(XSSFRow row, String value) {
        row.createCell(index).setCellValue(value);
        index++;
    }
    public static void parseValue(XSSFRow row, String... value) {
        Arrays.stream(value).forEach(e -> writeToExce(row, e));
    }

//    public static void parseValue(XSSFRow row,Function<String,String> funVal, String... values) {
//        Arrays.stream(values).forEach(value -> writeToExce(row, funVal.apply(value)));
//    }
    public static void parseValue(String[] stream, XSSFRow row,Function21<String,String,String> funVal, String... values) {
        Arrays.stream(stream).forEach(col -> {
            Arrays.stream(values).forEach(value -> writeToExce(row, ((Function<String,String>) value1 ->funVal.apply(col,value1)).apply(value)));
        });
    }

    public static void getDataSum(String cols) {
        String[] colList = cols.split(Constants.COMMA);
        XSSFRow row_title = sheet.createRow(0);
        parseValue(row_title, "name", "count");
        parseValue(colList, row_title,(col,value) -> value.replaceFirst(Constants.SQL_PH,col), "max_?" , "min_?" , "null_?_count");
        int i = 1;
        for (String dbname : db.split(Constants.COMMA)) {
            try {
                JDBC  table = new JDBC(DatabaseType.SQL_SERVER, dbname);
                ResultSet rstab = table.executeQuery(sql_get_table, dbname);
                final JDBC  info = new JDBC(DatabaseType.SQL_SERVER, dbname);
                while (rstab.next()) {
                    String table_name = rstab.getString(1);
                    Pattern r = Pattern.compile(tableFilter);
                    Matcher mat = r.matcher(table_name);
                    if (mat.find())
                        continue;
                    index = 0;
                    XSSFRow row = sheet.createRow(i);
                    parseValue(row, dbname + "." + table_name, get(sql_data_count,  info,table_name));
                    parseValue(colList,row, (col,value) -> get(value.replace("{table}",table_name) ,  info, col), sql_coln_max,sql_coln_min,sql_coln_null_count);
                    i++;
                }
                table.close();
                info.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String get(String sql, JDBC stm, String... replaces) {
        try {
            result = stm.executeQuery(sql, replaces);

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
}
