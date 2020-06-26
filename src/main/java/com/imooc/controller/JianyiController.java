package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.dataobject.JianyiDO;
import com.imooc.dataobject.SeatOrderDO;
import com.imooc.repository.JianyiRepository;
import com.imooc.repository.SeatOrderRepository;
import com.imooc.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/jianyi")
public class JianyiController {


    @Autowired
    JianyiRepository jianyiRepository;

    @Autowired
    SeatOrderRepository seatOrderRepository;

    //
    @GetMapping("/index")
    public ModelAndView jianyi(@RequestParam("uid") String uid,
                               Map<String,Object> map){
        log.info("进入jianyi方法......." );
        map.put("uid", uid);
        return new ModelAndView("mobile/jianyi", map);
    }


    @ResponseBody
    @GetMapping("/add")
    public ResultVO add(@RequestParam("uid") String uid,
                        @RequestParam("stype") String stype,
                        @RequestParam("fstation") String fstation,
                        @RequestParam("tstation") String tstation,
                        @RequestParam("banci") String banci,
                        @RequestParam("remark") String remark){
        log.info("进入add方法.......");

        JianyiDO jianyiDO = new JianyiDO();
        jianyiDO.setStype(stype);
        jianyiDO.setFstation(fstation);
        jianyiDO.setTstation(tstation);
        jianyiDO.setBanci(banci);
        jianyiDO.setRemark(remark);
        jianyiDO.setCreateTime(new Date());
        jianyiDO.setCreateUser(uid);
        jianyiRepository.save(jianyiDO);

        return ResultVOUtil.success();

    }

    @ResponseBody
    @PostMapping("/getOrder")
    public ResultVO getOrder(@RequestParam("info") String info,
                             @RequestParam("routeId") Long routeId,
                             @RequestParam("bizDate") String bizDate,
                             @RequestParam("bizTime") String bizTime){
        log.info("进入getOrder方法.......");

        SeatOrderDO seatOrderDO = seatOrderRepository.getOrderBySeat(info,routeId,bizDate,bizTime);
        String url = "/ticket/queryOrder?uid="+seatOrderDO.getCreateUser()+"&orderId="+seatOrderDO.getId();
        return ResultVOUtil.success(url);

    }

//    @RequestMapping("location")
//    public String location(Model model) throws Exception {
//        //32位随机数(UUID去掉-就是32位的)
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        long timestamp = new Date().getTime();
//        //jssdk权限验证参数
//        TreeMap<Object, Object> map = new TreeMap<>();
//        map.put("appId","");
//        map.put("timestamp",timestamp);//全小写
//        map.put("nonceStr",uuid);
//        map.put("signature",WeChatUtil.getSignature(timestamp,uuid,RequestUtil.getUrl(request)));
//        model.addAttribute("configMap",map);
//        return  "location"; //视图页面
//    }
}
