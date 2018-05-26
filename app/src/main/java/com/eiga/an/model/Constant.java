package com.eiga.an.model;

public class Constant {

    //http://192.168.1.196:8010/api/UserIndexPage/GetQuotaCategoryList
    public static final String Url_Common="http://192.168.1.196:8010";//http://admin.zjglcar.com
    public static final String Url_Common_Test="http://192.168.1.196:8010";
    public static final String Jd_key="54ae1b27174cf9b5263ec47f50259138";
    public static final String Url_Jd_miguan="https://way.jd.com/juxinli/henypot4JD";
    public static final String Url_Jd_jiedai="https://way.jd.com/XinShu/compBQuery";//个人黑名单多头借贷综合查询
    public static final String Url_Jd_p2p="https://way.jd.com/huiyutech/p2p_user";//P2P用户识别
    public static final String Url_Info_Collection=Url_Common+"/api/UserPage/GetQuotaCategoryList";//获取信息
    public static final String Url_Get_EMSCode=Url_Common+"/api/User/SendVaildateCode";//获取短信验证码
    public static final String Url_User_Login=Url_Common+"/api/User/RegisterUser";//登录
    public static final String Url_Info_Collection_Upload=Url_Common+"/api/User/SimpleQuota";//上传选则的评估信息
    public static final String Url_Main=Url_Common+"/api/UserPage/Index";//首页的请求
    public static final String Url_Bond_Bank=Url_Common+"/api/User/BankCardOperate";//绑定银行卡
    public static final String Url_Get_Bank=Url_Common+"/api/User/GetUserBankInfo";//获取银行卡信息
    public static final String Url_Upload_Idcard=Url_Common+"/api/User/VaildateIdentityCard";//上传身份证信息
    public static final String Url_Upload_Phoneinfo=Url_Common+"/api/User/MobileInfoVaildate";//手机运营商认证
    public static final String Url_User_GetInfo=Url_Common+"/api/UserPage/ModifyInfoPage";//用户信息
    public static final String Url_User_FixInfo=Url_Common+"/api/User/ModifyInfo";//上传信息
    public static final String Url_User_MyCenter=Url_Common+"/api/UserPage/UserCenter";//个人中心
    public static final String Url_User_Order_List=Url_Common+"/api/UserPage/GetCreditFlowList";//订单列表
    public static final String Url_User_Order_Info=Url_Common+"/api/UserPage/GetCreditFlowDetail";//订单详情


    public static final String Url_Sales_Login=Url_Common+"/api/Clerk/Login";//业务员登录
    public static final String Url_Sales_FixInfo=Url_Common+"/api/Clerk/ModifyInfo";//业务员自改头像  昵称
    public static final String Url_Sales_GetInfo=Url_Common+"/api/ClerkPage/ModifyInfoPage";//业务员头像 昵称
    public static final String Url_Sales_Main=Url_Common+"/api/ClerkPage/Index";//业务员首页
    public static final String Url_Sales_Customer_Product=Url_Common+"/api/ClerkPage/GetCreditProductInfo";//客户的推荐产品
    public static final String Url_Sales_Commit_Customer_Product=Url_Common+"/api/Clerk/CreateCreditFlow";//提交签单的产品
    public static final String Url_Sales_Get_Confidentiality_Agreement=Url_Common+"/api/UserPage/GetUserProtocolContent";//获取用户的隐私与保密协议

    public static final String Url_Sales_Customer=Url_Common+"/api/ClerkPage/GetUserList";//客户管理
    public static final String Url_Sales_Work_Order_List=Url_Common+"/api/ClerkPage/GetCreditFlowList";//工作订单列表
    public static final String Url_Sales_Work_Order_Info=Url_Common+"/api/ClerkPage/GetCreditFlowDetail";//工作订单详情
    public static final String Url_Sales_Get_Car_List=Url_Common+"/api/ClerkPage/GetCarModeInfo";//获取车型列表
    public static final String Url_Sales_Upload_Single_Contract=Url_Common+"/api/Clerk/ProcessContractPhoto";//上传单张合同照
    public static final String Url_Sales_Upload_Contracts=Url_Common+"/api/Clerk/CreateCarCreditFlowFinanceCompany";//提交贷款合同
    public static final String Url_Sales_Info_Process_Details=Url_Common+"/api/ClerkPage/GetFlowHtmlString?ContractType=2&";//提交贷款合同
    public static final String Url_Sales_Upload_Contracts_Recommit=Url_Common+"/api/clerk/ReSubmit";//重新提交贷款合同


    public static final String LocationLongitude="Location_Longitude";//定位的经纬度  以及城市
    public static final String LocationLatitude="Location_Latitude";
    public static final String LocationDistrictCity="Location_District_City";//县级市
    public static final String IsFirstOpenApp="first_open_app";//是否第一次打开app
    public static final String User_Login_Name="User_Login_Name";//用户的登录账号
    public static final String User_Login_Token="User_Login_Token";//用户的Token
    public static final String User_Login_Nickname="User_Login_Nickname";//用户的昵称
    public static final String WebUrl="web_url";
    public static final String WebTitle="web_title";

    public static final String User_Is_Have_Evaluation="User_Is_Have_Evaluation";//判断用户是否评估过
    public static final String Sales_Login_Phone="Sales_Login_Phone";//业务员的登录账号
    public static final String Sales_Login_Token="Sales_Login_Token";//业务员的token
    public static final String Sales_Login_Nickname="Sales_Login_Nickname";//业务员的昵称
    public static final String Sales_User_Id="Sales_User_Id";//业务员办业务的id
    public static final String Sales_Order_Id="sales_order_id";//业务员办业务订单ID
    public static final String Sales_CreditProduct_Id="Sales_CreditProduct_Id";//贷款产品id
    public static final String Order_Info_Type="Order_Info_Type";//业判断是哪个页面进入的产品详情
    public static final String Order_Info_Status="Order_Info_Status";//判断当前是哪个状态

}
