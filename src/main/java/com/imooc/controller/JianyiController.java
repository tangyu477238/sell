package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.config.WechatAccountConfig;
import com.imooc.dataobject.DriverGpsDO;
import com.imooc.dataobject.JianyiDO;
import com.imooc.dataobject.SeatOrderDO;
import com.imooc.repository.DriverGpsRepository;
import com.imooc.repository.JianyiRepository;
import com.imooc.repository.SeatOrderRepository;
import com.imooc.utils.ResultVOUtil;
import com.imooc.utils.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/jianyi")
public class JianyiController {


    @Autowired
    JianyiRepository jianyiRepository;

    @Autowired
    SeatOrderRepository seatOrderRepository;

    @Autowired
    private WechatAccountConfig accountConfig;

    //
    @GetMapping("/index")
    public ModelAndView jianyi(@RequestParam("uid") String uid,
                               Map<String,Object> map){
        log.info("进入jianyi方法......." );
        map.put("uid", uid);
        return new ModelAndView("mobile/jianyi", map);
    }


    @GetMapping("/location")
    public ModelAndView location(@RequestParam("station") String station){
        log.info("进入location方法......." );
        return new ModelAndView("mobile/"+station);
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

    /**
     * 获取页面需要的配置信息的参数
     * 获取用户微信地理位置信息
     * @param url
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @GetMapping("/getJsTicket")
    public Map<String, Object> getWeJsTicket(@RequestParam("url") String url) throws Exception {



        log.info("进入getJsTicket方法......."+url);
        Map<String, Object> map = new HashMap<>();

        String AppId = accountConfig.getMpAppId();//第三方用户唯一凭证
        String secret = accountConfig.getMpAppSecret();//第三方用户唯一凭证密钥，即appsecret

        //1、获取AccessToken
        String accessToken = WxUtils.getAccessToken(AppId,secret);
        //2、获取Ticket
        String jsapi_ticket = WxUtils.getTicket(accessToken);
        //3、时间戳和随机字符串
        String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
        //5、将参数排序并拼接字符串
        String str = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
        //6、将字符串进行sha1加密
        String signature =WxUtils.SHA1(str);


        // 获取微信signature
        map.put("appId", AppId);
        map.put("timestamp", timestamp);
        map.put("nonceStr", noncestr);
        map.put("signature", signature);

        return map;
    }


    @ResponseBody
    @CrossOrigin
    @GetMapping("/getDriverGps")
    public Map<String, Object> getDriverGps(@RequestParam("id") Long id) throws Exception {

        log.info("进入getDriverGps方法......."+id);
        Map<String, Object> map = new HashMap<>();
        SeatOrderDO seatOrderDO = seatOrderRepository.findByIdAndState(id,1);
        DriverGpsDO driverGpsDO = driverGpsRepository.findByRouteIdAndBizDateAndBizTime(seatOrderDO.getRouteId(),
                seatOrderDO.getBizDate(),seatOrderDO.getBizTime());

        map.put("latitude",driverGpsDO.getLat());
        map.put("longitude",driverGpsDO.getLon());
        map.put("name","楼巴-->"+seatOrderDO.getFromStation()+"-"+seatOrderDO.getToStation()+"/"+seatOrderDO.getBizTime());
        map.put("address","最新位置："+driverGpsDO.getAddr());
        return map;
    }


    @ResponseBody
    @PostMapping("/uploadGps")
    public ResultVO uploadGps(@RequestParam("lat") String lat,
                              @RequestParam("lon") String lon,
                              @RequestParam("addr") String addr){
        log.info("进入uploadGps方法.......");
        Long routeId = new Long(1);
        String bizDate="2020-06-27";
        String bizTime="08:00";


        synchronized (this){
            DriverGpsDO driverGpsDO = driverGpsRepository
                    .findByRouteIdAndBizDateAndBizTime(routeId,bizDate,bizTime);
            if (driverGpsDO == null) {
                driverGpsDO = new DriverGpsDO();
                driverGpsDO.setRouteId(routeId);
                driverGpsDO.setBizDate(bizDate);
                driverGpsDO.setBizTime(bizTime);
            }

            driverGpsDO.setUpdateTime(new Date());
            driverGpsDO.setLat(lat);
            driverGpsDO.setLon(lon);
            driverGpsDO.setAddr(addr);
            driverGpsRepository.save(driverGpsDO);
        }

        return ResultVOUtil.success();

    }

    @Autowired
    private DriverGpsRepository driverGpsRepository;

}
