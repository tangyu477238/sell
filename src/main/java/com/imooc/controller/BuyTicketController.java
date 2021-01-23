package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.config.ProjectUrlConfig;
import com.imooc.dataobject.SeatOrderDO;
import com.imooc.dataobject.SellerInfo;
import com.imooc.enums.LouPanEnum;
import com.imooc.repository.SellerInfoRepository;
import com.imooc.service.BuyTicketService;
import com.imooc.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/ticket")
public class BuyTicketController {

    @Autowired
    private BuyTicketService buyTicketService;

    @Autowired
    private SellerInfoRepository userRepository;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;



    private void setLpInfo(Map map, String lp){
        map.put("lp", lp);
        if (LouPanEnum.BEIBUWANKE.getCode().equals(lp)){
            map.put("lpName", "北部万科城");
            map.put("lpColor", "#2952f6");
        } else if (LouPanEnum.XINGFUYU.getCode().equals(lp)){
            map.put("lpName", "万科幸福誉");
            map.put("lpColor", "#F66D22");
        } else  {
            map.put("lpName", "万科幸福誉");
            map.put("lpColor", "#F66D22");
        }
    }

    //进入选线路
    @GetMapping("/ticket")
    public ModelAndView ticketIndex(@RequestParam("uid") String uid,
                                    @RequestParam("lp") String lp,
                                    Map<String,Object> map){
        log.info("进入TicketIndex方法......." + uid);
        SellerInfo sellerInfo = userRepository.findOne(uid);
        map.put("uid", uid);
        if(sellerInfo.getMobile() == null){
            return new ModelAndView("mobile/addUserInfo", map);
        }
        map = buyTicketService.findAll(lp);
        setLpInfo(map, lp);
        return new ModelAndView("mobile/index", map);
    }

    //进入补票
    @GetMapping("/bupiao")
    public ModelAndView ticketBupiao(@RequestParam("uid") String uid,
                                     @RequestParam("lp") String lp,
                                     Map<String,Object> map){
        log.info("ticketBupiao......." + uid);
        map.put("uid",uid);
        map = buyTicketService.bupiao(lp);
        setLpInfo(map, lp);
        return new ModelAndView("mobile/bupiao", map);
    }









    //选择座位
    @GetMapping("/cseat")
    public ModelAndView  cseat(@RequestParam("route") String route,
                               @RequestParam("time") String time,
                               @RequestParam("moment") String moment,
                               @RequestParam("routeStation") String routeStation,
                               @RequestParam("uid") String uid,
                               @RequestParam("lp") String lp){
        log.info("进入选座方法.......");
        log.info("----route-----" + route);
        log.info("----time-----" + time);
        log.info("----time-----" + moment);

        Map<String,Object>  map = buyTicketService.listSeatDetail(route,time,moment);
        if (map == null){
            map = new HashMap<>();
            map.put("uid",uid);
            return new ModelAndView("mobile/orderFail",map);
        }

        map.put("uid",uid);
        map.put("routeStation",routeStation);
        setLpInfo(map, lp);
        return new ModelAndView("mobile/chooseSeat", map);

    }

    //订单确认
    @GetMapping("/cord")
    public ModelAndView cord(@RequestParam("route") String route,
                             @RequestParam("time") String time,
                             @RequestParam("moment") String moment,
                             @RequestParam("seat") String seat,
                             @RequestParam("num") String num,
                             @RequestParam("uid") String uid,
                             @RequestParam("lp") String lp,
                             @RequestParam("routeStation") String routeStation,
                             Map<String,Object> map){
        log.info("-------------进入确认订单方法.......");

        map = buyTicketService.addOrder(route,time,moment,seat,num,uid,routeStation);

        map.put("uid",uid);
        log.info("--------进入确认订单方法-----"+map.toString());
        setLpInfo(map, lp);
        return new ModelAndView("mobile/sureOrder",map);

    }

    //补票确认
    @GetMapping("/cordBupiao")
    public ModelAndView cordBupiao(@RequestParam("route") String route,
                             @RequestParam("moment") String moment,
                             @RequestParam("uid") String uid,
                             Map<String,Object> map){
        log.info("-------------进入addBupiao.......");

        map = buyTicketService.addBupiao(route,moment,uid);

        map.put("uid",uid);
        return new ModelAndView("mobile/sureBupiao",map);

    }



