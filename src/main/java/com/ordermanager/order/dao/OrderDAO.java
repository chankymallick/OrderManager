package com.ordermanager.order.dao;

import com.google.gson.JsonArray;
import com.ordermanager.messanger.sendSMS;
import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.PropertyFileReader;
import com.ordermanager.utility.ResponseJSONHandler;
import com.ordermanager.utility.UtilityDAO;
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

    public List<String> getSavedItemSelectionList(String BillNo) {

        String SQL = "SELECT DISTINCT OD.ITEM_NAME AS ITEM_NAME FROM ORDER_ITEMS OD INNER JOIN ITEMS IT ON OD.ITEM_NAME = IT.ITEM_NAME WHERE IT.ITEM_TYPE = 'EXTRA' AND OD.BILL_NO=?";
        ResultSet rst = null;
        Connection con = null;
        PreparedStatement pst = null;
        ArrayList<String> SavedItems = new ArrayList();
        try {
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, BillNo);
            rst = pst.executeQuery();
            while (rst.next()) {
                SavedItems.add(rst.getString("ITEM_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                con.close();

            } catch (Exception e) {
            }
        }
        return SavedItems;
    }

    public int deleteExistingOrderItems(String Bill_No) {
        return this.getJdbcTemplate().update("DELETE FROM ORDER_ITEMS WHERE BILL_NO = ?", new Object[]{Bill_No});
    }

    public boolean modifyOrderItems(JSONArray NewListOfItems, String Bill_No) {
        if (isOrderCuttingInProgress(Bill_No) || isOrderStichingInProgress(Bill_No)) {//Condition need to be reviewd
            return false;
        }
        int deleteStatus = this.deleteExistingOrderItems(Bill_No);
        List<Object[]> ItemParams = new ArrayList();
        int Order_Item_Uid = this.getColumnAutoIncrementValue("ORDER_ITEMS", "ORDER_ITEMS_UID");
        for (int i = 0; i < NewListOfItems.length(); i++) {
            ItemParams.add(new Object[]{Order_Item_Uid, Bill_No, NewListOfItems.get(i), ""});
            Order_Item_Uid++;
        }
        int totalInsert[] = getJdbcTemplate().batchUpdate("INSERT INTO ORDER_ITEMS VALUES (?,?,?,?)", ItemParams);
        return true;
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
                paramData.put("CUSTOM_PRICE_ENABLED=NUM", 1);
                paramData.put("CUSTOM_PRICE_MASTER=NUM", MasterPrice);
                paramData.put("CUSTOM_PRICE_TAILOR=NUM", TailorPrice);
            } else {
                ItemNameArray = (JSONArray) paramData.get("ITEM_DATA");
                paramData.put("CUSTOM_PRICE_ENABLED=NUM", 0);
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
            int PayemtInsert = getJdbcTemplate().update("INSERT INTO PAYMENT_TRANSACTIONS VALUES (?,?,?,?,?)", new Object[]{PaymentUID, paramData.get("BILL_NO=STR"), ConstantContainer.PAYMENT_TYPE.ADVANCE.toString(), Advance, this.getParsedTimeStamp(paramData.get("ORDER_DATE=DATE").toString())});
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
            new sendSMS().sendSms(paramData.get("BILL_NO=STR").toString(), paramData.get("CONTACT_NO=STR").toString(), paramData.get("CUSTOMER_NAME=STR").toString());
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String updateItem(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            String ItemNewName = paramData.get("ITEM_NAME=STR").toString();
            String OldItemName = paramData.get("OLD_ITEM_NAME=STR").toString();
            if (!OldItemName.equalsIgnoreCase(ItemNewName)) {
                int isItemAlreadyAssignedtoOrder = this.getJdbcTemplate().queryForObject("SELECT COUNT(ITEM_NAME) FROM ORDER_ITEMS WHERE ITEM_NAME = ?", new Object[]{OldItemName}, Integer.class);
                if (isItemAlreadyAssignedtoOrder > 0) {
                    throw new Exception("Item Already used  in existing Orders ,  Cannot Modify the Name");
                }
            }

            String OLD_Value = getJdbcTemplate().queryForObject("SELECT CONCAT('[ITEM_UID]= ',ITEM_UID,', [ITEM_NAME]= ',ITEM_NAME,', [PARENT_ITEM]=',PARENT_ITEM,', [ITEM_SUB_TYPE]=',ITEM_SUB_TYPE,', [MASTER_PRICE]=',MASTER_PRICE,', [TAILOR_PRICE]=',TAILOR_PRICE,', [TAILOR_PRICE]=',TAILOR_PRICE,', [FINISHER_PRICE]=',FINISHER_PRICE,', [ITEM_ORDER]=',ITEM_ORDER,', [ACTIVE]=',ACTIVE,', [NOTE]=',NOTE) FROM ITEMS WHERE ITEM_NAME = ? ", new Object[]{OldItemName}, String.class);
            int UpdateStatus = getJdbcTemplate().update("UPDATE ITEMS SET ITEM_NAME=?, ITEM_TYPE=?,PARENT_ITEM=?,ITEM_SUB_TYPE=?,MASTER_PRICE=?,TAILOR_PRICE=?,FINISHER_PRICE=?,ACTIVE=?,NOTE=?  WHERE ITEM_NAME=?",
                    new Object[]{
                        ItemNewName,
                        paramData.get("ITEM_TYPE=STR"),
                        paramData.get("PARENT_ITEM=STR"),
                        paramData.get("ITEM_SUB_TYPE=STR"),
                        paramData.get("MASTER_PRICE=NUM"),
                        paramData.get("TAILOR_PRICE=NUM"),
                        paramData.get("FINISHER_PRICE=NUM"),
                        paramData.get("ACTIVE=NUM"),
                        paramData.get("NOTE=STR"),
                        OldItemName
                    }
            );
            if (UpdateStatus > 0) {
                auditor(AUDIT_TYPE.UPDATE, APP_MODULE.ITEMS, paramData.getInt("ITEM_UID=NUM"), OLD_Value, "Item price updated");
                this.getTransactionManager().commit(txStatus);
                generateSQLSuccessResponse(response, paramData.get("ITEM_NAME=STR") + " - Updated Succesfully. ");
            }
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
        String ExtraInfo = "";
        try {
            boolean isCustomRateActive = (paramData.getInt("CUSTOM_RATE=NUM") != 0);
            String BillNo = paramData.getString("BILL_NO=STR");
            String OrderStatus = paramData.get("ORDER_STATUS=STR").toString();
            String OrdersSubStatus = paramData.get("ORDER_SUB_STATUS=STR").toString();
            String CurrentLocation = paramData.get("CURRENT_LOCATION=STR").toString();
            String MainItemName = paramData.get("ORDER_TYPE=STR").toString();
            int MasterPrice = 0;
            int TailorPrice = 0;
            if (paramData.has("ITEM_DATA")) { //Review - Custom Rate On/Off without Clicking Ok button in Custom/Item window leads to incocistency               
                if (!(isOrderCuttingInProgress(BillNo) || isOrderStichingInProgress(BillNo))) {
                    JSONObject NEW_ITEM_DATA = (JSONObject) paramData.get("ITEM_DATA");
                    JSONArray NewListOfItems = new JSONArray();
                    if (isCustomRateActive) {
                        MasterPrice = NEW_ITEM_DATA.getInt("MASTER_RATE=STR");
                        TailorPrice = NEW_ITEM_DATA.getInt("TAILOR_RATE=STR");
                        int deleteStatus = this.deleteExistingOrderItems(BillNo);
                        this.getJdbcTemplate().update("UPDATE ORDERS SET CUSTOM_PRICE_ENABLED =?, CUSTOM_PRICE_MASTER = ? ,CUSTOM_PRICE_TAILOR=? WHERE BILL_NO = ?", new Object[]{1, MasterPrice, TailorPrice, BillNo});
                        int Order_Item_Uid_For_Main_Item = this.getColumnAutoIncrementValue("ORDER_ITEMS", "ORDER_ITEMS_UID");
                        int mainItem = getJdbcTemplate().update("INSERT INTO ORDER_ITEMS VALUES (?,?,?,?)", new Object[]{Order_Item_Uid_For_Main_Item, paramData.get("BILL_NO=STR"), MainItemName, ""});
                    } else {
                        NewListOfItems = (JSONArray) paramData.get("ITEM_DATA");
                        this.getJdbcTemplate().update("UPDATE ORDERS SET CUSTOM_PRICE_ENABLED =?,CUSTOM_PRICE_MASTER = ? ,CUSTOM_PRICE_TAILOR=? WHERE BILL_NO = ?", new Object[]{0, 0, 0, BillNo});
                        boolean isModificationStatusSuccess = this.modifyOrderItems(NewListOfItems, BillNo);
                        if (isModificationStatusSuccess) {
                            int Order_Item_Uid_For_Main_Item = this.getColumnAutoIncrementValue("ORDER_ITEMS", "ORDER_ITEMS_UID");
                            int mainItem = getJdbcTemplate().update("INSERT INTO ORDER_ITEMS VALUES (?,?,?,?)", new Object[]{Order_Item_Uid_For_Main_Item, paramData.get("BILL_NO=STR"), MainItemName, ""});
                        } else {
                            ExtraInfo += " [Info] - Item changing Failed , Order already assigned ";
                        }
                    }
                } else {
                    ExtraInfo += " <b style='color:RED;'>[Info] - No Change on Items/Wage Price ,  Order Already Assigned</b> ";
                }
            }
            int UpdateStatus = this.getJdbcTemplate().update("UPDATE ORDERS SET "
                    + "DELIVERY_DATE=?,"
                    + "CUSTOMER_NAME=?,"
                    + "CONTACT_NO=?,"
                    + "ORDER_TYPE=?,"//Review - Cannot Change after Order Assignment.
                    + "PRODUCT_TYPE=?,"
                    + "QUANTITY=?,"
                    + "MEASURED_BY=?,"
                    + "NOTE=?,"
                    + "PIECE_VENDOR=? "
                    + "WHERE BILL_NO =?",
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
            generateSQLSuccessResponse(response, paramData.get("BILL_NO=STR") + " - Updated Succesfully. " + ExtraInfo);
            this.getTransactionManager().commit(txStatus);
            //new sendSMS().sendSms( paramData.get("BILL_NO=STR").toString(),  paramData.get("CONTACT_NO=STR").toString(),paramData.get("CUSTOMER_NAME=STR").toString());
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
            int PayemtInsert = getJdbcTemplate().update("iNSERT INTO PAYMENT_TRANSACTIONS VALUES (?,?,?,?,?)", new Object[]{PaymentUID, paramData.get("BILL_NO=STR"), ConstantContainer.PAYMENT_TYPE.ADVANCE.toString(), Advance, paramData.get("ORDER_DATE=DATE_AUTO").toString()});
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

    public String addNewAccount(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int UID = getColumnAutoIncrementValue("ACCOUNT_REGISTER", "TRANSACTION_UID");
            paramData.put("TRANSACTION_UID=NUM", UID);
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ACCOUNT_REGISTER"));
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.ACCOUNT_REGISTER, UID, "New Account Created");
            generateSQLSuccessResponse(response, paramData.get("ACCOUNT_NAME=STR") + " - added Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String addNewAccountTransaction(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int UID = getColumnAutoIncrementValue("ACCOUNTS_BOOK", "TRANSACTION_UID");
            paramData.put("TRANSACTION_UID=NUM", UID);
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ACCOUNTS_BOOK"));
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.ACCOUNTS_BOOK, UID, "New Transaction Saved");
            generateSQLSuccessResponse(response, paramData.get("TR_TYPE=STR") + ":" + paramData.get("ACCOUNT_NAME=STR") + ":" + paramData.get("AMOUNT=NUM") + " - saved Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String addNewTask(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int UID = getColumnAutoIncrementValue("TASK_LIST", "TASK_ID");
            paramData.put("TASK_ID=NUM", UID);
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "TASK_LIST"));
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.TASK_LIST, UID, "New Task Created");
            generateSQLSuccessResponse(response, "New Task Created Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String updateTask(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int Task_ID = paramData.getInt("TASK_ID");
            int updateStatus = getJdbcTemplate().update("UPDATE TASK_LIST SET RESOLVE_DATE=? , TASK_STATUS =?, NOTE=CONCAT(NOTE+' ', ?)   WHERE TASK_ID = ? ", new Object[]{
                getParsedTimeStamp(paramData.getString("RESOLVE_DATE")),
                paramData.getString("TASK_STATUS"),
                paramData.getString("NOTE"),
                Task_ID
            });
            mainAuditor(ConstantContainer.AUDIT_TYPE.UPDATE, ConstantContainer.APP_MODULE.TASK_LIST, Task_ID, "Task Updated : " + paramData.getString("TASK_STATUS"));
            generateSQLSuccessResponse(response, "Task Updated Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String addNewAccountSubType(JSONObject paramData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            int UID = getColumnAutoIncrementValue("ACCOUNT_BOOK_SUBTYPES", "TRANSACTION_UID");
            paramData.put("TRANSACTION_UID=NUM", UID);
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ACCOUNT_BOOK_SUBTYPES"));
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.ACCOUNT_BOOK_SUBTYPES, UID, "New Transaction Saved");
            generateSQLSuccessResponse(response, "SubType  - saved Succesfully");
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
            int PayemtInsert = getJdbcTemplate().update("iNSERT INTO PAYMENT_TRANSACTIONS (TRANSACTION_UID,ORDER_BILL_NO,PAYMENT_TYPE,AMOUNT,TRANSACTION_DATE) VALUES (?,?,?,?,?)", new Object[]{PaymentUID, OrderBillNo, ConstantContainer.PAYMENT_TYPE.RE_ADVANCE.toString(), Advance, getCurrentTimeStamp()});
            int mobilityUpdate = this.orderMobiltyUpdate(OrderBillNo, this.getCurrentTimeStamp().toString(), MainStatus, SubStatus, Location, "Re Advance");
            mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.PAYMENT_TRANSACTION, PaymentUID, "RE ADVANCE FOR BILL NO : " + OrderBillNo + ", Advance :" + Integer.toString(Advance));
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
    public List<Object> getGridDataForAudit() {
        String SQL = "SELECT AUDIT_UID,AUDIT_TYPE, AUDIT_MODULE, AUDIT_DATETIME, CONVERT(VARCHAR(8),AUDIT_DATETIME,108) AS TIME, AUDITED_BY , AUDIT_KEY, AUDIT_HISTORY , NOTE FROM AUDIT ORDER BY AUDIT_UID DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForQuickOrders() {
        String SQL = "SELECT TOP 50 BILL_NO,ORDER_DATE,PRICE ,AMOUNT FROM ORDERS OD INNER JOIN PAYMENT_TRANSACTIONS PT ON OD.BILL_NO=PT.ORDER_BILL_NO ORDER BY PT.TRANSACTION_UID DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForTaskList() {
        String SQL = "SELECT TASK_ID,'<b>'+TASK_TYPE+'</b> : <b> '+TASK_NAME+'<br> </b>'+TASK_STATUS + '&nbsp;&nbsp; : &nbsp;'+ CONVERT(VARCHAR(30),SCHEDULE_DATE)  +'  <br>'+ CASE WHEN NOTE='' THEN '' ELSE NOTE+'<br>' END + '<a href=\"#\" class=\"taskButton\" onClick=\"externalEvent('+CONVERT(VARCHAR(10),TASK_ID)+')\" >Update Task</a>' AS DATA FROM TASK_LIST WHERE (DATEDIFF(day, CURRENT_TIMESTAMP  , SCHEDULE_DATE) <15) AND (TASK_STATUS <> 'FINISHED' AND TASK_STATUS <> 'CANCEL') ORDER BY TASK_STATUS DESC , SCHEDULE_DATE ASC,PRIORITY DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForPaymentSearchQuery(String BillNo) {
        String SQL = "SELECT TRANSACTION_UID,'<B>DATE : '+CONVERT(VARCHAR(20), CAST(TRANSACTION_DATE AS DATE),3)+'<BR></B>'+'ID:'+CONVERT(VARCHAR(10),TRANSACTION_UID) AS DATA,'<b>'+PAYMENT_TYPE+'</b></br>' AS TYPE,'<B style=\"font-size:20px;\">'+CONVERT(VARCHAR(10),AMOUNT)+'<B><BR>' AS AMOUNT  FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO =" + BillNo + " ORDER BY TRANSACTION_DATE ";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForOrderAssignmentSearchQuery(String BillNo) {
        String SQL = "SELECT ASSIGNMENT_UID,ASSIGNMENT_DATE ,ASSIGNMENT_TYPE,EMPLOYEE_NAME,WAGE_STATUS  FROM ORDER_ASSIGNMENTS WHERE BILL_NO =" + BillNo + " ORDER BY ASSIGNMENT_UID ";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForOrderItemsSearchQuery(String BillNo) {
        String SQL = "SELECT OD.ITEM_NAME FROM ORDER_ITEMS OD INNER JOIN ITEMS IT ON OD.ITEM_NAME=IT.ITEM_NAME WHERE BILL_NO =" + BillNo + "ORDER BY IT.ITEM_TYPE DESC";
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

    public List<Object> getGridDataForSchedulerOrderList(String Type, String DeliverDate) throws Exception {
        String SQL = "";

        if (Type.equals("TOTAL")) {
            String TempSQL = "SELECT OD.BILL_NO,OD.ORDER_TYPE,OD.CURRENT_STATUS,(SELECT TOP 1 SUB_STATUS FROM ORDER_MOBILITY WHERE BILL_NO = OD.BILL_NO ORDER BY MOBILITY_UID DESC) AS SUB_STATUS,(SELECT TOP 1 CURRENT_LOCATION FROM ORDER_MOBILITY WHERE BILL_NO = OD.BILL_NO ORDER BY MOBILITY_UID DESC) AS LOCATION,OD.DELIVERY_DATE,OD.NOTE FROM ORDERS OD WHERE DELIVERY_DATE  IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0,?), 1)";
            SQL = TempSQL.replace("?", "'" + getParsedTimeStamp(DeliverDate).toString() + "'");

        }
        if (Type.equals("READY")) {
            String TempSQL = "SELECT OD.BILL_NO,OD.ORDER_TYPE,OD.CURRENT_STATUS,(SELECT TOP 1 SUB_STATUS FROM ORDER_MOBILITY WHERE BILL_NO = OD.BILL_NO ORDER BY MOBILITY_UID DESC) AS SUB_STATUS,(SELECT TOP 1 CURRENT_LOCATION FROM ORDER_MOBILITY WHERE BILL_NO = OD.BILL_NO ORDER BY MOBILITY_UID DESC) AS LOCATION,OD.DELIVERY_DATE,OD.NOTE FROM ORDERS OD WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) AND (CURRENT_STATUS = 'READY_TO_DELIVER' OR CURRENT_STATUS = 'DELIVERY_COMPLETED')";
            SQL = TempSQL.replace("?", "'" + getParsedTimeStamp(DeliverDate).toString() + "'");
        }
        if (Type.equals("NOT_READY")) {
            String TempSQL = "SELECT OD.BILL_NO,OD.ORDER_TYPE,OD.CURRENT_STATUS,(SELECT TOP 1 SUB_STATUS FROM ORDER_MOBILITY WHERE BILL_NO = OD.BILL_NO ORDER BY MOBILITY_UID DESC) AS SUB_STATUS,(SELECT TOP 1 CURRENT_LOCATION FROM ORDER_MOBILITY WHERE BILL_NO = OD.BILL_NO ORDER BY MOBILITY_UID DESC) AS LOCATION,OD.DELIVERY_DATE,OD.NOTE FROM ORDERS OD WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) AND (CURRENT_STATUS <> 'READY_TO_DELIVER' OR CURRENT_STATUS IS NULL) AND (CURRENT_STATUS <> 'DELIVERY_COMPLETED' OR CURRENT_STATUS IS NULL)";
            SQL = TempSQL.replace("?", "'" + getParsedTimeStamp(DeliverDate).toString() + "'");
        }

        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForChartOrderList(String Type, String param) throws Exception {
        String SQL = "";

        if (Type.equals("ORDER_LOCATION")) {
            String TempSQL = "SELECT OD.BILL_NO,OD.PRICE,OD.QUANTITY,OD.ORDER_DATE,OD.DELIVERY_DATE,OD.CURRENT_STATUS,DBO.getCurrentLocation(OD.BILL_NO) AS CURRENT_LOCATION,OD.ORDER_TYPE,OD.NOTE FROM ORDERS OD where DBO.GETCURRENTLOCATION(od.BILL_NO) =? ";
            SQL = TempSQL.replace("?", "'" + param + "'");
        }
        if (Type.equals("ORDER_STATUS")) {

            String TempSQL = "SELECT OD.BILL_NO,OD.PRICE,OD.QUANTITY,OD.ORDER_DATE,OD.DELIVERY_DATE,OD.CURRENT_STATUS,DBO.getCurrentLocation(OD.BILL_NO) AS CURRENT_LOCATION,OD.ORDER_TYPE,OD.NOTE FROM ORDERS OD  WHERE CURRENT_STATUS <> '' AND CURRENT_STATUS <> 'DELIVERY_COMPLETED' AND CURRENT_STATUS <> 'CANCELLED'  AND ORDER_TYPE <> 'PIECE_SALE' AND CURRENT_STATUS = ?";
            SQL = TempSQL.replace("?", "'" + param + "'");
        }
        if (Type.equals("FUTURE_ORDER")) {
            String TempSQL = "SELECT OD.BILL_NO,OD.PRICE,OD.QUANTITY,OD.ORDER_DATE,OD.DELIVERY_DATE,OD.CURRENT_STATUS,DBO.getCurrentLocation(OD.BILL_NO) AS CURRENT_LOCATION,OD.ORDER_TYPE,OD.NOTE  FROM ORDERS OD WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) AND (CURRENT_STATUS <> 'READY_TO_DELIVER' AND CURRENT_STATUS <> 'DELIVERY_COMPLETED')";
            SQL = TempSQL.replace("?", "'" + param + "'");
        }

        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForQuickOrdersWithDate(String ReportDate) {
        try {
            Timestamp od = this.getParsedTimeStamp(ReportDate);
            String SQL = "SELECT  BILL_NO,ORDER_DATE,PRICE ,AMOUNT,PAYMENT_TYPE FROM ORDERS OD INNER JOIN PAYMENT_TRANSACTIONS PT ON OD.BILL_NO=PT.ORDER_BILL_NO AND PT.TRANSACTION_DATE > = DateAdd(Day, DateDiff(Day, 0, '" + od.toString() + "'), 0) AND PT.TRANSACTION_DATE  <  DateAdd(Day, DateDiff(Day,0,  '" + od.toString() + "'), 1)  AND (PT.PAYMENT_TYPE = 'ADVANCE' OR PT.PAYMENT_TYPE ='RE_ADVANCE') ORDER BY PT.TRANSACTION_UID DESC";
            return this.getJSONDataForGrid(SQL);
        } catch (Exception e) {
            return null;
        }

    }

    public List<Object> getGridDataForDeliveryTransactions(String ReportDate) {
        try {
            Timestamp od = this.getParsedTimeStamp(ReportDate);
            String SQL = "SELECT ROW_NUMBER() Over (Order by PT.TRANSACTION_DATE ) As [S.N.], ORDER_BILL_NO,CUSTOMER_NAME,PRICE,(SELECT SUM(AMOUNT) FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO = OD.BILL_NO  AND PAYMENT_TYPE = 'ADVANCE' OR PAYMENT_TYPE='READVANCE' ) AS ADVANCE,DISCOUNT,AMOUNT AS PAID ,NOTE FROM PAYMENT_TRANSACTIONS PT INNER JOIN ORDERS OD ON PT.ORDER_BILL_NO=OD.BILL_NO  WHERE PT.PAYMENT_TYPE = 'DELIVERY_PAYMENT' AND PT.TRANSACTION_DATE > = DateAdd(Day, DateDiff(Day, 0, '" + od.toString() + "'), 0) AND OD.ORDER_DATE <  DateAdd(Day, DateDiff(Day,0,  '" + od.toString() + "'), 1) ORDER BY PT.TRANSACTION_DATE ASC";
//            String SQL = "SELECT TOP 50 BILL_NO,ORDER_DATE,PRICE ,AMOUNT FROM ORDERS OD INNER JOIN PAYMENT_TRANSACTIONS PT ON OD.BILL_NO=PT.ORDER_BILL_NO AND OD.ORDER_DATE > = DateAdd(Day, DateDiff(Day, 0, '" + od.toString() + "'), 0) AND OD.ORDER_DATE <  DateAdd(Day, DateDiff(Day,0,  '" + od.toString() + "'), 1) ORDER BY PT.TRANSACTION_UID DESC";
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
            String TotalAdvance = this.getJdbcTemplate().queryForObject("SELECT SUM(PM.AMOUNT) FROM PAYMENT_TRANSACTIONS PM INNER JOIN ORDERS ODR  ON ODR.BILL_NO = PM.ORDER_BILL_NO WHERE ODR.LOCATION =? AND PM.PAYMENT_TYPE IN('ADVANCE','RE_ADVANCE','PIECE_SALE')  AND PM.TRANSACTION_DATE >=? AND PM.TRANSACTION_DATE <  DATEADD(DAY,1,?)", new Object[]{StoreLocation, OrderDate, OrderDate}, String.class);
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

    public List<String[]> getDeliveryPaymentStatistics(String orderDate, String StoreLocation) {
        ArrayList<String[]> statList = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rst = null;
        try {

            Timestamp od = this.getParsedTimeStamp(orderDate);
            String SQL = "SELECT  COUNT(ORDER_BILL_NO) AS TOTAL_BILLS,SUM(OD.QUANTITY) AS TOTAL_PIECE,SUM(PRICE) AS TOTAL_PRICE,SUM(DISCOUNT)AS TOTAL_DISCOUNT,SUM(AMOUNT) AS TOTAL_PAID FROM PAYMENT_TRANSACTIONS PT INNER JOIN ORDERS OD ON PT.ORDER_BILL_NO=OD.BILL_NO  WHERE PT.PAYMENT_TYPE = 'DELIVERY_PAYMENT' AND PT.TRANSACTION_DATE > = DateAdd(Day, DateDiff(Day, 0, '" + od.toString() + "'), 0) AND OD.ORDER_DATE <  DateAdd(Day, DateDiff(Day,0,  '" + od.toString() + "'), 1)";
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            rst = pst.executeQuery();
            String Total = "0";
            String TotalPiece = "0";
            String TotalBills = "0";
            String Discount = "0";
            String FinalTotal = "0";

            if (rst.next()) {
                Total = rst.getString("TOTAL_PRICE");
                TotalPiece = rst.getString("TOTAL_PIECE");
                TotalBills = rst.getString("TOTAL_BILLS");
                Discount = rst.getString("TOTAL_DISCOUNT");
                FinalTotal = rst.getString("TOTAL_PAID");

            }
            Total = (Total == null) ? "0" : Total;
            TotalPiece = (TotalPiece == null) ? "0" : TotalPiece;
            TotalBills = (TotalBills == null) ? "0" : TotalBills;
            Discount = (Discount == null) ? "0" : Discount;
            FinalTotal = (FinalTotal == null) ? "0" : FinalTotal;

            statList.add(new String[]{STATISTICS_TYPE.LARGE.toString(), "DATE", orderDate.toString()});
            statList.add(new String[]{STATISTICS_TYPE.LARGE.toString(), "BILL AMOUNT", Total});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "NO OF PIECE", TotalPiece});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "NO OF BILLS", TotalBills});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "DISCOUNT", Discount});
            statList.add(new String[]{STATISTICS_TYPE.MEDIUM.toString(), "TOTAL PAYMENT", FinalTotal});
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

    public String getOrderDetailsSingleAssignment(JSONObject params) {

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

    public String getOrderDetailsForDelivery(JSONObject params) {

        ResponseJSONHandler response = new ResponseJSONHandler();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rst = null;
        ResultSet rst2 = null;
        try {
            int ONLY_READY_TO_DELIVER = params.getInt("ONLY_READY_TO_DELIVER=NUM");
            String OrderBillNo = params.getString("BILL_NO=STR");
            String SQL = "SELECT BILL_NO,QUANTITY,PRICE, (SELECT SUM(AMOUNT) FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO= ? AND (PAYMENT_TYPE = 'ADVANCE' OR PAYMENT_TYPE='RE_ADVANCE')) AS ADVANCE, ISNULL(DISCOUNT,0) AS DISCOUNT,PRICE-(ISNULL(DISCOUNT,0)+( SELECT SUM(AMOUNT) FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO=? AND (PAYMENT_TYPE = 'ADVANCE' OR PAYMENT_TYPE='RE_ADVANCE'))) AS DUE,ORDER_TYPE,CURRENT_STATUS,NOTE FROM ORDERS WHERE BILL_NO = ?";
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, OrderBillNo);
            pst.setString(2, OrderBillNo);
            pst.setString(3, OrderBillNo);
            rst = pst.executeQuery();
            if (rst.next()) {
                if (rst.getString("CURRENT_STATUS").equalsIgnoreCase(ConstantContainer.ORDER_MAIN_STATUS.DELIVERY_COMPLETED.toString())) {
                    this.generateSQLExceptionResponse(response, new Exception("Delivery Already Completed"), "Delivery Already Completed");
                } else if (ONLY_READY_TO_DELIVER == 1 && !(rst.getString("CURRENT_STATUS").equalsIgnoreCase(ConstantContainer.ORDER_MAIN_STATUS.READY_TO_DELIVER.toString()))) {
                    this.generateSQLExceptionResponse(response, new Exception("Order Not Ready for Delivery"), "Order Not Ready for Delivery");
                } else {
                    StringBuilder data = new StringBuilder();
                    data.append(rst.getString("BILL_NO")).
                            append(",").
                            append(rst.getString("QUANTITY")).
                            append(",").
                            append(rst.getString("PRICE")).
                            append(",").
                            append(rst.getString("ADVANCE")).
                            append(",").
                            append(rst.getString("DISCOUNT")).
                            append(",").
                            append(rst.getString("DUE")).
                            append(",").
                            append(rst.getString("ORDER_TYPE")).
                            append(",").
                            append(rst.getString("CURRENT_STATUS")).
                            append(",").
                            append(rst.getString("NOTE")).
                            append(",").
                            append("<img height='20px' width='20px' src='resources/Images/cancel_order.png'/>").
                            append(",").
                            append("<img height='20px' width='20px' src='resources/Images/task.png'/>");
                    response.addResponseValue("DATA", data.toString());
                    this.generateSQLSuccessResponse(response, "Bill no sucesfully added to list");
                }
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

    public String getOrderDetailsForSearchQuery(String OrderBillNo) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rst = null;
        ResultSet rst2 = null;
        JSONObject billDetails = new JSONObject();
        try {
            String SQL = "SELECT BILL_NO,ORDER_DATE,DELIVERY_DATE,CUSTOMER_NAME,QUANTITY,PRICE, (SELECT SUM(AMOUNT) FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO= ? AND (PAYMENT_TYPE = 'ADVANCE' OR PAYMENT_TYPE='RE_ADVANCE')) AS ADVANCE, ISNULL(DISCOUNT,0) AS DISCOUNT,PRICE-(ISNULL(DISCOUNT,0)+( SELECT SUM(AMOUNT) FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO=? AND (PAYMENT_TYPE = 'ADVANCE' OR PAYMENT_TYPE='RE_ADVANCE'))) AS DUE,ORDER_TYPE,CURRENT_STATUS,NOTE FROM ORDERS WHERE BILL_NO = ?";
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, OrderBillNo);
            pst.setString(2, OrderBillNo);
            pst.setString(3, OrderBillNo);
            rst = pst.executeQuery();
            if (rst.next()) {
                billDetails.put("BILL_NO", rst.getString("BILL_NO"));
                billDetails.put("ORDER_DATE", getCustomFormatDate(rst.getTimestamp("ORDER_DATE")));
                billDetails.put("DELIVERY_DATE", getCustomFormatDate(rst.getTimestamp("DELIVERY_DATE")));
                billDetails.put("CUSTOMER_NAME", rst.getString("CUSTOMER_NAME"));
                billDetails.put("QUANTITY", rst.getString("QUANTITY"));
                billDetails.put("PRICE", rst.getString("PRICE"));
                billDetails.put("ADVANCE", rst.getString("ADVANCE"));
                billDetails.put("DISCOUNT", rst.getString("DISCOUNT"));
                billDetails.put("DUE", rst.getString("DUE"));
                billDetails.put("ORDER_TYPE", rst.getString("ORDER_TYPE"));
                billDetails.put("CURRENT_STATUS", rst.getString("CURRENT_STATUS"));
                billDetails.put("SUB_STATUS", getSubStatus(OrderBillNo));
                billDetails.put("CURRENT_LOCATION", getCurrentLocation(OrderBillNo));
                billDetails.put("IMAGE_COUNT", getImageCount(ConstantContainer.APP_MODULE.ORDERS.toString(), OrderBillNo));
                billDetails.put("NOTE", rst.getString("NOTE"));
                response.addResponseValue("ORDER_DATA", billDetails.toString());
                this.generateSQLSuccessResponse(response, "Data Fetched Succesfully");
            }
        } catch (Exception e) {
            this.generateSQLExceptionResponse(response, e, "Exception getOrderDetailsForSearchQuery ");
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

    public String getOrderMobilityDetailsForSearchQuery(String OrderBillNo) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rst = null;
        ResultSet rst2 = null;
        JSONArray mobilityArray = new JSONArray();
        try {
            String SQL = "SELECT MOBILITY_UID,MAIN_STATUS,SUB_STATUS,CURRENT_LOCATION,PROCESS_DATE,NOTE FROM ORDER_MOBILITY WHERE BILL_NO = ? ORDER BY MOBILITY_UID ASC";
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, OrderBillNo);
            rst = pst.executeQuery();
            int i = 1;
            while (rst.next()) {
                JSONObject billDetails = new JSONObject();
                billDetails.put("id", Integer.toString(i));
                billDetails.put("title", rst.getString("SUB_STATUS") + " : " + rst.getString("CURRENT_LOCATION"));
                billDetails.put("text", rst.getString("MAIN_STATUS") + " : " + getCustomFormatDate(rst.getTimestamp("PROCESS_DATE")));
                if (rst.getString("SUB_STATUS").contains("NEW_ORDER")) {
                    billDetails.put("img", "/resources/Images/neworder.png");
                } else if (rst.getString("SUB_STATUS").contains("CUTTING")) {
                    billDetails.put("img", "/resources/Images/master.png");
                } else if (rst.getString("SUB_STATUS").contains("STICHING")) {
                    billDetails.put("img", "/resources/Images/tailor.jpg");
                } else if (rst.getString("SUB_STATUS").contains("FINISHING")) {
                    billDetails.put("img", "/resources/Images/finisher.png");
                } else if (rst.getString("SUB_STATUS").contains("IRON")) {
                    billDetails.put("img", "/resources/Images/iron.png");
                } else {
                    billDetails.put("img", "/resources/Images/ok.png");
                }
                billDetails.put("height", "80");
                billDetails.put("width", "280");
                billDetails.put("NOTE", rst.getString("NOTE"));
                billDetails.put("css", "myStyle");
                mobilityArray.put(billDetails);
                i = i + 1;
            }
            i = i - 1;
            for (int j = 1; j < i; j++) {
                JSONObject billDetails = new JSONObject();
                billDetails.put("id", Integer.toString(i + j));
                billDetails.put("from", Integer.toString(j));
                billDetails.put("to", Integer.toString(j + 1));
                billDetails.put("type", "dash");
                mobilityArray.put(billDetails);
            }

            response.addResponseValue("MOBILITY_DATA", mobilityArray.toString());
            this.generateSQLSuccessResponse(response, "Data Fetched Succesfully");
        } catch (Exception e) {
            this.generateSQLExceptionResponse(response, e, "Exception getOrderMobilityDetailsForSearchQuery ");
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

    public String getOrderMobilityJSON(String OrderBillNo) { //Can be Used Later to Get Order Details for Mobile
        ResponseJSONHandler response = new ResponseJSONHandler();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rst = null;
        ResultSet rst2 = null;
        JSONArray mobilityArray = new JSONArray();
        try {
            String SQL = "SELECT MAIN_STATUS,SUB_STATUS,CURRENT_LOCATION,PROCESS_DATE,NOTE FROM ORDER_MOBILITY WHERE BILL_NO = ? ORDER BY MOBILITY_UID ASC";
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, OrderBillNo);
            rst = pst.executeQuery();
            while (rst.next()) {
                JSONObject billDetails = new JSONObject();
                billDetails.put("PROCESS_DATE", getCustomFormatDate(rst.getTimestamp("PROCESS_DATE")));
                billDetails.put("MAIN_STATUS", rst.getString("MAIN_STATUS"));
                billDetails.put("SUB_STATUS", rst.getString("SUB_STATUS"));
                billDetails.put("CURRENT_LOCATION", rst.getString("CURRENT_LOCATION"));
                billDetails.put("NOTE", rst.getString("NOTE"));
                mobilityArray.put(billDetails);
            }
            response.addResponseValue("MOBILITY_DATA", mobilityArray.toString());
            this.generateSQLSuccessResponse(response, "Data Fetched Succesfully");
        } catch (Exception e) {
            this.generateSQLExceptionResponse(response, e, "Exception getOrderMobilityDetailsForSearchQuery ");
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

    public String orderSingleAssignment(JSONObject jsonParams, String UserName) {

        ResponseJSONHandler response = new ResponseJSONHandler();
        try {
            String AssignmentType = jsonParams.getString("TYPE=STR");
            String Name = jsonParams.getString("NAME=STR");
            String CurrentLocation = jsonParams.getString("LOCATION=STR");
            String Date = getParsedTimeStamp(jsonParams.getString("ASSIGNMENT_DATE=DATE")).toString();
            JSONArray List_Of_Orders = jsonParams.getJSONArray("ALL_BILL_NO");
            Map<String, String> assignmentStatusMap = new HashMap();
            int SuccessCount = 0;
            int TotalBills = List_Of_Orders.length();
            if (AssignmentType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_MASTER.toString())) {
                for (int i = 0; i < TotalBills; i++) {
                    String BillNo = List_Of_Orders.getString(i);
                    String AssignmentStatus = this.addMasterAssignment(BillNo, Name, Date, CurrentLocation);
                    SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                    assignmentStatusMap.put(BillNo, AssignmentStatus);
                }
            } else if (AssignmentType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_TAILOR.toString())) {
                for (int i = 0; i < TotalBills; i++) {
                    String BillNo = List_Of_Orders.getString(i);
                    String AssignmentStatus = this.addTailorAssignment(BillNo, Name, Date, CurrentLocation);
                    SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                    assignmentStatusMap.put(BillNo, AssignmentStatus);
                }
            } else if (AssignmentType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_FINISHER.toString())) {
                for (int i = 0; i < TotalBills; i++) {
                    String BillNo = List_Of_Orders.getString(i);
                    String AssignmentStatus = this.addFinisherAssignment(BillNo, Name, Date, CurrentLocation);
                    SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                    assignmentStatusMap.put(BillNo, AssignmentStatus);
                }
            } else if (AssignmentType.equals(ConstantContainer.ASSIGNMENTS_TYPES.TO_IRON.toString())) {
                for (int i = 0; i < TotalBills; i++) {
                    String BillNo = List_Of_Orders.getString(i);
                    String AssignmentStatus = this.addIronAssignment(BillNo, Name, Date, CurrentLocation);
                    SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                    assignmentStatusMap.put(BillNo, AssignmentStatus);
                }
            }

            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully assigned.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }

        return response.getJSONResponse();

    }

    public String updateDeliveryCompletedBulk(JSONObject jsonParams, String UserName) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        try {
            int ONLY_READY_TO_DELIVER = jsonParams.getInt("ONLY_READY_TO_DELIVER=NUM");
            String Date = getParsedTimeStamp(jsonParams.getString("DELIVERY_DATE=DATE")).toString();

            JSONArray List_Of_Orders = jsonParams.getJSONArray("ALL_BILL_NO");
            JSONObject List_Of_Discount = jsonParams.getJSONObject("DISCOUNT_LIST");
            Map<String, String> assignmentStatusMap = new HashMap();
            int SuccessCount = 0;
            int TotalBills = List_Of_Orders.length();

            for (int i = 0; i < TotalBills; i++) {
                String BillNo = List_Of_Orders.getString(i);
                int Discount = Integer.parseInt(List_Of_Discount.get(BillNo).toString());
                String AssignmentStatus = this.updateDeliveryTransaction(BillNo, Discount, Date);
                SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                assignmentStatusMap.put(BillNo, AssignmentStatus);
            }

            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully delivered.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }

        return response.getJSONResponse();

    }

    public String assignmentStatusChangeUpdate(JSONObject jsonParams, String UserName) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        Map<String, String> assignmentStatusMap = new HashMap();
        int SuccessCount = 0;
        int TotalBills = 0;
        try {
            JSONObject Form_Values = jsonParams.getJSONObject("PARAMETER_VALUES");
            JSONArray List_Of_Orders = jsonParams.getJSONArray("ALL_BILL_NO");
            String Modification_Type = Form_Values.getString("TASK=STR");
            String AssignmentType = "TO_" + Form_Values.getString("TYPE=STR");

            String AssignmentDate = Form_Values.getString("ASSIGNMENT_DATE=DATE");

            if (Modification_Type.equals("CANCEL")) {
                String MainStatus = Form_Values.getString("MAIN_STATUS=STR");
                String SubStatus = Form_Values.getString("SUB_STATUS=STR");
                String Location = Form_Values.getString("LOCATION=STR");
                TotalBills = List_Of_Orders.length();
                for (int i = 0; i < TotalBills; i++) {
                    String BillNo = List_Of_Orders.getString(i);
                    String AssignmentStatus = cancelAssignmentTransactions(BillNo, AssignmentType, AssignmentDate, MainStatus, SubStatus, Location, UserName);
                    SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                    assignmentStatusMap.put(BillNo, AssignmentStatus);
                }
            }
            if (Modification_Type.equals("CHANGE")) {
                TotalBills = List_Of_Orders.length();
                for (int i = 0; i < TotalBills; i++) {
                    String Name = Form_Values.getString("NAME=STR");
                    String BillNo = List_Of_Orders.getString(i);
                    String AssignmentStatus = this.changeAssignmentTransactions(BillNo, AssignmentType, getParsedTimeStamp(AssignmentDate).toString(), Name, "Administrator");
                    SuccessCount = (AssignmentStatus.contains("SUCCES")) ? SuccessCount + 1 : SuccessCount;
                    assignmentStatusMap.put(BillNo, AssignmentStatus);
                }
            }
            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully delivered.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String changeAssignmentTransactions(String BillNo, String AssignmentType, String AssignmentDate, String Name, String UserName) {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            String oldname = getAssignmentEmployeeName(BillNo, AssignmentType);
            String ChangeStatus = this.changeAssignmentForUnpaidOrder(BillNo, AssignmentType, AssignmentDate, Name, UserName);
            this.addOrderMobility(BillNo, AssignmentDate, getMainStatus(BillNo), getSubStatus(BillNo), getCurrentLocation(BillNo), "Assignment Changed From - " + oldname);
            int auditKey = getDistinctIntDataFromDatabase("SELECT ASSIGNMENT_UID FROM ORDER_ASSIGNMENTS WHERE BILL_NO = ? AND ASSIGNMENT_TYPE=?", new Object[]{BillNo, AssignmentType});
            this.auditor(AUDIT_TYPE.UPDATE, APP_MODULE.ORDER_ASSIGNMENTS, auditKey, BillNo, Name);
            this.getTransactionManager().commit(txStatus);
            return ChangeStatus;
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
    }

    public String cancelAssignmentTransactions(String BillNo, String AssignmentType, String AssignmentDate, String MainStatus, String SubStatus, String Location, String UserName) {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            String ParsedTime = getParsedTimeStamp(AssignmentDate).toString();
            String oldname = getAssignmentEmployeeName(BillNo, AssignmentType);
            String CancelStatus = this.cancelAssignment(BillNo, AssignmentType, UserName);
            this.addOrderMobility(BillNo, ParsedTime, MainStatus, SubStatus, Location, AssignmentType + " - Assignment Cancelled From- " + oldname);
            this.getTransactionManager().commit(txStatus);
            return CancelStatus;
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
    }

    public String addMasterAssignment(String BillNo, String MasterName, String Date, String CurrentLocation) throws Exception {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if (isOrderCuttingInProgress(BillNo)) {
                return "FAILED,Order already assigned to Master";
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
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.CUTTING_IN_PROGRESS.toString(), CurrentLocation, "Assigned with BulkMasterEntry to : " + MasterName);
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID, "Assigned with BulkMasterTailorEntry to " + MasterName);
                this.getTransactionManager().commit(txStatus);
                return "SUCCES,DATAUPDATED";
            }
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
    }

    public String updateDeliveryTransaction(String BillNo, int Discount, String Date) throws Exception {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if (false) {
                return "FAILED,Order already assigned to Master";
            } else {

                int PaymentTransactionUid = this.getColumnAutoIncrementValue("PAYMENT_TRANSACTIONS", "TRANSACTION_UID");
                this.getJdbcTemplate().update("UPDATE ORDERS SET CURRENT_STATUS = 'DELIVERY_COMPLETED' , DISCOUNT=? WHERE BILL_NO = ?", new Object[]{Discount, BillNo});
                this.getJdbcTemplate().update("INSERT INTO PAYMENT_TRANSACTIONS (TRANSACTION_UID , ORDER_BILL_NO ,PAYMENT_TYPE , AMOUNT) VALUES (?,?,'DELIVERY_PAYMENT',?)", new Object[]{PaymentTransactionUid, BillNo, this.getOrderDue(BillNo)});
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.DELIVERY_COMPLETED.toString(), "", "", "Delivery Completed , Discount - " + Discount);
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ACCOUNTS.PAYMENT_TRANSACTION, PaymentTransactionUid, "Delivery Completed ");
                mainAuditor(AUDIT_TYPE.UPDATE, APP_MODULE.ORDERS, PaymentTransactionUid, "Delivery Completed ");
                this.getTransactionManager().commit(txStatus);
                return "SUCCES,DATAUPDATED";
            }
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
    }

    public String addTailorAssignment(String BillNo, String TailorName, String Date, String CurrentLocation) throws Exception {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if (isOrderStichingInProgress(BillNo)) {
                return "FAILED,Order already assigned to Master / Tailor, change the assignment ";
            } else {
                int AsssignMentUID = this.getColumnAutoIncrementValue("ORDER_ASSIGNMENTS", "ASSIGNMENT_UID");
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
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.STICHING_IN_PROGRESS.toString(), CurrentLocation, "Assigned with BulkMasterTailorEntry to : " + TailorName);
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID + 1, "Assigned with Bulk Tailor Entry to " + TailorName);
                this.getTransactionManager().commit(txStatus);
                return "SUCCES,DATAUPDATED";
            }
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
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

    public String getOrderDetailsForAssignmentChange(JSONObject params) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        PreparedStatement pst = null;
        ResultSet rst = null;
        Connection con = null;
        ResultSet rst2 = null;
        try {
            String OrderBillNo = params.getString("BILL_NO=STR");
            if (!isBillExist(OrderBillNo)) {
                this.generateSQLExceptionResponse(response, new Exception("Bill not found"), "Bill no not Exist");
            } else {
                String assignmentType = "TO_" + params.getString("TYPE=STR");
                String SQL = "SELECT OD.BILL_NO,LOCATION,DELIVERY_DATE,QUANTITY,( SELECT TOP 1 MAIN_STATUS FROM ORDER_MOBILITY WHERE BILL_NO = ? ORDER BY MOBILITY_UID DESC) AS MAIN_STATUS, (SELECT TOP 1 SUB_STATUS FROM ORDER_MOBILITY WHERE BILL_NO = ? ORDER BY MOBILITY_UID DESC) AS SUB_STATUS,(SELECT TOP 1 CURRENT_LOCATION FROM ORDER_MOBILITY WHERE BILL_NO = ? ORDER BY MOBILITY_UID DESC) AS CURRENT_LOCATION ,ORDER_TYPE, ASSIGNMENT_DATE,EMPLOYEE_NAME,WAGE_AMOUNT,WAGE_STATUS FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OA ON OD.BILL_NO=OA.BILL_NO WHERE OD.BILL_NO=? AND OA.ASSIGNMENT_TYPE=?";
                con = this.getJDBCConnection();
                pst = con.prepareStatement(SQL);
                pst.setString(1, OrderBillNo);
                pst.setString(2, OrderBillNo);
                pst.setString(3, OrderBillNo);
                pst.setString(4, OrderBillNo);
                pst.setString(5, assignmentType);
                rst = pst.executeQuery();
                if (rst.next()) {
                    StringBuilder data = new StringBuilder();
                    data.append(rst.getString("BILL_NO")).
                            append(",").
                            append(rst.getString("QUANTITY")).
                            append(",").
                            append(this.getCustomFormatDate(rst.getTimestamp("DELIVERY_DATE"))).
                            append(",").
                            append(rst.getString("EMPLOYEE_NAME")).
                            append(",").
                            append(this.getCustomFormatDate(rst.getTimestamp("ASSIGNMENT_DATE"))).
                            append(",").
                            append(rst.getString("MAIN_STATUS")).
                            append(",").
                            append(rst.getString("SUB_STATUS")).
                            append(",").
                            append(rst.getString("CURRENT_LOCATION")).
                            append(",").
                            append(rst.getString("WAGE_STATUS")).
                            append(",").
                            append(rst.getString("WAGE_AMOUNT")).
                            append(",").
                            append(rst.getString("ORDER_TYPE")).
                            append(",").
                            append("<img height='20px' width='20px' src='resources/Images/cancel_order.png'/>").
                            append(",").
                            append("<img height='20px' width='20px' src='resources/Images/task.png'/>");
                    response.addResponseValue("DATA", data.toString());
                    this.generateSQLSuccessResponse(response, "Bill no sucesfully added to list");
                } else {
                    this.generateSQLExceptionResponse(response, new Exception("Bll not Assigned or query mismatched"), "Bll not Assigned or query mismatched");
                }
            }
        } catch (Exception e) {
            this.generateSQLExceptionResponse(response, e, "Exception getOrderDetailsForAssignmentChange Details");
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

    public String addFinisherAssignment(String BillNo, String FinisherName, String Date, String CurrentLocation) throws Exception {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if (isOrderFinishingCompleted(BillNo)) {
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

                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.FINISHING_COMPLETED.toString(), CurrentLocation, "Assigned with Bulk Finisher Assignmenmt");
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID, "Assigned with Bulk Finisher Assignmenmt to " + FinisherName);
                this.getTransactionManager().commit(txStatus);
                return "SUCCES,DATAUPDATED";
            }
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            return "FAILED," + e.getMessage();
        }
    }

    public String addIronAssignment(String BillNo, String IronMan, String Date, String CurrentLocation) throws Exception {
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {
            if (isOrderIronCompleted(BillNo)) {
                return "FAILED,Order already assigned to Iron, change the assignment ";
            } else {
                int AsssignMentUID = this.getColumnAutoIncrementValue("ORDER_ASSIGNMENTS", "ASSIGNMENT_UID");
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
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString(), ConstantContainer.ORDER_SUB_STATUS.IRON_COMPLETED.toString(), CurrentLocation, "Assigned with BulkIronEntry");
                this.orderMobiltyUpdate(BillNo, Date, ConstantContainer.ORDER_MAIN_STATUS.READY_TO_DELIVER.toString(), "", CurrentLocation, "Automatic Status Change After Iron Completed Assigned");
                mainAuditor(AUDIT_TYPE.INSERT, APP_MODULE.ORDER_ASSIGNMENTS, AsssignMentUID, "Assigned with BulkIronEntry" + IronMan);
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
                int TotalOrder = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE  IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0,?), 1)", new Object[]{date, date}, Integer.class);
                int Ready = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) AND (CURRENT_STATUS = 'READY_TO_DELIVER' OR CURRENT_STATUS = 'DELIVERY_COMPLETED')", new Object[]{date, date}, Integer.class);
                int NotReady = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) AND (CURRENT_STATUS <> 'READY_TO_DELIVER' OR CURRENT_STATUS IS NULL) AND (CURRENT_STATUS <> 'DELIVERY_COMPLETED' OR CURRENT_STATUS IS NULL)", new Object[]{date, date}, Integer.class);
                String FormatedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
                if (TotalOrder > 0) {
                    String data[] = {"TOTAL : " + TotalOrder, "READY : " + Ready, "NOT READY : " + NotReady, FormatedDate};
                    datalist.add(data);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return datalist;
    }

    public String getOrderScheduleDayWiseBarChartData() {

        JSONArray jarray = new JSONArray();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            Date startDate = formatter.parse(getCustomFormatDate(getCurrentTimeStamp()));
            LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = start.plusDays(10);
            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                int TotalOrder = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE  IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0,?), 1)", new Object[]{date, date}, Integer.class);
                int Ready = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) AND (CURRENT_STATUS = 'READY_TO_DELIVER' OR CURRENT_STATUS = 'DELIVERY_COMPLETED')", new Object[]{date, date}, Integer.class);
                int NotReady = this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(QUANTITY),0) FROM ORDERS WHERE DELIVERY_DATE IS NOT NULL AND ORDER_TYPE <> 'CANCELLED' AND DELIVERY_DATE > = DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND DELIVERY_DATE < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1) AND (CURRENT_STATUS <> 'READY_TO_DELIVER' OR CURRENT_STATUS IS NULL) AND (CURRENT_STATUS <> 'DELIVERY_COMPLETED' OR CURRENT_STATUS IS NULL)", new Object[]{date, date}, Integer.class);

                String Day = Integer.toString(date.getDayOfMonth());

                JSONObject obj = new JSONObject();
                obj.put("READY", Integer.toString(Ready));
                obj.put("NOT_READY", Integer.toString(NotReady));
                obj.put("DATE", Day);
                obj.put("FULL_DATE", date.toString());
                jarray.put(obj);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return jarray.toString();
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
            mainQuery.setTimestamp(3, getParsedTimeStamp(PaymentDate));
            mainQuery.setTimestamp(4, getParsedTimeStamp(PaymentDate));

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
            int isFirstBill = getJdbcTemplate().queryForObject("SELECT COUNT(BILL_NO) FROM ORDERS", Integer.class);
            if (isFirstBill == 0) {
                return "1";
            }
            int lastBillNo = getJdbcTemplate().queryForObject("SELECT TOP 1 CAST(BILL_NO AS INT) AS BILL_NO FROM orders order by  CAST(BILL_NO AS INT) DESC", Integer.class);
            lastBillNo++;
            return Integer.toString(lastBillNo);
        } catch (Exception e) {
            return "";
        }

    }

    public String getDayWisePaidWagePaymentStats(String ProductionType, String Name, String PaymentDate) {
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
            TotalPiece = this.getJdbcTemplate().queryForObject("SELECT SUM(OD.QUANTITY) AS TOTAL_PIECE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=?  AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1)  ", new Object[]{ProductionType, Name, getParsedTimeStamp(PaymentDate), getParsedTimeStamp(PaymentDate)}, String.class);
            TotalWage = this.getJdbcTemplate().queryForObject("SELECT SUM(OAS.WAGE_AMOUNT) AS TOTAL_WAGE FROM ORDERS OD INNER JOIN ORDER_ASSIGNMENTS OAS ON OD.BILL_NO=OAS.BILL_NO WHERE OAS.ASSIGNMENT_TYPE =? AND OAS.EMPLOYEE_NAME=? AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1)  ", new Object[]{ProductionType, Name, getParsedTimeStamp(PaymentDate), getParsedTimeStamp(PaymentDate)}, String.class);
            TotalFinshed = this.getJdbcTemplate().queryForObject("SELECT DISTINCT SUM(OS.QUANTITY) FROM ORDERS OS INNER JOIN ORDER_ASSIGNMENTS OAS ON OS.BILL_NO=OAS.BILL_NO WHERE ASSIGNMENT_TYPE=? AND EMPLOYEE_NAME=? AND (OS.CURRENT_STATUS = 'READY_TO_DELIVER' OR OS.CURRENT_STATUS = 'DELIVERY_COMPLETED') AND OAS.WAGE_STATUS='PAID' AND OAS.PAYMENT_DATE >= DateAdd(DAY, DATEDIFF(DAY, 0, ?), 0) AND OAS.PAYMENT_DATE  < DateAdd(DAY, DATEDIFF(DAY,0, ?), 1)  ", new Object[]{ProductionType, Name, getParsedTimeStamp(PaymentDate), getParsedTimeStamp(PaymentDate)}, String.class);
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
                assignmentStatusMap.put(BillNo, PaymentStatus[0] + "," + PaymentStatus[1]);
            }
            response.setResponse_Value(new JSONObject(assignmentStatusMap));
            generateSQLSuccessResponse(response, SuccessCount + " out of " + TotalBills + " Succesfully Paid.");
        } catch (Exception e) {
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();

    }

    public String getChartDataOrderStatus(JSONObject chartParams) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        JSONArray dataArray = new JSONArray();
        try {
            String Days = chartParams.getString("DAYS");
            con = getJDBCConnection();
            pst = con.prepareStatement("SELECT CURRENT_STATUS,SUM(OD.QUANTITY) AS STOCK FROM ORDERS OD  WHERE CURRENT_STATUS <> '' AND CURRENT_STATUS <> 'DELIVERY_COMPLETED' AND CURRENT_STATUS <> 'CANCELLED'  AND ORDER_TYPE <> 'PIECE_SALE'  GROUP BY CURRENT_STATUS");
            rst = pst.executeQuery();
            while (rst.next()) {
                JSONObject obj = new JSONObject();
                obj.put("CURRENT_STATUS", rst.getString("CURRENT_STATUS"));
                obj.put("STOCK", rst.getString("STOCK"));

                if (rst.getString("CURRENT_STATUS").equals(ConstantContainer.ORDER_MAIN_STATUS.READY_TO_DELIVER.toString())) {
                    obj.put("color", "green");
                }

                if (rst.getString("CURRENT_STATUS").equals(ConstantContainer.ORDER_MAIN_STATUS.NEW_ORDER.toString())) {
                    obj.put("color", "red");
                }

                if (rst.getString("CURRENT_STATUS").equals(ConstantContainer.ORDER_MAIN_STATUS.IN_PROCESS.toString())) {
                    obj.put("color", "orange");
                }

                if (rst.getString("CURRENT_STATUS").equals(ConstantContainer.ORDER_MAIN_STATUS.DELIVERY_COMPLETED.toString())) {
                    obj.put("color", "white");
                }
                dataArray.put(obj);
            }

        } catch (Exception e) {
        } finally {
            rst.close();
            pst.close();
            con.close();
        }
        return dataArray.toString();
    }

    public String getChartDataLocationStatus(JSONObject chartParams) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        JSONArray dataArray = new JSONArray();
        try {
            int num = 0;
            String Days = chartParams.getString("DAYS");
            con = getJDBCConnection();
            pst = con.prepareStatement("SELECT dbo.GETCURRENTLOCATION(od.BILL_NO) AS CURRENT_LOCATION ,SUM(OD.QUANTITY) AS STOCK FROM ORDERS OD where DBO.GETCURRENTLOCATION(od.BILL_NO) <> '' group by  DBO.GETCURRENTLOCATION(od.BILL_NO)");
            rst = pst.executeQuery();
            while (rst.next()) {
                JSONObject obj = new JSONObject();
                obj.put("CURRENT_LOCATION", rst.getString("CURRENT_LOCATION"));
                obj.put("STOCK", rst.getString("STOCK"));
                obj.put("COLOR", UtilityDAO.getColor(num));
                dataArray.put(obj);
                num = num + 1;
            }

        } catch (Exception e) {
        } finally {
            rst.close();
            pst.close();
            con.close();
        }
        return dataArray.toString();
    }

    public static void main(String... s) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            Date startDate = formatter.parse(new DAOHelper().getCustomFormatDate(new DAOHelper().getCurrentTimeStamp()));
            LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = start.plusDays(10);
            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                System.out.println(date);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
