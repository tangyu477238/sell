package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.constant.CookieConstant;
import com.imooc.constant.RedisConstant;
import com.imooc.dataobject.SellerInfo;
import com.imooc.enums.LouPanEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.service.SellerService;
import com.imooc.utils.ComUtil;
import com.imooc.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by SqMax on 2018/4/1.
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String,Object> map){
        String url = "/sell/ticket/ticket";
        map.put("lp",LouPanEnum.XINGFUYU.getCode());
        return getData(openid, response, map, url);
    }
    @GetMapping("/buyMonthTicket")
    public ModelAndView buyMonthTicket(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String,Object> map){
        String url = "/sell/ticket/buyTicket";
        map.put("lp",LouPanEnum.XINGFUYU.getCode());
        return getData(openid,response,map,url);
    }
    @GetMapping("/bupiao")
    public ModelAndView bupiao(@RequestParam("openid") String openid,
                               HttpServletResponse response,
                               Map<String,Object> map){
        String url = "/sell/ticket/bupiao";
        map.put("lp",LouPanEnum.XINGFUYU.getCode());
        return getData(openid,response,map,url);
    }









    @GetMapping("/wklogin")
    public ModelAndView wankecheng(@RequestParam("openid") String openid,
                               HttpServletResponse response,
                               Map<String,Object> map){
        String url = "/sell/ticket/ticket";
        map.put("lp",LouPanEnum.BEIBUWANKE.getCode());
        return getData(openid,response,map,url);
    }
    @GetMapping("/wkbupiao")
    public ModelAndView wkbupiao(@RequestParam("openid") String openid,
                               HttpServletResponse response,
                               Map<String,Object> map){
        String url = "/sell/ticket/bupiao";
        map.put("lp",LouPanEnum.BEIBUWANKE.getCode());
        return getData(openid,response,map,url);
    }
    @GetMapping("/wkbuyMonthTicket")
    public ModelAndView wkbuyMonthTicket(@RequestParam("openid") String openid,
                                       HttpServletResponse response,
                                       Map<String,Object> map){
        String url = "/sell/ticket/buyTicket";
        map.put("lp",LouPanEnum.BEIBUWANKE.getCode());
        return getData(openid,response,map,url);
    }







    @GetMapping("/queryMonthOrder")
    public ModelAndView queryMonthOrder(@RequestParam("openid") String openid,
                                        HttpServletResponse response,
                                        Map<String,Object> map){
        String url = "/sell/ticket/queryMonthTicket";
        return getData(openid,response,map,url);

    }

    private ModelAndView getData(String openid,HttpServletResponse response,Map map,String mode){

        //1.openid去和数据库里的数据匹配
        SellerInfo sellerInfo =sellerService.findSellerInfoByOpenid(openid);
        if(sellerInfo==null){
            sellerService.addUser(openid);
            sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        }
        //2.设置token至redis
        String token= UUID.randomUUID().toString();
        Integer expire= RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),openid,expire, TimeUnit.SECONDS);

        //3.设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN,token,expire);

        map.put("uid",sellerInfo.getSellerId());

        String url = projectUrlConfig.getSell() + mode +"?uid="+sellerInfo.getSellerId();
        if (map.containsKey("lp")){
            url = url +"&lp="+map.get("lp");
        }
        return new ModelAndView("redirect:"+ url);
    }











    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String,Object> map){

        //1.从cookie里查询
        Cookie cookie=CookieUtil.get(request,CookieConstant.TOKEN);
        if(cookie!=null){
            //2.清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
            //3.清除cookie
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);
        }
        map.put("msg",ResultEnum.LOGOUT_SUCCESS.getMessage());
        //map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);

    }
}
