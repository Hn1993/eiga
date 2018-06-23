package com.eiga.an.model.jsonModel;

import java.util.List;

/**
 * Created by ASUS on 2018/6/22.
 */

public class ApiShowTdReportModel {


    /**
     * ReportContent : {"result_desc":{"ANTIFRAUD":{"error_info":null,"final_score":15,"final_decision":"PASS","risk_items":[{"rule_id":26634554,"score":5,"decision":"Accept","risk_name":"7天内设备或身份证或手机号申请次数过多","risk_detail":[{"type":"frequency_detail","suspect_team_detail_list":null,"description":null,"hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":[{"data":null,"detail":"7天内身份证申请次数：5"}],"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":0,"platform_detail_dimension":null,"platform_detail":null,"court_details":null}]},{"rule_id":26634724,"score":10,"decision":"Accept","risk_name":"7天内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"7天内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634734,"score":0,"decision":"Accept","risk_name":"1个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"1个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634744,"score":0,"decision":"Accept","risk_name":"3个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"3个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]}],"output_fields":null}},"success":true,"reason_code":null,"reason_desc":null,"id":"WF2018062210155818850142","nextService":null,"supplementInfo":null}
     * Status : 1
     * NeedReLogin : false
     * Msg : null
     * Data : null
     */

    public ReportContentBean ReportContent;
    public int Status;
    public boolean NeedReLogin;

    public String Msg;
    public String VaildateCellPhone;
    public String IdentityId;
    public String ReportDate;
    public String RealName;
    public Object Data;

    public static class ReportContentBean {
        /**
         * result_desc : {"ANTIFRAUD":{"error_info":null,"final_score":15,"final_decision":"PASS","risk_items":[{"rule_id":26634554,"score":5,"decision":"Accept","risk_name":"7天内设备或身份证或手机号申请次数过多","risk_detail":[{"type":"frequency_detail","suspect_team_detail_list":null,"description":null,"hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":[{"data":null,"detail":"7天内身份证申请次数：5"}],"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":0,"platform_detail_dimension":null,"platform_detail":null,"court_details":null}]},{"rule_id":26634724,"score":10,"decision":"Accept","risk_name":"7天内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"7天内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634734,"score":0,"decision":"Accept","risk_name":"1个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"1个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634744,"score":0,"decision":"Accept","risk_name":"3个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"3个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]}],"output_fields":null}}
         * success : true
         * reason_code : null
         * reason_desc : null
         * id : WF2018062210155818850142
         * nextService : null
         * supplementInfo : null
         */

        public ResultDescBean result_desc;
        public boolean success;
        public Object reason_code;
        public Object reason_desc;
        public String id;
        public Object nextService;
        public Object supplementInfo;

        public static class ResultDescBean {
            /**
             * ANTIFRAUD : {"error_info":null,"final_score":15,"final_decision":"PASS","risk_items":[{"rule_id":26634554,"score":5,"decision":"Accept","risk_name":"7天内设备或身份证或手机号申请次数过多","risk_detail":[{"type":"frequency_detail","suspect_team_detail_list":null,"description":null,"hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":[{"data":null,"detail":"7天内身份证申请次数：5"}],"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":0,"platform_detail_dimension":null,"platform_detail":null,"court_details":null}]},{"rule_id":26634724,"score":10,"decision":"Accept","risk_name":"7天内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"7天内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634734,"score":0,"decision":"Accept","risk_name":"1个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"1个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634744,"score":0,"decision":"Accept","risk_name":"3个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"3个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]}],"output_fields":null}
             */

            public ANTIFRAUDBean ANTIFRAUD;

            public static class ANTIFRAUDBean {
                /**
                 * error_info : null
                 * final_score : 15
                 * final_decision : PASS
                 * risk_items : [{"rule_id":26634554,"score":5,"decision":"Accept","risk_name":"7天内设备或身份证或手机号申请次数过多","risk_detail":[{"type":"frequency_detail","suspect_team_detail_list":null,"description":null,"hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":[{"data":null,"detail":"7天内身份证申请次数：5"}],"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":0,"platform_detail_dimension":null,"platform_detail":null,"court_details":null}]},{"rule_id":26634724,"score":10,"decision":"Accept","risk_name":"7天内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"7天内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634734,"score":0,"decision":"Accept","risk_name":"1个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"1个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]},{"rule_id":26634744,"score":0,"decision":"Accept","risk_name":"3个月内申请人在多个平台申请借款","risk_detail":[{"type":"platform_detail","suspect_team_detail_list":null,"description":"3个月内申请人在多个平台申请借款","hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":null,"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":2,"platform_detail_dimension":[{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人身份证"},{"count":2,"detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"dimension":"借款人手机"}],"platform_detail":[{"count":1,"industry_display_name":"小额贷款公司"},{"count":1,"industry_display_name":"第三方服务商"}],"court_details":null}]}]
                 * output_fields : null
                 */

                public Object error_info;
                public int final_score;
                public String final_decision;
                public Object output_fields;
                public List<RiskItemsBean> risk_items;

                public static class RiskItemsBean {
                    /**
                     * rule_id : 26634554
                     * score : 5
                     * decision : Accept
                     * risk_name : 7天内设备或身份证或手机号申请次数过多
                     * risk_detail : [{"type":"frequency_detail","suspect_team_detail_list":null,"description":null,"hit_type_display_name":null,"fraud_type_display_name":null,"grey_list_details":null,"fuzzy_list_details":null,"frequency_detail_list":[{"data":null,"detail":"7天内身份证申请次数：5"}],"cross_frequency_detail_list":null,"cross_event_detail_list":null,"discredit_times":0,"overdue_details":null,"high_risk_areas":null,"hit_list_datas":null,"platform_count":0,"platform_detail_dimension":null,"platform_detail":null,"court_details":null}]
                     */

                    public int rule_id;
                    public int score;
                    public String decision;
                    public String risk_name;
                    public List<RiskDetailBean> risk_detail;

                    public static class RiskDetailBean {
                        /**
                         * type : frequency_detail
                         * suspect_team_detail_list : null
                         * description : null
                         * hit_type_display_name : null
                         * fraud_type_display_name : null
                         * grey_list_details : null
                         * fuzzy_list_details : null
                         * frequency_detail_list : [{"data":null,"detail":"7天内身份证申请次数：5"}]
                         * cross_frequency_detail_list : null
                         * cross_event_detail_list : null
                         * discredit_times : 0
                         * overdue_details : null
                         * high_risk_areas : null
                         * hit_list_datas : null
                         * platform_count : 0
                         * platform_detail_dimension : null
                         * platform_detail : null
                         * court_details : null
                         */

                        public String type;
                        public Object suspect_team_detail_list;
                        public Object description;
                        public Object hit_type_display_name;
                        public Object fraud_type_display_name;
                        public Object grey_list_details;
                        public Object fuzzy_list_details;
                        public Object cross_frequency_detail_list;
                        public Object cross_event_detail_list;
                        public int discredit_times;
                        public Object overdue_details;
                        public Object high_risk_areas;
                        public Object hit_list_datas;
                        public int platform_count;
                        public Object platform_detail_dimension;
                        public Object platform_detail;
                        public Object court_details;
                        public List<FrequencyDetailListBean> frequency_detail_list;

                        public static class FrequencyDetailListBean {
                            /**
                             * data : null
                             * detail : 7天内身份证申请次数：5
                             */

                            public Object data;
                            public String detail;
                        }
                    }
                }
            }
        }
    }
}
