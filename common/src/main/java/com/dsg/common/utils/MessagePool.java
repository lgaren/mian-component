package com.dsg.common.utils;


import com.dsg.common.constants.Constants;
import com.dsg.common.constants.SecurityConstants;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MessagePool {

//    public static Map<String ,String > position = new LinkedHashMap<>();
    public static  StringBuffer message = new StringBuffer();

//    public static long dataCount = 0l;

    public static StringBuffer  addErrorMessage(String str){
        addError();
        message.append(str);
        SecurityConstants.ERROR_LOG.error(str);
        return message ;
    }

    public static void printStackTraceToString(Throwable t) {
        SecurityConstants.ERROR_LOG.error(t.getMessage(),t);
        addError();
        message.append(t.getMessage()).append(Constants.LF);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        message.append(sw.getBuffer().toString().replace("\n",Constants.HTML_LF).replace("\tat", Constants.HTML_SPACE +Constants.HTML_SPACE+Constants.HTML_SPACE + "at"));
    }

    private static void addError(){
        message.append(Constants.LF).append("ERROR : ");
    }

    public static String  addMessage(Object str){
        message.append(Constants.LF).append(str);
        return str.toString() ;
    }

    public static String  addErrorMessage(StringBuilder str){
        addError();
        message.append(str).append(Constants.LF);
        return str.toString() ;
    }
}
