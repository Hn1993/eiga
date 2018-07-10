package com.eiga.an.model.jsonModel;

/**
 * Created by ASUS on 2018/4/19.
 */

public class ApiMainModel {


    /**
     * User : {"Id":14,"CellPhone":"18367905175","LoginPassword":"d568112207b7573a4074b724ce9585e1","PayPassword":null,"HeadSculpture":null,"Nick":"18367905175","Sex":"男","IdentityCardId":"330782199510165113","RealName":"苗江伟","CreateDate":"2018-06-26T09:00:27.647","LastLoginDate":"2018-06-30T09:36:42.86","LoginToken":"c236d475f927f71fbf584d818aacc5fb","BelongToClerk":null,"CreditLevel":0,"LoginStatus":0,"IdentityCardOCRAuthentication":true,"IdentityCardOCRStatus":1,"Carrieroperator":"移动","IdentityCardFrontPhoto":null,"IdentityCardBackPhoto":null,"Adress":"浙江省义乌市江东街道供店村5组","CarPurchaseDemandType":null,"IsSimpleQuota":false,"SimpleQuotaLimit":90000,"AndroidUDID":"865166027766305","IOSUDID":"","CellPhoneProvince":"浙江","CellPhoneCity":"金华","ReportId":"WF2018062609014011968617","ReportReasonCode":100,"FinalDecision":1,"CreditProductIds":"1,2,3","MaximumLoanAmount":null,"FinalScore":15,"AdminDecision":1,"ExpectationCreditType":1,"ApplyDate":"2018-06-26T09:01:40.803","TrialDate":null,"FinalDate":null,"TrialAdminId":null,"FinalAdminId":null,"VaildateCellPhone":"13105791215"}
     * BankCardIsAuthentication : true
     * TokenStatus : 1
     * IsCreditPay : true
     * IsExistTongDunReport : true
     * MaxAmount : 400000.0
     * IsVaildateBaseInfo : true
     * AppVersion : {"Id":1,"Version":1,"CreateDate":"2018-06-30T08:43:30.193","HighLight":"首版","DownloadURL":"https://www.pgyer.com/RtL8"}
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
    public double MaxAmount;
    public boolean IsVaildateBaseInfo;
    public boolean IsUpdateExpectation; //是否提交了需求
    public AppVersionBean AppVersion;
    public int Status;
    public boolean NeedReLogin;
    public String Msg;
    public Object Data;

    public static class UserBean {
        /**
         * Id : 14
         * CellPhone : 18367905175
         * LoginPassword : d568112207b7573a4074b724ce9585e1
         * PayPassword : null
         * HeadSculpture : null
         * Nick : 18367905175
         * Sex : 男
         * IdentityCardId : 330782199510165113
         * RealName : 苗江伟
         * CreateDate : 2018-06-26T09:00:27.647
         * LastLoginDate : 2018-06-30T09:36:42.86
         * LoginToken : c236d475f927f71fbf584d818aacc5fb
         * BelongToClerk : null
         * CreditLevel : 0
         * LoginStatus : 0
         * IdentityCardOCRAuthentication : true
         * IdentityCardOCRStatus : 1
         * Carrieroperator : 移动
         * IdentityCardFrontPhoto : null
         * IdentityCardBackPhoto : null
         * Adress : 浙江省义乌市江东街道供店村5组
         * CarPurchaseDemandType : null
         * IsSimpleQuota : false
         * SimpleQuotaLimit : 90000.0
         * AndroidUDID : 865166027766305
         * IOSUDID :
         * CellPhoneProvince : 浙江
         * CellPhoneCity : 金华
         * ReportId : WF2018062609014011968617
         * ReportReasonCode : 100
         * FinalDecision : 1
         * CreditProductIds : 1,2,3
         * MaximumLoanAmount : null
         * FinalScore : 15
         * AdminDecision : 1
         * ExpectationCreditType : 1
         * ApplyDate : 2018-06-26T09:01:40.803
         * TrialDate : null
         * FinalDate : null
         * TrialAdminId : null
         * FinalAdminId : null
         * VaildateCellPhone : 13105791215
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
        public int IdentityCardOCRStatus;
        public String Carrieroperator;
        public Object IdentityCardFrontPhoto;
        public Object IdentityCardBackPhoto;
        public String Adress;
        public Object CarPurchaseDemandType;
        public boolean IsSimpleQuota;
        public double SimpleQuotaLimit;
        public String AndroidUDID;
        public String IOSUDID;
        public String CellPhoneProvince;
        public String CellPhoneCity;
        public String ReportId;
        public int ReportReasonCode;
        public int FinalDecision;
        public String CreditProductIds;
        public Object MaximumLoanAmount;
        public int FinalScore;
        public int AdminDecision;
        public int ExpectationCreditType;
        public String ApplyDate;
        public Object TrialDate;
        public Object FinalDate;
        public Object TrialAdminId;
        public Object FinalAdminId;
        public String VaildateCellPhone;
    }

    public static class AppVersionBean {
        /**
         * Id : 1
         * Version : 1.0
         * CreateDate : 2018-06-30T08:43:30.193
         * HighLight : 首版
         * DownloadURL : https://www.pgyer.com/RtL8
         */

        public int Id;
        public double Version;
        public String VersionName;
        public String CreateDate;
        public String HighLight;
        public String DownloadURL;
    }
}
