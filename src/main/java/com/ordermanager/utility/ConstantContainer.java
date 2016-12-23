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
    ITEMS,USERS,ORDERS,QUICK_ORDERS,ORDER_STATUS_TYPES,CURRENT_LOCATIONS
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
    PROBLEM_HALT,REQUIREMENT_HALT,ADVANCE_LOW
    }
    public enum ORDER_STATUS_TYPE{
    MAIN_STATUS,SUB_STATUS
    }
    public enum CURRENT_LOCATIONS{
    SHOP_NO_1,SHOP_NO_2, SHOP_NO_3, LOW_ADVANCE_RACK, REQUIREMENT_RACK, WORKSHOP, DELIVERY_RACKS,BILL_BOOKS
    }
    public static String Property_File_Path_Language_English; 
    public static String Property_File_Path_Language_Bengali; 
    public static String Query_File_Path_Microsoft_SQL_Server;
    public static String Application_Context_File_Path;
    
}
