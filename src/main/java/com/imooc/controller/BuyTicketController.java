package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.config.ProjectUrlConfig;
import com.imooc.dataobject.SeatOrderDO;
import com.imooc.dataobject.SellerInfo;
import com.imooc.repository.BuyTicketRepository;
import com.imooc.repository.SellerInfoRepository;
import com.imooc.service.BuyTicketService;
import com.imooc.utils.DateTimeUtil;
import com.imooc.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private BuyTicketRepository repository;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //进入选线路
    @GetMapping("/ticket")
    public ModelAndView ticketIndex(@RequestParam("uid") String uid,Map<String,Object> map){
        log.info("进入TicketIndex方法......." + uid);
        SellerInfo sellerInfo = userRepository.findOne(uid);
        map.put("uid",uid);
        if(sellerInfo.getMobile() == null){
            return new ModelAndView("mobile/addUserInfo", map);
        }
        map = buyTicketService.findAll();
        return new ModelAndView("mobile/index", map);
    }

    //进入补票
    @GetMapping("/bupiao")
    public ModelAndView ticketBupiao(@RequestParam("uid") String uid, Map<String,Object> map){
        log.info("ticketBupiao......." + uid);
        map.put("uid",uid);
//        SellerInfo sellerInfo = userRepository.findOne(uid);
//        if(sellerInfo.getMobile() == null){
//            return new ModelAndView("mobile/addUserInfo", map);
//        }
        map = buyTicketService.bupiao();
        return new ModelAndView("mobile/bupiao", map);
    }

    //选择座位
    @GetMapping("/cseat")
    public ModelAndView  cseat(@RequestParam("route") String route,
                               @RequestParam("time") String time,
                               @RequestParam("moment") String moment,
                               @RequestParam("routeStation") String routeStation,
                               @RequestParam("uid") String uid){
        log.info("进入选座方法.......");
        log.info("----route-----" + route);
        log.info("----time-----" + time);
        log.info("----time-----" + moment);

        Map<String,Object>  maplist = buyTicketService.listSeatDetail(route,time,moment);
        if (maplist == null){
            maplist = new HashMap<>();
            maplist.put("uid",uid);
            return new ModelAndView("mobile/orderFail",maplist);
        }

        maplist.put("uid",uid);
        maplist.put("routeStation",routeStation);
        return new ModelAndView("mobile/chooseSeat",maplist);

    }

    //订单确认
    @GetMapping("/cord")
    public ModelAndView cord(@RequestParam("route") String route,
                             @RequestParam("time") String time,
                             @RequestParam("moment") String moment,
                             @RequestParam("seat") String seat,
                             @RequestParam("num") String num,
                             @RequestParam("uid") String uid,
                             @RequestParam("routeStation") String routeStation,
                             Map<String,Object> map){
        log.info("-------------进入确认订单方法.......");

        map = buyTicketService.addOrder(route,time,moment,seat,num,uid,routeStation);

        map.put("uid",uid);
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

        map = buyTicketService.queryOrder(orderId,uid);

        SeatOrderDO sod =(SeatOrderDO) map.get("sod");
        map.put("uid",uid);
        map.put("qrcode",projectUrlConfig.getWechatMpAuthorize()+"/qrcode/"+ uid +"_"+sod.getId()+".jpg");
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
        log.info("进入我的积分方法.......");

        return "/shikebiao.html" ;

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


    //班次查询
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


    //班次查询
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
    public ModelAndView buyTicket(@RequestParam("uid") String uid,Map<String,Object> map){
        log.info("进入buyTicket方法.......");

        map = buyTicketService.buyTicket();
        map.put("uid",uid);

        List<Object[]> list = repository.getDayTimeFlag();
        int monthticketdays = Integer.parseInt(list.get(0)[3].toString());

        if(DateTimeUtil.getDaysOfTwoDate(DateTimeUtil.getMonthOfLastDayDate(0),new Date())<monthticketdays){
            map.put("month_name", DateTimeUtil.getMonthDay(3)+"～"+DateTimeUtil.getMonthDay(4));
            map.put("month_val",DateTimeUtil.getMonthDay(6));
        } else {
            map.put("month_name", DateTimeUtil.getMonthDay(1)+"～"+DateTimeUtil.getMonthDay(2));
            map.put("month_val",DateTimeUtil.getMonthDay(5));
        }

        return new ModelAndView("mobile/buyMonth",map);
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


    //cktikcet
    @ResponseBody
    @GetMapping("/ckticket")
    public Map cktikcet(@RequestParam("uid") String uid,
                               Map<String,Object> map){
        log.info("进入cktikcet订单方法.......");
        map = buyTicketService.cktikcet(uid);

        return map;
    }

}
