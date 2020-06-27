package com.imooc.utils;












import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WxUtils {


    private static JSONObject httpGet(String url) throws Exception{
        URL urlGet = new URL(url);
        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
        http.setRequestMethod("GET"); // 必须是get方式请求
        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        http.setDoOutput(true);
        http.setDoInput(true);
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
        http.connect();
        InputStream is = http.getInputStream();
        int size = is.available();
        byte[] jsonBytes = new byte[size];
        is.read(jsonBytes);
        String message = new String(jsonBytes, "UTF-8");
        JSONObject demoJson = JSON.parseObject(message);
        is.close();
        return demoJson;

    }

    public static String getAccessToken(String AppId,String secret)  throws Exception{

        String access_token = "";
        String grant_type = "client_credential";//获取access_token填写client_credential

        //这个url链接地址和参数皆不能变
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;

        try {
            JSONObject demoJson = httpGet(url);
            System.out.println("JSON字符串："+demoJson);
            access_token = demoJson.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }


    public static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变
        try {

            JSONObject demoJson =  httpGet(url);
            //System.out.println("JSON字符串："+demoJson);
            ticket = demoJson.getString("ticket");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
