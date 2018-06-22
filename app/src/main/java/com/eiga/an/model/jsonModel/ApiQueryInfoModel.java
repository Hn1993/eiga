package com.eiga.an.model.jsonModel;

/**
 * Created by ASUS on 2018/6/12.
 */

public class ApiQueryInfoModel {

    /**
     * VaildateIdentityCardStatus : 0
     * BankCardOperateStatus : 7
     * Status : 1
     * NeedReLogin : false
     * Msg : 绑定成功
     * Data : null
     */

    public int VaildateIdentityCardStatus;
    public int BankCardOperateStatus;
    public int Status;
    public boolean NeedReLogin;
    public boolean InvokePay;
    public String Msg;
    public Object Data;
}