    //订单明细
    @GetMapping("/queryOrder")
    public ModelAndView queryOrder(
                             @RequestParam("orderId") String orderId,
                             @RequestParam("uid") String uid,
                             Map<String,Object> map){
        log.info("-------------queryOrder.......");
        orderId = orderId.replace(",","");
        map = buyTicketService.queryOrder(orderId,uid);

        SeatOrderDO sod =(SeatOrderDO) map.get("sod");
        map.put("uid",uid);
        map.put("qrcode",projectUrlConfig.getWechatMpAuthorize()+"/qrcode/"+ uid +"_"+sod.getId()+".jpg");
        setLpInfo(map, sod.getLp());
        return new ModelAndView("mobile/queryOrder",map);

    }

    //支付订单
    @GetMapping("/payOrder")
    public ModelAndView payOrder(
                             @RequestParam("orderId") String orderId,
                             @RequestParam("uid") String uid,
                             Map<String,Object> map){
        log.info(orderId+"-------------payOrder......."+uid);
        map = buyTicketService.payOrder(orderId,uid);
        map.put("uid",uid);
        map.put("returnUrl","/sell/ticket/orderSuccess?orderNo=" + map.get("orderNo"));
        return new ModelAndView("pay/create",map);

    }





    @GetMapping("/orderSuccess")
    public ModelAndView orderSuccess(@RequestParam("orderNo") String orderNo,Map<String,Object> map){
        log.info(".....orderSuccess.......");
        map.put("orderNo",orderNo);
        return new ModelAndView("mobile/orderSuccess",map);

    }

    @GetMapping("/getsjf")
    public String getsjf(){
        log.info("进入我的积分方法.......");

        return "/sjf.html" ;

    }

    @GetMapping("/getskb")
    public String getskb(){
        log.info("wx_shikebiao.......");
        return "/wx_shikebiao.html" ;

    }


    //取消订单
    @ResponseBody
    @GetMapping("/delsorder")
    public ResultVO deleteSorder(@RequestParam("orderId") String orderId,
                                 @RequestParam("uid") String uid,
                                 Map<String,Object> map){
        log.info("进入取消订单方法.......");
        buyTicketService.deleteSorder(uid,orderId);

        return ResultVOUtil.success();

    }

    //个人信息补充
    @ResponseBody
    @GetMapping("/saveInfo")
    public ResultVO saveInfo(@RequestParam("name") String name,
                                @RequestParam("verify") String verify,
                                @RequestParam("phone") String phone,
                                @RequestParam("uid") String uid,
                                 Map<String,Object> map){
        log.info("saveInfo.......");

        buyTicketService.saveInfo( name, phone, uid, verify);
        return ResultVOUtil.success();

    }

    //个人信息补充
    @ResponseBody
    @GetMapping("/sendYzm")
    public ResultVO sendYzm(@RequestParam("name") String name,
                             @RequestParam("phone") String phone,
                             @RequestParam("uid") String uid,
                             Map<String,Object> map){
        log.info("saveInfo.......");

        buyTicketService.sendYzm( name, phone, uid);
        return ResultVOUtil.success();

    }






    //月票抵扣
    @ResponseBody
    @GetMapping("/yuepiaoOrder")
    public ResultVO updateYuepiaoOrder(@RequestParam("orderId") String orderId,
                                 @RequestParam("uid") String uid,
                                 Map<String,Object> map) throws Exception{
        log.info("进入updateYuepiaoOrder订单方法.......");
        buyTicketService.updateYuepiaoOrder(uid,orderId);

        return ResultVOUtil.success();

    }


    //购票班次查询
    @ResponseBody
    @PostMapping("/queryBanci")
    public String queryBanci(@RequestParam("route") String route,
                               @RequestParam("time") String time,
                               @RequestParam("uid") String uid,
                                       Map<String,Object> map){
        log.info("进入updateYuepiaoOrder订单方法.......");

        String banci = buyTicketService.queryBanci(route,time);
        return banci;
    }


