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
import org.apache.commons.lang3.StringUtils;
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
public class BuyTicketServiceImpl implements BuyTicketService {

    private static int houre1 = 18;
    private static int houre2 = 19;
    private static int houre3 = 20;

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
    private SeatOrderLogRepository seatOrderLogRepository;



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

    @Autowired
    private QrcodeColorRepository qrcodeColorRepository;




    @Override
    public void sendYzm(String name, String phone, String uid) {

        List<Object[]> listVerify = repository.checkVerifyNum(uid);
        String info = null;
        int num = 0 ;
        int verifyid = 0;
        for (Object[] obj: listVerify ) {
            info = obj[0].toString();
            verifyid = Integer.parseInt(obj[1].toString());
            num = Integer.parseInt(obj[2].toString());
        }
        if (ComUtil.isEmpty(info)){
            throw new SellException(500,"用户不存在！");
        }
        if (num>2){ //每天超过5次以后即不在发短信
            throw new SellException(500,"短信已达最多次数限制！");
        }

        SimpleSMSSender.SMS sms = SimpleSMSSender.newSMS();
        String randNum = GenerationSequenceUtil.getRandNum(4);
        sms.setPhoneNumbers(phone);
        sms.setTemplateParam("{\"code\":"+randNum+"}");
        sms.setTemplateCode("SMS_155425089");

        Verify verify = new Verify();
        verify.setMobile(phone);
        verify.setUid(uid);
        verify.setCreateTime(new Date());
        verify.setPtype(1);
        verify.setVerify(randNum);
        verify.setNum(1);
        if(verifyid > 0 ){
            verify.setId(verifyid);
            verify.setNum(num + 1);
        }
        verifyRepository.save(verify);
        SimpleSMSSender.send(sms);
    }

    //买票
    @Override
    public Map<String,Object>  findAll(String lp) {
        Map<String,Object> map = new HashMap();

        List<Object[]> plList = repository.getRouteInfo(lp);
        List<RouteDO> plist = new ArrayList();
        RouteDO cds ;
        for (Object[] obj: plList ) {
            cds = new RouteDO();
            cds.setId(Long.parseLong(obj[0].toString()));
            cds.setFromStation(obj[1].toString());
            cds.setToStation(obj[2].toString());
            plist.add(cds);
        }
        map.put("plList",plist);



        List daylist = new ArrayList();
        List<Object[]> list = repository.getDayTimeFlag();
        int days = Integer.parseInt(list.get(0)[0].toString());
        int flag = Integer.parseInt(list.get(0)[1].toString());
        for (int i = 0; i < days; i++) {
            if(i == days-1){//最后一个元素时
                if(flag==1) {
                    daylist.add(DateTimeUtil.getBeforeDay(i));
                }
            } else {
                daylist.add(DateTimeUtil.getBeforeDay(i));
            }
        }
        map.put("daysDate",DateTimeUtil.getBeforeDay(days-1));
        map.put("daylist",daylist);
        return map;
    }

    //补票
    @Override
    public Map<String,Object>  bupiao(String lp) {
        Map<String,Object> map = new HashMap();
        List<Object[]> plList = repository.getRouteInfo(lp);

        List<RouteDO> list = new ArrayList();
        RouteDO cds ;
        for (Object[] obj: plList ) {
             cds = new RouteDO();
             cds.setFromStation(obj[1].toString());
             cds.setId(Long.parseLong(obj[0].toString()));
             cds.setToStation(obj[2].toString());
             list.add(cds);
        }
        map.put("plList",list);

        map.put("day",DateTimeUtil.getBeforeDay(0));
        return map;
    }



    private void checkSaleTime(String time, String moment){
        List<Object[]> paramslist = repository.getDayTimeFlag();
        int days = Integer.parseInt(paramslist.get(0)[0].toString()); //天数
        String buybeforeticket1 = paramslist.get(0)[5].toString(); //可购买几点前的票(1)
        int buytime1 = Integer.parseInt(paramslist.get(0)[4].toString()); //几点开始售卖(1)
        if (DateTimeUtil.getBeforeDay(days-1).equals(time)){ //选的日期是最后一天
            log.info("--------"+DateTimeUtil.getHoursOfDay(new Date())+"----------"+time+"----------");
            if(DateTimeUtil.getHoursOfDay(new Date())<buytime1){  //19点之前买票
                throw new BusinessException("500","尚未到达售票时间！");
            } else if (DateTimeUtil.getHoursOfDay(new Date())>=buytime1
                    && DateTimeUtil.getHoursOfDay(new Date())<houre2 && moment.compareTo(buybeforeticket1)>0){ //19点-20之间买7点后的票
                throw new BusinessException("500","尚未到达售票时间！");
            } else if (DateTimeUtil.getHoursOfDay(new Date())>=houre2
                    && DateTimeUtil.getHoursOfDay(new Date())<houre3 && moment.compareTo("08:01")>0){ //20点-21之间买8点后的票
                throw new BusinessException("500","尚未到达售票时间！");
            }
        }
    }


    private List<Object[]> getSeatList(String route,String time, String moment, String seat,String uid){
        String seatNames [] = seat.split(",");
        List<Object[]> seatlist = repository.listSeatOder(route,time,moment,seatNames);
        if (ComUtil.isEmpty(seatlist)){
            throw new BusinessException("500","至少购买1张车票！");
        }

        for (int j = 0; j < seatlist.size(); j++) {
            if(seatlist.get(j)[2] != null){
                log.info(seatlist.get(j)[1]+"已被预定！");
                throw new BusinessException("500", seatlist.get(j)[1]+"已被其他人预定！");
            }
        }

        List<BigDecimal> numlist = repository.getBuyCarNum(route,time,moment,uid);
        BigDecimal cartotal = new BigDecimal(seatlist.size());
        if (!ComUtil.isEmpty(numlist) && !ComUtil.isEmpty(numlist.get(0))){
            cartotal = numlist.get(0).add(cartotal);
        }
        if (cartotal.intValue()>4){
            throw new BusinessException("500","同一班车最多购买或预定4张车票！");
        }

        return seatlist;
    }


