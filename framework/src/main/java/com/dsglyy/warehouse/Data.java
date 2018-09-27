package com.dsglyy.warehouse;

import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;
import com.dsg.common.funct.Function10;
import com.dsg.common.funct.Function21;
import com.dsg.common.funct.Function31;
import com.dsg.common.utils.JdbcFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

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
 * Created on 2018/7/24  Tue PM 13:37
 * mian-component
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: Data V 0.0, Jul 24, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class Data {

//    public static ResultSet result = null;
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

        ((Function10<String>)  Data::getDataSum).apply(cols);
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

    public static void writeToExce(XSSFRow row, String value,CellStyle stle) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(stle);

        index++;
    }
    public static void parseValue(XSSFRow row,CellStyle stle, String... value) {
        Arrays.stream(value).forEach(e -> writeToExce(row, e,stle));
    }

    static int rownum = 1;

    public static void getDataSum(String cols) {
        Pattern r = Pattern.compile(tableFilter);
        int i = 1;
//        style.setFillPattern(style.SOLID_FOREGROUND);

        CellStyle stylesc = workbook.createCellStyle();
        stylesc.setFillForegroundColor(IndexedColors.AQUA.getIndex());

        for (String dbname : db.split(Constants.COMMA)) {
            try {
                JdbcFactory.JDBC table = JdbcFactory.get(DatabaseType.SQL_SERVER, dbname);
                JdbcFactory.JDBC table_col = JdbcFactory.get(DatabaseType.SQL_SERVER, dbname);
                ResultSet rstab = table.executeQuery(sql_get_table, dbname);
                final JdbcFactory.JDBC info = JdbcFactory.get(DatabaseType.SQL_SERVER, dbname);
                while (rstab.next()) {
                    String table_name = rstab.getString(1);
                    ResultSet collis = table_col.executeQuery(get_col,table_name);
                    rownum ++ ;
                    List<String> colList12 = new ArrayList();
                    XSSFRow row = sheet.createRow(rownum);
                    parseValue(row, stylesc,dbname + "." + table_name);
                    while (collis.next()){
                        String col = collis.getString(2);
                        parseValue(row,stylesc, col);
                        colList12.add(col);
                    }
                    collis.close();
                    Matcher mat = r.matcher(table_name);
                    if (mat.find())
                        continue;
                    get("select * from {table} where  dupdate >= '2018-07-23' And  dupdate <= '2018-07-24'".replace("{table}",table_name) ,  info, colList12);
                    index = 0;
                    i++;
                }
                rstab.close();
                table.close();
                info.close();
                table_col.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void get(String sql, JdbcFactory.JDBC stm,List<String> cols) {
//        ResultSet result = null;
        System.out.println(sql );
        CellStyle style = workbook.createCellStyle();


        final ResultSet result;
        try {
            result = stm.executeQuery(sql);
            while (result.next()) {
                rownum  ++  ;
                XSSFRow row = sheet.createRow(rownum );
                index = 1;
                cols.forEach(col -> {
                    try {
                        writeToExce(row,result.getObject(col).toString(),style);
                    } catch (Exception e) {
                        index ++ ;
                    }
                });
            }
            result.close();
//            rownum  ++  ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}