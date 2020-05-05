package com.imooc.service.impl;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.config.SimpleSMSSender;
import com.imooc.config.WechatAccountConfig;
import com.imooc.dataobject.*;
import com.imooc.enums.LouPanEnum;
import com.imooc.exception.BusinessException;
import com.imooc.exception.SellException;
import com.imooc.repository.*;
import com.imooc.service.BuyTicketService;
import com.imooc.service.VerificationTicketService;
import com.imooc.utils.*;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import net.minidev.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Auther: Administrator
 * @Date: 2018\12\18 0018 23:45
 * @Description:
 */
@Service
@Slf4j
public class VerificationTicketServiceImpl implements VerificationTicketService {

    private static int ORDER_STATE_0 = 0; //订单待付款

    private static int ORDER_STATE_1 = 1; //订单已付款

    private static int ORDER_STATE_2 = 2; //订单不存在但已付款，退款

    private static int ORDER_STATE_3 = 3; //订单金额与支付金额不一致，退款

    private static int ORDER_STATE_4 = 4; //重复支付，退款


    private static String MONTH_STATE_0 = "0"; //月票待付款

    private static String MONTH_STATE_1 = "1"; //月票已付款



    @Autowired
    private SeatOrderRepository seatOrderRepository;

    @Autowired
    private RouteRepository routeRepository;


    @Autowired
    private VerificationTicketRepository verificationTicketRepository;


    @Override
    public void yuecheSave(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

    }

    @Override
    public Map<String,Object> cktikcet(String route, String bizDate, String bizTime, String uid) {

        log.info("route:"+route+"  bizDate:"+bizDate+"  bizTime:"+bizTime+"  uid:"+uid);

        Map map = new HashMap();
        String flag = "fail";
        map.put("state",flag);//验票失败
        if (ComUtil.isEmpty(uid)){
            return map;
        }
        String strs[] = uid.split("_");
        if (ComUtil.isEmpty(strs)||strs.length!=2||strs[0].length()!=32){
            return map;
        }

        SeatOrderDO sod = seatOrderRepository.findByIdAndStateAndCreateUser(new Long(strs[1]),ORDER_STATE_1,strs[0]);
        if (ComUtil.isEmpty(sod)){
            return map;
        }

        if (!sod.getRouteId().toString().equals(route)
                ||!sod.getBizDate().equals(bizDate)
                ||!sod.getBizTime().equals(bizTime)){
            map.put("state","banci");//班次不一致
            return map;
        }



        if (sod.getCkstate()==1){
            map.put("state","double");//重复验票
            return map;
        }
        sod.setCkstate(1);//更新验票状态
        sod.setUpdateTime(new Date());
        seatOrderRepository.save(sod);//更新状态


        //验票记录
        VerificationTicketDO verificationTicketDO = new VerificationTicketDO();
        BeanUtils.copyProperties(sod,verificationTicketDO);
        verificationTicketDO.setId(null);
        verificationTicketDO.setUpdateTime(new Date());
        verificationTicketDO.setOrderId(sod.getId());
        verificationTicketDO.setCreateUser("管理员");
        verificationTicketDO.setUserMobile(sod.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        verificationTicketRepository.save(verificationTicketDO);

        map.put("state",String.valueOf(sod.getNum().intValue()));//验票ok
        return map;
    }







    @Override
    public Map<String,Object> cktikcetYpjl(String routeId, String bizDate, String bizTime) {
        Map<String,Object> map = new HashMap<>();

        VerificationTicketDO seatOrderDO = new VerificationTicketDO();
        seatOrderDO.setRouteId(Long.parseLong(routeId));
        seatOrderDO.setBizDate(bizDate);
        seatOrderDO.setBizTime(bizTime);

        //按照更新时间排序
        Sort sort = new Sort(Sort.Direction.DESC, "id");

        Example<VerificationTicketDO> example = Example.of(seatOrderDO);
        List list = verificationTicketRepository.findAll(example, sort);


        map.put("plList",list);
        return map;
    }

    @Override
    public Map<String,Object> ckticketCzy() {

        Map<String,Object> map = new HashMap<>();
        map.put("plList",routeRepository.findAll());
        return map;
    }

    @Override
    public Map<String,Object> cktikcetTime(String route,String bizDate) {
        Map<String,Object> map = new HashMap<>();
        map.put("timelist",verificationTicketRepository.getTime(route,bizDate));
        return map;
    }


}