    private Date getLockTime(){
        List<Object[]> list = repository.getDayTimeFlag();
        int orderlock = Integer.parseInt(list.get(0)[2].toString());
        return DateTimeUtil.addMinutes(new Date(), orderlock);
    }


    private void getOrderInfo(SeatOrderDO so){
        List<Object[]> objs = repository.getPlanPrice(String.valueOf(so.getRouteId()),
                so.getBizDate(), so.getBizTime());
        if (ComUtil.isEmpty(objs) || objs.get(0) == null){
            throw new SellException(500,"网络加载失败，请重新操作！");
        }
        so.setFromStation(objs.get(0)[0].toString()); //出发站
        so.setToStation(objs.get(0)[1].toString()); //到达站
        so.setPrice(new BigDecimal(objs.get(0)[2].toString())); //价格
        so.setPlanId(new Long(objs.get(0)[3].toString())); //公式id
        so.setLp(objs.get(0)[4].toString());
        so.setAmout(so.getPrice().multiply(so.getNum()));

        so.setState(ORDER_STATE_0);//待付款
        so.setRemark("");
        so.setCreateTime(new Date());
        so.setUpdateTime(getLockTime());//锁定2分钟
        so.setOrderNo(KeyUtil.genUniqueKey());

        getOrderUserInfo(so); //设置创建人信息

    }


    private void getOrderUserInfo(SeatOrderDO so){
        SellerInfo sellerInfo = userRepository.findOne(so.getCreateUser());
        so.setUserName(sellerInfo.getName());
        so.setUserMobile(sellerInfo.getMobile());
    }


    private BigDecimal getMonthUserNum(SeatOrderDO so){
        MonthTicketUserDO mtu =
                monthTicketUserRepository.findByCreateUserAndMonthAndRemarkAndLp(so.getCreateUser(),
                        so.getBizDate().substring(0,7), MONTH_STATE_1,so.getLp());
        BigDecimal sysl = new BigDecimal(0);
        if(!ComUtil.isEmpty(mtu)){
            sysl = mtu.getTotalNum().subtract(mtu.getUseNum());
        }
        return sysl;
    }

    @Override
    synchronized
    public Map<String,Object> addOrder(String route,String time, String moment,
                                       String seat,String num111,String uid,String routeStation) {

        checkSaleTime(time, moment); //验证购票时间

        List<Object[]> seatlist = getSeatList(route, time, moment, seat, uid); //验证购票座位以及数量

        SeatOrderDO so = new SeatOrderDO();
        so.setBizDate(time);
        so.setBizTime(moment);
        so.setRouteId(new Long(route));
        so.setNum(new BigDecimal(seatlist.size()));
        so.setRouteStation(routeStation);//上车点
        so.setInfo(seat);
        so.setCreateUser(uid); //创建人

        getOrderInfo(so); //设置订单基础信息



        try {

            so = seatOrderRepository.save(so);
            log.info("order----->id:" + so.getId());
            for (int j = 0;  j < seatlist.size(); j++) {
                if(seatlist.get(j)[0] != null){
                    SeatOrderItemDO sod = new SeatOrderItemDO();
                    sod.setSeatId(Long.parseLong(seatlist.get(j)[0].toString()));
                    sod.setOrderId(so.getId());
                    seatOrderItemRepository.save(sod); //占票
                }
            }
        }catch (Exception e){
            throw new BusinessException("500", "座位已被其他人预定！");
        }

        Map map = new HashMap();
        log.info("----------确认订单界面id值----------------"+so.getId());
        map.put("sod", so);
        map.put("createTime", DateTimeUtil.formatDateTimetoString(so.getCreateTime(),"yyy-MM-dd HH:mm:ss"));
        map.put("orderNewId", String.valueOf(so.getId()));

        map.put("sysl", String.valueOf(getMonthUserNum(so))); //月票剩余数

        repository.addOrderLogs(so.getOrderNo(),so.getBizDate(),so.getBizTime(),so.getPlanId(),so.getInfo()
                ,so.getPrice(),so.getNum(),so.getAmout(),so.getCreateTime(),so.getUpdateTime()
                ,so.getCreateUser(),so.getState(),so.getRemark(),so.getFromStation()
                ,so.getToStation(),so.getUserName(),so.getUserMobile()
                ,so.getRouteStation(),so.getCkstate(),so.getRouteId());


        return map;
    }











    @Override
    public Map<String,Object> addBupiao(String route, String moment,String uid) {


        SeatOrderDO so = new SeatOrderDO();
        so.setBizDate(DateTimeUtil.getBeforeDay(0));
        so.setBizTime(moment);
        so.setRouteId(new Long(route));
        so.setNum(new BigDecimal(1));
        so.setCreateUser(uid); //创建人
        so.setInfo("补票");

        getOrderInfo(so);  //设置订单基础信息


        so = seatOrderRepository.save(so);

        log.info("order--bupiao--->id:"+so.getId()+"-----"+so.getOrderNo());
        Map map = new HashMap();
        map.put("sod",so);
        map.put("orderNewId",so.getId().toString());
        map.put("createTime",DateTimeUtil.formatDateTimetoString(so.getCreateTime(),"yyy-MM-dd HH:mm:ss"));

        map.put("sysl", String.valueOf(getMonthUserNum(so))); //月票剩余数
        return map;
    }




