package com.imooc.service;

import java.util.Map;

/**
 * Created by SqMax on 2018/3/17.
 *
 */
public interface SeatYudingOrderService {


    void yudingOrder(String uid, String workday, String route, String time);
    void yudingOrder(String uid, String workday, String route, String time,String startDate,String endDate);
    void yudingOrder(String uid, String route, String time, String bizDate, Integer dayNum);
    void addOrder();

    Map<String,Object> shikebiao(String route, String holiday, String uid);

    void sendMsgBanci(String first,String yuanyin,String content,String url);

    void pingjia(String uid,String orderId,String content,Integer fuwu) throws Exception;
}
