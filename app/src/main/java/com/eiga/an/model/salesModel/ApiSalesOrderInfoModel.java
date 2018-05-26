package com.eiga.an.model.salesModel;

import java.util.List;

/**
 * Created by ASUS on 2018/5/3.
 */

public class ApiSalesOrderInfoModel {


    /**
     * OrderId : P2201805241033079052712
     * StatusName : 业务员已签约
     * CreateDate : 2018-05-24 10:33:07
     * ContractSnapshot : [{"Id":1002,"ContractPhoto":"53cde149-4912-4d47-8931-a6d870efef7b.PNG","CreateDate":"2018-05-24T10:33:07.92","ContractPhotoType":2,"CreditFlowForeign":2008},{"Id":1003,"ContractPhoto":"646dffbc-dfaf-417d-899a-7e8a5ba62640.PNG","CreateDate":"2018-05-24T10:33:07.92","ContractPhotoType":2,"CreditFlowForeign":2008},{"Id":1004,"ContractPhoto":"bb612dd3-da63-488c-ac25-ce02ad666791.PNG","CreateDate":"2018-05-24T10:33:07.92","ContractPhotoType":2,"CreditFlowForeign":2008}]
     * CreditProductPicture : /Storage/CreditProductPicture/bd6f38a3-b741-403c-802d-af981f694f13.PNG
     * CreditProduct : 奇瑞金融
     * OrderStatus : 1
     * CreditFlowLog : [{"Id":9,"CreditFlowId":2008,"CreateDate":"2018-05-24T10:33:07.937","OperdateAdminId":0,"Step":1,"StepName":"业务员签约","FlowLogType":2,"IsBack":false,"BackMsg":null,"IsYeWu":true}]
     * Status : 1
     * Msg : null
     * Data : null
     */

    public String OrderId;
    public String StatusName;
    public String CreateDate;
    public String CreditProductPicture;
    public String CreditProduct;
    public int OrderStatus;
    public int Status;
    public String Msg;
    public String BackMsg;//驳回理由
    public String UserCellPhone;//用户电话
    public String ClerkCellPhone;//业务员电话
    public Object Data;
    public List<ContractSnapshotBean> ContractSnapshot;
    public List<CreditFlowLogBean> CreditFlowLog;

    public static class ContractSnapshotBean {
        /**
         * Id : 1002
         * ContractPhoto : 53cde149-4912-4d47-8931-a6d870efef7b.PNG
         * CreateDate : 2018-05-24T10:33:07.92
         * ContractPhotoType : 2
         * CreditFlowForeign : 2008
         */

        public String Id;
        public String ContractPhoto;
        public String CreateDate;
        public int ContractPhotoType;
        public int CreditFlowForeign;
    }

    public static class CreditFlowLogBean {
        /**
         * Id : 9
         * CreditFlowId : 2008
         * CreateDate : 2018-05-24T10:33:07.937
         * OperdateAdminId : 0
         * Step : 1
         * StepName : 业务员签约
         * FlowLogType : 2
         * IsBack : false
         * BackMsg : null
         * IsYeWu : true
         */

        public int Id;
        public int CreditFlowId;
        public String CreateDate;
        public int OperdateAdminId;
        public int Step;
        public String StepName;
        public String CreateDateString;
        public int FlowLogType;
        public boolean IsBack;
        public Object BackMsg;
        public boolean IsYeWu;
    }
}
