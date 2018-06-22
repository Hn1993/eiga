package com.eiga.an.model.jsonModel;

/**
 * Created by ASUS on 2018/6/20.
 */

public class EventBusBankResultModel {
    public String bankUserName;
    public String bankName;
    public String bankCard;

    @Override
    public String toString() {
        return "EventBusBankResultModel{" +
                "bankUserName='" + bankUserName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCard='" + bankCard + '\'' +
                '}';
    }
}
