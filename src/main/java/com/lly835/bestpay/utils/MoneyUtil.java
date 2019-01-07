package com.lly835.bestpay.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @Auther: Administrator
 * @Date: 2018\12\31 0031 18:32
 * @Description:
 */
@Slf4j
public class MoneyUtil {
    public MoneyUtil() {
    }

    public static Integer Yuan2Fen(Double yuan) {
        return (BigDecimal.valueOf(yuan)).movePointRight(2).intValue();
    }

    public static Double Fen2Yuan(Integer fen) {
        return (BigDecimal.valueOf(fen)).movePointLeft(2).doubleValue();
    }

//    public static void main (String str []){
//        log.info(MoneyUtil.Fen2Yuan(29).toString());
//    }

}
