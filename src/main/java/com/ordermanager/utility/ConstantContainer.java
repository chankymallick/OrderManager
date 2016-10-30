package com.ordermanager.utility;
public class ConstantContainer {
    
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
    ITEMS,USERS,ORDERS
    }
    public enum AUDIT_TYPE{
    INSERT,UPDATE,DELETED
    }
    public static String Property_File_Path_Language_English; 
    public static String Property_File_Path_Language_Bengali; 
    public static String Query_File_Path_Microsoft_SQL_Server;
    public static String Application_Context_File_Path;
    
}
