package com.eiga.an.model.jsonModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS on 2018/6/12.
 */

public class ApiQueryToPayModel {


    /**
     * appid : null
     * partnerid : null
     * prepayid : null
     * noncestr : null
     * timestamp : null
     * package : Sign=WXPay
     * sign : null
     * body : null
     * Status : 2
     * NeedReLogin : false
     * Msg : 统一支付接口响应错误
     * Data : null
     */

    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    @SerializedName("package")
    public String packageX;
    public String sign;
    public String body;
    public int Status;
    public boolean NeedReLogin;
    public String Msg;
    public Object Data;
}
