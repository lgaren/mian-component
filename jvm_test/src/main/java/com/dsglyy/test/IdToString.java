package com.dsglyy.test;

import com.dsglyy.jdk8Test.lambdaFunctional.FunctionalInter;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/5/11  Fri AM 09:50
 * mian-component   将sql 里面的id 字段转化为String
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: IdToString V 0.0, May 11, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class IdToString {

    public static Map<String, String> table = new LinkedHashMap<String, String>();
    public static List write = new LinkedList<String>();


    public static void main(String[] args) {

        BufferedReader br = null;
        BufferedWriter br1 = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:\\Program_Data\\IdeaProjects\\mian-component\\jvm_test\\src\\main\\java\\com\\dsglyy\\test\\old_sql")), "utf-8"));
            String line;
            br1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Program_Data\\IdeaProjects\\mian-component\\jvm_test\\src\\main\\java\\com\\dsglyy\\test\\code_sql")), "utf-8"));
            while ((line = br.readLine()) != null) {
                line = line.toLowerCase();
                String[] split = line.split(" ");
//                    String commont = old.get((table_name + split[0]).toLowerCase());
//                    System.out.println( split[0]);
                if (split[0].indexOf("id") >= 0 && !(split[0].indexOf("baidu")>=0)) {
                    String newline = split[0] + " " + "string";
                    for (int i = 2; i < split.length; i++) {
                        newline = newline + " " + split[i];
                    }
                    System.out.println(newline);
                    br1.write(newline);
                } else {
                    System.out.println(line);
                    br1.write(line);
                }
                br1.write("\n");
            }
            br1.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                br1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

