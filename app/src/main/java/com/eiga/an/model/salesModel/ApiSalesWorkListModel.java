package com.eiga.an.model.salesModel;

import java.util.List;

/**
 * Created by ASUS on 2018/5/3.
 */

public class ApiSalesWorkListModel {


    /**
     * Status : 1
     * Msg : null
     * Data : [{"Id":6,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-05-02T14:00:13.87","Remark":null,"StatusName":"业务员已签约(待审核)","UserId":9,"CreditProductId":1},{"Id":5,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-05-02T13:09:43.163","Remark":null,"StatusName":"业务员已签约(待审核)","UserId":9,"CreditProductId":1},{"Id":4,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-05-02T09:34:43.647","Remark":null,"StatusName":"业务员已签约(待审核)","UserId":9,"CreditProductId":1},{"Id":1,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-04-28T17:33:50.137","Remark":null,"StatusName":"业务员已签约(待审核)","UserId":9,"CreditProductId":1}]
     */

    public int Status;
    public String Msg;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * Id : 6
         * ClerkName : YEWU2
         * CreditProductName : 奇瑞金融
         * UserName : 苗江伟
         * Status : 0
         * CreateDate : 2018-05-02T14:00:13.87
         * Remark : null
         * StatusName : 业务员已签约(待审核)
         * UserId : 9
         * CreditProductId : 1
         */

        public String Id;
        public String ClerkName;
        public String CreditProductName;
        public String UserName;
        public int Status;
        public String CreateDate;
        public Object Remark;
        public String StatusName;
        public String UserHeadSculpture;
        public int UserId;
        public int CreditProductId;
    }
}
