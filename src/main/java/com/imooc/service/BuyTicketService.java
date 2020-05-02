package com.imooc.service;

import com.imooc.dataobject.Callplan;
import com.imooc.dataobject.SeatOrderDO;
import com.imooc.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface BuyTicketService {

    Map<String,Object> cktikcet(String uid);

    Map<String,Object> delVerify(String mobile);


    Map<String,Object>  findAll(String lp);

    Map<String,Object>  bupiao(String lp);

    Map<String,Object>  listSeatDetail(String route,String time, String moment);

    Map<String,Object> addOrder(String route, String time, String moment, String seat, String num,String uid,String routeStation);

    void deleteSorder (String uid,String orderId);

    Map<String,Object> payOrder(String orderId, String uid);

    PayResponse notify(String notifyData) throws Exception;

    PayResponse notifyMonth(String notifyData);

    Map<String, Object> buyTicket(String lp) throws ParseException;

    Map<String, Object> payMonthTick(String id, String uid,String month_val);

    void updateYuepiaoOrder(String uid, String orderId) throws Exception;

    Map<String, Object> queryMonthTicket(String uid);

    String queryBanci(String route, String time);

    void saveInfo(String name, String phone, String uid,String verify);

    Map<String, Object> queryOrder(String orderId, String uid);

    String queryBanci(String route);

    Map<String,Object> addBupiao(String route, String moment,String uid);

    void sendYzm(String name, String phone, String uid);

    RefundResponse refund(String orderNO,double amount);

    void delOrderByTimeOut();

    ////test
    Map<String,Object> payOrder2(String orderId, String uid);
}
