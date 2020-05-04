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
import org.springframework.beans.factory.annotation.Autowired;
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

    private static String MONTH_NotifyUrl = "/sell/ticket/notifyMonth"; //月票购买成功回调地址

    private static String NotifyUrl = "/sell/ticket/notify"; //车票购买成功回调地址

    private static String QRCODE_PATH = "/opt/app/photos/qrcode/";//二维码图片路径






    //联系电话
    private static String ORDER_link_TEL = "13922253183"; //

    private static String ORDER_WELCOME = "请提前至少10分钟排队等候上车";

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private com.lly835.bestpay.config.WxPayConfig wxPayConfig;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WechatAccountConfig accountConfig;

    @Autowired
    private SellerInfoRepository userRepository;

    @Autowired
    private BuyTicketRepository repository;

    @Autowired
    private SeatOrderRepository seatOrderRepository;

    @Autowired
    private SeatOrderItemRepository seatOrderItemRepository;


    @Autowired
    private MonthTicketRepository monthTicketRepository;

    @Autowired
    private MonthTicketUserRepository monthTicketUserRepository;

    @Autowired
    private MonthTicketUserLogRepository monthTicketUserLogRepository;


    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private VerifyRepository verifyRepository;


    @Override
    public Map<String,Object> cktikcet(String uid) {
        Map map = new HashMap();
        int flag = 0;
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
        if (sod.getCkstate()==1){
            flag = -1;
            map.put("state",flag);//重复验票
            return map;
        }
        sod.setCkstate(1);//更新验票状态
        sod.setUpdateTime(new Date());
        seatOrderRepository.save(sod);//更新状态
        flag = sod.getNum().intValue();
        map.put("state",flag);//验票ok
        return map;
    }





    @Override
    public void yuecheSave(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

    }

    @Override
    public Map<String,Object> cktikcetYpjl(Map<String,Object> map) {
        List list = seatOrderRepository.findAll();
        map.put("plList",list);
        return map;
    }

    @Override
    public Map<String,Object> ckticketCzy() {
        return null;
    }

    @Override
    public Map<String,Object> cktikcetTime(String s) {
        return null;
    }


}
