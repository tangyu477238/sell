package com.imooc.config;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by SqMax on 2018/3/26.
 */
@Component
public class WechatPayConfig {

    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean
    public WxPayConfig WxPayConfig(){
        WxPayConfig WxPayConfig=new WxPayConfig();
        WxPayConfig.setAppId(accountConfig.getMpAppId());
        WxPayConfig.setAppSecret(accountConfig.getMpAppSecret());
        WxPayConfig.setMchId(accountConfig.getMchId());
        WxPayConfig.setMchKey(accountConfig.getMchKey());
        WxPayConfig.setKeyPath(accountConfig.getKeyPath());
        WxPayConfig.setNotifyUrl(accountConfig.getNotifyUrl());

        return WxPayConfig;
    }
    @Bean
    public BestPayServiceImpl bestPayService(){

        BestPayServiceImpl bestPayService=new BestPayServiceImpl();
        bestPayService.setWxPayConfig(WxPayConfig());
        return bestPayService;
    }
}
