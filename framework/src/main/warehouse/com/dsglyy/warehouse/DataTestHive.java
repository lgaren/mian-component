package com.dsglyy.warehouse;

import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;
import com.dsg.common.utils.JDBC;
import com.dsg.common.utils.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created on 2018/5/17  Thu PM 18:27
 * mian-component
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: DataTestHive V 0.0, May 17, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class DataTestHive {

    public static ResultSet result = null;
    static XSSFWorkbook workbook;
    static XSSFSheet sheet;

    public static String DATA = "supp_goods_time_price\n" +
            "prod_product_branch_prop\n" +
            "com_photo\n" +
            "dist_distributor_goods\n" +
            "prod_product_prop\n" +
            "prod_product_attr\n" +
            "o_simple_supp_goods_time_price\n" +
            "prod_product_branch\n" +
            "prod_hotel_group_date\n" +
            "dist_channel_goods\n" +
            "supp_goods\n" +
            "prod_product\n" +
            "ebk_prod_apply\n" +
            "prod_subject\n" +
            "supp_goods_rebate\n" +
            "goods_sale_plan_info\n" +
            "product_log\n" +
            "productn_log\n" +
            "prod_tag\n" +
            "hotel_foreigh_time_price\n" +
            "prod_dest_re\n" +
            "supp_goods_time_p170425\n" +
            "prod_product_brand\n" +
            "prom_presnet_goods\n" +
            "exchange_rate_info\n" +
            "res_precontrol_time_price\n" +
            "supp_goods_group_stock\n" +
            "ebk_prod_price";

    public static void main(String[] dsg) {
        //创建工作簿
        workbook = new XSSFWorkbook();
        //新建工作表
        sheet = workbook.createSheet("data_audit");

        XSSFRow row_title = sheet.createRow(0);
        //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
        XSSFCell cell0 = row_title.createCell(0);
        //给单元格赋值
        cell0.setCellValue("name");

        XSSFCell cell1 = row_title.createCell(1);
        //给单元格赋值
        cell1.setCellValue("hive_count");

        XSSFCell cell2 = row_title.createCell(2);
        //给单元格赋值
        cell2.setCellValue("ori_count");

        String sql_count = "select count(1) from ? ";

        String sql_test = "SELECT count(1) FROM ? where  update_time >= DATE_FORMAT('20150618','%Y-%m-%d')  and update_time < DATE_FORMAT('20180315','%Y-%m-%d');";
        String all_sql_test = "SELECT count(1) FROM ? ;";
        String hive_sql = "select count(1) from ods.ods_lvmama_hotel_? ";
        String[] tables = DATA.replace("'", "").split("\n");
        JDBC hive_jdbc = null;
        JDBC mysql_jdbc = null;
        try {
            hive_jdbc = new JDBC(DatabaseType.HIVE_SERVER2, "ods");

//            hive_jdbc.getStatement();
            mysql_jdbc = new JDBC(DatabaseType.MYSQL, "db0");
//            System.out.println(tables.length);
            for (int i = 0; i < tables.length; i++) {
                String table_name = tables[i];
                //创建行,0表示第一行
                XSSFRow row = sheet.createRow(i + 1);
                //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
                XSSFCell cell0l = row.createCell(0);
                //给单元格赋值
                cell0l.setCellValue(table_name);
                //创建输出流
                XSSFCell cell1l = row.createCell(1);
                //给单元格赋值
                cell1l.setCellValue(get(hive_sql, table_name, hive_jdbc));
                XSSFCell cell2l = row.createCell(2);
////                //给单元格赋值
                cell2l.setCellValue(get(sql_count, table_name, mysql_jdbc));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        StringUtils.deleteWhitespace();
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
        hive_jdbc.close();
        mysql_jdbc.close();
    }

    public static String get(String sql, String table, JDBC stm) {
        try {
            System.out.println(sql.replace(Constants.SQL_PH, table));
            result = stm.executeQuery(sql, table);

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

    public static void close() {
        try {
            result.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

}
