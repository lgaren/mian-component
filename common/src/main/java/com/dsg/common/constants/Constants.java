package com.dsg.common.constants;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created on 11/21/17
 * seagull
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version: HiveConstants V 0.0, 11/21/17 10:26
 * <p>
 * Description:
 */
public class Constants {

    public final static String CONF_DIR = System.getProperty("project.configuration") + File.separator;

//    public final static String CONF_DIR = HOME_DIR + ".." + File.separator + "conf" + File.separator;

    public final static String LEFT = "{";
    public final static String RIGHT = "}";

    public final static String TRAN = "-";


    public static final String SPACE = " ";

    public static final String EMPTY = "";

    public static final String LF = "\n";

    public static final String CR = "\r";

    public static final int INDEX_NOT_FOUND = -1;

    public static final String URL_PARAMS_SPLIT = "/";

    public static final String SPLIT = ":";

    public static final String SQL_PH = "?";

    public static final String  confFile= "dsg-site.xml";

    /** 最大的字符数量 */
    final public static String[] emptyStringArray = {};

    //	 private final static Interner<String> strongInterner;
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

//    public final static String HIVESERVER2_URL = "jdbc:hive2://10.201.4.51:10000/ods";

//    public final static String HIVE_SERVER2_DRIVERNAME = "org.apache.hive.jdbc.HiveDriver";

}