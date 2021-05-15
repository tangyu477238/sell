package com.imooc.service.impl;

import com.imooc.dataobject.*;
import com.imooc.enums.DateTypeEnum;
import com.imooc.exception.BusinessException;
import com.imooc.exception.SellException;
import com.imooc.repository.*;
import com.imooc.service.BuyTicketService;
import com.imooc.service.SeatYudingOrderService;
import com.imooc.utils.ComUtil;
import com.imooc.utils.DateTimeUtil;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 *
 * @Auther: Administrator
 * @Date: 2018\12\18 0018 23:45
 * @Description:
 */
@Service
@Slf4j
public class SeatYudingOrderServiceImpl implements SeatYudingOrderService {

    @Autowired
    private SeatYudingOrderRepository seatYudingOrderRepository;

    @Autowired
    private SellerInfoRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private MonthTicketUserRepository monthTicketUserRepository;

    @Autowired
    private SeatOrderRepository seatOrderRepository;

    @Autowired
    private SeatOrderItemRepository seatOrderItemRepository;

    @Autowired
    private BuyTicketRepository buyTicketRepository;

    @Autowired
    private VerificationTicketRepository verificationTicketRepository;

    @Autowired
    private BuyTicketService buyTicketService;

    @Autowired
    private SeatOrderPingjiaRepository seatOrderPingjiaRepository;


    @Override
    public void pingjia(String uid, String orderId, String content, Integer fuwu) throws Exception {


        SeatOrderPingjiaDO seatOrderPingjiaDO = new SeatOrderPingjiaDO();
        seatOrderPingjiaDO.setContent(content);
        seatOrderPingjiaDO.setOrderId(new Long(orderId));
        seatOrderPingjiaDO.setFuwu(fuwu);
        if(!ComUtil.isEmpty(seatOrderPingjiaRepository.findByOrderId(seatOrderPingjiaDO.getOrderId()))){
            throw new Exception("不可重复评价！");
        }
        seatOrderPingjiaRepository.save(seatOrderPingjiaDO);
        SeatOrderDO seatOrderDO = seatOrderRepository.findOne(seatOrderPingjiaDO.getOrderId());
        seatOrderDO.setCkstate(1);
        seatOrderRepository.save(seatOrderDO);
    }

    @Override
    public void sendMsgBanci(String first,String yuanyin,String content,String url) {
        List<String> list  = seatYudingOrderRepository.listLastOrderCreateUser(DateTimeUtil.getMonthOfFistDay(-1));
        log.info("-------开始推送----班车变更通知--"+list.size());
        for (String uid : list){
            log.info("--uid---"+uid);
            buyTicketService.sendBiancheMessage(uid,first,yuanyin,content,url);
        }

    }

    @Override
    public Map<String, Object> shikebiao(String route, String holiday, String uid,String dateType) {
        String bizDate = verificationTicketRepository.getCalendar(holiday);
        Map<String,Object> map = new HashMap<>();
        map.put("timelist",verificationTicketRepository.getTime(route,bizDate));

        String currdate = getBizDate(1);//取得最早的一天
        String startDate = seatYudingOrderRepository.getStartDate(holiday,currdate); //取得第一天
        map.put("startDate",startDate);
        //预定开始日期
        String month = DateTimeUtil.getMonth(startDate);
        String endDate = seatYudingOrderRepository.getLastDate(holiday,startDate,month, DateTypeEnum.getNumByCode(dateType));
        map.put("endDate", endDate);

        List<BigInteger> dayNum = seatYudingOrderRepository.getWorkNum(holiday, startDate, endDate); //预定数量
        map.put("ydsl",new BigDecimal(dayNum.get(0).toString())); //预定数量

        RouteDO routeDO = routeRepository.getOne(new Long(route));
        MonthTicketUserDO mtu = monthTicketUserRepository.findByCreateUserAndMonthAndRemarkAndLp(uid,
                month, "1", routeDO.getLp());
        BigDecimal sysl = new BigDecimal(0);
        if (!ComUtil.isEmpty(mtu)) {
            sysl = mtu.getTotalNum().subtract(mtu.getUseNum());
        }
        map.put("sysl",sysl);
        log.info(map.toString());
        return map;
    }


    private void getOrderInfo(SeatOrderDO so){

        so.setAmout(so.getPrice().multiply(so.getNum()));
        so.setCkstate(0);
        so.setState(1);//付款
        so.setRemark("预约出票");
        so.setCreateTime(new Date());
        so.setUpdateTime(new Date());//锁定2分钟
        so.setOrderNo(KeyUtil.genUniqueKey());

        getOrderUserInfo(so); //设置创建人信息



    }


