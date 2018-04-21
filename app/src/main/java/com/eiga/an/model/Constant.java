package com.eiga.an.model;

public class Constant {

    //http://192.168.1.196:8010/api/UserIndexPage/GetQuotaCategoryList
    public static final String Url_Common="http://192.168.1.196:8010/";
    public static final String Jd_key="54ae1b27174cf9b5263ec47f50259138";
    public static final String Url_Jd_miguan="https://way.jd.com/juxinli/henypot4JD";
    public static final String Url_Jd_jiedai="https://way.jd.com/XinShu/compBQuery";//个人黑名单多头借贷综合查询
    public static final String Url_Jd_p2p="https://way.jd.com/huiyutech/p2p_user";//P2P用户识别
    public static final String Url_Info_Collection=Url_Common+"api/UserIndexPage/GetQuotaCategoryList";//获取信息
    public static final String Url_Get_EMSCode=Url_Common+"api/User/SendVaildateCode";//获取短信验证码
    public static final String Url_User_Login=Url_Common+"api/User/RegisterUser";//登录
    public static final String Url_Info_Collection_Upload=Url_Common+"api/User/SimpleQuota";//上传选则的评估信息
    public static final String Url_Main=Url_Common+"api/UserIndexPage/Index";//首页的请求
    public static final String Url_Bond_Bank=Url_Common+"api/User/BankCardOperate";//绑定银行卡
    public static final String Url_Get_Bank=Url_Common+"api/User/GetUserBankInfo";//获取银行卡信息
    public static final String Url_Upload_Idcard=Url_Common+"api/User/VaildateIdentityCard";//上传身份证信息
    public static final String Url_Upload_Phoneinfo=Url_Common+"api/User/MobileInfoVaildate";//手机运营商认证


    public static final String LocationLongitude="Location_Longitude";//定位的经纬度  以及城市
    public static final String LocationLatitude="Location_Latitude";
    public static final String LocationDistrictCity="Location_District_City";//县级市
    public static final String IsFirstOpenApp="first_open_app";//是否第一次打开app
    public static final String User_Login_Name="User_Login_Name";//用户的登录账号
    public static final String User_Login_Token="User_Login_Token";//用户的Token
    public static final String User_Is_Have_Evaluation="User_Is_Have_Evaluation";//判断用户是否评估过
    public static final String Sales_Login_Name="Sales_Login_Name";//业务员的登录账号

}
