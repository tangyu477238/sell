package com.imooc.controller;

import com.imooc.service.BuyTicketService;
import com.imooc.service.VerificationTicketService;
import com.imooc.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/verification")
public class VerificationTicketController {



    @Autowired
    private VerificationTicketService verificationTicketService;

    @Autowired
    private BuyTicketService buyTicketService;




    @GetMapping("/cktikcetCzy")
    public Map<String,Object> cktikcetCzy(){

        return verificationTicketService.ckticketCzy();
    }


    @GetMapping("/cktikcetTime")
    public Map<String,Object> cktikcetTime(@RequestParam() String route){
        return verificationTicketService.cktikcetTime(route,DateTimeUtil.getBeforeDay(0));
    }


    @GetMapping("/cktikcetYpjl")
    public Map<String,Object> cktikcetYpjl(@RequestParam("route") String route,
                                           @RequestParam("bizTime") String bizTime){

        return verificationTicketService.cktikcetYpjl(route, DateTimeUtil.getBeforeDay(0),bizTime);
    }
    //cktikcet  验票系统

    @GetMapping("/ckticket")
    public Map cktikcet(@RequestParam("route") String route,
                        @RequestParam("time") String time,
                        @RequestParam("uid") String uid){
        Map map = verificationTicketService.cktikcet(route, DateTimeUtil.getBeforeDay(0),time,uid);
        log.info("进入cktikcet订单方法......." + map.toString());
        return map;
    }



    @GetMapping("/addRegistrationId")
    public Map addRegistrationId(@RequestParam("registrationId") String registrationId){
        Map map = verificationTicketService.addRegistrationId(registrationId);
        log.info("addRegistrationId......." + map.toString());
        return map;
    }


    @GetMapping("/pushMessage")
    public Map pushMessage(@RequestParam("exts") String exts){
        String notificationTitle = "13排1座,13排2座,13排3座,13排4座";
        verificationTicketService.pushMessage(notificationTitle,exts);
        return new HashMap();
    }


    @GetMapping("/getQrcode")
    public Map getQrcode(@RequestParam("route") String route,
                         @RequestParam("bizDate") String bizDate,
                         @RequestParam("time") String time) throws Exception{

        Map map = verificationTicketService.getQrcode(route, bizDate,time);
        log.info("进入getQrcode订单方法......." + map.toString());
        return map;
    }





}
