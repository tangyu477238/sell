package com.imooc.controller;


import com.imooc.utils.WeChatQrcodeUtils;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jovan
 * @create 2019/8/12
 */
@RestController
public class QrcodeWeChatController {


    @Autowired
    private WeChatQrcodeUtils weChatQrcodeUtils;

    /**
     * <pre>
     * @desc: 创建生成二维码
     * @auth: cao_wencao
     * @date: 2019/4/10 14:00
     * </pre>
     */
    @RequestMapping("/createQrcode")
    @ResponseBody
    public Object createQrcode(int sceneId,String expireSeconds) throws Exception {

        WxMpQrCodeTicket wxMpQrCodeTicket = weChatQrcodeUtils.qrCodeCreateTmpTicket(sceneId, Integer.valueOf(expireSeconds));

        String urlPicture = weChatQrcodeUtils.qrCodePictureUrl(wxMpQrCodeTicket.getTicket());
        wxMpQrCodeTicket.setUrl(urlPicture);
        return wxMpQrCodeTicket;
    }

    /**
     * <pre>
     * @desc: 通过ticket获取二维码（长链接URL）
     * @auth: cao_wencao
     * @date: 2019/4/10 14:00
     * </pre>
     */
    @RequestMapping("/getQrcodeUrl")
    @ResponseBody
    public Object getQrcodeUrl(String ticket) throws Exception {
        String url = weChatQrcodeUtils.qrCodePictureUrl(ticket);
        return url;
    }

    /**
     * <pre>
     * @desc: 通过ticket获取二维码（短链接URL）
     * @auth: cao_wencao
     * @date: 2019/4/10 14:01
     * </pre>
     */
    @RequestMapping("/qrCodePictureUrl")
    @ResponseBody
    public Object qrCodePictureUrl(String ticket) throws Exception {
        String urlPicture = weChatQrcodeUtils.qrCodePictureUrl(ticket,true);
        // String url= urlPicture.replace("\\/", "/");
        return urlPicture;
    }

    
    
}
