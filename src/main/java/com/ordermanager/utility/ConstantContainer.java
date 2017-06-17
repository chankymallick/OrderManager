package com.ordermanager.utility;
public class ConstantContainer {
    
    public enum PAYMENT_TYPE{
    ADVANCE,RE_ADVANCE,DELIVERY_PAYMENT
    }
    public enum ResponseJSONKeys{
    RESPONSE_TYPE, RESPONSE_STATUS, RESPONSE_MESSAGE,RESPONSE_VALUE
    }
    public enum LANGUAGES{
    ENGLISH,BENGALI
    }
    enum ResponseJSONTypes{
    
    }
    public enum ResponseJSONStatus{
    SUCCESS,FAILED   
    }
    public enum APP_MODULE{
    ITEMS,USERS,ORDERS,QUICK_ORDERS,ORDER_STATUS_TYPES,CURRENT_LOCATIONS,PAYMENT_TRANSACTION,EMPLOYEES,ORDER_ASSIGNMENTS,ACCOUNTS
    }
    public enum AUDIT_TYPE{
    INSERT,UPDATE,DELETED
    }
    public enum LOCATION{
    BAGNAN,MECHEDA
    }
    public enum STATISTICS_TYPE{
    LARGE,MEDIUM,SMALL
    }
    public enum ORDER_MAIN_STATUS{
    NEW_ORDER,IN_PROCESS,READY_TO_DELIVER,DELIVERY_COMPLETED
    }
    public enum ORDER_SUB_STATUS{
    PROBLEM_HALT,REQUIREMENT_HALT,ADVANCE_LOW,NEW_ORDER,CUTTING_IN_PROGRESS,CUTTING_COMPLETED,STICHING_IN_PROGRESS,STICHING_COMPLETED,FINISHING_IN_PROGRESS,FINISHING_COMPLETED,IRON_IN_PROGRESS,IRON_COMPLETED
    }
    public enum ORDER_STATUS_TYPE{
    MAIN_STATUS,SUB_STATUS
    }
    public enum CURRENT_LOCATIONS{
    SHOP_NO_1,SHOP_NO_2, SHOP_NO_3, LOW_ADVANCE_RACK, REQUIREMENT_RACK, WORKSHOP, DELIVERY_RACKS,BILL_BOOKS
    }
    public enum ACCOUNTS_MODULE{
    EMPLOYEE_PAYMENT    
    }
    public enum ACCOUNTS_TRANSACTION_TYPE{
    OUTWARD,INWARD
    }
    public enum  EMPLOYEE_PAYMENT_TYPES{
    EXPENSE_ADVANCE,PAYMENT,LOAN_ISSUE,LOAN_REPAID,ALTERS,OTHERS
    }
    public enum ASSIGNMENTS_TYPES{
    TO_MASTER,TO_TAILOR,TO_FINISHER,TO_IRON
    }
    public static String Property_File_Path_Language_English; 
    public static String Property_File_Path_Language_Bengali; 
    public static String Query_File_Path_Microsoft_SQL_Server;
    public static String Application_Context_File_Path;
    public static String WEB_INF_PATH;
    
}
