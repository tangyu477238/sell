package com.imooc.controller;

import com.imooc.service.VerificationTicketService;
import com.imooc.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/verification")
public class VerificationTicketController {


    @Autowired
    private VerificationTicketService verificationTicketService;

    @ResponseBody
    @GetMapping("/cktikcetCzy")
    public Map<String,Object> cktikcetCzy(){

        return verificationTicketService.ckticketCzy();
    }

    @ResponseBody
    @GetMapping("/cktikcetTime")
    public Map<String,Object> cktikcetTime(@RequestParam() String route){
        return verificationTicketService.cktikcetTime(route,DateTimeUtil.getBeforeDay(0));
    }

    @ResponseBody
    @GetMapping("/cktikcetYpjl")
    public Map<String,Object> cktikcetYpjl(@RequestParam("route") String route,
                                           @RequestParam("bizTime") String bizTime){

        return verificationTicketService.cktikcetYpjl(route, DateTimeUtil.getBeforeDay(0),bizTime);
    }
    //cktikcet  验票系统
    @ResponseBody
    @GetMapping("/ckticket")
    public Map cktikcet(@RequestParam("uid") String uid){
        log.info("进入cktikcet订单方法.......");
        return verificationTicketService.cktikcet(uid);
    }
}
