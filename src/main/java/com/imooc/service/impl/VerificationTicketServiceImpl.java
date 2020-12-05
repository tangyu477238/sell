package com.imooc.service.impl;

import com.imooc.dataobject.*;
import com.imooc.enums.YanpiaoEnum;
import com.imooc.repository.*;
import com.imooc.service.BuyTicketService;
import com.imooc.service.VerificationTicketService;
import com.imooc.utils.ComUtil;
import com.imooc.utils.JPushService;
import com.imooc.utils.WeChatQrcodeUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private BuyTicketService buyTicketService;
    @Autowired
    private SeatOrderRepository seatOrderRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private VerificationTicketRepository verificationTicketRepository;
    @Autowired
    private JpushRepository jpushRepository;
    @Autowired
    private JPushService jPushService;
    @Autowired
    private SequenceRepository sequenceRepository;
    @Autowired
    private QrcodeOrderRepository qrcodeOrderRepository;
    @Autowired
    private WeChatQrcodeUtils weChatQrcodeUtils;


    @Override
    public void sendWandianMsg(Long route, String bizDate, String bizTime, String wtime) {

        buyTicketService.sendWandianMsg(route,bizDate,bizTime,"",wtime);
    }

    @Override
    public Map pushMessage(String notificationTitle,String exts) {

        List<JpushDo>  list = jpushRepository.findAll();
        List<String> aliasList = new ArrayList<>();
        for (JpushDo jpushDo : list){
            aliasList.add(jpushDo.getUid());
        }
        String msgContent = notificationTitle;
        //默认按注册ID推送 消息
        jPushService.sendToRegistRationIdsList(aliasList, notificationTitle,
                YanpiaoEnum.getMessageByCode(exts), msgContent, exts);
        //推送所有设备广播
        // jPushService.sendToAll(notificationTitle, msgTitle, msgContent, "exts");

        return null;
    }



    @Override
    public Map addRegistrationId(String registrationId) {

        JpushDo jPushDo = jpushRepository.findByUid(registrationId);
        if (jPushDo==null){
            jPushDo = new JpushDo();
            jPushDo.setMobile(registrationId);
            jPushDo.setUid(registrationId);
            jPushDo.setCreateTime(new Date());
            jpushRepository.save(jPushDo);
        }
        Map map = new HashMap();
        map.put("flag",1);
        return map;
    }

    @Override
    public void yuecheSave(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {

    }



    @Override
    public Map<String,Object> cktikcet(String route, String bizDate, String bizTime, String uid){

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
        SeatOrderDO sod = seatOrderRepository.findByIdAndStateAndCreateUser(new Long(strs[1]),
                ORDER_STATE_1,strs[0]);
        if (!sod.getRouteId().toString().equals(route)
                ||!sod.getBizDate().equals(bizDate)
                ||!sod.getBizTime().equals(bizTime)){
            map.put("state","banci");//班次不一致
            return map;
        }
        List list = new ArrayList();
        list.add(sod);
        map = cktikcet(list);
        return map;
    }

    @Override
    public Map<String,Object> cktikcet(List<SeatOrderDO> list) {

        Map map = new HashMap();
        if (ComUtil.isEmpty(list) || list.size()<1){
            map.put("state","fail");//验票失败
            return map;
        }
        SeatOrderDO sod = list.get(0);
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

        map.put("state", String.valueOf(sod.getNum().intValue()));//验票ok
        map.put("info", sod.getInfo() + "("+sod.getBizDate()+" "+sod.getBizTime()+")"+"["+sod.getFromStation()+"-"+sod.getToStation()+"]");
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

    @Override
    public Map<String, Object> shikebiao(String route, String holiday) {
        String bizDate = verificationTicketRepository.getCalendar(holiday);
        return cktikcetTime(route,bizDate);
    }

    @Override
    public Map<String, Object> getQrcode(String route, String bizDate, String bizTime) throws Exception{
        SequenceDO sequenceDO = sequenceRepository.findByBizDate(bizDate);
        if (sequenceDO == null){
            sequenceDO = new SequenceDO();
            sequenceDO.setBizDate(bizDate);
            sequenceDO.setNum(1);
        } else {
            sequenceDO.setNum(sequenceDO.getNum()+1);
        }
        sequenceRepository.save(sequenceDO);

        QrcodeOrderDO qrcodeOrderDO = new QrcodeOrderDO();
        qrcodeOrderDO.setRouteId(Long.parseLong(route));
        qrcodeOrderDO.setBizDate(bizDate);
        qrcodeOrderDO.setBizTime(bizTime);
        qrcodeOrderDO.setOrderNo(sequenceDO.getNum());
        qrcodeOrderDO.setCreateTime(new Date());
        qrcodeOrderRepository.save(qrcodeOrderDO);

        WxMpQrCodeTicket wxMpQrCodeTicket =
                weChatQrcodeUtils.qrCodeCreateTmpTicket(sequenceDO.getNum(), 7200);
        String urlPicture = weChatQrcodeUtils.qrCodePictureUrl(wxMpQrCodeTicket.getTicket());
        Map map = new HashMap();
        map.put("urlPicture", urlPicture);
        return map;
    }
}
