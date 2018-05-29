package com.eiga.an.model.salesModel;

import java.util.List;

/**
 * Created by ASUS on 2018/4/27.
 */

public class ApiSalesCustomerProductModel {

    /**
     * GetCreditProductInfoStatus : 0
     * Status : 1
     * Msg : null
     * Data : [{"Id":1,"Name":"奇瑞金融","IsRecommand":true,"MaximumLoanAmount":200000},{"Id":2,"Name":"平安新一证","IsRecommand":true,"MaximumLoanAmount":200000},{"Id":3,"Name":"咖喱车服以租代购产品","IsRecommand":false,"MaximumLoanAmount":200000}]
     */

    public int GetCreditProductInfoStatus;
    public int Status;
    public String Msg;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * Id : 1
         * Name : 奇瑞金融
         * IsRecommand : true
         * MaximumLoanAmount : 200000.0
         */

        public String CreditProductId;
        public int ContractPhoto;
        public String Name;
        public String Picture;
        public boolean IsRecommand;
        public boolean IsChoosed=false;
        public double MaximumLoanAmount;
    }
}
