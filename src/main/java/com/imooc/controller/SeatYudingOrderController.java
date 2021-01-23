package com.imooc.controller;

import com.imooc.VO.ResultVO;
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


    //提交预定信息
    @ResponseBody
    @GetMapping("/yudingOrder")
    public ResultVO yudingOrder(@RequestParam("uid") String uid,
                                @RequestParam("workday") String workday,
                                @RequestParam("route") String route,
                                @RequestParam("time") String time){

        seatYudingOrderService.yudingOrder(uid,workday,route,time);
        log.info("yudingOrder.......");
        return ResultVOUtil.success();
    }


    //时刻表
    @ResponseBody
    @GetMapping("/shikebiao")
    public Map<String,Object> shikebiao(@RequestParam("uid") String uid,
                                        @RequestParam() String route,
                                        @RequestParam() String holiday){
        return seatYudingOrderService.shikebiao(route,holiday,uid);
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
        return "/wx_yuding.html" ;

    }

}
