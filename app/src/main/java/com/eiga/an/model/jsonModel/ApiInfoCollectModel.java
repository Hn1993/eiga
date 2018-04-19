package com.eiga.an.model.jsonModel;

import java.util.List;

/**
 * Created by ASUS on 2018/4/11.
 */

public class ApiInfoCollectModel {


    /**
     * Status : 1
     * Msg : 请求成功
     * Data : [{"Id":13,"CateGoryName":"购车需求","CreateDate":"2018-04-13T15:41:23.507","UpdateDate":"2018-04-13T15:41:23.507","DisplaySequence":7,"IsInitialQuota":false,"IsCarPurchaseDemand":true,"QuotaItemList":[{"Id":17,"QuotaItemName":"新车","QuotaCategoryId":13,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":1},{"Id":18,"QuotaItemName":"二手车","QuotaCategoryId":13,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":2}]},{"Id":14,"CateGoryName":"车价","CreateDate":"2018-04-13T15:42:40.307","UpdateDate":"2018-04-13T15:42:40.307","DisplaySequence":1,"IsInitialQuota":true,"IsCarPurchaseDemand":false,"QuotaItemList":[{"Id":19,"QuotaItemName":"5万以下","QuotaCategoryId":14,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":50000,"CarPurchaseDemandType":0},{"Id":20,"QuotaItemName":"5-10万","QuotaCategoryId":14,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":100000,"CarPurchaseDemandType":0},{"Id":21,"QuotaItemName":"10-15万","QuotaCategoryId":14,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":150000,"CarPurchaseDemandType":0},{"Id":22,"QuotaItemName":"15-20万","QuotaCategoryId":14,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":200000,"CarPurchaseDemandType":0},{"Id":23,"QuotaItemName":"20-30万","QuotaCategoryId":14,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":300000,"CarPurchaseDemandType":0},{"Id":24,"QuotaItemName":"30万以上","QuotaCategoryId":14,"AddOrReduce":0,"QuotaChangeType":0,"AmplitudeOfVariation":0,"InitialQuota":500000,"CarPurchaseDemandType":0}]},{"Id":18,"CateGoryName":"社保情况","CreateDate":"2018-04-13T15:48:11.873","UpdateDate":"2018-04-13T15:48:11.873","DisplaySequence":1,"IsInitialQuota":false,"IsCarPurchaseDemand":false,"QuotaItemList":[{"Id":38,"QuotaItemName":"有社保","QuotaCategoryId":18,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":39,"QuotaItemName":"无社保","QuotaCategoryId":18,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0}]},{"Id":20,"CateGoryName":"每月收入","CreateDate":"2018-04-13T16:04:19.313","UpdateDate":"2018-04-13T16:04:19.313","DisplaySequence":2,"IsInitialQuota":false,"IsCarPurchaseDemand":false,"QuotaItemList":[{"Id":46,"QuotaItemName":"3千以下","QuotaCategoryId":20,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":47,"QuotaItemName":"3千-5千","QuotaCategoryId":20,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":48,"QuotaItemName":"5千-8千","QuotaCategoryId":20,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":49,"QuotaItemName":"8千-1万2","QuotaCategoryId":20,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":50,"QuotaItemName":"1万2-2万","QuotaCategoryId":20,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":51,"QuotaItemName":"2万以上","QuotaCategoryId":20,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0}]},{"Id":15,"CateGoryName":"首付比例","CreateDate":"2018-04-13T15:44:30.123","UpdateDate":"2018-04-13T15:44:30.123","DisplaySequence":3,"IsInitialQuota":false,"IsCarPurchaseDemand":false,"QuotaItemList":[{"Id":25,"QuotaItemName":"10%","QuotaCategoryId":15,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":10,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":26,"QuotaItemName":"20%","QuotaCategoryId":15,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":20,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":27,"QuotaItemName":"30%","QuotaCategoryId":15,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":30,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":28,"QuotaItemName":"40%","QuotaCategoryId":15,"AddOrReduce":3,"QuotaChangeType":2,"AmplitudeOfVariation":40,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":29,"QuotaItemName":"50%","QuotaCategoryId":15,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":50,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":30,"QuotaItemName":"60%","QuotaCategoryId":15,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":60,"InitialQuota":0,"CarPurchaseDemandType":0}]},{"Id":19,"CateGoryName":"职业身份","CreateDate":"2018-04-13T16:03:05.95","UpdateDate":"2018-04-13T16:03:05.95","DisplaySequence":4,"IsInitialQuota":false,"IsCarPurchaseDemand":false,"QuotaItemList":[{"Id":41,"QuotaItemName":"上班族","QuotaCategoryId":19,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":42,"QuotaItemName":"事业单位","QuotaCategoryId":19,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":43,"QuotaItemName":"企业主","QuotaCategoryId":19,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":44,"QuotaItemName":"个体商户","QuotaCategoryId":19,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":45,"QuotaItemName":"自由职业","QuotaCategoryId":19,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0}]},{"Id":17,"CateGoryName":"信用记录","CreateDate":"2018-04-13T15:47:10.823","UpdateDate":"2018-04-13T15:47:10.823","DisplaySequence":5,"IsInitialQuota":false,"IsCarPurchaseDemand":false,"QuotaItemList":[{"Id":34,"QuotaItemName":"信用良好","QuotaCategoryId":17,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":35,"QuotaItemName":"无信用记录","QuotaCategoryId":17,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":36,"QuotaItemName":"少数逾期","QuotaCategoryId":17,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":37,"QuotaItemName":"多次逾期","QuotaCategoryId":17,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0}]},{"Id":16,"CateGoryName":"住房情况","CreateDate":"2018-04-13T15:46:12.797","UpdateDate":"2018-04-13T15:46:12.797","DisplaySequence":6,"IsInitialQuota":false,"IsCarPurchaseDemand":false,"QuotaItemList":[{"Id":31,"QuotaItemName":"租房","QuotaCategoryId":16,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":32,"QuotaItemName":"有房有贷","QuotaCategoryId":16,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0},{"Id":33,"QuotaItemName":"有房无贷","QuotaCategoryId":16,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":0}]}]
     */

    public int Status;
    public String Msg;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * Id : 13
         * CateGoryName : 购车需求
         * CreateDate : 2018-04-13T15:41:23.507
         * UpdateDate : 2018-04-13T15:41:23.507
         * DisplaySequence : 7
         * IsInitialQuota : false
         * IsCarPurchaseDemand : true
         * QuotaItemList : [{"Id":17,"QuotaItemName":"新车","QuotaCategoryId":13,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":1},{"Id":18,"QuotaItemName":"二手车","QuotaCategoryId":13,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0,"CarPurchaseDemandType":2}]
         */

        public int Id;
        public String CateGoryName;
        public String CreateDate;
        public String UpdateDate;
        public int DisplaySequence;
        public boolean IsInitialQuota;
        public boolean IsCarPurchaseDemand;
        public List<QuotaItemListBean> QuotaItemList;

        public static class QuotaItemListBean {
            /**
             * Id : 17
             * QuotaItemName : 新车
             * QuotaCategoryId : 13
             * AddOrReduce : 3
             * QuotaChangeType : 1
             * AmplitudeOfVariation : 0.0
             * InitialQuota : 0.0
             * CarPurchaseDemandType : 1
             */

            public String Id;
            public String QuotaItemName;
            public int QuotaCategoryId;
            public int AddOrReduce;
            public int QuotaChangeType;
            public double AmplitudeOfVariation;
            public double InitialQuota;
            public int CarPurchaseDemandType;
        }
    }
}
