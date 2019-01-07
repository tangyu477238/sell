package com.imooc.utils;

import com.google.common.math.DoubleMath;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Created by SqMax on 2018/3/27.
 */
@Slf4j
public class MathUtil {

    private static final Double MONEY_RANGE=0.001;
    /**
     * 比较2个金额是否相等
     * @param d1
     * @param d2
     * @return
     */
    public static Boolean equals(Double d1,Double d2){

        Double result=Math.abs(BigDecimal.valueOf(d1).subtract(BigDecimal.valueOf(d2)).doubleValue());
        Double result2=Math.abs(BigDecimal.valueOf(d2).subtract(BigDecimal.valueOf(d1)).doubleValue());

        if(result<MONEY_RANGE && result2<MONEY_RANGE){
            return true;
        }else{
            return false;
        }
    }


    public static void main (String str []){
        double a = 0.03000000;
        BigDecimal d = new BigDecimal(0.029);
        log.info(""+MathUtil.equals(a,d.doubleValue()));
    }
}