    @Override
    public Map<String,Object>  listSeatDetail(String route,String time, String moment) {
        List<Object[]> seatlist = repository.listSeatDetail(route, time, moment);
        Map resMap = queryOrderSeats(seatlist,route, time, moment);
        return resMap;
    }


    @Override
    public void deleteSorder(String uid, String orderId) {
        repository.deleteByOrderPid(orderId);//删除orderItem
        SeatOrderDO seatOrderDO = seatOrderRepository.findByIdAndState(Long.parseLong(orderId),ORDER_STATE_0);
        repository.deleteByOrdId(orderId);//删除order
        SeatOrderLogDO logDO = seatOrderLogRepository.findByOrderNo(seatOrderDO.getOrderNo());
        logDO.setUpdateTime(new Date());
        seatOrderLogRepository.save(logDO);
    }





    //月票抵扣
    @Override
    public void updateYuepiaoOrder(String uid, String orderId) throws Exception{

        log.info("----------------updateYuepiaoOrder-------------------");
        SeatOrderDO sod = seatOrderRepository.findByIdAndStateAndCreateUser(new Long(orderId),0,uid);
        if (sod== null || DateTimeUtil.getSecondsOfTwoDate(sod.getUpdateTime(),new Date())<0){
            throw new SellException(500,"订单不存在或已超支付时间！");
        }

        checkMonthTicket(sod); //月票检查和扣减

        sod.setState(ORDER_STATE_1);
        sod.setRemark("月票抵扣");
        sod.setCkstate(0);//未验票
        seatOrderRepository.save(sod);

        createOrderQrcode(sod); //生成二维码

        //显示详情
        SellerInfo sellerInfo = userRepository.findOne(uid);
        sendMessage(sellerInfo.getOpenid(),"orderStatus",getOrderTemplateData(sod)
                ,projectUrlConfig.getWechatMpAuthorize()+"/sell/ticket/queryOrder?orderId="+sod.getId()+"&uid="+sellerInfo.getSellerId());
//        //显示二维码
//        sendMessage(sellerInfo.getOpenid(),"orderStatus",getOrderTemplateData(sod)
//                ,projectUrlConfig.getWechatMpAuthorize()+"/qrcode/"+sellerInfo.getSellerId()+"_"+sod.getId()+".jpg");


    }


    private void checkMonthTicket(SeatOrderDO sod){

        List<String> isMonth = repository.getIsMonthByOrder(String.valueOf(sod.getId()));
        if (ComUtil.isEmpty(isMonth) || "0".equals(isMonth.get(0))){
            throw new SellException(500,"此班次不支持月票抵扣！");
        }


        List<BigDecimal> numlist = repository.getBuyMonthNum(sod.getCreateUser(),
                sod.getBizDate(),sod.getBizTime(),sod.getPlanId());
        BigDecimal yuepnum = sod.getNum();
        if (!ComUtil.isEmpty(numlist)&&numlist.size()>0 && !ComUtil.isEmpty(numlist.get(0))){
            yuepnum = numlist.get(0).add(yuepnum);
        }
        if (yuepnum.intValue()>4){
            throw new SellException(500,"同一班车月票最多只能抵扣4张！");
        }

        //获取月票信息
        MonthTicketUserDO mtu =
                monthTicketUserRepository.findByCreateUserAndMonthAndRemarkAndLp(sod.getCreateUser(),
                        sod.getBizDate().substring(0,7),MONTH_STATE_1,sod.getLp());
        BigDecimal sy = mtu.getTotalNum().subtract(mtu.getUseNum()).subtract(sod.getNum());
        if (sy.doubleValue()<0){
            throw new SellException(500, "月票不足！");
        }
        if (sod.getNum().doubleValue()<1){
            throw new SellException(500, "非法订单！");
        }

        mtu.setUseNum(mtu.getUseNum().add(sod.getNum()));
        monthTicketUserRepository.save(mtu);
    }


    @Override
    public Map<String, Object> payOrder(String orderId, String uid) {
        SeatOrderDO sod = seatOrderRepository.findByIdAndStateAndCreateUser(new Long(orderId),0, uid);

        if (sod== null || DateTimeUtil.getSecondsOfTwoDate(sod.getUpdateTime(),new Date())<0){
            throw new BusinessException("500","订单不存在或已超支付时间！");
        }

        SellerInfo sellerInfo = userRepository.findOne(uid);
        Map map = new HashMap();



        //初始化支付
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(sellerInfo.getOpenid());
        log.info("" + sod.getAmout().doubleValue());
        payRequest.setOrderAmount(sod.getAmout().doubleValue());
        payRequest.setOrderId(sod.getOrderNo());
        payRequest.setOrderName(sod.getOrderNo());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);


        log.info("【微信支666付请求】发起支付，request={}", JsonUtil.toJson(payRequest));
        wxPayConfig.setNotifyUrl(projectUrlConfig.getWechatMpAuthorize() + NotifyUrl);
        getPayMchKey(sod.getLp()); //设置支付方式
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付返回】发起支付，response={}", JsonUtil.toJson(payResponse));

        map.put("payResponse",payResponse); //支付信息
        map.put("sod",sod);
        map.put("orderNo",sod.getOrderNo());

