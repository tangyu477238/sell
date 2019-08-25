package com.imooc.service.impl;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.config.SimpleSMSSender;
import com.imooc.config.WechatAccountConfig;
import com.imooc.dataobject.*;
import com.imooc.exception.BusinessException;
import com.imooc.exception.SellException;
import com.imooc.repository.*;
import com.imooc.service.BuyTicketService;
import com.imooc.utils.*;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
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
import java.util.*;

/**
 * @Auther: Administrator
 * @Date: 2018\12\18 0018 23:45
 * @Description:
 */
@Service
@Slf4j
public class BuyTicketServiceImpl implements BuyTicketService {

    private static int ORDER_STATE_0 = 0; //订单待付款

    private static int ORDER_STATE_1 = 1; //订单已付款

    private static String MONTH_STATE_0 = "0"; //月票待付款

    private static String MONTH_STATE_1 = "1"; //月票已付款

    private static String MONTH_NotifyUrl = "/sell/ticket/notifyMonth"; //月票购买成功回调地址

    private static String NotifyUrl = "/sell/ticket/notify"; //车票购买成功回调地址

    private static String QRCODE_PATH = "/opt/app/apache-tomcat-8.5.37/webapps/ROOT/qrcode/";//二维码图片路径


    //订单锁定时间
    private static int ORDER_LOCK_5_ = 5; //5分钟

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private WxPayH5Config  wxPayH5Config;

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
    private BestPayServiceImpl bestPayService;

