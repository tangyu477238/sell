package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.service.VerificationTicketService;
import com.imooc.utils.DateTimeUtil;
import com.imooc.utils.ResultVOUtil;
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

    //登录
    @GetMapping("/login")
    public ResultVO login(@RequestParam("username") String username,
                          @RequestParam("password") String password){
        int flag = verificationTicketService.login(username,password);
        if (flag==1){
            return ResultVOUtil.success();
        } else {
            return ResultVOUtil.error(500,"用户名或密码错误");
        }
    }


    //去除黑名单
    @GetMapping("/updateBlack")
    public ResultVO updateBlack(@RequestParam("mobile") String mobile){
        verificationTicketService.updateBlack(mobile);
        log.info("updateBlack......." );
        return ResultVOUtil.success();
    }

    //卡票记录
    @GetMapping("/black") //1
    public Map black(@RequestParam("mobile") String mobile){
        Map map = verificationTicketService.black(mobile);
        log.info("black......." + map.toString());
        return map;
    }

    //黑名单列表
    @GetMapping("/blacklist") //1
    public Map blacklist(){
        Map map = verificationTicketService.blacklist();
        log.info("blacklist......." + map.toString());
        return map;
    }


    //修改售票天数
    @GetMapping("/updateSellday")
    public ResultVO updateSellday(@RequestParam("days") Integer days){
        verificationTicketService.updateSellday(days);
        log.info("updateSellday......." );
        return ResultVOUtil.success();
    }
    //退单
    @GetMapping("/tuidan")
    public ResultVO tuidan(@RequestParam("orderId") Long orderId,
                                  @RequestParam("uid") String uid,
                                  @RequestParam("username") String username,
                                  @RequestParam("password") String password){
        verificationTicketService.tuidan(orderId, uid, username, password);
        log.info("tuidan......." );
        return ResultVOUtil.success();
    }
    //锁定全部班车
    @GetMapping("/lockAll")
    public ResultVO lockAll(@RequestParam("route") Long route,
                                  @RequestParam("bizDate") String bizDate,
                                  @RequestParam("time") String time,
                                  @RequestParam("username") String username,
                                  @RequestParam("password") String password){
        verificationTicketService.lockAll(route, bizDate, time, username, password);
        log.info("lockAll......." );
        return ResultVOUtil.success();
    }
    //取消班车通知
    @GetMapping("/sendCancelMsg")
    public ResultVO sendCancelMsg(@RequestParam("route") Long route,
                                  @RequestParam("bizDate") String bizDate,
                                  @RequestParam("time") String time,
                                  @RequestParam("username") String username,
                                  @RequestParam("password") String password){
        verificationTicketService.sendCancelMsg(route, bizDate, time, username, password);
        log.info("sendCancelMsg......." );
        return ResultVOUtil.success();
    }

    //晚点通知
    @GetMapping("/sendWandianMsg")
    public ResultVO sendWandianMsg(@RequestParam("route") Long route,
                                   @RequestParam("bizDate") String bizDate,
                                   @RequestParam("time") String time,
                                   @RequestParam("wtime") String wtime){
        verificationTicketService.sendWandianMsg(route, bizDate, time, wtime);
        log.info("sendWandianMsg......." );
        return ResultVOUtil.success();
    }


    @GetMapping("/cktikcetCzyNew") //2
    public Map<String,Object> cktikcetCzy(){

        return verificationTicketService.ckticketCzy();
    }


    @GetMapping("/cktikcetTimeNew") //3
    public Map<String,Object> cktikcetTime(@RequestParam() String route,
                                           @RequestParam("bizDate") String bizDate){
        return verificationTicketService.cktikcetTime(route, bizDate);
    }
    //时刻表
    @GetMapping("/shikebiao")
    public Map<String,Object> shikebiao(@RequestParam() String route,
                                           @RequestParam() String holiday){
        return verificationTicketService.shikebiao(route,holiday);
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



    @GetMapping("/addRegistrationIdNew") //1
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
