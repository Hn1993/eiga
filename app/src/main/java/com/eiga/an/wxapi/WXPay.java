package com.eiga.an.wxapi;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * 微信支付
 *
 */
public class WXPay {

    private static WXPay mWXPay;
    private IWXAPI mWXApi;
    private HashMap<String,String> mPayParam;
    private WXPayResultCallBack mCallback;

    public static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    public static final int ERROR_PAY = 3;  //支付失败

    public interface WXPayResultCallBack {
        void onSuccess(); //支付成功
        void onError(int error_code);   //支付失败
        void onCancel();    //支付取消
    }

    public WXPay(Context context, String wx_appid) {
        mWXApi = WXAPIFactory.createWXAPI(context, null);
        mWXApi.registerApp(wx_appid);
    }

    public static void init(Context context, String wx_appid) {
        if(mWXPay == null) {
            mWXPay = new WXPay(context, wx_appid);
        }
    }
    public static WXPay getInstance(){
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }
    /**
     * 发起微信支付
     */
    public void doPay(HashMap<String,String> pay_param, WXPayResultCallBack callback) {
        mPayParam = pay_param;
        Log.e("WXPAY","e="+mPayParam.toString());
        mCallback = callback;

        if(!check()) {
            if(mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }

        if(TextUtils.isEmpty(mPayParam.get("appid")) || TextUtils.isEmpty(mPayParam.get("partnerid"))
                || TextUtils.isEmpty(mPayParam.get("prepayid")) || TextUtils.isEmpty(mPayParam.get("package")) ||
                TextUtils.isEmpty(mPayParam.get("noncestr")) || TextUtils.isEmpty(mPayParam.get("timestamp")) ||
                TextUtils.isEmpty(mPayParam.get("sign"))) {
            if(mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = mPayParam.get("appid");
        req.partnerId = mPayParam.get("partnerid");
        req.prepayId = mPayParam.get("prepayid");
        req.packageValue = mPayParam.get("package");
        req.nonceStr = mPayParam.get("noncestr");
        req.timeStamp = mPayParam.get("timestamp");
        req.sign = mPayParam.get("sign");

        Log.e("WXPAY","req.appId="+req.appId);
        Log.e("WXPAY","req.partnerId="+req.partnerId);
        Log.e("WXPAY"," req.prepayId="+req.prepayId);
        Log.e("WXPAY"," req.packageValue="+req.packageValue);
        Log.e("WXPAY"," req.nonceStr="+req.nonceStr);
        Log.e("WXPAY"," req.timeStamp="+req.timeStamp);
        Log.e("WXPAY"," req.sign="+req.sign);
        mWXApi.sendReq(req);
    }

    //支付回调响应
    public void onResp(int error_code) {
        if(mCallback == null) {
            return;
        }
        Log.e("WXPAY","error_code="+error_code);
        if(error_code == 0) {   //成功
            mCallback.onSuccess();
        } else if(error_code == -1) {   //错误
            mCallback.onError(ERROR_PAY);
        } else if(error_code == -2) {   //取消
            mCallback.onCancel();
        }

        mCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }
}
