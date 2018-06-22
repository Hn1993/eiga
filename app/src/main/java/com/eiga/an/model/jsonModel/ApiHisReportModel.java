package com.eiga.an.model.jsonModel;

import java.util.List;

/**
 * Created by ASUS on 2018/6/22.
 */

public class ApiHisReportModel {

    /**
     * ReportList : [{"ReportId":"WF2018062210155818850142","ReportDate":"2018-06-22T10:15:44.12"}]
     * Status : 2
     * NeedReLogin : false
     * Msg : null
     * Data : null
     */

    public int Status;
    public boolean NeedReLogin;
    public Object Msg;
    public Object Data;
    public List<ReportListBean> ReportList;

    public static class ReportListBean {
        /**
         * ReportId : WF2018062210155818850142
         * ReportDate : 2018-06-22T10:15:44.12
         */

        public String ReportId;
        public String ReportDate;
        public String CellPhone;
        public String RealName;

    }
}
