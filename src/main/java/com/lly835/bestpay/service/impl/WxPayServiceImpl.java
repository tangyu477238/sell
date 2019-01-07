//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.model.wxpay.WxPayApi;
import com.lly835.bestpay.model.wxpay.request.WxPayRefundRequest;
import com.lly835.bestpay.model.wxpay.request.WxPayUnifiedorderRequest;
import com.lly835.bestpay.model.wxpay.response.WxPayAsyncResponse;
import com.lly835.bestpay.model.wxpay.response.WxPayRefundResponse;
import com.lly835.bestpay.model.wxpay.response.WxPaySyncResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.utils.MoneyUtil;
import com.lly835.bestpay.utils.RandomUtil;
import com.lly835.bestpay.utils.XmlUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class WxPayServiceImpl implements BestPayService {
    private static final Logger log = LoggerFactory.getLogger(WxPayServiceImpl.class);
    private WxPayH5Config wxPayH5Config;

    public WxPayServiceImpl() {
    }

    public void setWxPayH5Config(WxPayH5Config wxPayH5Config) {
        this.wxPayH5Config = wxPayH5Config;
    }

    public PayResponse pay(PayRequest request) {
        WxPayUnifiedorderRequest wxRequest = new WxPayUnifiedorderRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setTotalFee(MoneyUtil.Yuan2Fen(request.getOrderAmount()));
        wxRequest.setBody(request.getOrderName());
        wxRequest.setOpenid(request.getOpenid());
        wxRequest.setTradeType("JSAPI");
        wxRequest.setAppid(this.wxPayH5Config.getAppId());
        wxRequest.setMchId(this.wxPayH5Config.getMchId());

        log.info(wxRequest.getTotalFee()+"-----this.wxPayH5Config.getNotifyUrl()--------"+this.wxPayH5Config.getNotifyUrl());
        wxRequest.setNotifyUrl(this.wxPayH5Config.getNotifyUrl());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSpbillCreateIp("8.8.8.8");
        wxRequest.setSign(WxPaySignature.sign(this.buildMap(wxRequest), this.wxPayH5Config.getMchKey()));
        Retrofit retrofit = (new Builder()).baseUrl("https://api.mch.weixin.qq.com").addConverterFactory(SimpleXmlConverterFactory.create()).build();
        String xml = XmlUtil.toXMl(wxRequest);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Call<WxPaySyncResponse> call = ((WxPayApi)retrofit.create(WxPayApi.class)).unifiedorder(body);
        Response retrofitResponse = null;

        try {
            retrofitResponse = call.execute();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信统一支付】发起支付, 网络异常");
        } else {
            WxPaySyncResponse response = (WxPaySyncResponse)retrofitResponse.body();
            if (!response.getReturnCode().equals("SUCCESS")) {
                throw new RuntimeException("【微信统一支付】发起支付, returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
            } else if (!response.getResultCode().equals("SUCCESS")) {
                throw new RuntimeException("【微信统一支付】发起支付, resultCode != SUCCESS, err_code = " + response.getErrCode() + " err_code_des=" + response.getErrCodeDes());
            } else {
                return this.buildPayResponse(response);
            }
        }
    }

    public boolean verify(Map map, SignType signType, String sign) {
        return WxPaySignature.verify(map, this.wxPayH5Config.getMchKey());
    }

    public PayResponse syncNotify(HttpServletRequest request) {
        return null;
    }

    public PayResponse asyncNotify(String notifyData) {
        WxPayAsyncResponse asyncResponse = (WxPayAsyncResponse)XmlUtil.fromXML(notifyData, WxPayAsyncResponse.class);
        if (!WxPaySignature.verify(this.buildMap(asyncResponse), this.wxPayH5Config.getMchKey())) {
            log.error("【微信支付异步通知】签名验证失败, response={}", asyncResponse);
            throw new RuntimeException("【微信支付异步通知】签名验证失败");
        } else if (!asyncResponse.getReturnCode().equals("SUCCESS")) {
            throw new RuntimeException("【微信支付异步通知】发起支付, returnCode != SUCCESS, returnMsg = " + asyncResponse.getReturnMsg());
        } else if (!asyncResponse.getResultCode().equals("SUCCESS") && asyncResponse.getErrCode().equals("ORDERPAID")) {
            return this.buildPayResponse(asyncResponse);
        } else if (!asyncResponse.getResultCode().equals("SUCCESS")) {
            throw new RuntimeException("【微信支付异步通知】发起支付, resultCode != SUCCESS, err_code = " + asyncResponse.getErrCode() + " err_code_des=" + asyncResponse.getErrCodeDes());
        } else {
            return this.buildPayResponse(asyncResponse);
        }
    }

    public RefundResponse refund(RefundRequest request) {
        WxPayRefundRequest wxRequest = new WxPayRefundRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setOutRefundNo(request.getOrderId());
        wxRequest.setTotalFee(MoneyUtil.Yuan2Fen(request.getOrderAmount()));
        wxRequest.setRefundFee(MoneyUtil.Yuan2Fen(request.getOrderAmount()));
        wxRequest.setAppid(this.wxPayH5Config.getAppId());
        wxRequest.setMchId(this.wxPayH5Config.getMchId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSign(WxPaySignature.sign(this.buildMap(wxRequest), this.wxPayH5Config.getMchKey()));
        if (this.wxPayH5Config.getSslContext() == null) {
            this.wxPayH5Config.initSSLContext();
        }

        okhttp3.OkHttpClient.Builder okHttpClient = (new OkHttpClient()).newBuilder().sslSocketFactory(this.wxPayH5Config.getSslContext().getSocketFactory());
        Retrofit retrofit = (new Builder()).baseUrl("https://api.mch.weixin.qq.com").addConverterFactory(SimpleXmlConverterFactory.create()).client(okHttpClient.build()).build();
        String xml = XmlUtil.toXMl(wxRequest);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Call<WxPayRefundResponse> call = ((WxPayApi)retrofit.create(WxPayApi.class)).refund(body);
        Response retrofitResponse = null;

        try {
            retrofitResponse = call.execute();
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信退款】发起退款, 网络异常");
        } else {
            WxPayRefundResponse response = (WxPayRefundResponse)retrofitResponse.body();
            if (!response.getReturnCode().equals("SUCCESS")) {
                throw new RuntimeException("【微信退款】发起退款, returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
            } else if (!response.getResultCode().equals("SUCCESS")) {
                throw new RuntimeException("【微信退款】发起退款, resultCode != SUCCESS, err_code = " + response.getErrCode() + " err_code_des=" + response.getErrCodeDes());
            } else {
                return this.buildRefundResponse(response);
            }
        }
    }

    private RefundResponse buildRefundResponse(WxPayRefundResponse response) {
        RefundResponse refundResponse = new RefundResponse();
        refundResponse.setOrderId(response.getOutTradeNo());
        refundResponse.setOrderAmount(MoneyUtil.Fen2Yuan(response.getTotalFee()));
        refundResponse.setOutTradeNo(response.getTransactionId());
        refundResponse.setRefundId(response.getOutRefundNo());
        refundResponse.setOutRefundNo(response.getRefundId());
        return refundResponse;
    }

    private Map<String, String> buildMap(WxPayRefundRequest request) {
        Map<String, String> map = new HashMap();
        map.put("appid", request.getAppid());
        map.put("mch_id", request.getMchId());
        map.put("nonce_str", request.getNonceStr());
        map.put("sign", request.getSign());
        map.put("sign_type", request.getSignType());
        map.put("transaction_id", request.getTransactionId());
        map.put("out_trade_no", request.getOutTradeNo());
        map.put("out_refund_no", request.getOutRefundNo());
        map.put("total_fee", String.valueOf(request.getTotalFee()));
        map.put("refund_fee", String.valueOf(request.getRefundFee()));
        map.put("refund_fee_type", request.getRefundFeeType());
        map.put("refund_desc", request.getRefundDesc());
        map.put("refund_account", request.getRefundAccount());
        return map;
    }

    private PayResponse buildPayResponse(WxPayAsyncResponse response) {
        PayResponse payResponse = new PayResponse();
        payResponse.setOrderAmount(MoneyUtil.Fen2Yuan(response.getTotalFee()));
        payResponse.setOrderId(response.getOutTradeNo());
        payResponse.setOutTradeNo(response.getTransactionId());
        return payResponse;
    }

    private Map<String, String> buildMap(WxPayAsyncResponse response) {
        Map<String, String> map = new HashMap();
        map.put("return_code", response.getReturnCode());
        map.put("return_msg", response.getReturnMsg());
        map.put("appid", response.getAppid());
        map.put("mch_id", response.getMchId());
        map.put("device_info", response.getDeviceInfo());
        map.put("nonce_str", response.getNonceStr());
        map.put("sign", response.getSign());
        map.put("result_code", response.getResultCode());
        map.put("err_code", response.getErrCode());
        map.put("err_code_des", response.getErrCodeDes());
        map.put("openid", response.getOpenid());
        map.put("is_subscribe", response.getIsSubscribe());
        map.put("trade_type", response.getTradeType());
        map.put("bank_type", response.getBankType());
        map.put("total_fee", String.valueOf(response.getTotalFee()));
        map.put("fee_type", response.getFeeType());
        map.put("cash_fee", response.getCashFee());
        map.put("cash_fee_type", response.getCashFeeType());
        map.put("coupon_fee", response.getCouponFee());
        map.put("coupon_count", response.getCouponCount());
        map.put("transaction_id", response.getTransactionId());
        map.put("out_trade_no", response.getOutTradeNo());
        map.put("attach", response.getAttach());
        map.put("time_end", response.getTimeEnd());
        return map;
    }

    private Map<String, String> buildMap(WxPayUnifiedorderRequest request) {
        Map<String, String> map = new HashMap();
        map.put("appid", request.getAppid());
        map.put("mch_id", request.getMchId());
        map.put("nonce_str", request.getNonceStr());
        map.put("sign", request.getSign());
        map.put("attach", request.getAttach());
        map.put("body", request.getBody());
        map.put("detail", request.getDetail());
        map.put("notify_url", request.getNotifyUrl());
        map.put("openid", request.getOpenid());
        map.put("out_trade_no", request.getOutTradeNo());
        map.put("spbill_create_ip", request.getSpbillCreateIp());
        map.put("total_fee", String.valueOf(request.getTotalFee()));
        map.put("trade_type", request.getTradeType());
        return map;
    }

    private PayResponse buildPayResponse(WxPaySyncResponse response) {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000L);
        String nonceStr = RandomUtil.getRandomStr();
        String packAge = "prepay_id=" + response.getPrepayId();
        String signType = "MD5";
        Map<String, String> map = new HashMap();
        map.put("appId", response.getAppid());
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", packAge);
        map.put("signType", signType);
        PayResponse payResponse = new PayResponse();
        payResponse.setAppId(response.getAppid());
        payResponse.setTimeStamp(timeStamp);
        payResponse.setNonceStr(nonceStr);
        payResponse.setPackAge(packAge);
        payResponse.setSignType(signType);
        payResponse.setPaySign(WxPaySignature.sign(map, this.wxPayH5Config.getMchKey()));
        return payResponse;
    }
}
