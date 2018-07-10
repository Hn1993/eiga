package com.eiga.an.model.jsonModel;

import java.util.List;

/**
 * Created by ASUS on 2018/6/14.
 */

public class ApiMallLoadTypeModel {

    /**
     * CreditTypes : [{"Id":1,"TypeName":"车贷","MaxCreditAmount":800000,"Photo":"/Storage/CarTypePhoto/936858f4-d007-4826-8b09-69691be7a30c.PNG"}]
     * Status : 1
     * NeedReLogin : false
     * Msg : null
     * Data : null
     */

    public int Status;
    public boolean NeedReLogin;
    public String Msg;
    public Object Data;
    public List<CreditTypesBean> CreditTypes;

    public static class CreditTypesBean {
        /**
         * Id : 1
         * TypeName : 车贷
         * MaxCreditAmount : 800000
         * Photo : /Storage/CarTypePhoto/936858f4-d007-4826-8b09-69691be7a30c.PNG
         */

        public String Id;
        public String TypeName;
        public int MaxCreditAmount;
        public String Photo;
    }
}
