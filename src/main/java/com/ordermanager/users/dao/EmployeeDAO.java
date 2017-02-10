/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.users.dao;

import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
import org.json.JSONObject;

/**
 *
 * @author Maliick
 */
public class EmployeeDAO extends DAOHelper {

    public String addNewEmployee(JSONObject paramJson, String CurrentUser) {
        ResponseJSONHandler rsj = new ResponseJSONHandler();
        try {
            int UID = this.getColumnAutoIncrementValue("EMPLOYEES", "EMPLOYEE_UID");
            paramJson.put("EMPLOYEE_UID=NUM", UID);
            String SQL = this.getSimpleSQLInsert(paramJson, "EMPLOYEES");
            int insertStatus = this.getJdbcTemplate().update(SQL);
            this.generateSQLSuccessResponse(rsj, "New employee succesfully added");
            this.mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.EMPLOYEES,UID,"EMPLOYEE ENLISTED");
        } catch (Exception e) {
            this.generateSQLExceptionResponse(rsj, e, "Exception occured see Logs..");
        }
        return rsj.getJSONResponse();

    }
}
