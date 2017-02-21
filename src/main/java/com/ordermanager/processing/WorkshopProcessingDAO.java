/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.processing;

import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.CORBA.portable.ResponseHandler;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author Maliick
 */
public class WorkshopProcessingDAO extends DAOHelper {

    public String orderAssignmentMasterTailor(JSONObject jsonParams, String UserName) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        try {
            String MasterName = jsonParams.getString("MASTER_NAME=STR");
            String TailorName = jsonParams.getString("TAILOR_NAME");
            String Date = jsonParams.getString("ASSIGNMENT_DATE=DATE");
            JSONArray List_Of_Orders = jsonParams.getJSONArray("ALL_BILL_NO");
            Map<String, String> assignmentStatusMap = new HashMap();
            int SuccessCount = 0;
            int TotalBills = List_Of_Orders.length();

            for (int i = 0; i < TotalBills; i++) {
                String BillNo = List_Of_Orders.getString(i);
                String AssignmentStatus = this.addMasterTailorAssignment(BillNo, MasterName, TailorName, Date);
                SuccessCount = (AssignmentStatus.equals("SUCCES")) ? SuccessCount++ : SuccessCount;
                assignmentStatusMap.put(BillNo, AssignmentStatus);
            }
            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully assigned.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }

        return response.getJSONResponse();

    }

    public String addMasterTailorAssignment(String BillNo, String MasterName, String TailorName, String Date) {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if(isOrderCuttingInProgress(BillNo) || isOrderStichingInProgress(BillNo)){
            return "FAILED,Order already assigned to Master / Tailor, change the assignment ";
            }
            else{
            int AsssignMentUID  =  this.getColumnAutoIncrementValue("ORDER_ASSIGNMENTS", "ASSIGNMENT_UID");
            int MasterInsertStatus  = this.getJdbcTemplate().update("INSERT INTO"
                    + " ORDER_ASSIGNMENTS ("
                    + "ASSIGNMENT_UID,"
                    + "BILL_NO,"
                    + "ASSIGNMENT_DATE,"
                    + "ASSIGNMENT_TYPE,"
                    + "EMPLOYEE_NAME,"
                    + "WAGE_AMOUNT,"
                    + "WAGE_STATUS,"                    
                    + "NOTE) VALUES (?,?,?,?,?,?,?,?,?)", 
                    new Object[]{
                        AsssignMentUID,
                        BillNo,
                        Date,
                        "TO_MASTER",
                        MasterName,
                        0,
                        "UNPAID",
                        ""                        
                    });
            int TailorInsertStatus  = this.getJdbcTemplate().update("INSERT INTO"
                    + " ORDER_ASSIGNMENTS ("
                    + "ASSIGNMENT_UID,"
                    + "BILL_NO,"
                    + "ASSIGNMENT_DATE,"
                    + "ASSIGNMENT_TYPE,"
                    + "EMPLOYEE_NAME,"
                    + "WAGE_AMOUNT,"
                    + "WAGE_STATUS,"                    
                    + "NOTE) VALUES (?,?,?,?,?,?,?,?,?)", 
                    new Object[]{
                        AsssignMentUID,
                        BillNo,
                        Date,
                        "TO_TAILOR",
                        TailorName,
                        0,
                        "UNPAID",
                        ""                        
                    });      
            this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.CUTTING_IN_PROGRESS.toString(), ConstantContainer.CURRENT_LOCATIONS.WORKSHOP.toString(), "Assigned with BulkMasterTailorEntry to"+MasterName);
            this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.STICHING_IN_PROGRESS.toString(), ConstantContainer.CURRENT_LOCATIONS.WORKSHOP.toString(), "Assigned with BulkMasterTailorEntry to"+TailorName);
            mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID, "Assigned with BulkMasterTailorEntry to"+MasterName+"/"+TailorName);
            this.getTransactionManager().commit(txStatus);
            return "SUCCES,DATAUPDATED";
            }
            
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
        }
        return "FAILED,APPLICATION ERROR ";
    }

}
