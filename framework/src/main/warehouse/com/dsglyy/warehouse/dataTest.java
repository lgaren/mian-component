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
public class dataTest {


//    public static Connection con = null;
//    public static Statement stmt = null;
    public static ResultSet result = null;

//    public static Connection hiveServer2Conn = null;
//    public static Statement hiveServer2Stmt = null;

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

    public static String DATA = "pay_transaction\n" +
            "pay_payment\n" +
            "cash_account\n" +
            "cash_bonus_return\n" +
            "cash_change\n" +
            "cash_protect\n" +
            "expired_bonus\n" +
            "ord_refundment_item\n" +
            "ord_refundment\n" +
            "fin_recon_bank_statement\n" +
            "ord_refundment_item_prod\n" +
            "pay_payment_refundment\n" +
            "cash_pay\n" +
            "ord_refundment_item_split\n" +
            "ord_refund_apply\n" +
            "stored_card\n" +
            "pay_pre_payment\n" +
            "pay_payment_detail\n" +
            "cash_refundment\n" +
            "cash_recharge\n" +
            "stored_card_usage\n" +
            "cash_money_draw\n" +
            "cash_draw\n" +
            "offline_pay_binding\n" +
            "offline_pay_register\n" +
            "pay_payment_ihui_detail\n" +
            "stored_card_out_details\n" +
            "stored_card_out\n" +
            "stored_card_in\n" +
            "ord_refund_process_job\n" +
            "cash_freeze_queue\n" +
            "fin_white_user\n" +
            "pay_pos_user\n" +
            "pay_pos\n" +
            "stored_card_order_bind\n" +
            "stored_card_order_import\n" +
            "pay_payment_gateway\n" +
            "pay_global_params_control\n" +
            "pay_receiving_bank\n" +
            "instalment_exclusive_product\n" +
            "pay_gateway_rout_weight\n" +
            "pay_stats_daily\n" +
            "instalment_bu_category_config\n" +
            "instalment_gateway_config\n" +
            "pay_payment_gateway_tab_config\n" +
            "pay_payment_gateway_config\n" +
            "pay_payment_gateway_element\n" +
            "cash_account_tmp\n" +
            "pay_receiving_company\n" +
            "pay_decision_attribute\n" +
            "presell_refundment\n" +
            "credit_cark_user\n" +
            "stored_card_stock\n" +
            "stored_card_batch\n" +
            "fin_recon_result\n" +
            "pay_pos_commercial\n" +
            "com_bank\n" +
            "pay_payment_geex_detail\n" +
            "com_code\n" +
            "pay_virtual_credit_card\n" +
            "pay_instalment_order_trace\n" +
            "department_store_card\n" +
            "offline_pay_register_seq\n" +
            "pay_advert\n" +
            "pay_supplier_payment\n" +
            "pay_supplier_refundment\n" +
            "stored_card_lock\n";

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

        String sql_count = "select count(1) from ? ;";
        String sql_update_time = "select UPDATE_TIME from ?  ORDER BY  UPDATE_TIME  desc limit 1 ;";
        String sql_create_time = "select create_time from ?  ORDER BY  UPDATE_TIME  desc limit 1 ;";

        String sql_test = "SELECT count(1) FROM ? where  update_time >= DATE_FORMAT('20150618','%Y-%m-%d')  and update_time < DATE_FORMAT('20180314','%Y-%m-%d');";
        String all_sql_test = "SELECT count(1) FROM ? ;";
        String hive_sql = "select count(1) from ods.ods_lvmama_pay_? where par_day=20150618";
        String[] tables = DATA.replace("'", "").split("\n");
        JDBC hive_jdbc =null ;
        JDBC mysql_jdbc = null ;
        try {
//            hive_jdbc = new JDBC(JDBC.DatabaseType.HIVE_SERVER2, "ods");
            mysql_jdbc = new JDBC(DatabaseType.MYSQL, "lvmama_pay");
            System.out.println(tables.length);
            for (int i = 0; i < tables.length; i++) {
//                System.out.println(tables[i] + "\t" + get(sql_count, tables[i], mysql_jdbc) + "\t" + get(sql_update_time, tables[i], mysql_jdbc) + "\t" + get(sql_create_time, tables[i], mysql_jdbc));

                String ori = get(sql_test, tables[i], mysql_jdbc).toString();
//                String wh = get(hive_sql, tables[i], hive_jdbc).toString();
//
//                System.out.println(tables[i] + "\t" + ori + "\t" + wh + "==============");
                //创建行,0表示第一行
                XSSFRow row = sheet.createRow(i + 1);
                //创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
                XSSFCell cell0l = row.createCell(0);
                //给单元格赋值
                cell0l.setCellValue(tables[i]);
                //创建输出流
                XSSFCell cell1l = row.createCell(1);
                //给单元格赋值
//                cell1l.setCellValue(get(sql_count, tables[i], mysql_jdbc));
//                cell1l.setCellValue(ori);
//                XSSFCell cell2l = row.createCell(2);
//                //给单元格赋值
////                cell2l.setCellValue(wh);
//                cell2l.setCellValue(get(sql_update_time, tables[i], mysql_jdbc));
//
//                XSSFCell cell3l = row.createCell(3);
//                //给单元格赋值
//                cell3l.setCellValue(get(sql_create_time, tables[i], mysql_jdbc));
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
