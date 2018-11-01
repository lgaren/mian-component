package com.dsg.common.conf;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2018/7/20  Fri PM 16:12
 * mian-component
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: ConfigLoad V 0.0, Jul 20, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */


public class ConfigLoad {

  public static   String pattern = "\\$\\{(.*)\\}";
    public static   Pattern r = Pattern.compile(pattern);

    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        try {
           Object  ret = yaml.load(new FileInputStream(System.getProperty("conf")));
            getValue("",ret);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void getValue(String  keyPre, Object obj){
        if (obj instanceof  Map) ((Map)obj).forEach((key,value) -> {
            if (keyPre.equals(""))  getValue(keyPre  + key, value);
            else getValue(keyPre + "." + key, value);
        });
       else  {
            try {
                String str = obj.toString();
                Matcher mat = r.matcher(str);

                if (mat.find()){
                    System.out.println(mat.group(0));
                    System.out.println(mat.group(1));
                }
                System.out.println(keyPre + "==="+obj.toString() );
            }catch (NullPointerException e){
                System.out.println(keyPre + "==="+null );
            }

           }
    }

}