        return map;
    }

    private void getPayMchKey(String lp){
        if (LouPanEnum.BEIBUWANKE.getCode().equals(lp)){
            wxPayConfig.setMchId(accountConfig.getMchIdOne());
            wxPayConfig.setMchKey(accountConfig.getMchKeyOne());
            wxPayConfig.setKeyPath(accountConfig.getKeyPathOne());
        } else if (LouPanEnum.XINGFUYU.getCode().equals(lp)){
            wxPayConfig.setMchId(accountConfig.getMchId());
            wxPayConfig.setMchKey(accountConfig.getMchKey());
            wxPayConfig.setKeyPath(accountConfig.getKeyPath());
        }
        bestPayService.setWxPayConfig(wxPayConfig);
    }

    private void createOrderQrcode(SeatOrderDO sod) throws Exception{
        QrcodeColorDO qrcodeColorDO = qrcodeColorRepository.findByRouteIdAndBizDate(
                sod.getRouteId(), sod.getBizDate());
        if (ComUtil.isEmpty(qrcodeColorDO)){
            qrcodeColorDO = new QrcodeColorDO();
            qrcodeColorDO.setBizDate(sod.getBizDate());
            qrcodeColorDO.setBizTime(sod.getBizTime());
            qrcodeColorDO.setRouteId(sod.getRouteId());
            qrcodeColorDO.setQrcode(QRCodeUtil.getColor());
            qrcodeColorDO.setCreateTime(new Date());
            qrcodeColorRepository.save(qrcodeColorDO);
        }
        QRCodeUtil.encode(sod.getCreateUser()+"_"+sod.getId(),QRCODE_PATH,qrcodeColorDO.getQrcode());
    }

    @Override
    public PayResponse notify(String notifyData) throws Exception {
        //1.验证签名
        //2.支付状态
        //3. 支付金额
        //4. 支付人（下单人==支付人）
        PayResponse payResponse=bestPayService.asyncNotify(notifyData);//可以完成1、2两步
        log.info("【微信支付 异步通知】，payResponse={}",JsonUtil.toJson(payResponse));

        //增加支付成功记录



        log.info("----------------增加支付成功记录日志----------");

        //查询订单
        SeatOrderDO sod = seatOrderRepository.findByOrderNo(payResponse.getOrderId());
        //判断订单是否存在  不存在说明超时支付，需要发起退款操作，但目前不做直接退款，只做记录
        if(sod == null || DateTimeUtil.getSecondsOfTwoDate(sod.getUpdateTime(),new Date())<0){
            log.error("【微信支付】 异步通知，订单不存在，orderId={}",payResponse.getOrderId());

            repository.addPayLogs(payResponse.getOrderId(),new BigDecimal(payResponse.getOrderAmount()),new Date(),ORDER_STATE_2);

            SeatOrderLogDO logDO = seatOrderLogRepository.findByOrderNo(payResponse.getOrderId());
            SellerInfo sellerLog = userRepository.findOne(logDO.getCreateUser());
            sendMessage(sellerLog.getOpenid(), "orderTuiStatus", getOrderTuiTemplateData(logDO), null);

            //refund(payResponse.getOrderId(), payResponse.getOrderAmount()); //退款

            log.error("【微信支付】-----待退款记录------- 异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            return payResponse;
        }

        if(sod.getState()!=ORDER_STATE_0){
            log.error("【微信支付】 异步通知，重复支付，orderId={}",payResponse.getOrderId());
            //repository.addPayLogs(payResponse.getOrderId(),new BigDecimal(payResponse.getOrderAmount()),new Date(),ORDER_STATE_4);
            log.error("【微信支付】----异步通知，重复支付，getState={}",sod.getState());
            return payResponse;
        }


        //判断金额是否一致(0.10   0.1)
        log.info(payResponse.getOrderAmount().toString()+"-----判断金额是否一致--------"+sod.getAmout().toString());
        if(!MathUtil.equals(payResponse.getOrderAmount(),sod.getAmout().doubleValue())){
            log.error("【微信支付】 异步通知，订单不存在，orderId={}",payResponse.getOrderId());

            repository.addPayLogs(payResponse.getOrderId(),new BigDecimal(payResponse.getOrderAmount()),new Date(),ORDER_STATE_3);//待退款记录

            log.error("【微信支付】 异步通知，订单金额不一致，orderId={},微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    sod.getAmout());
            return payResponse;
        }


        //修改订单的支付状态
        sod.setRemark("微信支付");
        sod.setState(ORDER_STATE_1);
        sod.setCkstate(0);//未验票
        seatOrderRepository.save(sod);

        createOrderQrcode(sod); //生成二维码

        SellerInfo sellerInfo = userRepository.findOne(sod.getCreateUser());
        SeatOrderDO sod2 = seatOrderRepository.findByOrderNo(payResponse.getOrderId());
        if(sod2!=null && sod2.getState()==ORDER_STATE_1) {
            log.info("---------开始发送购买成功通知---------"+payResponse.getOrderId());
            //显示订单详情
            sendMessage(sellerInfo.getOpenid(), "orderStatus", getOrderTemplateData(sod)
                    , projectUrlConfig.getWechatMpAuthorize() + "/sell/ticket/queryOrder?orderId=" + sod.getId() + "&uid=" + sellerInfo.getSellerId());
            repository.addPayLogs(payResponse.getOrderId(), new BigDecimal(payResponse.getOrderAmount()), new Date(), ORDER_STATE_1);//付款成功
        } else {
            log.info("---------购买失败，订单已不存在---------"+payResponse.getOrderId());
            repository.addPayLogs(payResponse.getOrderId(), new BigDecimal(payResponse.getOrderAmount()), new Date(), ORDER_STATE_2);//需退款
        }
        return payResponse;
    }




    private List<WxMpTemplateData> getOrderTuiTemplateData(SeatOrderLogDO sod){
        //发送出票失败通知
        List<WxMpTemplateData> data= Arrays.asList(
                new WxMpTemplateData("first","您好，您的订单已支付超时，出票失败了！"),
                new WxMpTemplateData("keyword1",sod.getFromStation()+"-"+sod.getToStation(),"#B5B5B5"),
                new WxMpTemplateData("keyword2",sod.getBizDate()+" "+sod.getBizTime(),"#B5B5B5"),
                new WxMpTemplateData("keyword3",sod.getOrderNo(),"#B5B5B5"),
                new WxMpTemplateData("remark","退款将在3个工作日内按原路返回。\r\n若需购票请重新下单。\r\n如需帮助请致电"+ORDER_link_TEL+"。","#173177"));

        return data;
    }



    private List<WxMpTemplateData> getOrderTemplateData(SeatOrderDO sod){
        //发送通知
        List<WxMpTemplateData> data= Arrays.asList(
                new WxMpTemplateData("first","您好，您已成功购票。"),
                new WxMpTemplateData("keyword1",sod.getBizDate()+" "+sod.getBizTime(),"#B5B5B5"),
                new WxMpTemplateData("keyword2",sod.getOrderNo() + "," + ORDER_WELCOME,"#B5B5B5"),
                new WxMpTemplateData("keyword3",sod.getInfo(),"#B5B5B5"),
                new WxMpTemplateData("keyword4",sod.getNum()+"人","#B5B5B5"),
                new WxMpTemplateData("remark","为避免超载，请主动为小朋友购买车票。\r\n欢迎再次购买。\r\n如需帮助请致电"+ORDER_link_TEL+"。","#173177"));

        return data;
    }

    private List<WxMpTemplateData> getOrderMonthTemplateData(MonthTicketUserLogDO mtu){
        //发送通知
        List<WxMpTemplateData> data= Arrays.asList(
                new WxMpTemplateData("first","您好，您已成功购买"+mtu.getMonth()+"月卡。"),
                new WxMpTemplateData("keyword1",mtu.getPtypeName()+"(总共"+mtu.getTotalNum()+"张)","#B5B5B5"),
                new WxMpTemplateData("keyword2",mtu.getPrice()+"元","#B5B5B5"),
                new WxMpTemplateData("keyword3",mtu.getMonth()+"月","#B5B5B5"),
                new WxMpTemplateData("remark","欢迎再次购买。\r\n如需帮助请致电"+ORDER_link_TEL+"。","#173177"));

        return data;
    }

    private List<WxMpTemplateData> getWandianTemplateData(SeatOrderDO sod, String carNum, String wtime){
        //发送通知
        List<WxMpTemplateData> data= Arrays.asList(
                new WxMpTemplateData("first","抱歉，您购买的"+sod.getBizDate()+" "+sod.getBizTime()+"班车，因故大约晚点"+wtime+"分钟！"),
                new WxMpTemplateData("keyword1",sod.getFromStation()+"-"+sod.getToStation(),"#B5B5B5"),
                new WxMpTemplateData("keyword2",carNum,"#B5B5B5"),
                new WxMpTemplateData("keyword3",sod.getBizDate()+" "+sod.getBizTime()+"(晚点"+wtime+"分钟)","#FF0000"),
                new WxMpTemplateData("keyword4",ORDER_link_TEL,"#B5B5B5"),
                new WxMpTemplateData("remark","给您带来的不便，我们深感歉意！","#173177"));

        return data;
    }


    private void sendMessage(String openid,String moban,List<WxMpTemplateData> data,String url){

        WxMpTemplateMessage templateMessage=new WxMpTemplateMessage();
        templateMessage.setTemplateId(accountConfig.getTemplateId().get(moban));//模板id:"GoCullfix05R-rCibvoyI87ZUg50cyieKA5AyX7pPzo"
        templateMessage.setToUser(openid);//openid:"ozswp1Ojl2rA57ZK97ntGw2WQ2CA
        templateMessage.setData(data);
        templateMessage.setUrl(url);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }catch (WxErrorException e){
            log.error("【微信模板消息】发送失败，{}",e);
            try {
                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            } catch (WxErrorException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> buyTicket(String lp) throws ParseException{

        List<MonthTicketDO> mtdlist = monthTicketRepository.findByStateAndLp(1, lp);
        Map<String,Object> map = new HashMap();
        map.put("mtdlist", mtdlist);

        getMonthInfo(map); //设置和计算当前的的月份信息

        return map;
    }


    //设置和计算当前的的月份信息
    private void getMonthInfo(Map<String,Object> map) throws ParseException {
        List<Object[]> list = repository.getDayTimeFlag();
        int monthticketdays = Integer.parseInt(list.get(0)[3].toString());

        Date lastDate = DateTimeUtil.getMonthOfLastDayDate(0);
        int subDay = DateTimeUtil.daysBetween(new Date(), lastDate);

        if(subDay < monthticketdays){
            map.put("month_name", DateTimeUtil.getMonthDay(3)+"～"+DateTimeUtil.getMonthDay(4));
            map.put("month_val", DateTimeUtil.getMonthDay(6));
        } else {
            map.put("month_name", DateTimeUtil.getMonthDay(1)+"～"+DateTimeUtil.getMonthDay(2));
            map.put("month_val", DateTimeUtil.getMonthDay(5));
        }
    }

    @Override
    public Map<String, Object> payMonthTick(String id, String uid,String month_val) {

//        MonthTicketUserDO mtud =
//                monthTicketUserRepository.findByCreateUserAndMonth(uid,month_val);
//        if (!ComUtil.isEmpty(mtud) && "1".equals(mtud.getRemark())){
//            //叠加月票数量
//            throw new BusinessException("500","月票不可重复购买！");
//        }
//
//        if (ComUtil.isEmpty(mtud)){
//            mtud = new MonthTicketUserDO();
//            mtud.setCreateTime(new Date());
//        }


        MonthTicketDO mtd = monthTicketRepository.findById(new Long(id));

        MonthTicketUserLogDO mtudlog = monthTicketUserLogRepository.findByCreateUserAndMonthAndRemarkAndLp(uid
                ,month_val,MONTH_STATE_0,mtd.getLp());
        if (ComUtil.isEmpty(mtudlog)){
            mtudlog = new MonthTicketUserLogDO();
        }
        mtudlog.setCreateUser(uid);
        mtudlog.setMonth(month_val);
        mtudlog.setPtypeId(mtd.getId());
        mtudlog.setPrice(mtd.getPrice());
        mtudlog.setTotalNum(mtd.getTotalNum());
        mtudlog.setPtypeName(mtd.getPtypeName());
        mtudlog.setLp(mtd.getLp()); //楼盘

        mtudlog.setCreateTime(new Date());
        mtudlog.setUseNum(new BigDecimal(0));
        mtudlog.setUpdateTime(new Date());
        mtudlog.setRemark(MONTH_STATE_0);
        mtudlog.setOrderNo(KeyUtil.genUniqueKey());
        monthTicketUserLogRepository.save(mtudlog);

        SellerInfo sellerInfo = userRepository.findOne(uid);
        //初始化支付
        PayRequest payRequest = new PayRequest();

        payRequest.setOpenid(sellerInfo.getOpenid());
        log.info("" + mtudlog.getPrice());
        payRequest.setOrderAmount(mtudlog.getPrice().doubleValue());
        payRequest.setOrderId(mtudlog.getOrderNo());
        payRequest.setOrderName(mtudlog.getOrderNo());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【月票-微信支付请求】发起支付，request={}", JsonUtil.toJson(payRequest));

        //设置月票回调URL
        wxPayConfig.setNotifyUrl(projectUrlConfig.getWechatMpAuthorize()+MONTH_NotifyUrl);
        getPayMchKey(mtd.getLp());
        PayResponse payResponse=bestPayService.pay(payRequest);
        log.info("【月票-微信支付返回】发起支付，response={}",JsonUtil.toJson(payResponse));

        Map map = new HashMap();
        map.put("payResponse", payResponse); //支付信息
        map.put("mtu",mtudlog);
        return map;
    }





    @Override
    public Map<String, Object> queryMonthTicket(String uid) {

        List<MonthTicketUserDO> mtulist =
                monthTicketUserRepository.getMonthByUser(uid,DateTimeUtil.getMonth());
        Map map = new HashMap();
        map.put("mtulist",mtulist);

        List<SeatOrderDO> sodlist = seatOrderRepository.findSeatOrderByUser(uid,DateTimeUtil.getMonth());
        map.put("sodlist",sodlist);

        return map;
    }

    @Override
    public String queryBanci(String route, String time) {

        List<String> timelist = repository.getBuyTime(route,time);

        return getSelectStr(timelist,time);
    }

    //补票班次
    @Override
    public String queryBanci(String route) {

        List<String> timelist = repository.getBuyTimeBp(route);

        return getSelectStrBupiao(timelist);
    }


    private String getSelectStrBupiao(List<String> timelist) {
        String str = "";
        if(ComUtil.isEmpty(timelist) || timelist.get(0)==null || "null".equals(timelist.get(0))){
            str = "<option value=\"\">当前时间没有可补票的班次。</option>";
        } else {
            for (String timestr : timelist){
                str = str + "<option value='" + timestr + "'>" + timestr + "</option>";
            }
        }

        return "<select id='moment' name='moment' class='address'>" + str + "</select>";
    }




    private String getSelectStr(List<String> timelist, String time) {
        String str = "";
        if(ComUtil.isEmpty(timelist)  || timelist.get(0)==null || "null".equals(timelist.get(0)) ){
            str = "<option value=\"\">今天已经没有班车了，请选择明天的车。</option>";
        } else {

            List<Object[]> list = repository.getDayTimeFlag();
            int days = Integer.parseInt(list.get(0)[0].toString());
            int buytime1 = Integer.parseInt(list.get(0)[4].toString());
            String buybeforeticket1 = list.get(0)[5].toString();

            for (String timestr : timelist){
                if (DateTimeUtil.getBeforeDay(days-1).equals(time)){ //选的日期是最后一天
                    log.info("--------"+DateTimeUtil.getHoursOfDay(new Date())+"----------"+time+"----------"+timestr);
                    if(DateTimeUtil.getHoursOfDay(new Date())>=houre3){
                        str = str + "<option value='" + timestr + "'>" + timestr + "</option>";
                    } else if(DateTimeUtil.getHoursOfDay(new Date())>=houre2 && timestr.compareTo("08:01")<0){
                        str = str + "<option value='" + timestr + "'>" + timestr + "</option>";
                    } else if(DateTimeUtil.getHoursOfDay(new Date())>=buytime1 && timestr.compareTo(buybeforeticket1)<0){
                        str = str + "<option value='" + timestr + "'>" + timestr + "</option>";
                    }

                } else {
                    str = str + "<option value='" + timestr + "'>" + timestr + "</option>";
                }
            }
        }

        return "<select id='moment' name='moment' class='address'>" + str + "</select>";
    }



    @Override
    public void saveInfo(String name, String phone, String uid,String verify) {

        List verinum =  repository.checkVerify(uid,phone,verify);
        if(verinum == null ||verinum.size() == 0 || ComUtil.isEmpty(verinum.get(0))){
            throw new SellException(500,"验证码错误或已过期！");
        }
        SellerInfo sellerInfo = userRepository.findOne(uid);
        sellerInfo.setName(name);
        sellerInfo.setMobile(phone);
        userRepository.save(sellerInfo);
    }

    @Override
    public Map<String, Object> queryOrder(String orderId, String uid) {
        Map map = new HashMap();
        SeatOrderDO sod = seatOrderRepository.findByIdAndStateAndCreateUser(new Long(orderId),ORDER_STATE_1,uid);
        sod.setUserMobile(sod.getUserMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        map.put("createTime",DateTimeUtil.formatDateTimetoString(sod.getCreateTime(),"yyy-MM-dd HH:mm:ss"));
        map.put("sod",sod);

        return map;
    }

    @Override
    public PayResponse notifyMonth(String notifyData) {
        //1.验证签名
        //2.支付状态
        //3. 支付金额
        //4. 支付人（下单人==支付人）
        PayResponse payResponse=bestPayService.asyncNotify(notifyData);//可以完成1、2两步
        log.info("【月票微信支付 异步通知】，payResponse={}",JsonUtil.toJson(payResponse));

        //增加支付成功记录



        MonthTicketUserLogDO logDO =
                monthTicketUserLogRepository.findByOrderNoAndRemark(payResponse.getOrderId(), MONTH_STATE_0);
        if(logDO == null){
            repository.addPayLogs(payResponse.getOrderId(),
                    new BigDecimal(payResponse.getOrderAmount()),new Date(),ORDER_STATE_2);//退款
            log.error("【月票微信支付】 异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            return payResponse;
        }

        //判断金额是否一致(0.10   0.1)
        log.info(payResponse.getOrderAmount().toString()+"-----月票判断金额是否一致--------"+logDO.getPrice().toString());
        if(!MathUtil.equals(payResponse.getOrderAmount(), logDO.getPrice().doubleValue())){
            repository.addPayLogs(payResponse.getOrderId(),new BigDecimal(payResponse.getOrderAmount()),new Date(),ORDER_STATE_3);//退款

            log.error("【月票微信支付】 异步通知，订单金额不一致，orderId={},微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    logDO.getPrice());
            return payResponse;
        }


        //查询月票订单
        MonthTicketUserDO mtu =
                monthTicketUserRepository.findByCreateUserAndMonthAndRemarkAndLp(logDO.getCreateUser(),
                        logDO.getMonth(), MONTH_STATE_1, logDO.getLp());
        //判断订单是否存在
        if(mtu == null){
            mtu = new MonthTicketUserDO();

            mtu.setCreateUser(logDO.getCreateUser());
            mtu.setMonth(logDO.getMonth());
            mtu.setPtypeId(logDO.getId());
            mtu.setPrice(logDO.getPrice());
            mtu.setTotalNum(logDO.getTotalNum());
            mtu.setLp(logDO.getLp()); //楼盘
            mtu.setPtypeName("月票");
            if (LouPanEnum.XINGFUYU.getCode().equals(mtu.getLp())){
                mtu.setPtypeName(mtu.getPtypeName() + "("+LouPanEnum.XINGFUYU.getMessage()+")");
            } else if (LouPanEnum.BEIBUWANKE.getCode().equals(mtu.getLp())){
                mtu.setPtypeName(mtu.getPtypeName() + "("+LouPanEnum.BEIBUWANKE.getMessage()+")");
            }
            mtu.setCreateTime(new Date());
            mtu.setUpdateTime(new Date());
            mtu.setUseNum(new BigDecimal(0));
            mtu.setRemark(MONTH_STATE_1);
            mtu.setOrderNo(KeyUtil.genUniqueKey());

        } else {
            //如果存在,叠加次数
            mtu.setPrice(mtu.getPrice().add(logDO.getPrice()));
            mtu.setTotalNum(mtu.getTotalNum().add(logDO.getTotalNum()));
            mtu.setUpdateTime(new Date());
        }


        //保存月票订单
        monthTicketUserRepository.save(mtu);

        logDO.setRemark(MONTH_STATE_1);
        monthTicketUserLogRepository.save(logDO); //月票购买记录更新为已购买

        SellerInfo sellerInfo = userRepository.findOne(mtu.getCreateUser());

        sendMessage(sellerInfo.getOpenid(),"orderMonthStatus",getOrderMonthTemplateData(logDO),
                null);
        repository.addPayLogs(payResponse.getOrderId(),new BigDecimal(payResponse.getOrderAmount()),new Date(),ORDER_STATE_1);//付款成功
        return payResponse;
    }




    @Override
    public Map<String,Object> delVerify(String mobile) {
        Map map = new HashMap();
        int flag = repository.delVerify(mobile);

        map.put("state",flag);//
        return map;
    }



    /**
     * 退款
     * @param
     */
    @Override
    public RefundResponse refund(String orderNO,double amount) {
        RefundRequest refundRequest=new RefundRequest();
        refundRequest.setOrderAmount(amount);
        refundRequest.setOrderId(orderNO);
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【微信退款】 request={}",JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】 response={}",JsonUtil.toJson(refundResponse));



        repository.updatePayLogs(9,orderNO);//退款成功
        return refundResponse;
    }

    @Override
    public void delOrderByTimeOut() {

        List<Object[]> list = repository.getDelOrderInfo();
        for (int i = 0; list!=null && i < list.size(); i++) {
            List<Object[]> paylist =  repository.getDelPayInfo(list.get(i)[0].toString());
            if(paylist == null || paylist.isEmpty()){
                log.info(list.get(i)[0].toString()+"----订单删除开始----"+list.get(i)[1].toString());
                repository.delOrderItem(list.get(i)[1].toString());//
                repository.delOrder(list.get(i)[1].toString());//
            }
        }



    }

    @Override
    public void sendWandianMsg(Long route, String bizDate, String bizTime,String carNum,String wtime) {
        List<SeatOrderDO> list = seatOrderRepository.findByRouteIdAndBizDateAndBizTimeAndState(route,bizDate,bizTime,ORDER_STATE_1);
        for (int i = 0; i < list.size(); i++) {
            SeatOrderDO sod = list.get(i);
            SellerInfo sellerInfo = userRepository.findOne(sod.getCreateUser());
            sendMessage(sellerInfo.getOpenid(),"orderWanStatus",getWandianTemplateData(sod,carNum,wtime),
                    null);
        }
    }

    public Map<String,Object>  getOrderSeats(String route, String time, String moment) {
        List<Object[]> seatlist = repository.getOrderSeats(route, time, moment);
        Map resMap =queryOrderSeats(seatlist, route, time, moment);
        List<SeatOrderDO>  list = seatOrderRepository.findByRouteIdAndBizDateAndBizTimeAndInfoAndState(
                Long.valueOf(route),time,moment,"补票",1);
        resMap.put("bupiao",list.size());

        List<SeatOrderDO>  listZhanwei = seatOrderRepository.findByRouteIdAndBizDateAndBizTimeAndState(
                Long.valueOf(route),time,moment,0);

        List liststr = new ArrayList();
        for(SeatOrderDO seatOrderDO : listZhanwei){
            liststr.add(seatOrderDO.getInfo());
        }
        resMap.put("zhanpiao", StringUtils.join(liststr, ","));
//        log.info(StringUtils.join(liststr, ","));
        return resMap;
    }


    private Map<String,Object>  queryOrderSeats(List<Object[]> seatlist, String route, String time, String moment) {
        String carId = "";
        if (seatlist==null||seatlist.size()==0){
            return null ;
            //throw new SellException(500,"本班次已被售完！");
        }
        Set<Integer> set = new HashSet(); //存放排数
        for (int j = 0; seatlist != null && j < seatlist.size(); j++) {
            if (seatlist.get(j)[0]!=null){
                set.add(Integer.parseInt(seatlist.get(j)[0].toString()));
                carId = seatlist.get(j)[4].toString();
            }
        }
        //排数值进行排序一遍
        List tempList = new ArrayList();
        tempList.addAll(set);
        Collections.reverse(tempList);

        Map map = new LinkedHashMap ();
        Map m ;

        StringBuffer seated = new StringBuffer();
        List selected = new ArrayList();
        for (Integer rows : set) {
            List list = new ArrayList();
            for (int i = 0; seatlist != null && i < seatlist.size(); i++) {
                if (rows == Integer.parseInt(seatlist.get(i)[0].toString())){
                    m = new LinkedHashMap();
                    String col = seatlist.get(i)[1].toString();//列(座位)
                    m.put("colIndex", col);
                    m.put("seatType", seatlist.get(i)[2].toString());
//                    if(Integer.parseInt(col)>2){
//                        col = "" + (Integer.parseInt(col)-1);
//                    }
                    String seatName = seatlist.get(i)[5].toString();
//                  new StringBuilder(rows.toString()).append("排").append(col).append("座").toString();
                    m.put("name",seatName);
                    list.add(m);
                    //存放已经选完的座位
                    if (seatlist.get(i)[3] != null){
                        selected.add(seatName);
                        seated.append(seatName).append(",");
                    }
                }
            }
            map.put(rows,list);
        }



        List<Object[]> carInfo = repository.getCarInfo(carId);

        map.put("total",carInfo.get(0)[1]);
        map.put("rows",carInfo.get(0)[0]);
        map.put("cols",5);
        map.put("left",2);
        map.put("right",2);
        log.info("------"+JSONObject.toJSONString(map));

        Map resMap = new HashMap();
        resMap.put("seatlist",JSONObject.toJSONString(map));
        resMap.put("seatlistMobile",map);
        resMap.put("time",time);
        resMap.put("route",route);
        resMap.put("moment",moment);
        if(selected!=null&&selected.toString().length()>0){
            resMap.put("selected",selected.toString().substring(1,selected.toString().length()-1));
            //log.info(selected.toString().substring(1,selected.toString().length()-1));
        }
        return resMap;
    }

//    @Override
//    public void createOrderQrcode() throws Exception{
//
//        List<SeatOrderDO> list = seatOrderRepository.getorderlist();
//        for(SeatOrderDO sod : list){
//            QrcodeColorDO qrcodeColorDO = qrcodeColorRepository.findByRouteIdAndBizDate(
//                    sod.getRouteId(), sod.getBizDate());
//            if (ComUtil.isEmpty(qrcodeColorDO)){
//                qrcodeColorDO = new QrcodeColorDO();
//                qrcodeColorDO.setBizDate(sod.getBizDate());
//                qrcodeColorDO.setBizTime(sod.getBizTime());
//                qrcodeColorDO.setRouteId(sod.getRouteId());
//                qrcodeColorDO.setQrcode(QRCodeUtil.getColor());
//                qrcodeColorDO.setCreateTime(new Date());
//                qrcodeColorRepository.save(qrcodeColorDO);
//            }
//            QRCodeUtil.encode(sod.getCreateUser()+"_"+sod.getId(),QRCODE_PATH, qrcodeColorDO.getQrcode());
//        }
//
//    }
}
