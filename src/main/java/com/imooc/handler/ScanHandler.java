package com.imooc.handler;

import com.imooc.builder.TextBuilder;
import com.imooc.dataobject.QrcodeOrderDO;
import com.imooc.dataobject.SeatOrderDO;
import com.imooc.dataobject.SellerInfo;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.YanpiaoEnum;
import com.imooc.repository.QrcodeOrderRepository;
import com.imooc.repository.SeatOrderRepository;
import com.imooc.service.SellerService;
import com.imooc.service.VerificationTicketService;
import com.imooc.utils.ComUtil;
import com.imooc.utils.DateTimeUtil;
import com.lly835.bestpay.utils.DateUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class ScanHandler extends AbstractHandler {

//    public static String SEND_MSG="本公司主营服务范围：旅游包车、企业包车、商务包车等。公司或企业员工上下班接送、机场接送、看楼用车、学校用车、展会用车、婚宴用车等等。我们根据不同的客户要求提供各款优质车辆，价格实惠，服务周到；同时为各大企业单位、机关部门及社会团体提供长短期包车的汽车租赁业务。服务热线：13922253183";

    @Autowired
    private SellerService sellerService;

    @Autowired
    private QrcodeOrderRepository qrcodeOrderRepository;

    @Autowired
    private SeatOrderRepository seatOrderRepository;

    @Autowired
    private VerificationTicketService verificationTicketService;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        // 获取微信用户基本信息
//        try {
//            WxMpUser userWxInfo = weixinService.getUserService()
//                    .userInfo(wxMessage.getFromUser(), null);
//            if (userWxInfo != null) {
//
//            }
//        } catch (WxErrorException e) {
//            if (e.getError().getErrorCode() == 48001) {
//                this.logger.info("该公众号没有获取用户信息权限！");
//            }
//        }

        SellerInfo sinfo = sellerService.findSellerInfoByOpenid(wxMessage.getFromUser());
        Integer orderNo = Integer.parseInt(wxMessage.getEventKey());
        QrcodeOrderDO qrcodeOrderDO = qrcodeOrderRepository.findByOrderNoAndBizDate(orderNo,
                DateTimeUtil.getBeforeDay(0));
        if (qrcodeOrderDO == null){
            return new TextBuilder().build("无效的二维码", wxMessage, weixinService);
        }

        List<SeatOrderDO> seatOrderDo = seatOrderRepository
                .findByCreateUserAndRouteIdAndBizDateAndBizTimeAndStateAndCkstate(sinfo.getSellerId(),
                        qrcodeOrderDO.getRouteId(),
                        qrcodeOrderDO.getBizDate(),
                        qrcodeOrderDO.getBizTime(),
                        PayStatusEnum.SUCCESS.getCode(),0);
        Map<String,Object> map = verificationTicketService.cktikcet(seatOrderDo);
        String msg ;
        if (ComUtil.isEmpty(map.get("info"))){
            msg = YanpiaoEnum.getMessageByCode(
                    map.get("state").toString());
        } else {
            msg = "验票成功："+ map.get("info").toString();
        }
        verificationTicketService.pushMessage(msg, map.get("state").toString());



        try {
            return new TextBuilder().build(msg, wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
            throws Exception {
        logger.info(wxMessage.toString());
        //TODO
        return null;
    }
}
