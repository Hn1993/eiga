package com.eiga.an.model.jsonModel;

/**
 * Created by ASUS on 2018/4/19.
 */

public class ApiMainModel {


    /**
     * User : {"Id":9,"CellPhone":"18367905175","LoginPassword":"d568112207b7573a4074b724ce9585e1","PayPassword":null,"HeadSculpture":null,"Nick":"18367905175","Sex":"男","IdentityCardId":null,"RealName":null,"CreateDate":"2018-04-19T14:55:33.513","LastLoginDate":"2018-04-19T15:19:55.947","LoginToken":"58abd947bb143e7fe299d08239b88332","BelongToClerk":null,"CreditLevel":0,"LoginStatus":0,"IdentityCardOCRAuthentication":false,"IdentityCardOCRStatus":null,"Carrieroperator":null,"IdentityCardFrontPhoto":null,"IdentityCardBackPhoto":null,"Adress":null,"CarPurchaseDemand":null,"IsSimpleQuota":true,"SimpleQuotaLimit":45000,"AndroidUDID":"865166027766305","IOSUDID":""}
     * IsAuthentication : false
     * TokenStatus : 1
     * Status : 1
     * Msg : null
     * Data : null
     */

    public UserBean User;
    public boolean BankCardIsAuthentication;
    public int TokenStatus;
    public int Status;
    public String Msg;
    public boolean NeedReLogin;
    public Object Data;

    public static class UserBean {
        /**
         * Id : 9
         * CellPhone : 18367905175
         * LoginPassword : d568112207b7573a4074b724ce9585e1
         * PayPassword : null
         * HeadSculpture : null
         * Nick : 18367905175
         * Sex : 男
         * IdentityCardId : null
         * RealName : null
         * CreateDate : 2018-04-19T14:55:33.513
         * LastLoginDate : 2018-04-19T15:19:55.947
         * LoginToken : 58abd947bb143e7fe299d08239b88332
         * BelongToClerk : null
         * CreditLevel : 0
         * LoginStatus : 0
         * IdentityCardOCRAuthentication : false
         * IdentityCardOCRStatus : null
         * Carrieroperator : null
         * IdentityCardFrontPhoto : null
         * IdentityCardBackPhoto : null
         * Adress : null
         * CarPurchaseDemand : null
         * IsSimpleQuota : true
         * SimpleQuotaLimit : 45000.0
         * AndroidUDID : 865166027766305
         * IOSUDID :
         */

        public int Id;
        public String CellPhone;
        public String LoginPassword;
        public Object PayPassword;
        public Object HeadSculpture;
        public String Nick;
        public String Sex;
        public Object IdentityCardId;
        public Object RealName;
        public String CreateDate;
        public String LastLoginDate;
        public String LoginToken;
        public Object BelongToClerk;
        public int CreditLevel;
        public int LoginStatus;
        public boolean IdentityCardOCRAuthentication;
        public Object IdentityCardOCRStatus;
        public Object Carrieroperator;
        public Object IdentityCardFrontPhoto;
        public Object IdentityCardBackPhoto;
        public Object Adress;
        public Object CarPurchaseDemand;
        public boolean IsSimpleQuota;
        public double SimpleQuotaLimit;
        public String AndroidUDID;
        public String IOSUDID;
    }

}
