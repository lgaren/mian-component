package com.dsg.common.utils;

import java.io.*;

/**
 * Created on 2018/9/20  Thu AM 09:55
 * producttag
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: FileEncode V 0.0, Sep 20, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class FileEncode {

    public static void change(String file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
        String line = "";
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\1\\Desktop\\000"),"UTF-8")));
        while((line = in.readLine())  != null ){
            out.println(line);
        }
        out.flush();
        out.close();
        in .close();
    }

    public static void main(String[] args) throws IOException {
        change("C:\\Users\\1\\Desktop\\31个标签.txt");
    }
}
