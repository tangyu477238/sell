package com.imooc.service;

import java.util.Map;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface VerificationTicketService {



    void yuecheSave(String s, String s1, String s2, String s3, String s4, String s5, String s6,
                    String s7, String s8);

    Map<String,Object> cktikcetYpjl(String routeId, String bizDate, String bizTime);

    Map<String,Object> ckticketCzy();

    Map<String,Object> cktikcetTime(String route,String bizDate);

    Map<String,Object> cktikcet(String uid);
}
