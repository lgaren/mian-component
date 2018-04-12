package com.dsg.common.constants;

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

    final public static String[] emptyStringArray = {};

    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
}