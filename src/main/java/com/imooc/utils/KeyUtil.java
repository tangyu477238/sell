package com.imooc.utils;

import java.util.Date;
import java.util.Random;

/**
 * Created by SqMax on 2018/3/18.
 */
public class KeyUtil {

    /**
     * 生成唯一的主键
     * 格式：时间+随机数
     * @return
     */
    public static synchronized String genUniqueKey(){
        Random random=new Random();
        Integer number=random.nextInt(900000)+100000;

        return System.currentTimeMillis()+String.valueOf(number);
    }

    public static synchronized String genUniqueShortKey(){
//        Random random=new Random();
//        Integer number=random.nextInt(900000)+100000;
        return DateTimeUtil.formatDateTimetoString(new Date(),DateTimeUtil.FMT_yyyyMMddHHmmssS_15);
    }

    public static void main (String str []){
        System.out.println(KeyUtil.genUniqueShortKey());
    }
}
