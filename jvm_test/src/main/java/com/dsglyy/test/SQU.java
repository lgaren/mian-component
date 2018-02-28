package com.dsglyy.test;

import com.dsglyy.jdk8Test.lambdaFunctional.FunctionalInter;

import java.io.*;
import java.util.*;


/**
 * Created on 2017/12/192017 十二月 星期二下午 19:22
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  SQU, V 0.0  2017/12/19 下午 19:22 DSG Exp$$
 */
public class SQU {
    public static Map<String, String> old = new LinkedHashMap<String, String>();
    public static Map<String, String> table = new LinkedHashMap<String, String>();
    public static List write = new LinkedList<String>();

    public static void main(String[] args) {
        fileUtils((table_name, line) -> {
                    String[] split = line.split(" ");
                    boolean flag = false;
                    String commont = "";
                    for (String world : split) {
                        if (flag) commont = commont + world;
                        if (world.equals("COMMENT")) flag = true;
                        else continue;
                    }
                    old.put((table_name + split[0]).toLowerCase(), commont);
//                    System.out.println(commont);
                    return table_name;
                },
                (table_name, line) -> {
                    String[] split = line.split(" ");
                    String table_commont = "";
                    for (int i = 2; ; i++) {
                        if (split[i].equals("PARTITIONED")) {
                            break;
                        } else {
                            table_commont = table_commont + split[i] + " ";
                        }
                    }
                    table.put(table_name.toLowerCase(), table_commont);
//                    System.out.println(split[2]);   962   912
                    return line;
                }
                , "D:\\Program_Data\\IdeaProjects\\mian-component\\jvm_test\\src\\main\\java\\com\\dsglyy\\test\\old_sql", false
        );

        fileUtils((table_name, line) -> {
                    String[] split = line.split(" ");
                    String commont = old.get((table_name + split[0]).toLowerCase());
                    if (split[0].indexOf("amount") >= 0 && split[0].toLowerCase().indexOf("id") == -1 && line.indexOf("金额") >= 0 && line.indexOf("类型") == -1) {
                        return split[0] + " " + "DECIMAL (28, 10) COMMENT " + commont;
                    }
                    ;
                    if (split[0].indexOf("money") >= 0 && split[0].toLowerCase().indexOf("id") == -1 && line.indexOf("金额") >= 0 && line.indexOf("类型") == -1) {
                        return split[0] + " " + "DECIMAL (28, 10) COMMENT " + commont;
                    }
                    ;
                    if (split[0].indexOf("price") >= 0 && split[0].toLowerCase().indexOf("id") == -1 && line.indexOf("价格") >= 0 && line.indexOf("类型") == -1) {
                        return split[0] + " " + "DECIMAL (28, 10) COMMENT " + commont;
                    }
                    ;
                    String new_line = "";
                    for (String world : split) {
                        new_line = new_line + world + " ";
                        if (world.equals("COMMENT")) break;
                    }
                    return new_line + commont;
                },
                (e1, e2) -> {
                    String[] split = e2.split(" ");
                    String commont = table.get(e1.toLowerCase());
                    String new_line = split[0] + " " + "COMMENT" + " " + commont;
                    for (int i = 3; i < split.length; i++) {
                        new_line = new_line + " " + split[i];
//                    String new_line = split[0] + " " + split[1] + " " + split[2] + " " + commont;
                    }
                    return new_line;
                }, "D:\\Program_Data\\IdeaProjects\\mian-component\\jvm_test\\src\\main\\java\\com\\dsglyy\\test\\new_sql", true
        );

        write.forEach(e -> System.out.println(e));
//        old.forEach((k,v) ->
//        System.out.println(k + "  " + v)
//        );

    }

    public static void fileUtils(FunctionalInter.Functionally<String, String, String> fun, FunctionalInter.Functionally<String, String, String> fun2, String file, boolean wFlag) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), "utf-8"));
            String line = null;
            boolean flag = false;
            String table_name = null;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("ROW FORMAT DELIMITED FIELDS TERMINATED BY") >= 0) {
                    flag = false;
                    line = fun2.accept(table_name, line);
                }
                if (flag) line = fun.accept(table_name, line.replace("\t", ""));

                if (line.indexOf("IF NOT EXISTS") >= 0) {
                    flag = true;
                    table_name = line.split(" ")[3];
                }
                if (wFlag) write.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
