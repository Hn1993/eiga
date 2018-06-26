package com.eiga.an.model.jsonModel;

/**
 * Created by ASUS on 2018/4/19.
 */

public class ApiMainModel {


    /**
     * User : {"Id":2,"CellPhone":"18367905175","LoginPassword":"d568112207b7573a4074b724ce9585e1","PayPassword":null,"HeadSculpture":null,"Nick":"18367905175","Sex":"男","IdentityCardId":"360681199306032210","RealName":"黄安","CreateDate":"2018-06-12T14:30:29.67","LastLoginDate":"2018-06-20T12:11:36.293","LoginToken":"ef38f8be4b876d1bc89a9443e64763ee","BelongToClerk":null,"CreditLevel":0,"LoginStatus":0,"IdentityCardOCRAuthentication":false,"IdentityCardOCRStatus":null,"Carrieroperator":null,"IdentityCardFrontPhoto":null,"IdentityCardBackPhoto":null,"Adress":null,"CarPurchaseDemandType":null,"IsSimpleQuota":false,"SimpleQuotaLimit":800000,"AndroidUDID":"865166027766305","IOSUDID":"","CellPhoneProvince":null,"CellPhoneCity":null,"ReportId":"WF2018061409581516731721","ReportReasonCode":200,"FinalDecision":1,"CreditProductIds":null,"MaximumLoanAmount":20000,"FinalScore":null,"AdminDecision":4,"ExpectationCreditType":1,"ApplyDate":"2018-06-14T11:43:21.093","TrialDate":"2018-06-13T10:28:55.783","FinalDate":"2018-06-13T10:29:10.827","TrialAdminId":7,"FinalAdminId":2,"VaildateCellPhone":null}
     * BankCardIsAuthentication : false
     * TokenStatus : 1
     * IsCreditPay : false
     * IsExistTongDunReport : true
     * Status : 1
     * NeedReLogin : false
     * Msg : null
     * Data : null
     */

    public UserBean User;
    public boolean BankCardIsAuthentication;
    public int TokenStatus;
    public boolean IsCreditPay;
    public boolean IsExistTongDunReport;
    public boolean IsVaildateBaseInfo;//是否存在验证信息
    public double MaxAmount;
    public int Status;
    public boolean NeedReLogin;
    public String Msg;
    public Object Data;

    public static class UserBean {
        /**
         * Id : 2
         * CellPhone : 18367905175
         * LoginPassword : d568112207b7573a4074b724ce9585e1
         * PayPassword : null
         * HeadSculpture : null
         * Nick : 18367905175
         * Sex : 男
         * IdentityCardId : 360681199306032210
         * RealName : 黄安
         * CreateDate : 2018-06-12T14:30:29.67
         * LastLoginDate : 2018-06-20T12:11:36.293
         * LoginToken : ef38f8be4b876d1bc89a9443e64763ee
         * BelongToClerk : null
         * CreditLevel : 0
         * LoginStatus : 0
         * IdentityCardOCRAuthentication : false
         * IdentityCardOCRStatus : null
         * Carrieroperator : null
         * IdentityCardFrontPhoto : null
         * IdentityCardBackPhoto : null
         * Adress : null
         * CarPurchaseDemandType : null
         * IsSimpleQuota : false
         * SimpleQuotaLimit : 800000.0
         * AndroidUDID : 865166027766305
         * IOSUDID :
         * CellPhoneProvince : null
         * CellPhoneCity : null
         * ReportId : WF2018061409581516731721
         * ReportReasonCode : 200
         * FinalDecision : 1
         * CreditProductIds : null
         * MaximumLoanAmount : 20000.0
         * FinalScore : null
         * AdminDecision : 4
         * ExpectationCreditType : 1
         * ApplyDate : 2018-06-14T11:43:21.093
         * TrialDate : 2018-06-13T10:28:55.783
         * FinalDate : 2018-06-13T10:29:10.827
         * TrialAdminId : 7
         * FinalAdminId : 2
         * VaildateCellPhone : null
         */

        public int Id;
        public String CellPhone;
        public String LoginPassword;
        public Object PayPassword;
        public Object HeadSculpture;
        public String Nick;
        public String Sex;
        public String IdentityCardId;
        public String RealName;
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
        public Object CarPurchaseDemandType;
        public boolean IsSimpleQuota;
        public double SimpleQuotaLimit;
        public String AndroidUDID;
        public String IOSUDID;
        public Object CellPhoneProvince;
        public Object CellPhoneCity;
        public String ReportId;
        public int ReportReasonCode;
        public int FinalDecision;
        public Object CreditProductIds;
        public double MaximumLoanAmount;
        public Object FinalScore;
        public int AdminDecision;
        public int ExpectationCreditType;
        public String ApplyDate;
        public String TrialDate;
        public String FinalDate;
        public int TrialAdminId;
        public int FinalAdminId;
        public Object VaildateCellPhone;
    }
}