    private void getOrderUserInfo(SeatOrderDO so){
        SellerInfo sellerInfo = userRepository.findOne(so.getCreateUser());
        so.setUserName(sellerInfo.getName());
        so.setUserMobile(sellerInfo.getMobile());
    }


    @Override
    public void addOrder() {
        List<Object[]> paramslist = buyTicketRepository.getDayTimeFlag();
        int days = Integer.parseInt(paramslist.get(0)[0].toString()); //天数
        String bizDate = DateTimeUtil.getBeforeDay(days-1);

        List<SeatYudingOrderDO> list = seatYudingOrderRepository.listSeatYuding(bizDate);
        Collections.shuffle(list);
        for (SeatYudingOrderDO seatYudingOrderDO : list){
            SeatOrderDO so = new SeatOrderDO();
            so.setBizDate(bizDate);
            so.setBizTime(seatYudingOrderDO.getBizTime());
            so.setRouteId(seatYudingOrderDO.getRouteId());
            so.setCreateUser(seatYudingOrderDO.getCreateUser()); //创建人

            RouteDO routeDO = routeRepository.findOne(so.getRouteId());
            so.setLp(routeDO.getLp());
            so.setFromStation(routeDO.getFromStation()); //出发站
            so.setToStation(routeDO.getToStation()); //到达站

            List<Object[]> objs = seatYudingOrderRepository.getPlanPrice(String.valueOf(so.getRouteId()),
                    so.getBizDate(), so.getBizTime());
            if (ComUtil.isEmpty(objs) || objs.get(0) == null){
                //班次可能已经取消，出票失败通知
                buyTicketService.sendPiaoFailMessage(so);
                continue;
            }
            so.setPlanId(new Long(objs.get(0)[0].toString())); //公式id
            so.setPrice(new BigDecimal(objs.get(0)[1].toString())); //价格
            so.setNum(new BigDecimal(1));

            getOrderInfo(so); //设置订单基础信息

            List<Object[]> seatlist = seatYudingOrderRepository.getSeatInfo(so.getRouteId(), so.getBizDate(), so.getBizTime());
            if (seatlist.isEmpty()){
                //出票失败通知
                buyTicketService.sendPiaoFailMessage(so);
                continue;
            }
            SeatOrderItemDO sod = new SeatOrderItemDO();
            sod.setSeatId(Long.parseLong(seatlist.get(0)[0].toString()));//座位id

            so.setInfo(seatlist.get(0)[1].toString());//座位
            try {

                so = seatOrderRepository.save(so);
                log.info("order----->id:" + so.getId());

                sod.setOrderId(so.getId());
                seatOrderItemRepository.save(sod); //占票

                seatYudingOrderDO.setUseNum(seatYudingOrderDO.getUseNum().add(new BigDecimal(1))); //完成出票+1
                if (seatYudingOrderDO.getNum().compareTo(seatYudingOrderDO.getUseNum())==0){
                    seatYudingOrderDO.setState(0);
                }
                seatYudingOrderRepository.save(seatYudingOrderDO);

                //发送通知
                buyTicketService.sendBuyMessage(so);

                buyTicketRepository.addOrderLogs(so.getOrderNo(),so.getBizDate(),so.getBizTime(),so.getPlanId(),so.getInfo()
                        ,so.getPrice(),so.getNum(),so.getAmout(),so.getCreateTime(),so.getUpdateTime()
                        ,so.getCreateUser(),so.getState(),so.getRemark(),so.getFromStation()
                        ,so.getToStation(),so.getUserName(),so.getUserMobile()
                        ,so.getRouteStation(),so.getCkstate(),so.getRouteId());

            }catch (Exception e){
                throw new BusinessException("500", "座位已被其他人预定！");
            }




        }










    }

    private String getBizDate(int type){

        Date bizDateTime = DateTimeUtil.addDays(new Date(),3);
        if (type==1)
            return DateTimeUtil.formatDateTimetoString(bizDateTime, DateTimeUtil.FMT_yyyyMMdd);
        if (type==2)
            return DateTimeUtil.getLastDayOfMonth(bizDateTime);
        else
            return "";
    }

    @Override
    public void yudingOrder(String uid, String workday, String route, String time,String dateType) {
//        String startDate = getBizDate(1);
//        String endDate = getBizDate(2);

        String currdate = getBizDate(1);//取得最早的一天
        String startDate = seatYudingOrderRepository.getStartDate(workday,currdate); //取得第一天
        //预定开始日期
        String month = DateTimeUtil.getMonth(startDate);
        String endDate = seatYudingOrderRepository.getLastDate(workday,startDate,month,DateTypeEnum.getNumByCode(dateType));

        yudingOrder(uid, workday, route, time, startDate, endDate);
    }

