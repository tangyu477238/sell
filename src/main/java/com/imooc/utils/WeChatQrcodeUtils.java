package com.imooc.utils;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * <pre>
 * @Description: 生成带参数的二维码
 * @Aouth: cao_wencao
 * @Date: 2019-02-18 15:10
 * </pre>
 */
@Slf4j
@Component
public class WeChatQrcodeUtils {

    @Autowired
    private WxMpService wxMpService;

    /**
     * <pre>
     * 创建临时二维码ticket
     * @param sceneId       场景值ID，临时二维码时为32位非0整型
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * @auther: cao_wencao
     * @date: 2019/2/18 16:58
     * </pre>
     */
    public WxMpQrCodeTicket qrCodeCreateTmpTicket(int sceneId, Integer expireSeconds) throws Exception {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(sceneId, expireSeconds);
        return wxMpQrCodeTicket;
    }

    /**
     * <pre>
     * 创建临时二维码ticket
     * @param sceneStr      场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     * @auther: cao_wencao
     * @date: 2019/2/18 17:01
     * </pre>
     */
    public WxMpQrCodeTicket qrCodeCreateTmpTicket(String sceneStr, Integer expireSeconds) throws Exception {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(sceneStr, expireSeconds);
        return wxMpQrCodeTicket;
    }

    /**
     * <pre>
     * 创建永久二维码ticket
     * @param sceneId 场景值ID，最大值为100000（目前参数只支持1--100000）
     * @auther: cao_wencao
     * @date: 2019/2/18 17:03
     * </pre>
     */
    public WxMpQrCodeTicket qrCodeCreateLastTicket(int sceneId) throws Exception {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(sceneId);
        return wxMpQrCodeTicket;
    }

    /**
     * <pre>
     * 创建永久字符串二维码ticket
     * @param sceneStr 参数。字符串类型长度现在为1到64
     * @auther: cao_wencao
     * @date: 2019/2/18 17:05
     * </pre>
     */
    public WxMpQrCodeTicket qrCodeCreateLastTicket(String sceneStr) throws Exception {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(sceneStr);
        return wxMpQrCodeTicket;
    }

    /**
     * <pre>
     * 换取二维码图片文件，jpg格式
     * @param ticket 二维码ticket
     * @auther: cao_wencao
     * @date: 2019/2/18 17:07
     * </pre>
     */
    public File qrCodePicture(WxMpQrCodeTicket ticket) throws Exception {
        File file = wxMpService.getQrcodeService().qrCodePicture(ticket);
        return file;

    }

    /**
     * <pre>
     * 换取二维码图片url地址（可以选择是否生成压缩的网址）
     * @param ticket       二维码ticket
     * @param needShortUrl 是否需要压缩的二维码地址
     * @auther: cao_wencao
     * @date: 2019/2/18 17:10
     * </pre>
     */
    public String qrCodePictureUrl(String ticket, boolean needShortUrl) throws Exception {
        String qrCodeUrl = wxMpService.getQrcodeService().qrCodePictureUrl(ticket, needShortUrl);
        return qrCodeUrl;
    }

    /**
     * <pre>
     * 换取二维码图片url地址
     * @param ticket 二维码ticket
     * @auther: cao_wencao
     * @date: 2019/2/18 17:11
     * </pre>
     */
    public String qrCodePictureUrl(String ticket) throws Exception {
        String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket);
        return url;
    }

}
