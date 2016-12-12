package com.ordermanager.order.dao;

import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
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
            paramData.put("ORDER_UID=NUM", UID);          
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ORDERS"));
            int PaymentUID = this.getColumnAutoIncrementValue("PAYMENT_TRANSACTIONS", "TRANSACTION_UID");
            int PayemtInsert = getJdbcTemplate().update("iNSERT INTO PAYMENT_TRANSACTIONS VALUES (?,?,?,?)", new Object[]{PaymentUID, paramData.get("BILL_NO=STR"), ConstantContainer.PAYMENT_TYPE.ADVANCE.toString(), Advance});
            int Order_Item_Uid_For_Main_Item = this.getColumnAutoIncrementValue("ORDER_ITEMS", "ORDER_ITEMS_UID");
            int mainItem = getJdbcTemplate().update("INSERT INTO ORDER_ITEMS VALUES (?,?,?,?)", new Object[]{Order_Item_Uid_For_Main_Item, paramData.get("BILL_NO=STR"), MainItemName, ""});
            int OrderStatusLocationInsert = getJdbcTemplate().update("INSERT INTO ORDER_MOBILITY VALUES (?,?,?,?,?,?,?)",new Object[]{
                    this.getColumnAutoIncrementValue("ORDER_MOBILITY", "MOBILITY_UID"),
                    paramData.get("BILL_NO=STR"),
                    getParsedTimeStamp((String) paramData.get("ORDER_DATE=DATE")),
                    OrderStatus,
                    "",
                    "",
                    "ADDED WITH NEW ORDER ENTRY"                    
                    });
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
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.QUICK_ORDERS, UID, "Bill No :" + paramData.getString("BILL_NO=STR"));
            generateSQLSuccessResponse(response, paramData.get("BILL_NO=STR") + " - added Succesfully");
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
            mainAuditor(ConstantContainer.AUDIT_TYPE.UPDATE, ConstantContainer.APP_MODULE.ORDERS,Integer.parseInt(getAnySingleData("ORDERS", "ORDER_UID", "BILL_NO", BillNo)), paramData.toString());
            generateSQLSuccessResponse(response, paramData.get("BILL_NO=STR") + " - Updated Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }
    public String getAnySingleData(String Table,String ColumnDataToFetch,String KeyName,String KeyValue){
       return this.getJdbcTemplate().queryForObject("SELECT "+ColumnDataToFetch+" FROM "+Table+" WHERE "+KeyName+"=?",new Object[]{KeyValue},String.class);
    } 
    public boolean updateOrder(JSONObject paramJson){    
    return true;
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

}
