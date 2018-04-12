package com.dsglyy.warehouse;

import com.dsg.common.constants.Constants;
import com.dsg.common.constants.DatabaseType;
import com.dsg.common.utils.JDBC;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

//    public static String DATA="'alipay_bill_detail','bonus_return_tmp','cash_account','cash_account_tmp','cash_bonus_return','cash_change',\n" +
//            "'cash_draw','cash_freeze_queue','cash_money_draw','cash_pay','cash_protect','cash_recharge','cash_refundment',\n" +
//            "'com_bank','com_code','credit_cark_user','department_store_card','expired_bonus','fin_recon_bank_statement',\n" +
//            "'fin_recon_result','fin_white_user','instalment_bu_category_config','instalment_exclusive_product',\n" +
//            "'instalment_gateway_config','instalment_white_product','intf_order_20170828_temp_5','offline_pay_binding',\n" +
//            "'offline_pay_register','offline_pay_register_seq','ord_refund_apply','ord_refund_process_job','ord_refundment',\n" +
//            "'ord_refundment_item','ord_refundment_item_prod','ord_refundment_item_split','pay_advert','pay_decision_attribute',\n" +
//            "'pay_gateway_rout_weight','pay_global_params_control','pay_instalment_order_trace','pay_payment','pay_payment_detail',\n" +
//            "'pay_payment_gateway','pay_payment_gateway_config','pay_payment_gateway_element','pay_payment_gateway_tab_config',\n" +
//            "'pay_payment_geex_detail','pay_payment_ihui_detail','pay_payment_refundment','pay_pos','pay_pos_commercial',\n" +
//            "'pay_pos_user','pay_pre_payment','pay_receiving_bank','pay_receiving_company','pay_stats_daily','pay_supplier_payment',\n" +
//            "'pay_supplier_refundment','pay_transaction','pay_virtual_credit_card','presell_refundment','stored_card','stored_card_batch',\n" +
//            "'stored_card_in','stored_card_lock','stored_card_order_bind','stored_card_order_import','stored_card_out','stored_card_out_details',\n" +
//            "'stored_card_stock','stored_card_usage'";
//    select * from ods_lvmama_user_usr_receivers  where receiver_id not rlike '^\\d+$'
    public static String DATA = "biz_branch\n" +
        "biz_brand\n" +
        "biz_dest_content\n" +
        "biz_traffic_line\n" +
        "prod_route_feature";

    public static void main(String[] dsg) {
        //创建工作簿
        workbook = new XSSFWorkbook();
        //新建工作表
        sheet = workbook.createSheet("data_test1");

        XSSFRow row_title = sheet.createRow(0);
        //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
        XSSFCell cell0 = row_title.createCell(0);
        //给单元格赋值
        cell0.setCellValue("name");

        XSSFCell cell1 = row_title.createCell(1);
        //给单元格赋值
        cell1.setCellValue("count");

        XSSFCell cell2 = row_title.createCell(2);
        //给单元格赋值
        cell2.setCellValue("update_time");

        XSSFCell cell3 = row_title.createCell(3);
        //给单元格赋值
        cell3.setCellValue("create_time");

        String sql_count = "select count(1) from ? ";
        String sql_update_time = "select max(UPDATE_TIME) from ?  ";
        String sql_create_time = "select max(create_time) from ? ";

        String sql_test = "SELECT count(1) FROM ? where  update_time >= DATE_FORMAT('20150618','%Y-%m-%d')  and update_time < DATE_FORMAT('20180315','%Y-%m-%d');";
        String all_sql_test = "SELECT count(1) FROM ? ;";
        String hive_sql = "select count(1) from ods.ods_lvmama_ver_? where par_day=20161218";
        String[] tables = DATA.replace("'", "").split("\n");
        JDBC hive_jdbc =null ;
        JDBC mysql_jdbc = null ;
        try {
            hive_jdbc = new JDBC(DatabaseType.HIVE_SERVER2, "ods");
//            hive_jdbc.getStatement();
            mysql_jdbc = new JDBC(DatabaseType.ORACLE, "lvmamadb");
            System.out.println(tables.length);
            for (int i = 0; i < tables.length; i++) {
//                System.out.println(tables[i] + "\t" + get(sql_count, tables[i], mysql_jdbc) + "\t" + get(sql_update_time, tables[i], mysql_jdbc) + "\t" + get(sql_create_time, tables[i], mysql_jdbc));
                String table_name = "LVMAMA_VER." + tables[i];
                String ori = get(sql_count, table_name, mysql_jdbc).toString();
                String wh  = get(hive_sql, tables[i], hive_jdbc).toString();
//
//                System.out.println(tables[i] + "\t" + ori + "\t" + wh + "==============");
                //创建行,0表示第一行
                XSSFRow row = sheet.createRow(i + 1);
                //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
                XSSFCell cell0l = row.createCell(0);
                //给单元格赋值
                cell0l.setCellValue(table_name);
                //创建输出流
                XSSFCell cell1l = row.createCell(1);
                //给单元格赋值
//                cell1l.setCellValue(get(sql_count, table_name, mysql_jdbc));
                cell1l.setCellValue(ori);
                XSSFCell cell2l = row.createCell(2);
//                //给单元格赋值
                cell2l.setCellValue(wh);
//                cell2l.setCellValue(get(sql_update_time, table_name, mysql_jdbc));
//
//                XSSFCell cell3l = row.createCell(3);
//                //给单元格赋值
//                cell3l.setCellValue(get(sql_create_time, table_name, mysql_jdbc));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
//        hive_jdbc.close();
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
