package com.eiga.an.model.salesModel;

import java.util.List;

/**
 * Created by ASUS on 2018/4/27.
 */

public class ApiSalesCustomerListModel {


    /**
     * Status : 1
     * Msg : null
     * Data : [{"RealName":"苗江伟","UserId":9,"CellPhone":"18367905175","Nick":"18367905175","HeadSculpture":"/Storage/UserHeadSculpture/9.jpg","FinalDecision":"通过"}]
     */

    public int Status;
    public Object Msg;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * RealName : 苗江伟
         * UserId : 9
         * CellPhone : 18367905175
         * Nick : 18367905175
         * HeadSculpture : /Storage/UserHeadSculpture/9.jpg
         * FinalDecision : 通过
         */

        public String RealName;
        public String UserId;
        public String CellPhone;
        public String Nick;
        public String HeadSculpture;
        public String FinalDecision;
    }
}
