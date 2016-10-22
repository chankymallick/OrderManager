package com.ordermanager.users.dao;

import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
import org.json.JSONObject;


public class UserDAO extends DAOHelper {
    
    public String addUser(JSONObject userdata,String CurrentUser){
        ResponseJSONHandler rsj = new ResponseJSONHandler();
        try {
           int UID = this.getColumnAutoIncrementValue("USERS", "USER_UID");
           userdata.put("USER_UID=NUM",UID);
           userdata.remove("REPASSWORD=STR");
           String SQL =  this.getSimpleSQLInsert(userdata,"USERS");           
           int insertStatus = this.getJdbcTemplate().update(SQL);
           this.generateSQLSuccessResponse(rsj, "New user succesfully added");           
           this.auditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.USERS, "","");
        } catch (Exception e) {
            this.generateSQLExceptionResponse(rsj, e, "Exception occured see Logs..");
        }
        return rsj.getJSONResponse();
    }
}
