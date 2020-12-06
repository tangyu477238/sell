package com.imooc.config;

import com.imooc.dataobject.PayLogDO;
import com.imooc.repository.PayLogRepository;
import com.imooc.repository.SeatOrderLogRepository;
import com.imooc.service.BuyTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//@Configuration
@Component
@EnableScheduling // 此注解必加
@Slf4j
public class ScheduleTask {

    @Autowired
    private BuyTicketService buyTicketService;

    @Autowired
    private PayLogRepository payLogRepository;

    @Autowired
    private SeatOrderLogRepository seatOrderLogRepository;

    @Scheduled(cron = "${task.schedule.delOrder}")
    public void marketingActivity() throws Exception {
        log.info("----------开始进入定时任务---------------");
        if(buyTicketService != null){
            buyTicketService.delOrderByTimeOut();
            log.info("----------执行订单超时释放任务---------------");
        }
        if(payLogRepository != null){
            List<PayLogDO> list = payLogRepository.findByState(2);
            for (PayLogDO payLogDO : list){
                log.info("--退款开始----payLogDO.getOrderNo()----"+payLogDO.getOrderNo()+"----payLogDO.getAmout()----" + payLogDO.getAmout().doubleValue());
                buyTicketService.refund(payLogDO.getOrderNo(), payLogDO.getAmout().doubleValue());
            }

        }
    }
    @Scheduled(cron = "${task.schedule.warningUsers}")
    public void warningOrderUsers() throws Exception {
        log.info("----------开始检测卡票用户---------------");
        if(buyTicketService != null){
            buyTicketService.getwarningOrderUsers();
            log.info("----------执行检测卡票释放任务---------------");
        }

    }

}