package com.dsg.common.utils;


import com.dsg.common.constants.Constants;

public class ZnsjHash {


    /**
     * Description:
     * <br/>
     * Liu Yuanyuan May 22, 2018  <br/>
     * <br/>
     *
     * @param str �����ַ���
     * @return str �ַ�����hashֵ
     * @throws Exception  If a String is whitespace, "null" or null.
     * 	  <p>
     *    <code>
     *    if (
     *    str == null    ||
     *    str.toLowerCase() == "null"  ||
     *    str == ""      ||
     *    str == "  "
     *    )
     *    <br>
     *     &nbsp;&nbsp;&nbsp;&nbsp; throw new Exception("Illegal string value !");
     *    </code>
     *    </p>
     */
    public static String toHash(String str ) throws Exception {
        str = str.replaceAll(Constants.LF, Constants.EMPTY).replaceAll(Constants.CR, Constants.EMPTY);
        if (str == null || str.length() == 0) {
            throw new Exception("Illegal string value !");
        }
        str = StringUtils.deleteWhitespace(str);
        if (StringUtils.isBlank(str))
            throw new Exception("Illegal string value !");
        long hash = str.hashCode();
        hash <<= 32;
        hash |= FNVHash1(str);
        return Long.toHexString(hash);
    }

    public static int FNVHash1(String data) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for(int i=0;i<data.length();i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

}
