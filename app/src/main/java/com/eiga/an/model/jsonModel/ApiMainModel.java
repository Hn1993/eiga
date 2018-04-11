package com.eiga.an.model.jsonModel;

import java.util.List;

/**
 * Created by ASUS on 2018/4/11.
 */

public class ApiMainModel {

    /**
     * Status : 1
     * Data : [{"Id":9,"CateGoryName":"您要申请的额度为","CreateDate":"2018-04-10T13:55:24.723","UpdateDate":"2018-04-10T13:55:24.723","DisplaySequence":1,"IsInitialQuota":true,"QuotaItemList":[{"Id":1,"QuotaItemName":"15万到30万","QuotaCategoryId":9,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":400,"InitialQuota":300000},{"Id":6,"QuotaItemName":"5万","QuotaCategoryId":9,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":50000},{"Id":7,"QuotaItemName":"5万到10万","QuotaCategoryId":9,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":100000},{"Id":8,"QuotaItemName":"10万到15万","QuotaCategoryId":9,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":150000}]},{"Id":10,"CateGoryName":"你的首付比例","CreateDate":"2018-04-10T13:55:46.207","UpdateDate":"2018-04-11T11:10:57.89","DisplaySequence":1,"IsInitialQuota":false,"QuotaItemList":[{"Id":5,"QuotaItemName":"10%","QuotaCategoryId":10,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":10,"InitialQuota":0},{"Id":9,"QuotaItemName":"20%","QuotaCategoryId":10,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":20,"InitialQuota":0},{"Id":10,"QuotaItemName":"30%","QuotaCategoryId":10,"AddOrReduce":2,"QuotaChangeType":2,"AmplitudeOfVariation":30,"InitialQuota":0}]},{"Id":11,"CateGoryName":"你的年收入为","CreateDate":"2018-04-11T11:11:47.07","UpdateDate":"2018-04-11T11:11:47.07","DisplaySequence":2,"IsInitialQuota":false,"QuotaItemList":[{"Id":11,"QuotaItemName":"5w一下","QuotaCategoryId":11,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0},{"Id":12,"QuotaItemName":"5万到50万","QuotaCategoryId":11,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0},{"Id":13,"QuotaItemName":"10万以上","QuotaCategoryId":11,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":0}]}]
     */

    public int Status;
    public List<DataBean> Data;

    public static class DataBean {
        /**
         * Id : 9
         * CateGoryName : 您要申请的额度为
         * CreateDate : 2018-04-10T13:55:24.723
         * UpdateDate : 2018-04-10T13:55:24.723
         * DisplaySequence : 1
         * IsInitialQuota : true
         * QuotaItemList : [{"Id":1,"QuotaItemName":"15万到30万","QuotaCategoryId":9,"AddOrReduce":3,"QuotaChangeType":1,"AmplitudeOfVariation":400,"InitialQuota":300000},{"Id":6,"QuotaItemName":"5万","QuotaCategoryId":9,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":50000},{"Id":7,"QuotaItemName":"5万到10万","QuotaCategoryId":9,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":100000},{"Id":8,"QuotaItemName":"10万到15万","QuotaCategoryId":9,"AddOrReduce":1,"QuotaChangeType":1,"AmplitudeOfVariation":0,"InitialQuota":150000}]
         */

        public int Id;
        public String CateGoryName;
        public String CreateDate;
        public String UpdateDate;
        public int DisplaySequence;
        public boolean IsInitialQuota;
        public List<QuotaItemListBean> QuotaItemList;

        public static class QuotaItemListBean {
            /**
             * Id : 1
             * QuotaItemName : 15万到30万
             * QuotaCategoryId : 9
             * AddOrReduce : 3
             * QuotaChangeType : 1
             * AmplitudeOfVariation : 400.0
             * InitialQuota : 300000.0
             */

            public String Id;
            public String QuotaItemName;
            public int QuotaCategoryId;
            public int AddOrReduce;
            public int QuotaChangeType;
            public double AmplitudeOfVariation;
            public double InitialQuota;
        }
    }
}
