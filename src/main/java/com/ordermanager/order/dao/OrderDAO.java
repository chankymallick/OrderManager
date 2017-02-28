package com.ordermanager.order.dao;

import com.ordermanager.messanger.sendSMS;
import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author Maliick
 */
@Transactional
public class OrderDAO extends DAOHelper {

    public String addItem(JSONObject paramJson) {
        ResponseJSONHandler responseJSON = new ResponseJSONHandler();
        try {
            int UIDvalue = this.getColumnAutoIncrementValue("ITEMS", "ITEM_UID");
            paramJson.put("ITEM_UID=NUM", UIDvalue);
            String InsertQuery = this.getSimpleSQLInsert(paramJson, "ITEMS");
            this.getJdbcTemplate().update(InsertQuery);
            this.generateSQLSuccessResponse(responseJSON, "Item Succesfully Saved");
            this.auditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.ITEMS);
        } catch (Exception ex) {
            this.generateSQLExceptionResponse(responseJSON, ex, "Failed to Save Item Data");
        }
        return responseJSON.getJSONResponse();
    }

    public Map<String, Object> getItemSelectionList(String ITEM_TYPE) {
        String DISTINCT_ITEM_SUB_TYPE = "SELECT DISTINCT ITEM_SUB_TYPE FROM ITEMS WHERE ACTIVE=1 AND ITEM_TYPE='EXTRA' AND  PARENT_ITEM= ? ORDER BY ITEM_SUB_TYPE ASC";
        String SQL = "SELECT ITEM_SUB_TYPE,ITEM_NAME FROM ITEMS WHERE ACTIVE=1 AND ITEM_TYPE='EXTRA' AND  PARENT_ITEM= ? AND ITEM_SUB_TYPE=?";
        ResultSet distinctRst = null;
        ResultSet rst = null;
        Map<String, Object> map = new HashMap();
        ArrayList<String> Keys = new ArrayList();
        try {
            PreparedStatement pst = this.getJDBCConnection().prepareStatement(SQL);
            PreparedStatement distinctPst = this.getJDBCConnection().prepareStatement(DISTINCT_ITEM_SUB_TYPE);
            distinctPst.setString(1, ITEM_TYPE);
            distinctRst = distinctPst.executeQuery();
            while (distinctRst.next()) {
                ArrayList<String> ItemNameList = new ArrayList<String>();
                String SUB_ITEM = distinctRst.getString("ITEM_SUB_TYPE");
                pst.setString(1, ITEM_TYPE);
                pst.setString(2, SUB_ITEM);
                rst = pst.executeQuery();
                while (rst.next()) {
                    ItemNameList.add(rst.getString("ITEM_NAME"));
                }
                map.put(SUB_ITEM, ItemNameList);
                Keys.add(SUB_ITEM);
            }
            map.put("Key_Name", Keys);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public String addNewOrder(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            boolean isCustomRateActive = (paramData.getInt("CUSTOM_RATE=NUM") != 0);
            int UID = getColumnAutoIncrementValue("ORDERS", "ORDER_UID");
            int Advance = paramData.getInt("ADVANCE=NUM");
            String MainItemName = paramData.get("ORDER_TYPE=STR").toString();
            String OrderStatus = paramData.get("ORDER_STATUS=STR").toString();
            String OrdersSubStatus = paramData.get("ORDER_SUB_STATUS=STR").toString();
            String CurrentLocation = paramData.get("CURRENT_LOCATION=STR").toString();
            int MasterPrice = 0;
            int TailorPrice = 0;
            JSONObject CustomPrice = null;
            JSONArray ItemNameArray = new JSONArray();
            if (isCustomRateActive) {
                CustomPrice = (JSONObject) paramData.get("ITEM_DATA");
                MasterPrice = CustomPrice.getInt("MASTER_RATE=STR");
                TailorPrice = CustomPrice.getInt("TAILOR_RATE=STR");
            } else {
                ItemNameArray = (JSONArray) paramData.get("ITEM_DATA");
            }
            paramData.remove("CUSTOM_RATE=NUM");
            paramData.remove("ITEM_DATA");
            paramData.remove("VERIFY_BILL_NO=STR");
            paramData.remove("ADVANCE=NUM");
            paramData.remove("ORDER_STATUS=STR");
            paramData.remove("CURRENT_LOCATION=STR");
            paramData.remove("ORDER_SUB_STATUS=STR");
            paramData.put("ORDER_UID=NUM", UID);
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ORDERS"));
            int PaymentUID = this.getColumnAutoIncrementValue("PAYMENT_TRANSACTIONS", "TRANSACTION_UID");
            int PayemtInsert = getJdbcTemplate().update("iNSERT INTO PAYMENT_TRANSACTIONS VALUES (?,?,?,?)", new Object[]{PaymentUID, paramData.get("BILL_NO=STR"), ConstantContainer.PAYMENT_TYPE.ADVANCE.toString(), Advance});
            int Order_Item_Uid_For_Main_Item = this.getColumnAutoIncrementValue("ORDER_ITEMS", "ORDER_ITEMS_UID");
            int mainItem = getJdbcTemplate().update("INSERT INTO ORDER_ITEMS VALUES (?,?,?,?)", new Object[]{Order_Item_Uid_For_Main_Item, paramData.get("BILL_NO=STR"), MainItemName, ""});
            int OrderStatusLocationInsert = orderMobiltyUpdate(
                    paramData.get("BILL_NO=STR").toString(),
                    getParsedTimeStamp((String) paramData.get("ORDER_DATE=DATE")).toString(),
                    OrderStatus,//Need to Be reviewed for assigning next stage statuses
                    OrdersSubStatus,//Need to Be reviewed for assigning next stage statuses
                    CurrentLocation,
                    "ADDED WITH NEW ORDER ENTRY");
            if (isCustomRateActive) {

            } else if (ItemNameArray.length() > 0) {
                List<Object[]> ItemParams = new ArrayList();
                int Order_Item_Uid = this.getColumnAutoIncrementValue("ORDER_ITEMS", "ORDER_ITEMS_UID");
                for (int i = 0; i < ItemNameArray.length(); i++) {
                    ItemParams.add(new Object[]{Order_Item_Uid, paramData.get("BILL_NO=STR"), ItemNameArray.get(i), ""});
                    Order_Item_Uid++;
                }
                int totalInsert[] = getJdbcTemplate().batchUpdate("INSERT INTO ORDER_ITEMS VALUES (?,?,?,?)", ItemParams);
            }
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.ORDERS, UID, "Bill No :" + paramData.getString("BILL_NO=STR"));
            generateSQLSuccessResponse(response, paramData.get("BILL_NO=STR") + " - added Succesfully");
            this.getTransactionManager().commit(txStatus);
            //       new sendSMS().sendSms( paramData.get("BILL_NO=STR").toString(),  paramData.get("CONTACT_NO=STR").toString(),paramData.get("CUSTOMER_NAME=STR").toString());
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String addNewQuickOrder(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int UID = getColumnAutoIncrementValue("ORDERS", "ORDER_UID");
            int PaymentUID = this.getColumnAutoIncrementValue("PAYMENT_TRANSACTIONS", "TRANSACTION_UID");
            int Advance = paramData.getInt("ADVANCE=NUM");
            int Price = paramData.getInt("PRICE=NUM");
            paramData.remove("ADVANCE=NUM");
            paramData.put("ORDER_UID=NUM", UID);
            paramData.put("QUANTITY=NUM", 1);
            paramData.put("LOCATION=STR", LOCATION.BAGNAN.toString());
            paramData.put("ORDER_DATE=DATE_AUTO", this.getCurrentTimeStamp().toString());
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ORDERS"));
            int PayemtInsert = getJdbcTemplate().update("iNSERT INTO PAYMENT_TRANSACTIONS VALUES (?,?,?,?)", new Object[]{PaymentUID, paramData.get("BILL_NO=STR"), ConstantContainer.PAYMENT_TYPE.ADVANCE.toString(), Advance});
            int OrderStatusLocationInsert = orderMobiltyUpdate(paramData.get("BILL_NO=STR").toString(), paramData.get("ORDER_DATE=DATE_AUTO").toString(),
                    ConstantContainer.ORDER_MAIN_STATUS.NEW_ORDER.toString(),
                    ConstantContainer.ORDER_MAIN_STATUS.NEW_ORDER.toString(),
                    ConstantContainer.CURRENT_LOCATIONS.BILL_BOOKS.toString(),
                    "ADDED WITH QUICK ORDER ENTRY");
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.QUICK_ORDERS, UID, "Bill No :" + paramData.getString("BILL_NO=STR"));
            generateSQLSuccessResponse(response, paramData.get("BILL_NO=STR") + " - added Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String addNewStatusType(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int UID = getColumnAutoIncrementValue("ORDER_STATUS_TYPES", "STATUS_TYPE_UID");
            paramData.put("STATUS_TYPE_UID=NUM", UID);
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ORDER_STATUS_TYPES"));
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.ORDER_STATUS_TYPES, UID, "New Status Inserted");
            generateSQLSuccessResponse(response, paramData.get("STATUS_NAME=STR") + " - added Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String addNewLocation(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int UID = getColumnAutoIncrementValue("CURRENT_LOCATIONS", "CURRENT_LOCATIONS_UID");
            paramData.put("CURRENT_LOCATIONS_UID=NUM", UID);
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "CURRENT_LOCATIONS"));
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.CURRENT_LOCATIONS, UID, "New Location Inserted");
            generateSQLSuccessResponse(response, paramData.get("LOCATION_NAME=STR") + " - added Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String updateNewOrder(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            String BillNo = paramData.getString("BILL_NO=STR");
            String OrderStatus = paramData.get("ORDER_STATUS=STR").toString();
            String OrdersSubStatus = paramData.get("ORDER_SUB_STATUS=STR").toString();
            String CurrentLocation = paramData.get("CURRENT_LOCATION=STR").toString();
            int UpdateStatus = this.getJdbcTemplate().update("UPDATE ORDERS SET "
                    + "DELIVERY_DATE=?,"
                    + "CUSTOMER_NAME=?,"
                    + "CONTACT_NO=?,"
                    + "ORDER_TYPE=?,"
                    + "PRODUCT_TYPE=?,"
                    + "QUANTITY=?,"
                    + "MEASURED_BY=?,"
                    + "NOTE=?,"
                    + "PIECE_VENDOR=? "
                    + " WHERE BILL_NO =?",
                    new Object[]{
                        getParsedTimeStamp(paramData.get("DELIVERY_DATE=DATE").toString()),
                        paramData.get("CUSTOMER_NAME=STR"),
                        paramData.get("CONTACT_NO=STR"),
                        paramData.get("ORDER_TYPE=STR"),
                        paramData.get("PRODUCT_TYPE=STR"),
                        paramData.get("QUANTITY=NUM"),
                        paramData.get("MEASURED_BY=STR"),
                        paramData.get("NOTE=STR"),
                        paramData.get("PIECE_VENDOR=STR"),
                        BillNo
                    });
            int OrderStatusLocationInsert = orderMobiltyUpdate(
                    paramData.get("BILL_NO=STR").toString(),
                    paramData.get("ORDER_DATE=DATE").toString(),
                    OrderStatus,
                    OrdersSubStatus,
                    CurrentLocation,
                    "ADDED WITH UPDATE ORDER ENTRY");
            mainAuditor(ConstantContainer.AUDIT_TYPE.UPDATE, ConstantContainer.APP_MODULE.ORDERS, Integer.parseInt(getDistinctDataFromDatabase("ORDERS", "ORDER_UID", "BILL_NO", BillNo)), paramData.toString());
            generateSQLSuccessResponse(response, paramData.get("BILL_NO=STR") + " - Updated Succesfully");
            this.getTransactionManager().commit(txStatus);
            //new sendSMS().sendSms( paramData.get("BILL_NO=STR").toString(),  paramData.get("CONTACT_NO=STR").toString(),paramData.get("CUSTOMER_NAME=STR").toString());
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String addAdvance(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            String OrderBillNo = paramData.getString("BILL_NO");
            int Advance = Integer.parseInt(paramData.getString("ADVANCE_AMOUNT=NUM"));
            String MainStatus = paramData.getString("ORDER_STATUS=STR");
            String SubStatus = paramData.getString("ORDER_SUB_STATUS=STR");
            String DeliveryDate = paramData.getString("DELIVERY_DATE=DATE");
            String Location = paramData.getString("CURRENT_LOCATION=STR");
            int PaymentUID = this.getColumnAutoIncrementValue("PAYMENT_TRANSACTIONS", "TRANSACTION_UID");
            int PayemtInsert = getJdbcTemplate().update("iNSERT INTO PAYMENT_TRANSACTIONS (TRANSACTION_UID,ORDER_BILL_NO,PAYMENT_TYPE,AMOUNT) VALUES (?,?,?,?)", new Object[]{PaymentUID, OrderBillNo, ConstantContainer.PAYMENT_TYPE.RE_ADVANCE.toString(), Advance});
            int mobilityUpdate = this.orderMobiltyUpdate(OrderBillNo, this.getCurrentTimeStamp().toString(), MainStatus, SubStatus, Location, "Re Advance");
            mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.PAYMENT_TRANSACTION, PaymentUID, "RE ADVAMCE FOR BILL NO : " + OrderBillNo + ", Advance :" + Integer.toString(Advance));
            this.getTransactionManager().commit(txStatus);
            generateSQLSuccessResponse(response, paramData.get("BILL_NO") + " - Advance Succesfully done");
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }

        return response.getJSONResponse();
    }

    public List<Object> getGridData(String TableName, String Order_Column) {
        String SQL = "SELECT *FROM " + TableName + " ORDER BY " + Order_Column + " DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForOrders() {
        String SQL = "SELECT TOP 50 BILL_NO,ORDER_DATE,DELIVERY_DATE,'NEW ORDER' AS ORDER_STATUS,PIECE_VENDOR,PRICE,ORDER_TYPE, PRODUCT_TYPE FROM ORDERS ORDER BY ORDER_UID DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForQuickOrders() {
        String SQL = "SELECT TOP 50 BILL_NO,ORDER_DATE,PRICE ,AMOUNT FROM ORDERS OD INNER JOIN PAYMENT_TRANSACTIONS PT ON OD.BILL_NO=PT.ORDER_BILL_NO ORDER BY PT.TRANSACTION_UID DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForStatusTypes() {
        String SQL = "SELECT STATUS_TYPE_UID,STATUS_TYPE,STATUS_NAME,STATUS_PARENT_NAME,STATUS_ORDER,NOTE,ACTIVE FROM ORDER_STATUS_TYPES ORDER BY STATUS_TYPE_UID DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForNewLocation() {
        String SQL = "SELECT CURRENT_LOCATIONS_UID,LOCATION_NAME,PARENT_STATUS,NOTE,ACTIVE FROM CURRENT_LOCATIONS  ORDER BY CURRENT_LOCATIONS_UID DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForQuickOrdersWithDate(String ReportDate) {
        try {
            Timestamp od = this.getParsedTimeStamp(ReportDate);
            String SQL = "SELECT TOP 50 BILL_NO,ORDER_DATE,PRICE ,AMOUNT FROM ORDERS OD INNER JOIN PAYMENT_TRANSACTIONS PT ON OD.BILL_NO=PT.ORDER_BILL_NO AND OD.ORDER_DATE > = DateAdd(Day, DateDiff(Day, 0, '" + od.toString() + "'), 0) AND OD.ORDER_DATE <  DateAdd(Day, DateDiff(Day,0,  '" + od.toString() + "'), 1) ORDER BY PT.TRANSACTION_UID DESC";
            return this.getJSONDataForGrid(SQL);
        } catch (Exception e) {
            return null;
        }

    }

    public List<String[]> getAdvanceStatistics(String orderDate, String StoreLocation) {
        ArrayList<String[]> statList = new ArrayList();
        try {
            Timestamp od = this.getParsedTimeStamp(orderDate);
            String OrderDate = this.getDatePartOfTimestamp(od.toString());
            String Expense = "0";
            String TotalBill = this.getJdbcTemplate().queryForObject("SELECT COUNT(BILL_NO) FROM ORDERS WHERE LOCATION = ? AND  ORDER_DATE >= ? AND ORDER_DATE <  DATEADD(DAY,1,?)", new Object[]{StoreLocation, OrderDate, OrderDate}, String.class);
            String TotalOrder = this.getJdbcTemplate().queryForObject("SELECT SUM(QUANTITY) AS TOTAL_PIECE FROM ORDERS WHERE LOCATION = ? AND  ORDER_DATE >= ? AND ORDER_DATE <  DATEADD(DAY,1,?)", new Object[]{StoreLocation, OrderDate, OrderDate}, String.class);
            String TotalAdvance = this.getJdbcTemplate().queryForObject("SELECT SUM(PM.AMOUNT) FROM PAYMENT_TRANSACTIONS PM INNER JOIN ORDERS ODR  ON ODR.BILL_NO = PM.ORDER_BILL_NO WHERE ODR.LOCATION =? AND PM.PAYMENT_TYPE IN('ADVANCE','RE_ADVANCE','PIECE_SALE')  AND ODR.ORDER_DATE >=? AND ODR.ORDER_DATE <  DATEADD(DAY,1,?)", new Object[]{StoreLocation, OrderDate, OrderDate}, String.class);
            String TotalSale = this.getJdbcTemplate().queryForObject("SELECT SUM(ODR.PRICE) FROM PAYMENT_TRANSACTIONS PM INNER JOIN ORDERS ODR  ON ODR.BILL_NO = PM.ORDER_BILL_NO WHERE ODR.LOCATION = ? AND PM.PAYMENT_TYPE IN('ADVANCE','RE_ADVANCE','PIECE_SALE')  AND ODR.ORDER_DATE >= ? AND ODR.ORDER_DATE <  DATEADD(DAY,1,?)", new Object[]{StoreLocation, OrderDate, OrderDate}, String.class);

            TotalBill = (TotalBill == null) ? "0" : TotalBill;
            TotalOrder = (TotalOrder == null) ? "0" : TotalOrder;
            TotalAdvance = (TotalAdvance == null) ? "0" : TotalAdvance;
            TotalSale = (TotalSale == null) ? "0" : TotalSale;

            statList.add(new String[]{STATISTICS_TYPE.LARGE.toString(), "TOTAL ADVANCE", TotalAdvance});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "NO OF PIECE", TotalOrder});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "BILL FINISHED", TotalBill});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "SALE AMOUNT", TotalSale});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "EXPENSES", Expense});
        } catch (Exception e) {
            return statList;
        }
        return statList;
    }

    public String getOrderDetails(JSONObject params) {
        
        ResponseJSONHandler response = new ResponseJSONHandler();      
        PreparedStatement pst = null;
        ResultSet rst = null;
        try {
            String OrderBillNo = params.getString("BILL_NO=STR");
            String SQL = "SELECT BILL_NO,ORDER_DATE,DELIVERY_DATE,QUANTITY,(select datename(dw,DELIVERY_DATE)) AS DAY_NAME,(SELECT  DATEDIFF(day, CURRENT_TIMESTAMP  , DELIVERY_DATE)) AS REMAINING FROM ORDERS WHERE BILL_NO = ?";
            pst = this.getJDBCConnection().prepareStatement(SQL);
            pst.setString(1, OrderBillNo);
            rst = pst.executeQuery();
            if (rst.next()) {
                PreparedStatement pst2 = this.getJDBCConnection().prepareStatement("SELECT DISTINCT ITEM_NAME FROM ORDER_ITEMS WHERE BILL_NO=?");
                pst2.setString(1, OrderBillNo);
                ResultSet rst2 = pst2.executeQuery();
                StringBuilder data = new StringBuilder();
                data.append(rst.getString("BILL_NO")).
                        append(",").
                        append(this.getCustomFormatDate(rst.getTimestamp("ORDER_DATE"))).
                        append(",").
                        append(this.getCustomFormatDate(rst.getTimestamp("DELIVERY_DATE"))).
                        append(",").
                        append(rst.getString("QUANTITY")).
                        append(",").
                        append(rst.getString("DAY_NAME")).
                        append(",").
                        append(rst.getString("REMAINING")).
                        append(",").
                        append("").
                        append(",");                        
                        while(rst2.next()){
                        data.append("<img style='cursor:pointer;display:inline;' height='20px' width='20px' src='resources/Images/"+rst2.getString("ITEM_NAME")+".png'/>");
                        }   
                        data.append("").
                        append(",").
                        append(",").
                        append("<img height='20px' width='20px' src='resources/Images/cancel_order.png'/>").
                        append(",").
                        append("<img height='20px' width='20px' src='resources/Images/task.png'/>");
                response.addResponseValue("DATA", data.toString());
                this.generateSQLSuccessResponse(response, "Bill no sucesfully added to list");
            } else {
                this.generateSQLExceptionResponse(response, new Exception("Bill not found"), "Bill no not Exist");
            } 
        } catch (Exception e) {
            this.generateSQLExceptionResponse(response, e, "Exception getorder Details");
        }
        
        return response.getJSONResponse();
    }
     public String orderAssignmentMasterTailor(JSONObject jsonParams, String UserName) {         
        ResponseJSONHandler response = new ResponseJSONHandler();        
        try {            
            String MasterName = jsonParams.getString("MASTER_NAME=STR");
            String TailorName = jsonParams.getString("TAILOR_NAME=STR");      
            String CurrentLocation = jsonParams.getString("LOCATION=STR");
            String Date = getParsedTimeStamp(jsonParams.getString("ASSIGNMENT_DATE=DATE")).toString();
            JSONArray List_Of_Orders = jsonParams.getJSONArray("ALL_BILL_NO");
            Map<String, String> assignmentStatusMap = new HashMap();
            int SuccessCount = 0;
            int TotalBills = List_Of_Orders.length();

            for (int i = 0; i < TotalBills; i++) {
                String BillNo = List_Of_Orders.getString(i);
                String AssignmentStatus = this.addMasterTailorAssignment(BillNo, MasterName, TailorName, Date,CurrentLocation);
                SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount+1 : SuccessCount;
                assignmentStatusMap.put(BillNo, AssignmentStatus);
            }
            response.setResponse_Value(new JSONObject(assignmentStatusMap));           
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully assigned.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");            
        }

        return response.getJSONResponse();

    }

    public String addMasterTailorAssignment(String BillNo, String MasterName, String TailorName, String Date,String CurrentLocation) throws Exception {     
            TransactionDefinition txDef = new DefaultTransactionDefinition();
            TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
            try{
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
                    + "NOTE) VALUES (?,?,?,?,?,?,?,?)", 
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
                    + "NOTE) VALUES (?,?,?,?,?,?,?,?)", 
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
            this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.CUTTING_IN_PROGRESS.toString(), CurrentLocation, "Assigned with BulkMasterTailorEntry to : "+MasterName);
            this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.STICHING_IN_PROGRESS.toString(), CurrentLocation, "Assigned with BulkMasterTailorEntry to : "+TailorName);
            mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID, "Assigned with BulkMasterTailorEntry to "+MasterName+"/"+TailorName);
             this.getTransactionManager().commit(txStatus);
            return "SUCCES,DATAUPDATED";
            } 
            }catch(Exception e){
            this.getTransactionManager().rollback(txStatus);
            return "FAILED,"+e.getMessage();
            }
    }

}
