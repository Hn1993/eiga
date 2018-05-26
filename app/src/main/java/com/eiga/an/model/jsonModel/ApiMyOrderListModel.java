package com.eiga.an.model.jsonModel;

import java.util.List;

/**
 * Created by ASUS on 2018/5/3.
 */

public class ApiMyOrderListModel {


    /**
     * Status : 1
     * Msg : null
     * Data : [{"CarModel":"帝豪GS1.3T自动","CarBrand":"吉利","CarPrice":100000,"OrderId":"P201805041001414866392","UserHeadSculpture":"9.jpg","Id":1,"Status":0,"CreateDate":"2018-05-04T10:01:41.483","ClerkName":"YEWU2","UserId":9,"UserName":"苗江伟","CreditProductName":"奇瑞金融","CreditProductId":1,"Remark":null,"StatusName":"待审核","CarPicture":"/Storage/CarPicture/"}]
     */

    public int Status;
    public String Msg;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * CarModel : 帝豪GS1.3T自动
         * CarBrand : 吉利
         * CarPrice : 100000.0
         * OrderId : P201805041001414866392
         * UserHeadSculpture : 9.jpg
         * Id : 1
         * Status : 0
         * CreateDate : 2018-05-04T10:01:41.483
         * ClerkName : YEWU2
         * UserId : 9
         * UserName : 苗江伟
         * CreditProductName : 奇瑞金融
         * CreditProductId : 1
         * Remark : null
         * StatusName : 待审核
         * CarPicture : /Storage/CarPicture/
         */

        public String CarModel;
        public String CarBrand;
        public double CarPrice;
        public String OrderId;
        public String CreditProcuctPicture;
        public String UserHeadSculpture;
        public String Id;
        public int Status;
        public String CreateDate;
        public String ClerkName;
        public int UserId;
        public String UserName;
        public String CreditProductName;
        public int CreditProductId;
        public Object Remark;
        public String StatusName;
        public String CarPicture;
    }
}
