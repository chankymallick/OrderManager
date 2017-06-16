package com.ordermanager.order.dao;

import com.ordermanager.messanger.sendSMS;
import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement distinctPst = null;
        Map<String, Object> map = new HashMap();
        ArrayList<String> Keys = new ArrayList();
        try {
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            distinctPst = this.getJDBCConnection().prepareStatement(DISTINCT_ITEM_SUB_TYPE);
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
        } finally {
            try {
                distinctRst.close();
                rst.close();

                pst.close();
                distinctPst.close();
                con.close();
            } catch (Exception e) {
            }
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
        Connection con = null;
        ResultSet rst = null;
        ResultSet rst2 = null;
        try {
            String OrderBillNo = params.getString("BILL_NO=STR");
            String SQL = "SELECT BILL_NO,ORDER_DATE,DELIVERY_DATE,QUANTITY,(select datename(dw,DELIVERY_DATE)) AS DAY_NAME,(SELECT  DATEDIFF(day, CURRENT_TIMESTAMP  , DELIVERY_DATE)) AS REMAINING FROM ORDERS WHERE BILL_NO = ?";
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, OrderBillNo);
            rst = pst.executeQuery();
            if (rst.next()) {
                PreparedStatement pst2 = this.getJDBCConnection().prepareStatement("SELECT DISTINCT ITEM_NAME FROM ORDER_ITEMS WHERE BILL_NO=?");
                pst2.setString(1, OrderBillNo);
                rst2 = pst2.executeQuery();
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
                while (rst2.next()) {
                    data.append("<img style='cursor:pointer;display:inline;' height='20px' width='20px' src='resources/Images/" + rst2.getString("ITEM_NAME") + ".png'/>");
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
        } finally {
            try {
                rst.close();
                rst2.close();
                pst.close();
                con.close();
            } catch (Exception e) {
            }

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
                String AssignmentStatus = this.addMasterTailorAssignment(BillNo, MasterName, TailorName, Date, CurrentLocation);
                SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                assignmentStatusMap.put(BillNo, AssignmentStatus);
            }
            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully assigned.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }

        return response.getJSONResponse();

    }

    public String addMasterTailorAssignment(String BillNo, String MasterName, String TailorName, String Date, String CurrentLocation) throws Exception {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if (isOrderCuttingInProgress(BillNo) || isOrderStichingInProgress(BillNo)) {
                return "FAILED,Order already assigned to Master / Tailor, change the assignment ";
            } else {
                int AsssignMentUID = this.getColumnAutoIncrementValue("ORDER_ASSIGNMENTS", "ASSIGNMENT_UID");
                int MasterInsertStatus = this.getJdbcTemplate().update("INSERT INTO"
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
                            this.getWageAmount(BillNo, ASSIGNMENTS_TYPES.TO_MASTER),
                            "UNPAID",
                            ""
                        });
                int TailorInsertStatus = this.getJdbcTemplate().update("INSERT INTO"
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
                            AsssignMentUID + 1,
                            BillNo,
                            Date,
                            "TO_TAILOR",
                            TailorName,
                            this.getWageAmount(BillNo, ASSIGNMENTS_TYPES.TO_TAILOR),
                            "UNPAID",
                            ""
                        });
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.CUTTING_IN_PROGRESS.toString(), CurrentLocation, "Assigned with BulkMasterTailorEntry to : " + MasterName);
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.STICHING_IN_PROGRESS.toString(), CurrentLocation, "Assigned with BulkMasterTailorEntry to : " + TailorName);
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID, "Assigned with BulkMasterTailorEntry to " + MasterName);
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID + 1, "Assigned with BulkMasterTailorEntry to " + TailorName);
                this.getTransactionManager().commit(txStatus);
                return "SUCCES,DATAUPDATED";
            }
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
    }

    public String getOrderDetailsForReadyToDeliver(JSONObject params) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        PreparedStatement pst = null;
        ResultSet rst = null;
        Connection con = null;
        ResultSet rst2 = null;
        try {
            String OrderBillNo = params.getString("BILL_NO=STR");
            String SQL = "SELECT BILL_NO,LOCATION,DELIVERY_DATE,QUANTITY,(SELECT TOP 1 MAIN_STATUS FROM ORDER_MOBILITY WHERE BILL_NO = ? ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS,ORDER_TYPE FROM ORDERS WHERE BILL_NO=?";
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, OrderBillNo);
            pst.setString(2, OrderBillNo);
            rst = pst.executeQuery();
            if (rst.next()) {
                PreparedStatement pst2 = this.getJDBCConnection().prepareStatement("SELECT OI.ITEM_NAME FROM ORDER_ITEMS OI INNER JOIN ITEMS IT ON OI.ITEM_NAME = IT.ITEM_NAME WHERE OI.BILL_NO=? AND IT.ITEM_TYPE='EXTRA'");
                pst2.setString(1, OrderBillNo);
                rst2 = pst2.executeQuery();
                StringBuilder data = new StringBuilder();
                data.append(rst.getString("BILL_NO")).
                        append(",").
                        append(rst.getString("LOCATION")).
                        append(",").
                        append(this.getCustomFormatDate(rst.getTimestamp("DELIVERY_DATE"))).
                        append(",").
                        append(rst.getString("QUANTITY")).
                        append(",").
                        append(rst.getString("MAIN_STATUS")).
                        append(",").
                        append(rst.getString("ORDER_TYPE")).
                        append(",");
                while (rst2.next()) {
                    data.append(rst2.getString("ITEM_NAME") + "  ");
                }
                data.append(",").
                        append("<img height='20px' width='20px' src='resources/Images/cancel_order.png'/>");
                response.addResponseValue("DATA", data.toString());
                this.generateSQLSuccessResponse(response, "Bill no sucesfully added to list");
            } else {
                this.generateSQLExceptionResponse(response, new Exception("Bill not found"), "Bill no not Exist");
            }
        } catch (Exception e) {
            this.generateSQLExceptionResponse(response, e, "Exception getorder Details");
        } finally {
            try {
                pst.close();
                rst.close();
                rst2.close();
                con.close();
            } catch (Exception e) {
            }

        }

        return response.getJSONResponse();
    }

    public String orderAssignmentReadyToDeliver(JSONObject jsonParams, String UserName) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        try {
            String FinisherName = jsonParams.getString("FINISHER_NAME=STR");
            String IronMan = jsonParams.getString("IRON=STR");
            String CurrentLocation = jsonParams.getString("LOCATION=STR");
            String Date = getParsedTimeStamp(jsonParams.getString("FINISHING_DATE=DATE")).toString();
            JSONArray List_Of_Orders = jsonParams.getJSONArray("ALL_BILL_NO");
            Map<String, String> assignmentStatusMap = new HashMap();
            int SuccessCount = 0;
            int TotalBills = List_Of_Orders.length();

            for (int i = 0; i < TotalBills; i++) {
                String BillNo = List_Of_Orders.getString(i);
                String AssignmentStatus = this.addFinisherIronAssignment(BillNo, FinisherName, IronMan, Date, CurrentLocation);
                SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                assignmentStatusMap.put(BillNo, AssignmentStatus);
            }
            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully assigned.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }

        return response.getJSONResponse();

    }

    public String addFinisherIronAssignment(String BillNo, String FinisherName, String IronMan, String Date, String CurrentLocation) throws Exception {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if (isOrderIronCompleted(BillNo)) {
                return "FAILED,Order already assigned to Iron, change the assignment ";
            } else if (isOrderFinishingCompleted(BillNo)) {
                return "FAILED,Order already assigned to Finisher, change the assignment ";
            } else {
                int AsssignMentUID = this.getColumnAutoIncrementValue("ORDER_ASSIGNMENTS", "ASSIGNMENT_UID");
                int FinisherInsertStatus = this.getJdbcTemplate().update("INSERT INTO"
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
                            "TO_FINISHER",
                            FinisherName,
                            this.getWageAmount(BillNo, ASSIGNMENTS_TYPES.TO_FINISHER),
                            "UNPAID",
                            ""
                        });
                int IronInsertStatus = this.getJdbcTemplate().update("INSERT INTO"
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
                            "TO_IRON",
                            IronMan,
                            this.getWageAmount(BillNo, ASSIGNMENTS_TYPES.TO_IRON),
                            "UNPAID",
                            ""
                        });
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.READY_TO_DELIVER.toString(), "", CurrentLocation, "Assigned with BulkReadyToDeliverEntry");
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID, "Assigned with BulkReadyToDeliver to " + FinisherName + "/" + IronMan);
                this.getTransactionManager().commit(txStatus);
                return "SUCCES,DATAUPDATED";
            }
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
    }

    public List getOrderScheduleStatusByMonth(String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String[]> datalist = new ArrayList();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            Date startDate = formatter.parse(fromDate);
            Date endDate = formatter.parse(toDate);
            LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                int TotalOrder = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE  IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, '" + date + "'), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, '" + date + "'), 1)", Integer.class);
                int Ready = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, '" + date + "'), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, '" + date + "'), 1) AND (CURRENT_STATUS = 'READY_TO_DELIVER' OR CURRENT_STATUS = 'DELIVERY_COMPLETED')", Integer.class);
                int NotReady = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, '" + date + "'), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, '" + date + "'), 1) AND (CURRENT_STATUS <> 'READY_TO_DELIVER' OR CURRENT_STATUS IS NULL) AND (CURRENT_STATUS <> 'DELIVERY_COMPLETED' OR CURRENT_STATUS IS NULL)", Integer.class);
                String FormatedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
                if (TotalOrder > 0) {
                    String data[] = {"Total : " + TotalOrder, "Ready : " + Ready, "Not Ready : " + NotReady, FormatedDate};
                    datalist.add(data);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return datalist;
    }

    public Map<String, Object> getProductionData(String ProductionType, String Name, String ReportType, String FromDate, String ToDate) {
        Connection con = null;
        PreparedStatement mainQuery = null;
        ResultSet mainGridResultSet = null;
        Map<String, Object> dateWiseListMap = new LinkedHashMap();

        try {
            con = this.getJDBCConnection();
            if (ReportType.equals("ALL")) {
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? ORDER BY OAS.ASSIGNMENT_DATE DESC");

                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
                mainGridResultSet = mainQuery.executeQuery();
                ArrayList<Object> allBillsOfADay = new ArrayList();
                String LastDate = null;
                while (mainGridResultSet.next()) {
                    String CurrentDate = this.getCustomFormatDate(mainGridResultSet.getTimestamp("ASSIGNMENT_DATE"));
                    Map<String, Object> singleBillData = new HashMap();
                    singleBillData.put("BILL_NO", mainGridResultSet.getString("BILL_NO"));
                    singleBillData.put("ASSIGNMENT_DATE", CurrentDate);
                    singleBillData.put("PIECE_VENDOR", mainGridResultSet.getString("PIECE_VENDOR"));
                    singleBillData.put("ORDER_TYPE", mainGridResultSet.getString("ORDER_TYPE"));
                    singleBillData.put("QUANTITY", mainGridResultSet.getInt("QUANTITY"));
                    singleBillData.put("ITEMS", mainGridResultSet.getString("ITEMS"));
                    singleBillData.put("WAGE_STATUS", mainGridResultSet.getString("WAGE_STATUS"));
                    singleBillData.put("WAGE_AMOUNT", mainGridResultSet.getInt("WAGE_AMOUNT"));
                    singleBillData.put("PAYMENT_DATE", this.getCustomFormatDate(mainGridResultSet.getTimestamp("PAYMENT_DATE")));
                    singleBillData.put("MAIN_STATUS", mainGridResultSet.getString("MAIN_STATUS"));
                    singleBillData.put("SUB_STATUS", mainGridResultSet.getString("SUB_STATUS"));
                    singleBillData.put("LOCATION", mainGridResultSet.getString("LOCATION"));

                    if ((LastDate == null)) {
                        allBillsOfADay.add(singleBillData);
                    } else if (LastDate.equals(CurrentDate)) {
                        allBillsOfADay.add(singleBillData);
                    } else {
                        dateWiseListMap.put(LastDate, allBillsOfADay);
                        allBillsOfADay = new ArrayList();
                        allBillsOfADay.add(singleBillData);
                    }
                    LastDate = this.getCustomFormatDate(mainGridResultSet.getTimestamp("ASSIGNMENT_DATE"));
                }
                return dateWiseListMap;
            }

        } catch (Exception e) {
            return null;
        } finally {
            try {
                mainGridResultSet.close();
                mainQuery.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public List<Object> getProductionData2(String ProductionType, String Name, String ReportType, String FromDate, String ToDate) {
        Connection con = null;
        PreparedStatement mainQuery = null;
        ResultSet mainGridResultSet = null;
        Map<String, Object> dateWiseListMap = new LinkedHashMap();

        try {
            con = this.getJDBCConnection();
            if (ReportType.equals("ALL")) {
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,DATENAME(dw,OAS.ASSIGNMENT_DATE) as WEEKDAY,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? ORDER BY OAS.ASSIGNMENT_DATE DESC");
                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
            }
            if (ReportType.equals("BY DATE")) {
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,DATENAME(dw,OAS.ASSIGNMENT_DATE) as WEEKDAY,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ? ORDER BY OAS.ASSIGNMENT_DATE ASC");
                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
                mainQuery.setTimestamp(3, this.getParsedTimeStamp(FromDate));
                mainQuery.setTimestamp(4, this.getParsedTimeStamp(ToDate));
            }
            if (ReportType.equals("WEEKLY")) {
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,DATENAME(dw,OAS.ASSIGNMENT_DATE) as WEEKDAY,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() ORDER BY OAS.ASSIGNMENT_DATE ASC");
                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
            }
            mainGridResultSet = mainQuery.executeQuery();
            ArrayList<Object> allBillsOfADay = new ArrayList();
            while (mainGridResultSet.next()) {
                String CurrentDate = this.getCustomFormatDate(mainGridResultSet.getTimestamp("ASSIGNMENT_DATE"));
                Map<String, Object> singleBillData = new HashMap();
                singleBillData.put("BILL_NO", mainGridResultSet.getString("BILL_NO"));
                singleBillData.put("ASSIGNMENT_DATE", CurrentDate);
                singleBillData.put("WEEKDAY", mainGridResultSet.getString("WEEKDAY"));
                singleBillData.put("PIECE_VENDOR", mainGridResultSet.getString("PIECE_VENDOR"));
                singleBillData.put("ORDER_TYPE", mainGridResultSet.getString("ORDER_TYPE"));
                singleBillData.put("QUANTITY", mainGridResultSet.getInt("QUANTITY"));
                singleBillData.put("ITEMS", mainGridResultSet.getString("ITEMS"));
                singleBillData.put("WAGE_STATUS", mainGridResultSet.getString("WAGE_STATUS"));
                singleBillData.put("WAGE_AMOUNT", mainGridResultSet.getInt("WAGE_AMOUNT"));
                singleBillData.put("PAYMENT_DATE", this.getCustomFormatDate(mainGridResultSet.getTimestamp("PAYMENT_DATE")));
                singleBillData.put("MAIN_STATUS", mainGridResultSet.getString("MAIN_STATUS"));
                singleBillData.put("SUB_STATUS", mainGridResultSet.getString("SUB_STATUS"));
                singleBillData.put("LOCATION", mainGridResultSet.getString("LOCATION"));
                allBillsOfADay.add(singleBillData);
            }
            return allBillsOfADay;

        } catch (Exception e) {
            return null;
        } finally {
            try {
                mainGridResultSet.close();
                mainQuery.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public List<Object> getProductionDataNonPaidWage(String ProductionType, String Name, String ReportType, String FromDate, String ToDate) {
        Connection con = null;
        PreparedStatement mainQuery = null;
        ResultSet mainGridResultSet = null;
        Map<String, Object> dateWiseListMap = new LinkedHashMap();

        try {
            con = this.getJDBCConnection();
            if (ReportType.equals("ALL")) {
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,DATENAME(dw,OAS.ASSIGNMENT_DATE) as WEEKDAY,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.WAGE_STATUS='UNPAID' ORDER BY OAS.ASSIGNMENT_DATE DESC");
                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
            }
            if (ReportType.equals("BY DATE")) {
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,DATENAME(dw,OAS.ASSIGNMENT_DATE) as WEEKDAY,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ? AND OAS.WAGE_STATUS='UNPAID' ORDER BY OAS.ASSIGNMENT_DATE ASC");
                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
                mainQuery.setTimestamp(3, this.getParsedTimeStamp(FromDate));
                mainQuery.setTimestamp(4, this.getParsedTimeStamp(ToDate));
            }
            if (ReportType.equals("WEEKLY")) {
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,DATENAME(dw,OAS.ASSIGNMENT_DATE) as WEEKDAY,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() AND OAS.WAGE_STATUS='UNPAID' ORDER BY OAS.ASSIGNMENT_DATE ASC");
                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
            }
            mainGridResultSet = mainQuery.executeQuery();
            ArrayList<Object> allBillsOfADay = new ArrayList();
            while (mainGridResultSet.next()) {
                String CurrentDate = this.getCustomFormatDate(mainGridResultSet.getTimestamp("ASSIGNMENT_DATE"));
                Map<String, Object> singleBillData = new HashMap();
                singleBillData.put("BILL_NO", mainGridResultSet.getString("BILL_NO"));
                singleBillData.put("ASSIGNMENT_DATE", CurrentDate);
                singleBillData.put("WEEKDAY", mainGridResultSet.getString("WEEKDAY"));
                singleBillData.put("PIECE_VENDOR", mainGridResultSet.getString("PIECE_VENDOR"));
                singleBillData.put("ORDER_TYPE", mainGridResultSet.getString("ORDER_TYPE"));
                singleBillData.put("QUANTITY", mainGridResultSet.getInt("QUANTITY"));
                singleBillData.put("ITEMS", mainGridResultSet.getString("ITEMS"));
                singleBillData.put("WAGE_STATUS", mainGridResultSet.getString("WAGE_STATUS"));
                singleBillData.put("WAGE_AMOUNT", mainGridResultSet.getInt("WAGE_AMOUNT"));
                singleBillData.put("PAYMENT_DATE", this.getCustomFormatDate(mainGridResultSet.getTimestamp("PAYMENT_DATE")));
                singleBillData.put("MAIN_STATUS", mainGridResultSet.getString("MAIN_STATUS"));
                singleBillData.put("SUB_STATUS", mainGridResultSet.getString("SUB_STATUS"));
                singleBillData.put("LOCATION", mainGridResultSet.getString("LOCATION"));
                allBillsOfADay.add(singleBillData);
            }
            return allBillsOfADay;

        } catch (Exception e) {
            return null;
        } finally {
            try {
                mainGridResultSet.close();
                mainQuery.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }
    public List<Object> getDayWisePaidWagePaymentOrders(String ProductionType, String Name, String PaymentDate) {
        Connection con = null;
        PreparedStatement mainQuery = null;
        ResultSet mainGridResultSet = null;
        Map<String, Object> dateWiseListMap = new LinkedHashMap();    
        try {
                con = this.getJDBCConnection();
                mainQuery = con.prepareStatement("SELECT OAS.BILL_NO,OAS.ASSIGNMENT_DATE,DATENAME(dw,OAS.ASSIGNMENT_DATE) as WEEKDAY,OD.PIECE_VENDOR,OD.ORDER_TYPE,OD.QUANTITY, (Select DISTINCT substring( ( Select ','+ST1.ITEM_NAME AS [text()] From dbo.ORDER_ITEMS ST1 INNER JOIN ITEMS ITM ON ST1.ITEM_NAME=ITM.ITEM_NAME Where ST1.BILL_NO = ST2.BILL_NO AND ITM.ITEM_TYPE='EXTRA' ORDER BY ST1.ITEM_NAME For XML PATH ('') ), 2, 1000) [ORDER_ITEMS] From dbo.ORDER_ITEMS ST2 WHERE ST2.BILL_NO=OAS.BILL_NO) AS ITEMS, OAS.WAGE_STATUS,OAS.WAGE_AMOUNT,OAS.PAYMENT_DATE, ( SELECT TOP 1 OM.MAIN_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS MAIN_STATUS, ( SELECT TOP 1 OM.SUB_STATUS FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS SUB_STATUS, ( SELECT TOP 1 OM.CURRENT_LOCATION FROM ORDER_MOBILITY OM WHERE OM.BILL_NO=OAS.BILL_NO ORDER BY MOBILITY_UID DESC ) AS LOCATION FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) ORDER BY OAS.ASSIGNMENT_DATE DESC");
                mainQuery.setString(1, ProductionType);
                mainQuery.setString(2, Name);
                mainQuery.setTimestamp(3,getParsedTimeStamp(PaymentDate));
                mainQuery.setTimestamp(4,getParsedTimeStamp(PaymentDate));
          
            mainGridResultSet = mainQuery.executeQuery();
            ArrayList<Object> allBillsOfADay = new ArrayList();
            while (mainGridResultSet.next()) {
                String CurrentDate = this.getCustomFormatDate(mainGridResultSet.getTimestamp("ASSIGNMENT_DATE"));
                Map<String, Object> singleBillData = new HashMap();
                singleBillData.put("BILL_NO", mainGridResultSet.getString("BILL_NO"));
                singleBillData.put("ASSIGNMENT_DATE", CurrentDate);
                singleBillData.put("WEEKDAY", mainGridResultSet.getString("WEEKDAY"));
                singleBillData.put("PIECE_VENDOR", mainGridResultSet.getString("PIECE_VENDOR"));
                singleBillData.put("ORDER_TYPE", mainGridResultSet.getString("ORDER_TYPE"));
                singleBillData.put("QUANTITY", mainGridResultSet.getInt("QUANTITY"));
                singleBillData.put("ITEMS", mainGridResultSet.getString("ITEMS"));
                singleBillData.put("WAGE_STATUS", mainGridResultSet.getString("WAGE_STATUS"));
                singleBillData.put("WAGE_AMOUNT", mainGridResultSet.getInt("WAGE_AMOUNT"));
                singleBillData.put("PAYMENT_DATE", this.getCustomFormatDate(mainGridResultSet.getTimestamp("PAYMENT_DATE")));
                singleBillData.put("MAIN_STATUS", mainGridResultSet.getString("MAIN_STATUS"));
                singleBillData.put("SUB_STATUS", mainGridResultSet.getString("SUB_STATUS"));
                singleBillData.put("LOCATION", mainGridResultSet.getString("LOCATION"));
                allBillsOfADay.add(singleBillData);
            }
            return allBillsOfADay;

        } catch (Exception e) {
            return null;
        } finally {
            try {
                mainGridResultSet.close();
                mainQuery.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public String getDayWiseProductionStats(String ProductionType, String Name, String ReportType, String FromDate, String ToDate) {
        ResponseJSONHandler rsp = new ResponseJSONHandler();
        Map<String, String> itemValues = new HashMap();
        Connection con = null;
        PreparedStatement itemsQuery = null;
        ResultSet itemsResultSet = null;
        String TotalPiece = "0";
        String TotalFinshed = "0";
        String TotalWage = "0";

        try {
            con = this.getJDBCConnection();
            if (ReportType.equals("ALL")) {

                TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=?", new Object[]{ProductionType, Name}, String.class);
                TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=?", new Object[]{ProductionType, Name}, String.class);
                TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED')", new Object[]{ProductionType, Name}, String.class);
                itemsQuery = con.prepareStatement("SELECT OI.ITEM_NAME,SUM(ODS.QUANTITY) FROM ORDER_ITEMS OI INNER JOIN ORDER_ASSIGNMENTS OAS ON OI.BILL_NO=OAS.BILL_NO INNER JOIN ORDERS ODS ON OI.BILL_NO = ODS.BILL_NO INNER JOIN ORDERS OD ON OD.BILL_NO = OI.BILL_NO WHERE OAS.ASSIGNMENT_TYPE=? AND OAS.EMPLOYEE_NAME = ? GROUP BY OI.ITEM_NAME");
                itemsQuery.setString(1, ProductionType);
                itemsQuery.setString(2, Name);
                itemsResultSet = itemsQuery.executeQuery();

                while (itemsResultSet.next()) {
                    itemValues.put(itemsResultSet.getString("ITEM_NAME"), itemsResultSet.getString(2));
                }
            }
            if (ReportType.equals("BY DATE")) {

                TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ?", new Object[]{ProductionType, Name, this.getParsedTimeStamp(FromDate), this.getParsedTimeStamp(ToDate)}, String.class);
                TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ?", new Object[]{ProductionType, Name, this.getParsedTimeStamp(FromDate), this.getParsedTimeStamp(ToDate)}, String.class);
                TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED') AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ?", new Object[]{ProductionType, Name, this.getParsedTimeStamp(FromDate), this.getParsedTimeStamp(ToDate)}, String.class);
                itemsQuery = con.prepareStatement("SELECT OI.ITEM_NAME,SUM(ODS.QUANTITY) FROM ORDER_ITEMS OI INNER JOIN ORDER_ASSIGNMENTS OAS ON OI.BILL_NO=OAS.BILL_NO INNER JOIN ORDERS ODS ON OI.BILL_NO = ODS.BILL_NO INNER JOIN ORDERS OD ON OD.BILL_NO = OI.BILL_NO WHERE OAS.ASSIGNMENT_TYPE=? AND OAS.EMPLOYEE_NAME = ? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ? GROUP BY OI.ITEM_NAME");
                itemsQuery.setString(1, ProductionType);
                itemsQuery.setString(2, Name);
                itemsQuery.setTimestamp(3, this.getParsedTimeStamp(FromDate));
                itemsQuery.setTimestamp(4, this.getParsedTimeStamp(ToDate));
                itemsResultSet = itemsQuery.executeQuery();
                while (itemsResultSet.next()) {
                    itemValues.put(itemsResultSet.getString("ITEM_NAME"), itemsResultSet.getString(2));
                }

            }
            if (ReportType.equals("WEEKLY")) {

                TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE()", new Object[]{ProductionType, Name}, String.class);
                TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE()", new Object[]{ProductionType, Name}, String.class);
                TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED') AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() ", new Object[]{ProductionType, Name}, String.class);
                itemsQuery = con.prepareStatement("SELECT OI.ITEM_NAME,SUM(ODS.QUANTITY) FROM ORDER_ITEMS OI INNER JOIN ORDER_ASSIGNMENTS OAS ON OI.BILL_NO=OAS.BILL_NO INNER JOIN ORDERS ODS ON OI.BILL_NO = ODS.BILL_NO INNER JOIN ORDERS OD ON OD.BILL_NO = OI.BILL_NO WHERE OAS.ASSIGNMENT_TYPE=? AND OAS.EMPLOYEE_NAME = ? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() GROUP BY OI.ITEM_NAME");
                itemsQuery.setString(1, ProductionType);
                itemsQuery.setString(2, Name);
                itemsResultSet = itemsQuery.executeQuery();
                while (itemsResultSet.next()) {
                    itemValues.put(itemsResultSet.getString("ITEM_NAME"), itemsResultSet.getString(2));
                }

            }
            TotalPiece = (TotalPiece == null) ? "0" : TotalPiece;
            TotalFinshed = (TotalFinshed == null) ? "0" : TotalFinshed;
            TotalWage = (TotalWage == null) ? "0" : TotalWage;
            String TotalUnfinished = Integer.toString(Integer.parseInt(TotalPiece) - Integer.parseInt(TotalFinshed));
            rsp.addResponseValue("ALL_ITEMS", new JSONObject(itemValues));
            rsp.addResponseValue("TOTAL_PIECE", TotalPiece);
            rsp.addResponseValue("TOTAL_WAGE", TotalWage);
            rsp.addResponseValue("TOTAL_FINISHED", TotalFinshed);
            rsp.addResponseValue("TOTAL_UNFINISHED", TotalUnfinished);

            return rsp.getJSONResponse();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                itemsResultSet.close();
                itemsQuery.close();
                con.close();
            } catch (Exception e) {
            }
        }

    }

    public String getDayWiseProductionStatsUnpaid(String ProductionType, String Name, String ReportType, String FromDate, String ToDate, String removedBillNos) {
        ResponseJSONHandler rsp = new ResponseJSONHandler();
        Map<String, String> itemValues = new HashMap();
        Connection con = null;
        PreparedStatement itemsQuery = null;
        ResultSet itemsResultSet = null;
        String TotalPiece = "0";
        String TotalFinshed = "0";
        String TotalWage = "0";

        try {
            con = this.getJDBCConnection();
            if (ReportType.equals("ALL")) {
                TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=?  AND OAS.WAGE_STATUS='UNPAID'  AND OAS.BILL_NO NOT IN (" + removedBillNos + ") ", new Object[]{ProductionType, Name}, String.class);
                TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ") ", new Object[]{ProductionType, Name}, String.class);
                TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED') AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ")  ", new Object[]{ProductionType, Name}, String.class);
                itemsQuery = con.prepareStatement("SELECT OI.ITEM_NAME,SUM(ODS.QUANTITY) FROM ORDER_ITEMS OI INNER JOIN ORDER_ASSIGNMENTS OAS ON OI.BILL_NO=OAS.BILL_NO INNER JOIN ORDERS ODS ON OI.BILL_NO = ODS.BILL_NO INNER JOIN ORDERS OD ON OD.BILL_NO = OI.BILL_NO WHERE OAS.ASSIGNMENT_TYPE=? AND OAS.EMPLOYEE_NAME = ? AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ")  GROUP BY OI.ITEM_NAME");
                itemsQuery.setString(1, ProductionType);
                itemsQuery.setString(2, Name);
                itemsResultSet = itemsQuery.executeQuery();

                while (itemsResultSet.next()) {
                    itemValues.put(itemsResultSet.getString("ITEM_NAME"), itemsResultSet.getString(2));
                }
            }
            if (ReportType.equals("BY DATE")) {

                TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ? AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ")  ", new Object[]{ProductionType, Name, this.getParsedTimeStamp(FromDate), this.getParsedTimeStamp(ToDate)}, String.class);
                TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ? AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ") ", new Object[]{ProductionType, Name, this.getParsedTimeStamp(FromDate), this.getParsedTimeStamp(ToDate)}, String.class);
                TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED') AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ? AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ") ", new Object[]{ProductionType, Name, this.getParsedTimeStamp(FromDate), this.getParsedTimeStamp(ToDate)}, String.class);
                itemsQuery = con.prepareStatement("SELECT OI.ITEM_NAME,SUM(ODS.QUANTITY) FROM ORDER_ITEMS OI INNER JOIN ORDER_ASSIGNMENTS OAS ON OI.BILL_NO=OAS.BILL_NO INNER JOIN ORDERS ODS ON OI.BILL_NO = ODS.BILL_NO INNER JOIN ORDERS OD ON OD.BILL_NO = OI.BILL_NO WHERE OAS.ASSIGNMENT_TYPE=? AND OAS.EMPLOYEE_NAME = ? AND OAS.ASSIGNMENT_DATE BETWEEN ? AND ? AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ")  GROUP BY OI.ITEM_NAME");
                itemsQuery.setString(1, ProductionType);
                itemsQuery.setString(2, Name);
                itemsQuery.setTimestamp(3, this.getParsedTimeStamp(FromDate));
                itemsQuery.setTimestamp(4, this.getParsedTimeStamp(ToDate));
                itemsResultSet = itemsQuery.executeQuery();
                while (itemsResultSet.next()) {
                    itemValues.put(itemsResultSet.getString("ITEM_NAME"), itemsResultSet.getString(2));
                }

            }
            if (ReportType.equals("WEEKLY")) {

                TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ") ", new Object[]{ProductionType, Name}, String.class);
                TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ") ", new Object[]{ProductionType, Name}, String.class);
                TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED') AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ") ", new Object[]{ProductionType, Name}, String.class);
                itemsQuery = con.prepareStatement("SELECT OI.ITEM_NAME,SUM(ODS.QUANTITY) FROM ORDER_ITEMS OI INNER JOIN ORDER_ASSIGNMENTS OAS ON OI.BILL_NO=OAS.BILL_NO INNER JOIN ORDERS ODS ON OI.BILL_NO = ODS.BILL_NO INNER JOIN ORDERS OD ON OD.BILL_NO = OI.BILL_NO WHERE OAS.ASSIGNMENT_TYPE=? AND OAS.EMPLOYEE_NAME = ? AND OAS.ASSIGNMENT_DATE BETWEEN (SELECT GETDATE() - (SELECT DATEPART(dw,GETDATE()))+1) AND GETDATE() AND OAS.WAGE_STATUS='UNPAID' AND OAS.BILL_NO NOT IN (" + removedBillNos + ")  GROUP BY OI.ITEM_NAME");
                itemsQuery.setString(1, ProductionType);
                itemsQuery.setString(2, Name);
                itemsResultSet = itemsQuery.executeQuery();
                while (itemsResultSet.next()) {
                    itemValues.put(itemsResultSet.getString("ITEM_NAME"), itemsResultSet.getString(2));
                }

            }
            TotalPiece = (TotalPiece == null) ? "0" : TotalPiece;
            TotalFinshed = (TotalFinshed == null) ? "0" : TotalFinshed;
            TotalWage = (TotalWage == null) ? "0" : TotalWage;
            String TotalUnfinished = Integer.toString(Integer.parseInt(TotalPiece) - Integer.parseInt(TotalFinshed));
            rsp.addResponseValue("ALL_ITEMS", new JSONObject(itemValues));
            rsp.addResponseValue("TOTAL_PIECE", TotalPiece);
            rsp.addResponseValue("TOTAL_WAGE", TotalWage);
            rsp.addResponseValue("TOTAL_FINISHED", TotalFinshed);
            rsp.addResponseValue("TOTAL_UNFINISHED", TotalUnfinished);

            return rsp.getJSONResponse();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                itemsResultSet.close();
                itemsQuery.close();
                con.close();
            } catch (Exception e) {
            }
        }

    }

    public String getNextBillNo() {
        try {
            int lastBillNo = getJdbcTemplate().queryForObject("SELECT TOP 1 CAST(BILL_NO AS INT) AS BILL_NO FROM orders order by  CAST(BILL_NO AS INT) DESC", Integer.class);
            lastBillNo++;
            return Integer.toString(lastBillNo);
        } catch (Exception e) {
            return "";
        }

    }
    public String getDayWisePaidWagePaymentStats(String ProductionType, String Name,String PaymentDate) {
        ResponseJSONHandler rsp = new ResponseJSONHandler();
        Map<String, String> itemValues = new HashMap();
        Connection con = null;
        PreparedStatement itemsQuery = null;
        ResultSet itemsResultSet = null;
        String TotalPiece = "0";
        String TotalFinshed = "0";
        String TotalWage = "0";
        try {
            con = this.getJDBCConnection();       
                TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=?  AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1)  ", new Object[]{ProductionType, Name,getParsedTimeStamp(PaymentDate),getParsedTimeStamp(PaymentDate)}, String.class);
                TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1)  ", new Object[]{ProductionType, Name,getParsedTimeStamp(PaymentDate),getParsedTimeStamp(PaymentDate)}, String.class);
                TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED') AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1)  ", new Object[]{ProductionType, Name,getParsedTimeStamp(PaymentDate),getParsedTimeStamp(PaymentDate)}, String.class);
                itemsQuery = con.prepareStatement("SELECT OI.ITEM_NAME,SUM(ODS.QUANTITY) FROM ORDER_ITEMS OI INNER JOIN ORDER_ASSIGNMENTS OAS ON OI.BILL_NO=OAS.BILL_NO INNER JOIN ORDERS ODS ON OI.BILL_NO = ODS.BILL_NO INNER JOIN ORDERS OD ON OD.BILL_NO = OI.BILL_NO WHERE OAS.ASSIGNMENT_TYPE=? AND OAS.EMPLOYEE_NAME = ? AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1)  GROUP BY OI.ITEM_NAME");
                itemsQuery.setString(1, ProductionType);
                itemsQuery.setString(2, Name);
                itemsQuery.setTimestamp(3, getParsedTimeStamp(PaymentDate));
                itemsQuery.setTimestamp(4, getParsedTimeStamp(PaymentDate));              
                itemsResultSet = itemsQuery.executeQuery();
                
                while (itemsResultSet.next()) {
                    itemValues.put(itemsResultSet.getString("ITEM_NAME"), itemsResultSet.getString(2));
                }          
            
            TotalPiece = (TotalPiece == null) ? "0" : TotalPiece;
            TotalFinshed = (TotalFinshed == null) ? "0" : TotalFinshed;
            TotalWage = (TotalWage == null) ? "0" : TotalWage;
            String TotalUnfinished = Integer.toString(Integer.parseInt(TotalPiece) - Integer.parseInt(TotalFinshed));
            rsp.addResponseValue("ALL_ITEMS", new JSONObject(itemValues));
            rsp.addResponseValue("TOTAL_PIECE", TotalPiece);
            rsp.addResponseValue("TOTAL_WAGE", TotalWage);
            rsp.addResponseValue("TOTAL_FINISHED", TotalFinshed);
            rsp.addResponseValue("TOTAL_UNFINISHED", TotalUnfinished);

            return rsp.getJSONResponse();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                itemsResultSet.close();
                itemsQuery.close();
                con.close();
            } catch (Exception e) {
            }
        }

    }


    public String wagePaymentModule(JSONObject jsonParams, String UserName, String ProductionType, String Name) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        try {
            JSONArray List_Of_Orders = jsonParams.getJSONArray("ALL_BILL_NO");
            Map<String, String> assignmentStatusMap = new HashMap();
            int SuccessCount = 0;
            int TotalBills = List_Of_Orders.length();

            for (int i = 0; i < TotalBills; i++) {
                String BillNo = List_Of_Orders.getString(i);
                String[] PaymentStatus = null;

                if (ProductionType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_MASTER.toString())) {
                    PaymentStatus = this.payMasterWage(BillNo, Name);
                } else if (ProductionType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_TAILOR.toString())) {
                    PaymentStatus = this.payTailorWage(BillNo, Name);
                } else if (ProductionType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_FINISHER.toString())) {
                    PaymentStatus = this.payFinisherWage(BillNo, Name);
                } else if (ProductionType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_IRON.toString())) {
                    PaymentStatus = this.payIronWage(BillNo, Name);
                }

                SuccessCount = (PaymentStatus[0].equals("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                assignmentStatusMap.put(BillNo, PaymentStatus[0]+","+PaymentStatus[1]);
            }
            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully Paid.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();

    }

}
