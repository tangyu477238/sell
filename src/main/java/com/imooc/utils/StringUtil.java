package com.imooc.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
    public static String MD5(String sourceStr) {

        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result.toUpperCase();
    }

    public static void main(String[] args) {
        String str = MD5("gzjhqc");
        System.out.println(str);
        System.out.println(MD5("admin"+str));
    }
}