    @Override
    public void yudingOrder(String uid, String route, String time, String bizDate, Integer dayNum) {
        List<Object[]> paramslist = buyTicketRepository.getDayTimeFlag();
        int days = Integer.parseInt(paramslist.get(0)[0].toString()); //天数
        String currdate = DateTimeUtil.getBeforeDay(days); //取得最早的一天
        String workday = seatYudingOrderRepository.getHoliday(bizDate);
        String startDate = seatYudingOrderRepository.getStartDate(workday,currdate); //取得第一天
        String endDate = seatYudingOrderRepository.getEndDateNum(workday,startDate,dayNum);

        yudingOrder(uid, workday, route, time, startDate, endDate);
    }

    @Override
    public void yudingOrder(String uid, String workday, String route, String time, String startDate, String endDate) {

        List<Object[]> yudingTimes =  seatYudingOrderRepository.getYudingTimes();
        Object routes = yudingTimes.get(0)[1]; //routes
        if (!ComUtil.isEmpty(routes) && !Arrays.asList(routes.toString().split(",")).contains(route)){
            throw new SellException(500, "目前未开放此线路预订！");
        }
        Object times = yudingTimes.get(0)[0]; //times
        if (!ComUtil.isEmpty(times) && !Arrays.asList(times.toString().split(",")).contains(time)){
            throw new SellException(500, "目前仅开放"+times+"班次预订！");
        }

        if (!DateTimeUtil.getMonth(startDate).equals(DateTimeUtil.getMonth(endDate))) {
            throw new SellException(500, "预定的”有效区间“不能跨月！");
        }

        SellerInfo sellerInfo = userRepository.findOne(uid);
        RouteDO routeDO = routeRepository.getOne(new Long(route));
        SeatYudingOrderDO seatYudingOrderDO = new SeatYudingOrderDO();
        seatYudingOrderDO.setWorkday(workday);
        seatYudingOrderDO.setBizTime(time);
        seatYudingOrderDO.setRouteId(new Long(route));
        seatYudingOrderDO.setFromStation(routeDO.getFromStation());
        seatYudingOrderDO.setToStation(routeDO.getToStation());
        seatYudingOrderDO.setLp(routeDO.getLp());
        seatYudingOrderDO.setUserName(sellerInfo.getName());
        seatYudingOrderDO.setCreateUser(sellerInfo.getSellerId());
        seatYudingOrderDO.setUserMobile(sellerInfo.getMobile());

        seatYudingOrderDO.setBizDate(startDate); //预定开始日期
        String month = DateTimeUtil.getMonth(seatYudingOrderDO.getBizDate());
        seatYudingOrderDO.setEndDate(endDate);

        List<SeatYudingOrderDO> list = seatYudingOrderRepository.listYuding(workday,route,month,uid);
        if (!ComUtil.isEmpty(list)) {
            throw new SellException(500, "每月同线路只能预定一个班次！");
        }

        List<BigInteger> dayNum = seatYudingOrderRepository.getWorkNum(seatYudingOrderDO.getWorkday(),
                seatYudingOrderDO.getBizDate(), seatYudingOrderDO.getEndDate()); //预定数量
        seatYudingOrderDO.setNum(new BigDecimal(dayNum.get(0).toString())); //预定数量
        if (seatYudingOrderDO.getNum().doubleValue() < 1) {
            throw new SellException(500, "非法订单！");
        }
        MonthTicketUserDO mtu = monthTicketUserRepository.findByCreateUserAndMonthAndRemarkAndLp(seatYudingOrderDO
                .getCreateUser(), month, "1", seatYudingOrderDO.getLp());
        if (ComUtil.isEmpty(mtu)) {
            throw new SellException(500, "月票不足！");
        }
        BigDecimal sy = mtu.getTotalNum().subtract(mtu.getUseNum()).subtract(seatYudingOrderDO.getNum());
        if (sy.doubleValue() < 0) {
            throw new SellException(500, "月票不足！");
        }
        seatYudingOrderDO.setState(1);
        seatYudingOrderDO.setRemark("月票抵扣");
        seatYudingOrderDO.setUseNum(new BigDecimal(0));
        seatYudingOrderDO.setCreateTime(new Date());
        seatYudingOrderDO.setUpdateTime(new Date());
        seatYudingOrderRepository.save(seatYudingOrderDO);

        mtu.setUseNum(mtu.getUseNum().add(seatYudingOrderDO.getNum()));//抵扣月票
        monthTicketUserRepository.save(mtu);


    }
}
