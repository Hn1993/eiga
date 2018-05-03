package com.eiga.an.model.jsonModel;

import java.util.List;

/**
 * Created by ASUS on 2018/5/3.
 */

public class ApiMyOrderListModel {

    /**
     * Status : 1
     * Msg : null
     * Data : [{"Id":7,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":6,"CreateDate":"2018-05-02T14:19:44.59","Remark":null,"StatusName":"审核驳回","UserId":9,"CreditProductId":1,"UserHeadSculpture":"9.jpg"},{"Id":6,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-05-02T14:00:13.87","Remark":null,"StatusName":"待审核","UserId":9,"CreditProductId":1,"UserHeadSculpture":"9.jpg"},{"Id":5,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-05-02T13:09:43.163","Remark":null,"StatusName":"待审核","UserId":9,"CreditProductId":1,"UserHeadSculpture":"9.jpg"},{"Id":4,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-05-02T09:34:43.647","Remark":null,"StatusName":"待审核","UserId":9,"CreditProductId":1,"UserHeadSculpture":"9.jpg"},{"Id":3,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":5,"CreateDate":"2018-05-02T09:25:48.703","Remark":"5368936","StatusName":"已还款","UserId":9,"CreditProductId":1,"UserHeadSculpture":"9.jpg"},{"Id":2,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":1,"CreateDate":"2018-05-02T08:40:01.47","Remark":null,"StatusName":"审核通过","UserId":9,"CreditProductId":1,"UserHeadSculpture":"9.jpg"},{"Id":1,"ClerkName":"YEWU2","CreditProductName":"奇瑞金融","UserName":"苗江伟","Status":0,"CreateDate":"2018-04-28T17:33:50.137","Remark":null,"StatusName":"待审核","UserId":9,"CreditProductId":1,"UserHeadSculpture":"9.jpg"}]
     */

    public int Status;
    public String Msg;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * Id : 7
         * ClerkName : YEWU2
         * CreditProductName : 奇瑞金融
         * UserName : 苗江伟
         * Status : 6
         * CreateDate : 2018-05-02T14:19:44.59
         * Remark : null
         * StatusName : 审核驳回
         * UserId : 9
         * CreditProductId : 1
         * UserHeadSculpture : 9.jpg
         */

        public String Id;
        public String ClerkName;
        public String CreditProductName;
        public String UserName;
        public int Status;
        public String CreateDate;
        public Object Remark;
        public String StatusName;
        public int UserId;
        public int CreditProductId;
        public String UserHeadSculpture;
    }
}
