package com.dsglyy.warehouse;

import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;
import com.dsg.common.funct.Function10;
import com.dsg.common.funct.Function21;
import com.dsg.common.utils.JdbcFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2018/7/24  Tue AM 11:17
 * mian-component
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: DataTestUp V 0.0, Jul 24, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class DataTestUp {

    public static ResultSet result = null;
    static XSSFWorkbook workbook;
    static XSSFSheet sheet;

    static String sql_data_count =  "select count(1) from ? ";
    static String sql_coln_max = "select max(?) from {table}  " ;
    static String sql_coln_min = "select min(?) from {table}  ";
    static String sql_coln_null_count = "select count(1) from {table} where ? is null " ;
    static int index = 0;
    //        String sql_get_table = "select table_name from TABLES where TABLE_SCHEMA='?' ";
    /**
     * 获取一个库的所有表格的语句
     */
    static String sql_get_table = "select name from sysobjects where xtype='u' ";


    static String get_col = "SELECT d.name as tab_name, " +
            "a.name as col_name, " +
            "b.name as coltype " +
            "FROM   syscolumns   a " +
            "left   join   systypes   b   on   a.xusertype=b.xusertype " +
            "inner  join   sysobjects   d   on   a.id=d.id     and   d.xtype='U'   and     d.name<>'dtproperties' " +
            "where d.name='?'";
//  SELECT t.table_name FROM all_tables  t WHERE   t.OWNER='BI_USER'





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

        ((Function10<String>)  DataTestUp::getDataSum).apply(cols);
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
    public static void parseValue(String[] stream, XSSFRow row, Function21<String,String,String> funVal, String... values) {
        Arrays.stream(stream).forEach(col -> {
            Arrays.stream(values).forEach(value -> {
                writeToExce(row, funVal.apply(col,value));
            });
        });
    }

    public static void parseValue(List<String> stream, XSSFRow row, Function21<String,String,String> funVal, String... values) {
        stream.forEach(col -> {
            Arrays.stream(values).forEach(value -> {
                writeToExce(row,col);
                writeToExce(row, funVal.apply(col,value));
            });
        });
    }
    //    smalldatetime ,timestamp,datetime
    public static void getDataSum(String cols) {
        String[] colList = cols.split(Constants.COMMA);
        Pattern r = Pattern.compile(tableFilter);
        Pattern colr = Pattern.compile("smalldatetime|timestamp|datetime");
        XSSFRow row_title = sheet.createRow(0);
        parseValue(row_title, "name", "count");
        parseValue(colList, row_title,(col,value) -> value.replaceFirst(Constants.SQL_PH,col), "max_?" , "min_?");
        int i = 1;
        for (String dbname : db.split(Constants.COMMA)) {
            try {
                JdbcFactory.JDBC table = JdbcFactory.get(DatabaseType.SQL_SERVER, dbname);
                JdbcFactory.JDBC table_col = JdbcFactory.get(DatabaseType.SQL_SERVER, dbname);
                ResultSet rstab = table.executeQuery(sql_get_table, dbname);
                final JdbcFactory.JDBC info = JdbcFactory.get(DatabaseType.SQL_SERVER, dbname);
                while (rstab.next()) {
                    String table_name = rstab.getString(1);
                    ResultSet collis = table_col.executeQuery(get_col,table_name);
                    List<String> colList12 = new ArrayList();
                    while (collis.next()){
                        Matcher mat = colr.matcher(collis.getString(3));
//                        System.out.println(collis.getString(3) + "=========");
                        if (mat.find()) colList12.add(collis.getString(2));
                    }
                    collis.close();
                    Matcher mat = r.matcher(table_name);
                    if (mat.find())
                        continue;
                    index = 0;
                    XSSFRow row = sheet.createRow(i);
                    parseValue(row, dbname + "." + table_name, get(sql_data_count,  info,table_name));
                    parseValue(colList12,row, (col,value) -> get(value.replace("{table}",table_name) ,  info, col), sql_coln_max);
                    i++;
                }
                table.close();
                info.close();
                table_col.close();
            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
            } catch (SQLException e) {
//                e.printStackTrace();
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    public static String get(String sql, JdbcFactory.JDBC stm, String... replaces) {
        try {
            result = stm.executeQuery(sql, replaces);
            if (result.next()) return result.getObject(1).toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (SQLException e) {
            }
        }
        return "";
    }
}

