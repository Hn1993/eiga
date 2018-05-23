package com.eiga.an.model.jsonModel;

/**
 * Created by ASUS on 2018/4/19.
 */

public class ApiGetBankInfoModel {

    /**
     * BankCard : {"Id":5,"UserId":4,"BankName":"农业银行","CardHolderName":"苗江伟","CardId":"6228487975332015","IsAuthentication":false,"CreateDate":"2018-04-18T15:44:42.317"}
     * Status : 1
     * Msg : null
     * Data : null
     */

    public BankCardBean BankCard;
    public int Status;
    public String Msg;
    public Object Data;

    public static class BankCardBean {
        /**
         * Id : 5
         * UserId : 4
         * BankName : 农业银行
         * CardHolderName : 苗江伟
         * CardId : 6228487975332015
         * IsAuthentication : false
         * CreateDate : 2018-04-18T15:44:42.317
         */

        public int Id;
        public int UserId;
        public String BankName;
        public String CardHolderName;
        public String CardId;
        public boolean IsAuthentication;
        public String CreateDate;
    }
}
