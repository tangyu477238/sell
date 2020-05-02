package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.config.WechatAccountConfig;
import com.imooc.config.WxMpConfiguration;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@RestController
@RequestMapping("/wx/menu/{appid}")
@Slf4j
public class WxMenuController {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    /**
     * <pre>
     * 自定义菜单创建接口
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013&token=&lang=zh_CN
     * 如果要创建个性化菜单，请设置matchrule属性
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @return 如果是个性化菜单，则返回menuid，否则返回null
     */
    @PostMapping("/create")
    public String menuCreate(@PathVariable String appid, @RequestBody WxMenu menu) throws WxErrorException {
        return WxMpConfiguration.getMpServices().get(appid).getMenuService().menuCreate(menu);
    }



    //http://localhost:8080/wx/menu/wxa5b1eca9389268b9/create?appid=wxa5b1eca9389268b9
    @GetMapping("/create")
    public String menuCreateSample(@PathVariable String appid) throws WxErrorException, MalformedURLException {
        String strurl = projectUrlConfig.getWechatMpAuthorize();

        String buyStrurl = strurl+"/sell/wechat/authorize?returnUrl="+strurl+"/sell/seller/login";//购票
        String buyMonthTickUrl = strurl+"/sell/wechat/authorize?returnUrl="+strurl+"/sell/seller/buyMonthTicket";//月票充值
        String bupiaoUrl = strurl+"/sell/wechat/authorize?returnUrl="+strurl+"/sell/seller/bupiao";//补票
        String queryMonthOrderUrl = strurl+"/sell/wechat/authorize?returnUrl="+strurl+"/sell/seller/queryMonthOrder";//查询订单

        String wk_buyStrurl = strurl+"/sell/wechat/authorize?returnUrl="+strurl+"/sell/seller/wklogin";//万科
        String wk_buyMonthTickUrl = strurl+"/sell/wechat/authorize?returnUrl="+strurl+"/sell/seller/wkbuyMonthTicket";//月票充值
        String wk_bupiaoUrl = strurl+"/sell/wechat/authorize?returnUrl="+strurl+"/sell/seller/wkbupiao";//补票

        WxMenu menu = new WxMenu();
//        WxMenuButton button1 = new WxMenuButton();
//        button1.setType(MenuButtonType.CLICK);
//        button1.setName("点击事件");
//        button1.setKey("V1001_TODAY_MUSIC");

//        WxMenuButton button2 = new WxMenuButton();
//        button2.setType(WxConsts.MenuButtonType.MINIPROGRAM);
//        button2.setName("小程序");
//        button2.setAppId("wx286b93c14bbf93aa");
//        button2.setPagePath("pages/lunar/index.html");
//        button2.setUrl("http://mp.weixin.qq.com");


        WxMenuButton button1 = new WxMenuButton();
//        button1.setName("幸福誉购票");  ----记得打开

        button1.setType(MenuButtonType.VIEW); //记得注释
        button1.setName("我要订票"); //记得注释
        button1.setUrl(buyStrurl); //记得注释
        log.info(buyStrurl); //记得注释



        WxMenuButton button2 = new WxMenuButton();
//        button2.setName("万科城购票"); ----记得打开

        button2.setName("月票/补票");



        WxMenuButton button3 = new WxMenuButton();
        button3.setName("我的");

        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);


        ////////////////////////我要订票/////////////////////////////////////


//        setMenuButton(button1, "订购车票", buyStrurl);  ----记得打开
//        setMenuButton(button1, "月票充值", buyMonthTickUrl);  ----记得打开
//        setMenuButton(button1, "我要补票", bupiaoUrl);  ----记得打开




        ////////////////////月票或补票/////////////////////////////////////////
//        setMenuButton(button2, "订购车票", wk_buyStrurl);  ----记得打开
//        setMenuButton(button2, "月票充值", wk_buyMonthTickUrl);  ----记得打开
//        setMenuButton(button2, "我要补票", wk_bupiaoUrl);  ----记得打开


        setMenuButton(button2, "月票充值", buyMonthTickUrl);
        setMenuButton(button2, "我要补票", bupiaoUrl);



        ///////////////////////我的//////////////////////////////////////////

        setMenuButton(button3, "我的订单", queryMonthOrderUrl);
        setMenuButton(button3, "时刻表", strurl + "/sell/ticket/getskb");

