package com.eiga.an.model.jsonModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ASUS on 2018/3/24.
 */

public class ApiWxPayModel {

    /**
     * appid : wxd22c523ec819b8be
     * noncestr : dnGWivX1Ac97JkSP
     * package : Sign=WXPay
     * partnerid : 1496322702
     * prepayid : wx20180324151217bff19de9dc0239142005
     * timestamp : 1521875537
     * sign : 4D7CB321527184ABD58C59993497D1AC
     */

    public String appid;
    public String noncestr;
    @SerializedName("package")
    public String packageX;
    public String partnerid;
    public String prepayid;
    public String timestamp;
    public String sign;

    @Override
    public String toString() {
        return "ApiWxPayModel{" +
                "appid='" + appid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", packageX='" + packageX + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
