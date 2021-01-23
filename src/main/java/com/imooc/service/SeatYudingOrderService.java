package com.imooc.service;

import java.util.Map;

/**
 * Created by SqMax on 2018/3/17.
 *
 */
public interface SeatYudingOrderService {



    void yudingOrder(String uid, String workday, String route, String time);

    void addOrder();

    Map<String,Object> shikebiao(String route, String holiday, String uid);

}
