package com.imooc.service;

import com.imooc.dataobject.SeatOrderDO;

import java.util.List;
import java.util.Map;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface VerificationTicketService {


    void yuecheSave(String s, String s1, String s2, String s3, String s4, String s5, String s6,
                    String s7, String s8);

    Map<String,Object> cktikcetYpjl(String route, String bizDate, String bizTime);

    Map<String,Object> ckticketCzy();

    Map<String,Object> cktikcetTime(String route,String bizDate);

    Map<String,Object> shikebiao(String route,String holiday);


    Map<String,Object> cktikcet(String route, String bizDate, String bizTime,String uid);

    Map<String,Object> cktikcet(List<SeatOrderDO> sodlist);

    Map addRegistrationId(String registrationId);

    Map pushMessage(String notificationTitle,String exts);

    Map<String,Object> getQrcode(String route, String bizDate, String bizTime) throws Exception;

}
