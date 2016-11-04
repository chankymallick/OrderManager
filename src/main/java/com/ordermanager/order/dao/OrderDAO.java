package com.ordermanager.order.dao;

import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            String MainItemName =  paramData.get("ORDER_TYPE=STR").toString(); 
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
            paramData.put("ORDER_UID=NUM", UID);           
            int InsertStatus = getJdbcTemplate().update(getSimpleSQLInsert(paramData, "ORDERS"));      
            int Order_Item_Uid_For_Main_Item = this.getColumnAutoIncrementValue("ORDER_ITEMS", "ORDER_ITEMS_UID");
            int mainItem = getJdbcTemplate().update("INSERT INTO ORDER_ITEMS VALUES (?,?,?,?)", new Object[]{Order_Item_Uid_For_Main_Item,paramData.get("BILL_NO=STR"),MainItemName,""});
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

    public List<Object> getGridData(String TableName, String Order_Column) {
        String SQL = "SELECT *FROM " + TableName + " ORDER BY " + Order_Column + " DESC";
        return this.getJSONDataForGrid(SQL);
    }

    public List<Object> getGridDataForOrders() {
        String SQL = "SELECT TOP 50 BILL_NO,ORDER_DATE,DELIVERY_DATE,ORDER_STATUS,PIECE_VENDOR,PRICE,ORDER_TYPE, PRODUCT_TYPE FROM ORDERS ORDER BY ORDER_UID DESC";
        return this.getJSONDataForGrid(SQL);
    }

}