        /******
        WxMenuButton button31 = new WxMenuButton();
        button31.setType(MenuButtonType.VIEW);
        button31.setName("我的订单");
        button31.setUrl(queryMonthOrderUrl);


        WxMenuButton button33 = new WxMenuButton();
        button33.setType(MenuButtonType.VIEW);
        button33.setName("时刻表");
        button33.setUrl(strurl + "/sell/ticket/getskb");

//        WxMenuButton button34 = new WxMenuButton();
//        button34.setType(MenuButtonType.VIEW);
//        button34.setName("获取用户信息");

//        ServletRequestAttributes servletRequestAttributes =
//            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (servletRequestAttributes != null) {
//            HttpServletRequest request = servletRequestAttributes.getRequest();
//            URL requestURL = new URL(request.getRequestURL().toString());
//            String url = WxMpConfiguration.getMpServices().get(appid)
//                .oauth2buildAuthorizationUrl(
//                    String.format("%s://%s/wx/redirect/%s/greet",
//                            requestURL.getProtocol(), requestURL.getHost(), appid),
//                    WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
//            button34.setUrl(url);
//        }
//
        button3.getSubButtons().add(button31);
//        button3.getSubButtons().add(button32);
        button3.getSubButtons().add(button33);
//        button3.getSubButtons().add(button34);


         ******/


        log.info("--------------创建菜单---------------");
        return WxMpConfiguration.getMpServices().get(appid).getMenuService().menuCreate(menu);
    }

    private void setMenuButton(WxMenuButton button,String buttonName,String url){
        WxMenuButton button11 = new WxMenuButton();
        button11.setType(MenuButtonType.VIEW);
        button11.setName(buttonName);
        button11.setUrl(url);
        button.getSubButtons().add(button11);
    }




    /**
     * <pre>
     * 自定义菜单创建接口
     * 详情请见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013&token=&lang=zh_CN
     * 如果要创建个性化菜单，请设置matchrule属性
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @param json
     * @return 如果是个性化菜单，则返回menuid，否则返回null
     */
    @PostMapping("/createByJson")
    public String menuCreate(@PathVariable String appid, @RequestBody String json) throws WxErrorException {
        return WxMpConfiguration.getMpServices().get(appid).getMenuService().menuCreate(json);
    }

    /**
     * <pre>
     * 自定义菜单删除接口
     * 详情请见: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141015&token=&lang=zh_CN
     * </pre>
     */
    @GetMapping("/delete")
    public void menuDelete(@PathVariable String appid) throws WxErrorException {
        WxMpConfiguration.getMpServices().get(appid).getMenuService().menuDelete();
    }

    /**
     * <pre>
     * 删除个性化菜单接口
     * 详情请见: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @param menuId 个性化菜单的menuid
     */
    @GetMapping("/delete/{menuId}")
    public void menuDelete(@PathVariable String appid, @PathVariable String menuId) throws WxErrorException {
        WxMpConfiguration.getMpServices().get(appid).getMenuService().menuDelete(menuId);
    }

    /**
     * <pre>
     * 自定义菜单查询接口
     * 详情请见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141014&token=&lang=zh_CN
     * </pre>
     */
    @GetMapping("/get")
    public WxMpMenu menuGet(@PathVariable String appid) throws WxErrorException {
        return WxMpConfiguration.getMpServices().get(appid).getMenuService().menuGet();
    }

    /**
     * <pre>
     * 测试个性化菜单匹配结果
     * 详情请见: http://mp.weixin.qq.com/wiki/0/c48ccd12b69ae023159b4bfaa7c39c20.html
     * </pre>
     *
     * @param userid 可以是粉丝的OpenID，也可以是粉丝的微信号。
     */
    @GetMapping("/menuTryMatch/{userid}")
    public WxMenu menuTryMatch(@PathVariable String appid, @PathVariable String userid) throws WxErrorException {
        return WxMpConfiguration.getMpServices().get(appid).getMenuService().menuTryMatch(userid);
    }

    /**
     * <pre>
     * 获取自定义菜单配置接口
     * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置。
     * 请注意：
     * 1、第三方平台开发者可以通过本接口，在旗下公众号将业务授权给你后，立即通过本接口检测公众号的自定义菜单配置，并通过接口再次给公众号设置好自动回复规则，以提升公众号运营者的业务体验。
     * 2、本接口与自定义菜单查询接口的不同之处在于，本接口无论公众号的接口是如何设置的，都能查询到接口，而自定义菜单查询接口则仅能查询到使用API设置的菜单配置。
     * 3、认证/未认证的服务号/订阅号，以及接口测试号，均拥有该接口权限。
     * 4、从第三方平台的公众号登录授权机制上来说，该接口从属于消息与菜单权限集。
     * 5、本接口中返回的图片/语音/视频为临时素材（临时素材每次获取都不同，3天内有效，通过素材管理-获取临时素材接口来获取这些素材），本接口返回的图文消息为永久素材素材（通过素材管理-获取永久素材接口来获取这些素材）。
     *  接口调用请求说明:
     * http请求方式: GET（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN
     * </pre>
     */
    @GetMapping("/getSelfMenuInfo")
    public WxMpGetSelfMenuInfoResult getSelfMenuInfo(@PathVariable String appid) throws WxErrorException {
        return WxMpConfiguration.getMpServices().get(appid).getMenuService().getSelfMenuInfo();
    }
}
