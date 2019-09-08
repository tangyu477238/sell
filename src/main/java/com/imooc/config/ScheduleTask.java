package com.imooc.config;

import com.imooc.service.BuyTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
@Slf4j
public class ScheduleTask {

    @Autowired
    private BuyTicketService buyTicketService;


    public void marketingActivity() throws Exception {
        log.info("----------开始进入定时清除订单任务---------------");
        if(buyTicketService != null){
            log.info("----------执行订单超时释放任务---------------");
            buyTicketService.delOrderByTimeOut();
        }
    }

}