    @Autowired
    private VerifyRepository verifyRepository;


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
    public Map<String,Object>  findAll() {
        Map<String,Object> map = new HashMap();

        List<Object[]> plList = repository.listRoute();
        List<CarDatetimeSeatDO> plist = new ArrayList();
        CarDatetimeSeatDO cds ;
        for (Object[] obj: plList ) {
            cds = new CarDatetimeSeatDO();
            cds.setFromStation(obj[0].toString());
            cds.setToStation(obj[1].toString());
            cds.setId(Long.parseLong(obj[2].toString()));
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

        map.put("daylist",daylist);
        return map;
    }

    //补票
    @Override
    public Map<String,Object>  bupiao() {
        Map<String,Object> map = new HashMap();
        List<Object[]> plList = repository.listBupiao();

        List<CarDatetimeSeatDO> list = new ArrayList();
        CarDatetimeSeatDO cds ;
        for (Object[] obj: plList ) {
             cds = new CarDatetimeSeatDO();
             cds.setFromStation(obj[0].toString());
             cds.setToStation(obj[1].toString());
             cds.setBizDate(obj[2].toString());
             cds.setId(Long.parseLong(obj[3].toString()));
             cds.setBizTime(obj[4].toString());
            list.add(cds);
        }
        map.put("plList",list);


        map.put("day",DateTimeUtil.getBeforeDay(0));
        return map;
    }


    @Override
    synchronized
    public Map<String,Object> addOrder(String route,String time, String moment,
                                       String seat,String num,String uid,String routeStation) {

        List<BigDecimal> numlist = repository.getBuyCarNum(route,time,moment,uid);
        BigDecimal carnum = new BigDecimal(num);
        if (!ComUtil.isEmpty(numlist)&&numlist.size()>0 && !ComUtil.isEmpty(numlist.get(0))){
            carnum = numlist.get(0).add(carnum);
        }
        if (carnum.intValue()>4){
            throw new BusinessException("500","同一班车最多购买或预定4张车票！");
        }


        String seatNames [] = seat.split(",");
        List<Object[]> seatlist = repository.listSeatOder(route,time,moment,seatNames);
        if (!ComUtil.isEmpty(seatlist)){
            for (int j = 0;  j < seatlist.size(); j++) {
                if(seatlist.get(j)[2] != null){
                    log.info(seatlist.get(j)[1]+"已被预定！");
                    throw new BusinessException("500", seatlist.get(j)[1]+"已被其他人预定！");
                }
            }
        }

        SeatOrderDO so = new SeatOrderDO();
        so.setBizDate(time);
        so.setBizTime(moment);
        so.setPlanId(new Long(route));
        so.setNum(new BigDecimal(num));
        so.setCreateTime(new Date());

        so.setRouteStation(routeStation);//上车点

        List<Object[]> list = repository.getDayTimeFlag();
        int orderlock = Integer.parseInt(list.get(0)[2].toString());
        so.setUpdateTime(DateTimeUtil.addMinutes(new Date(),orderlock));//锁定2分钟
        so.setInfo(seat);
        so.setState(ORDER_STATE_0);//待付款
        so.setRemark("");

        List<Object[]> objs = repository.getPlanPrice(route);
        if (!ComUtil.isEmpty(objs) && objs.size()>0) {
            so.setFromStation(objs.get(0)[0].toString());
            so.setToStation(objs.get(0)[1].toString());
            so.setPrice(new BigDecimal(objs.get(0)[2].toString()));
        }
        so.setAmout(so.getPrice().multiply(so.getNum()));
        so.setCreateUser(uid); //创建人

        SellerInfo sellerInfo = userRepository.findOne(uid);

        so.setUserName(sellerInfo.getName());
        so.setUserMobile(sellerInfo.getMobile());
        so.setOrderNo(KeyUtil.genUniqueKey());

        so = seatOrderRepository.save(so);
        log.info("order----->id:"+so.getId());
        for (int j = 0;  j < seatlist.size(); j++) {
            if(seatlist.get(j)[0] != null){
                SeatOrderItemDO sod = new SeatOrderItemDO();
                sod.setSeatId(Long.parseLong(seatlist.get(j)[0].toString()));
                sod.setOrderId(so.getId());
                seatOrderItemRepository.save(sod); //占票
            }
        }

        Map map = new HashMap();

        map.put("sod",so);
        map.put("createTime",DateTimeUtil.formatDateTimetoString(so.getCreateTime(),"yyy-MM-dd HH:mm:ss"));

        MonthTicketUserDO mtu =
                monthTicketUserRepository.findByCreateUserAndMonthAndRemark(uid,time.substring(0,7),MONTH_STATE_1);
        if(ComUtil.isEmpty(mtu)){
            map.put("sysl","0");
        }else {
            map.put("sysl",mtu.getTotalNum().subtract(mtu.getUseNum()).toString());
        }


        return map;
    }











    @Override
    public Map<String,Object> addBupiao(String route, String moment,String uid) {


        SeatOrderDO so = new SeatOrderDO();
        so.setBizDate(DateTimeUtil.getBeforeDay(0));
        so.setBizTime(moment);
        so.setPlanId(new Long(route));
        so.setNum(new BigDecimal(1));
        so.setCreateTime(new Date());

        List<Object[]> list = repository.getDayTimeFlag();
        int orderlock = Integer.parseInt(list.get(0)[2].toString());

        so.setUpdateTime(DateTimeUtil.addMinutes(new Date(),orderlock));//锁定2分钟(补票)
        so.setState(ORDER_STATE_0);//待付款(补票)
        so.setRemark("");
        so.setInfo("补票");

        List<Object[]> objs = repository.getPlanPrice(route);
        if (!ComUtil.isEmpty(objs) && objs.size()>0) {
            so.setFromStation(objs.get(0)[0].toString());
            so.setToStation(objs.get(0)[1].toString());
            so.setPrice(new BigDecimal(objs.get(0)[2].toString()));
        }
        so.setAmout(so.getPrice().multiply(so.getNum()));
        so.setCreateUser(uid); //创建人
        SellerInfo sellerInfo = userRepository.findOne(uid);
        so.setUserName(sellerInfo.getName());
        so.setUserMobile(sellerInfo.getMobile());
        so.setOrderNo(KeyUtil.genUniqueKey());
        so = seatOrderRepository.save(so);
        log.info("order--bupiao--->id:"+so.getId()+"-----"+so.getOrderNo());
        Map map = new HashMap();
        map.put("sod",so);
        map.put("createTime",DateTimeUtil.formatDateTimetoString(so.getCreateTime(),"yyy-MM-dd HH:mm:ss"));

        MonthTicketUserDO mtu =
                monthTicketUserRepository.findByCreateUserAndMonthAndRemark(uid,
                        DateTimeUtil.getMonth(), MONTH_STATE_1);
        if(ComUtil.isEmpty(mtu)){
            map.put("sysl","0");
        }else {
            map.put("sysl",mtu.getTotalNum().subtract(mtu.getUseNum()).toString());
        }
        return map;
    }




    @Override
    public Map<String,Object>  listSeatDetail(String route,String time, String moment) {

        String carId = "";
        List<Object[]> seatlist = repository.listSeatDetail(route, time, moment);
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
//                            new StringBuilder(rows.toString()).append("排").append(col).append("座").toString();
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
        resMap.put("time",time);
        resMap.put("route",route);
        resMap.put("moment",moment);

        if(selected!=null&&selected.toString().length()>0){
            resMap.put("selected",selected.toString().substring(1,selected.toString().length()-1));
            //log.info(selected.toString().substring(1,selected.toString().length()-1));
        }
        return resMap;
    }


    @Override
    public void deleteSorder(String uid, String orderId) {
        repository.deleteByOrderPid(orderId);//删除orderItem
        repository.deleteByOrdId(orderId);//删除order
    }

    //月票抵扣
    @Override
    public void updateYuepiaoOrder(String uid, String orderId) throws Exception{

        log.info("----------------updateYuepiaoOrder-------------------");
        SeatOrderDO sod = seatOrderRepository.findByIdAndStateAndCreateUser(new Long(orderId),0,uid);
        double ttime =DateTimeUtil.getSecondsOfTwoDate(sod.getUpdateTime(),new Date()) ;
        if (sod== null || ttime<0){
            throw new SellException(500,"订单不存在或已超支付时间！");
        }
        List<BigDecimal> numlist = repository.getBuyMonthNum(uid,sod.getBizDate(),sod.getBizTime(),sod.getPlanId());
        BigDecimal yuepnum = sod.getNum();
        if (!ComUtil.isEmpty(numlist)&&numlist.size()>0 && !ComUtil.isEmpty(numlist.get(0))){
            yuepnum = numlist.get(0).add(yuepnum);
        }
        if (yuepnum.intValue()>3){
            throw new SellException(500,"同一班车月票最多只能抵扣3张！");
        }
//        List<SeatOrderItemDO> sodilist = seatOrderItemRepository.findByOrderId(sod.getId());
//        SeatOrderItemDO sodi = null ;
//        for (int i = 0; sodilist!=null && i < sodilist.size(); i++) {
//            sodi = sodilist.get(i);
//        }
        MonthTicketUserDO mtu =
                monthTicketUserRepository.findByCreateUserAndMonthAndRemark(uid,sod.getBizDate().substring(0,7),MONTH_STATE_1);
        BigDecimal sy = mtu.getTotalNum().subtract(mtu.getUseNum()).subtract(sod.getNum());
        if (sy.doubleValue()<0){
            throw new SellException(500,"月票不足！");
        }
        mtu.setUseNum(mtu.getUseNum().add(sod.getNum()));
        monthTicketUserRepository.save(mtu);
        sod.setState(ORDER_STATE_1);
        sod.setRemark("月票抵扣");
        sod.setCkstate(0);//未验票
        seatOrderRepository.save(sod);



        SellerInfo sellerInfo = userRepository.findOne(uid);

        QRCodeUtil.encode(sellerInfo.getSellerId()+"_"+sod.getId(),QRCODE_PATH);
        //显示详情
//        sendMessage(sellerInfo.getOpenid(),"orderStatus",getOrderTemplateData(sod)
//                ,projectUrlConfig.getWechatMpAuthorize()+"/sell/ticket/queryOrder?orderId="+sod.getId()+"&uid="+sellerInfo.getSellerId());
        //显示二维码
        sendMessage(sellerInfo.getOpenid(),"orderStatus",getOrderTemplateData(sod)
                ,projectUrlConfig.getWechatMpAuthorize()+"/qrcode/"+sellerInfo.getSellerId()+"_"+sod.getId()+".jpg");


    }





    @Override
    public Map<String, Object> payOrder(String orderId, String uid) {
        SeatOrderDO sod = seatOrderRepository.findByIdAndStateAndCreateUser(new Long(orderId),0, uid);

        if (sod== null){
            throw new SellException(501,"订单不存在或已超支付时间！");
        }
        double ttime1 =DateTimeUtil.getSecondsOfTwoDate(sod.getUpdateTime(),new Date()) ;
        if (ttime1<0){
            throw new SellException(501,"订单不存在或已超支付时间！");
        }

        SellerInfo sellerInfo = userRepository.findOne(uid);

        //初始化支付
        PayRequest payRequest=new PayRequest();
        payRequest.setOpenid(sellerInfo.getOpenid());
        log.info(""+sod.getAmout().doubleValue());
        payRequest.setOrderAmount(sod.getAmout().doubleValue());
        payRequest.setOrderId(sod.getOrderNo());
        payRequest.setOrderName(sod.getOrderNo());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付请求】发起支付，request={}", JsonUtil.toJson(payRequest));
        wxPayH5Config.setNotifyUrl(projectUrlConfig.getWechatMpAuthorize()+NotifyUrl);
        bestPayService.setWxPayH5Config(wxPayH5Config);

        PayResponse payResponse=bestPayService.pay(payRequest);
        log.info("【微信支付返回】发起支付，response={}",JsonUtil.toJson(payResponse));

        Map map = new HashMap();
        map.put("payResponse",payResponse); //支付信息
        map.put("sod",sod);
        map.put("orderNo",sod.getOrderNo());
        return map;
    }


    @Override
    public PayResponse notify(String notifyData) throws Exception {
        //1.验证签名
        //2.支付状态
        //3. 支付金额
        //4. 支付人（下单人==支付人）
        PayResponse payResponse=bestPayService.asyncNotify(notifyData);//可以完成1、2两步
        log.info("【微信支付 异步通知】，payResponse={}",JsonUtil.toJson(payResponse));

        //查询订单
        SeatOrderDO sod = seatOrderRepository.findByOrderNoAndState(payResponse.getOrderId(),0);

        //判断订单是否存在
        if(sod == null){
            log.error("【微信支付】 异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            return payResponse;
            //throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断金额是否一致(0.10   0.1)
        log.info(payResponse.getOrderAmount().toString()+"-----判断金额是否一致--------"+sod.getAmout().toString());
        if(!MathUtil.equals(payResponse.getOrderAmount(),sod.getAmout().doubleValue())){
            log.error("【微信支付】 异步通知，订单金额不一致，orderId={},微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    sod.getAmout());
            return payResponse;
        }


        //修改订单的支付状态
        sod.setRemark("微信支付");
        sod.setState(1);
        sod.setCkstate(0);//未验票
        seatOrderRepository.save(sod);

        SellerInfo sellerInfo = userRepository.findOne(sod.getCreateUser());

        QRCodeUtil.encode(sellerInfo.getSellerId()+"_"+sod.getId(),QRCODE_PATH);
        //显示订单详情
        sendMessage(sellerInfo.getOpenid(),"orderStatus",getOrderTemplateData(sod)
                ,projectUrlConfig.getWechatMpAuthorize()+"/sell/ticket/queryOrder?orderId="+sod.getId()+"&uid="+sellerInfo.getSellerId());

//        //只显示二维码
//        sendMessage(sellerInfo.getOpenid(),"orderStatus",getOrderTemplateData(sod)
//                ,projectUrlConfig.getWechatMpAuthorize()+"/qrcode/"+sellerInfo.getSellerId()+"_"+sod.getId()+".jpg");
        return payResponse;
    }








    private List<WxMpTemplateData> getOrderTemplateData(SeatOrderDO sod ){
        //发送通知
        List<WxMpTemplateData> data= Arrays.asList(
                new WxMpTemplateData("first","您好，您已成功购票。"),
                new WxMpTemplateData("keyword1",sod.getBizDate()+" "+sod.getBizTime(),"#B5B5B5"),
                new WxMpTemplateData("keyword2",sod.getOrderNo() + ",请提前至少10分钟取票上车","#B5B5B5"),
                new WxMpTemplateData("keyword3",sod.getInfo(),"#B5B5B5"),
                new WxMpTemplateData("keyword4",sod.getNum()+"人","#B5B5B5"),
                new WxMpTemplateData("remark","为避免超载，请主动为小朋友购买车票。\r\n欢迎再次购买。\r\n如需帮助请致电13922700093。","#173177"));

        return data;
    }

    private List<WxMpTemplateData> getOrderMonthTemplateData(MonthTicketUserDO mtu ){
        //发送通知
        List<WxMpTemplateData> data= Arrays.asList(
                new WxMpTemplateData("first","您好，您已成功购买"+mtu.getMonth()+"月卡。"),
                new WxMpTemplateData("keyword1",mtu.getPtypeName()+"(总共"+mtu.getTotalNum()+"张)","#B5B5B5"),
                new WxMpTemplateData("keyword2",mtu.getPrice()+"元","#B5B5B5"),
                new WxMpTemplateData("keyword3",mtu.getMonth()+"月","#B5B5B5"),
                new WxMpTemplateData("remark","欢迎再次购买。\r\n如需帮助请致电13922700093。","#173177"));

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
    public Map<String, Object> buyTicket() {

        MonthTicketDO mtd = monthTicketRepository.findByState(1);

//        SellerInfo sellerInfo = userRepository.findOne(uid);


        Map map = new HashMap();
        map.put("mtd",mtd);
        return map;
    }




    @Override
    public Map<String, Object> payMonthTick(String id, String uid,String month_val) {

        MonthTicketUserDO mtud =
                monthTicketUserRepository.findByCreateUserAndMonth(uid,month_val);
        if (!ComUtil.isEmpty(mtud)&&"1".equals(mtud.getRemark())){
            throw new BusinessException("500","月票不可重复购买！");
        }
        MonthTicketDO mtd = monthTicketRepository.findById(new Long(id));
        if (ComUtil.isEmpty(mtud)){
            mtud = new MonthTicketUserDO();
        }
        mtud.setCreateTime(new Date());
        mtud.setCreateUser(uid);
        mtud.setMonth(month_val);
        mtud.setPtypeId(mtd.getId());
        mtud.setPrice(mtd.getPrice());
        mtud.setTotalNum(mtd.getTotalNum());
        mtud.setUseNum(new BigDecimal(0));
        mtud.setUpdateTime(new Date());
        mtud.setPtypeName(mtd.getPtypeName());
        mtud.setRemark(MONTH_STATE_0);
        mtud.setOrderNo(KeyUtil.genUniqueKey());
        monthTicketUserRepository.save(mtud);

        SellerInfo sellerInfo = userRepository.findOne(uid);
        //初始化支付
        PayRequest payRequest = new PayRequest();

        payRequest.setOpenid(sellerInfo.getOpenid());
        log.info("" + mtd.getPrice());
        payRequest.setOrderAmount(mtud.getPrice().doubleValue());
        payRequest.setOrderId(mtud.getOrderNo());
        payRequest.setOrderName(mtud.getOrderNo());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【月票-微信支付请求】发起支付，request={}", JsonUtil.toJson(payRequest));

        //设置月票回调URL
        wxPayH5Config.setNotifyUrl(projectUrlConfig.getWechatMpAuthorize()+MONTH_NotifyUrl);
        bestPayService.setWxPayH5Config(wxPayH5Config);

        PayResponse payResponse=bestPayService.pay(payRequest);
        log.info("【月票-微信支付返回】发起支付，response={}",JsonUtil.toJson(payResponse));

        Map map = new HashMap();
        map.put("payResponse",payResponse); //支付信息
        map.put("mtu",mtud);
        return map;
    }


    @Override
    public Map<String, Object> queryMonthTicket(String uid) {

        List<MonthTicketUserDO> mtulist =
                monthTicketUserRepository.findByCreateUserAndRemarkOrderByIdDesc(uid,MONTH_STATE_1);
        Map map = new HashMap();
        map.put("mtulist",mtulist);

        List<SeatOrderDO> sodlist = seatOrderRepository.findByStateAndCreateUserOrderByIdDesc(ORDER_STATE_1,uid);
        map.put("sodlist",sodlist);

        return map;
    }

    @Override
    public String queryBanci(String route, String time) {

        List<String> timelist = repository.getBuyTime(route,time);

        return getSelectStr(timelist);
    }

    //补票班次
    @Override
    public String queryBanci(String route) {

        List<String> timelist = repository.getBuyTimeBp(route);

        return getSelectStr(timelist);
    }

    private String getSelectStr(List<String> timelist) {
        String str = "";
        if(ComUtil.isEmpty(timelist)){
            str = "<option value=\"\">今天已经没有班车了，请选择明天的车。</option>";
        }
        for (String timestr : timelist){
            str = str + "<option value='" + timestr + "'>" + timestr + "</option>";
        }
        return "<select name='moment' class='address'>" + str + "</select>";
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



        //查询订单
        MonthTicketUserDO mtu =
                monthTicketUserRepository.findByOrderNoAndRemark(payResponse.getOrderId(),MONTH_STATE_0);

        //判断订单是否存在
        if(mtu == null){
            log.error("【月票微信支付】 异步通知，订单不存在，orderId={}",payResponse.getOrderId());
            return payResponse;
            //throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //判断金额是否一致(0.10   0.1)
        log.info(payResponse.getOrderAmount().toString()+"-----月票判断金额是否一致--------"+mtu.getPrice().toString());
        if(!MathUtil.equals(payResponse.getOrderAmount(),mtu.getPrice().doubleValue())){
            log.error("【月票微信支付】 异步通知，订单金额不一致，orderId={},微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    mtu.getPrice());
            //throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY);
            return payResponse;
        }


        //修改订单的支付状态
        mtu.setRemark(MONTH_STATE_1);
        monthTicketUserRepository.save(mtu);
        SellerInfo sellerInfo = userRepository.findOne(mtu.getCreateUser());

        sendMessage(sellerInfo.getOpenid(),"orderMonthStatus",getOrderMonthTemplateData(mtu),
                null);
        return payResponse;
    }

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

    public static void main (String str []){


//        TreeSet treeSet = new TreeSet(originalList);
//        List tempList = new ArrayList();
//        tempList.addAll(treeSet);
//        treeSet 默认的排序为升序，根据实际情况添加是否需要反排序
//        Collections.reverse(tempList);
    }
}
