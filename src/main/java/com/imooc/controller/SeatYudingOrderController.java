package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.dataobject.SeatOrderDO;
import com.imooc.repository.SeatOrderRepository;
import com.imooc.service.SeatYudingOrderService;
import com.imooc.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/seatYudingOrder")
public class SeatYudingOrderController {

    @Autowired
    private SeatYudingOrderService seatYudingOrderService;

    @Autowired
    private SeatOrderRepository seatOrderRepository;


    //评价
    @ResponseBody
    @GetMapping("/pingjia")
    public ResultVO pingjia(@RequestParam("uid") String uid,
                            @RequestParam("orderId") String orderId,
                            @RequestParam("fuwu") Integer fuwu,
                            @RequestParam("content") String content) throws Exception{
        seatYudingOrderService.pingjia(uid, orderId.replace(",",""),content,fuwu);
        log.info("pingjia.......");
        return ResultVOUtil.success();
    }

    //试用预定信息
    @ResponseBody
    @GetMapping("/sendMsgBanci")
    public ResultVO sendMsgBanci(){
        seatYudingOrderService.sendMsgBanci("尊敬的业主您好，\r\n2021-02-22起恢复琶洲线路班车，为缓解出行压力，紧急增加幸福誉至宏发07:05班车！",
                "恢复琶洲线路,增幸福誉至宏发07:05班车","以系统放票为准",null);
        log.info("sendMsgBanci.......");
        return ResultVOUtil.success();
    }


    //试用预定信息
    @ResponseBody
    @GetMapping("/testYuding")
    public ResultVO testYuding(@RequestParam("uid") String uid,
                               @RequestParam("orderId") String orderId){
        orderId = orderId.replace(",","");
        SeatOrderDO seatOrderDO = seatOrderRepository.findOne(new Long(orderId));
        seatYudingOrderService.yudingOrder(uid,
                seatOrderDO.getRouteId().toString(),seatOrderDO.getBizTime(),seatOrderDO.getBizDate(),3);
        log.info("testYuding.......");
        return ResultVOUtil.success();
    }

    //提交预定信息
    @ResponseBody
    @GetMapping("/yudingOrder")
    public ResultVO yudingOrder(@RequestParam("uid") String uid,
                                @RequestParam("workday") String workday,
                                @RequestParam("route") String route,
                                @RequestParam("time") String time,
                                String dateType){

        seatYudingOrderService.yudingOrder(uid,workday,route,time,dateType);
        log.info("yudingOrder.......");
        return ResultVOUtil.success();
    }


    //时刻表
    @ResponseBody
    @GetMapping("/shikebiao")
    public Map<String,Object> shikebiao(@RequestParam("uid") String uid,
                                        @RequestParam() String route,
                                        @RequestParam() String holiday,
                                        String dateType){
        return seatYudingOrderService.shikebiao(route,holiday,uid,dateType);
    }


    //预定规则
    @GetMapping("/yudingguize")
    public ModelAndView yudingguize(@RequestParam("uid") String uid,
                                    Map<String,Object> map){
        log.info("-------------预定规则.......");
        map.put("uid",uid);
        return new ModelAndView("mobile/yuyue_guize",map);

    }

    @GetMapping("/wx_yuding")
    public String wx_yuding(@RequestParam("uid") String uid,
                            Map<String,Object> map){
        log.info("wx_yuding.......");
        return "/wx_yuding.html?uid="+uid ;

    }

}
