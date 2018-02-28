package com.dsg.common.constants;

/**
 * Created on 11/27/17
 * seagull
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version: HIveGet V 0.0, 11/27/17 17:43
 * <p>
 * Description:
 */
public  enum ErrorCode {
    AUTHEN_FAIL(1000,"access denied!"),
    FILE_ONT_FOUND(1001,"The file you visited does not exist! "),
    FILE_TOO_LARGE(1002,"The file size you visited exceeds limit!"),
    TABLE_NOT_FOUND(1003,"The table  you visited does not found. "),
    TABLE_ROWCOUNT_EXCEEDS_LIMIT(1004,"The table's rowcount  you visited exceeds limit!"),
    TABLE_SIZE_EXCEEDS_LIMIT(1005,"The table data size  you visited exceeds limit!"),
    SESSION_TIMEOUT(1006,"Is you first coming ? Or your session already time out ,please use api '/getTable' !"),
    NEED_PARAMS(1007,"Need params!")
    ;


    private final int errorCode;
    private  final String errorMessage;

    ErrorCode(int code,String message){
        this.errorCode = code;
        this.errorMessage = message;
    }

    public String getMessage(){
        return errorMessage;
    }

    public int getErrorCode(){return errorCode;}

    public static String getMessage(int code){
        ErrorCode[] values = ErrorCode.values();
        for ( ErrorCode error:values) {
            if (error.errorCode == code ) return error.errorMessage;
        }
        return "error not defined";
    }

    public static int getErrorCode(String message){
        ErrorCode[] values = ErrorCode.values();
        for ( ErrorCode error:values) {
            if (error.errorMessage.equals(message)) return error.errorCode;
        }
        return -1 ;
    }
}
