package com.eiga.an.model.salesModel;

import java.util.List;

/**
 * Created by ASUS on 2018/5/5.
 */

public class ApiSalesChooseCarListModel {


    /**
     * CarBrandInfoList : [{"CarBrandId":1,"CarBrandPicture":"/Storage/CarBrandPicture/6afc16b4-c876-4e8c-bc5a-0d7faaadd4a0.PNG","CarModelList":[{"Id":5,"ModelName":"x2","CarBrandId":1,"CreateDate":"2018-05-04T16:50:12.083","Picture":"/Storage/CarModelPicture/1e307fbb-b0b8-4f8a-812a-c9a266f16c07.JPG"}]}]
     * Status : 0
     * Msg : null
     * Data : null
     */

    public int Status;
    public String Msg;
    public Object Data;
    public List<CarBrandInfoListBean> CarBrandInfoList;

    public static class CarBrandInfoListBean {
        /**
         * CarBrandId : 1
         * CarBrandPicture : /Storage/CarBrandPicture/6afc16b4-c876-4e8c-bc5a-0d7faaadd4a0.PNG
         * CarModelList : [{"Id":5,"ModelName":"x2","CarBrandId":1,"CreateDate":"2018-05-04T16:50:12.083","Picture":"/Storage/CarModelPicture/1e307fbb-b0b8-4f8a-812a-c9a266f16c07.JPG"}]
         */
        public boolean isChoosed=false;
        public boolean isFirstLoad=true;
        public int CarBrandId;
        public String CarBrandPicture;
        public List<CarModelListBean> CarModelList;

        public static class CarModelListBean {
            /**
             * Id : 5
             * ModelName : x2
             * CarBrandId : 1
             * CreateDate : 2018-05-04T16:50:12.083
             * Picture : /Storage/CarModelPicture/1e307fbb-b0b8-4f8a-812a-c9a266f16c07.JPG
             */

            public String Id;
            public String ModelName;
            public String CarBrandName;
            public int CarBrandId;
            public String CreateDate;
            public String Picture;
        }
    }
}