    //补票班次查询
    @ResponseBody
    @PostMapping("/queryBanciBp")
    public String queryBanciBp(@RequestParam("route") String route,
                             @RequestParam("uid") String uid,
                             Map<String,Object> map){
        log.info("进入queryBanciBp订单方法.......");

        String banci = buyTicketService.queryBanci(route);
        return banci;
    }

    //进入购买月票
    @GetMapping("/buyTicket")
    public ModelAndView buyTicket(@RequestParam("uid") String uid,
                                  @RequestParam("lp") String lp,
                                  Map<String,Object> map)  throws ParseException {
        log.info("进入buyTicket方法.......");

        map = buyTicketService.buyTicket(lp);
        map.put("uid",uid);

        setLpInfo(map, lp);
        return new ModelAndView("mobile/buyMonth", map);
    }



    //我的订单列表/月票/次票
    @GetMapping("/queryMonthTicket")
    public ModelAndView queryMonthTicket(@RequestParam("uid") String uid,Map<String,Object> map){
        log.info("queryMonthTicket.......");

        map = buyTicketService.queryMonthTicket(uid);
        map.put("uid",uid);

        return new ModelAndView("mobile/sorder",map);
    }



    //购买月票付款
    @PostMapping("/payMonthTick")
    public ModelAndView payMonthTick(
            @RequestParam("id") String id,
            @RequestParam("uid") String uid,
            @RequestParam("month_val") String month_val,
            Map<String,Object> map){
        log.info(id+"-------------payMonthTick......."+uid);
        map = buyTicketService.payMonthTick(id,uid,month_val);
        map.put("uid",uid);

        map.put("returnUrl","/sell/ticket/orderMonthSuccess");
        return new ModelAndView("pay/create",map);
    }

    @GetMapping("/orderMonthSuccess")
    public ModelAndView orderMonthSuccess(Map<String,Object> map){
        log.info(".....orderSuccess.......");
        //map.put("orderNo",orderNo);
        return new ModelAndView("mobile/orderMonthSuccess",map);

    }



    /**
     * 订票微信异步通知
     * @param notifyData
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) throws Exception {

        log.info("notifyData:{}",notifyData);
        buyTicketService.notify(notifyData);

        //返回给微信处理结果
//        String string="";
        return new ModelAndView("pay/success");
    }

    /**
     * 月票微信异步通知
     * @param notifyData
     */
    @PostMapping("/notifyMonth")
    public ModelAndView notifyMonth(@RequestBody String notifyData){

        log.info("notifyData:{}",notifyData);
        buyTicketService.notifyMonth(notifyData);

        //返回给微信处理结果
//        String string="";
        return new ModelAndView("pay/success");
    }




    //cktikcet  删除验证码
    @ResponseBody
    @GetMapping("/cktikcetDelRand")
    public Map cktikcetDelRand(@RequestParam("mobile") String mobile,
                        Map<String,Object> map){
        log.info("ckticketDelRand.......");
        map = buyTicketService.delVerify(mobile);

        return map;
    }


    @GetMapping("/yueche")
    public String yueche(){
        log.info("进入我的yueche方法.......");

        return "/yuechetemp.html" ;

    }

    //退款 cktikcet
    @ResponseBody
    @GetMapping("/cktikcetRefund")
    public Map cktikcetRefund(@RequestParam() Map<String,Object> map){
        log.info("--退款---ckticketRefund.......");


        buyTicketService.refund(map.get("orderNo").toString(),
                Double.parseDouble(map.get("amout").toString()));

        return map;
    }

    //取得订单的座位数
    @ResponseBody
    @GetMapping("/queryOrderGetSeats")
    public Map queryOrderGetSeats(@RequestParam("route") String route,
                                  @RequestParam("bizDate") String bizDate,
                                  @RequestParam("time") String time) throws Exception{

        Map<String,Object>  map = buyTicketService.getOrderSeats(route,bizDate,time);
        log.info("进入getSeats订单方法......." + map.toString());
        return map;
    }

//    @ResponseBody
//    @GetMapping("/getorderlist")
//    public String getorderlist() throws Exception{
//
//        buyTicketService.createOrderQrcode();
//
//        return "";
//    }





}
