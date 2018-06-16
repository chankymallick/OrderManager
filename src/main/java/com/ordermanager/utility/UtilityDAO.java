/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

import com.ordermanager.backupmanager.BackUpSQLServer;
import com.ordermanager.security.FileCryptoUtils;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class UtilityDAO extends DAOHelper {

    public String isValueUnique(String Value, String TableName, String ColumnName) {
        return this.isValueExistInTable(Value, TableName, ColumnName);
    }

    public String isCompositeValueUnique(String TableName, String ColumnName1, String ColumnName2, String Value1, String Value2) {
        return this.isCompositeValueExistInTable(TableName, ColumnName1, ColumnName2, Value1, Value2);
    }

    public String saveUpdateDefaultFormValue(String Value, String Key, String Module) {
        return this.saveUpdateDefaultFormValue(Module, Key, Value);
    }

    /**
     * It is used to get App data for HTTP request , it returns blank {} json if
     * nothing is there , internally it is calling getAppData
     *
     * @param Module Name of the Module like Ex.addNewForm , updateOrder
     * @param Key Name of the Key to get Value
     * @return
     */
    public String getApplicationData(String Module, String Key) {
        String appdata = this.getAppData(Module, Key);
        if (appdata.equals("")) {
            return "{}";
        } else {
            return appdata;
        }
    }

    /**
     * It returns all Field of Order Form combined with existing Value fetched
     * from Orders Table and Default form data from APP_DATA table
     *
     * @param BILL_NO
     * @return
     */
    public JSONObject getdefaultDataOrderFormWithExistingData(String BILL_NO) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst3 = null;
        ResultSet rstPayment = null;
        ResultSet rstOrder = null;
        ResultSet rstSubStatusAndLocation = null;
        try {
            JSONObject defaultData = new JSONObject(this.getApplicationData("FORM_DEFAULT_VALUE", "addNewOrder"));
            con = this.getJDBCConnection();
            pst = con.prepareStatement("SELECT *FROM ORDERS WHERE BILL_NO =?");
            pst.setString(1, BILL_NO);
            rstOrder = pst.executeQuery();
            pst1 = con.prepareStatement("SELECT SUM(AMOUNT) AS TOTAL_PAID FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO =? AND (PAYMENT_TYPE=? OR PAYMENT_TYPE=?)");
            pst1.setString(1, BILL_NO);
            pst1.setString(2, ConstantContainer.PAYMENT_TYPE.ADVANCE.toString());
            pst1.setString(3, ConstantContainer.PAYMENT_TYPE.RE_ADVANCE.toString());
            rstPayment = pst1.executeQuery();

            pst3 = con.prepareStatement("SELECT TOP 1 MAIN_STATUS,SUB_STATUS,CURRENT_LOCATION FROM ORDER_MOBILITY WHERE BILL_NO=? ORDER BY MOBILITY_UID DESC");
            pst3.setString(1, BILL_NO);
            rstSubStatusAndLocation = pst3.executeQuery();

            if (rstSubStatusAndLocation.next()) {
                defaultData.put("ORDER_STATUS=STR", rstSubStatusAndLocation.getString("MAIN_STATUS"));
                defaultData.put("ORDER_SUB_STATUS=STR", rstSubStatusAndLocation.getString("SUB_STATUS"));
                defaultData.put("CURRENT_LOCATION=STR", rstSubStatusAndLocation.getString("CURRENT_LOCATION"));
            }

            if (rstPayment.next()) {
                defaultData.put("ADVANCE=NUM", rstPayment.getInt("TOTAL_PAID"));
            }
            while (rstOrder.next()) {
                if (rstOrder.getString("LOCATION") != null) {
                    defaultData.put("LOCATION=STR", rstOrder.getString("LOCATION"));
                }
                if (rstOrder.getString("DELIVERY_DATE") != null) {
                    defaultData.put("DELIVERY_DATE=DATE", getCustomFormatDate(rstOrder.getTimestamp("DELIVERY_DATE")));
                }
                if (rstOrder.getString("CUSTOMER_NAME") != null) {
                    defaultData.put("CUSTOMER_NAME=STR", rstOrder.getString("CUSTOMER_NAME"));
                }
                if (rstOrder.getString("CONTACT_NO") != null) {
                    defaultData.put("CONTACT_NO=STR", rstOrder.getString("CONTACT_NO"));
                }
                if (rstOrder.getString("PIECE_VENDOR") != null) {
                    defaultData.put("PIECE_VENDOR=STR", rstOrder.getString("PIECE_VENDOR"));
                }
                if (rstOrder.getString("QUANTITY") != null) {
                    defaultData.put("QUANTITY=STR", rstOrder.getInt("QUANTITY"));
                }
                if (rstOrder.getString("PRODUCT_TYPE") != null) {
                    defaultData.put("PRODUCT_TYPE=STR", rstOrder.getString("PRODUCT_TYPE"));
                }
                if (rstOrder.getString("ORDER_TYPE") != null) {
                    defaultData.put("ORDER_TYPE=STR", rstOrder.getString("ORDER_TYPE"));
                }
                if (rstOrder.getString("MEASURED_BY") != null) {
                    defaultData.put("MEASURED_BY=STR", rstOrder.getString("MEASURED_BY"));
                }
                if (rstOrder.getString("MEASURED_BY") != null) {
                    defaultData.put("MEASURED_BY=STR", rstOrder.getString("MEASURED_BY"));
                }
                if (rstOrder.getString("CUSTOM_PRICE_ENABLED") != null) {
                    defaultData.put("CUSTOM_RATE=NUM", rstOrder.getInt("CUSTOM_PRICE_ENABLED"));
                }
                if (rstOrder.getString("NOTE") != null) {
                    defaultData.put("NOTE=STR", rstOrder.getString("NOTE"));
                }

                defaultData.put("ORDER_DATE=DATE", getCustomFormatDate(rstOrder.getTimestamp("ORDER_DATE")));
                defaultData.put("PRICE=NUM", rstOrder.getString("PRICE"));
                defaultData.put("BILL_NO=STR", rstOrder.getString("BILL_NO"));
                defaultData.put("VERIFY_BILL_NO=STR", rstOrder.getString("BILL_NO"));

            }
            return defaultData;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                pst.close();
                pst1.close();
                pst3.close();
                rstPayment.close();
                rstOrder.close();
                rstSubStatusAndLocation.close();
                con.close();

            } catch (Exception e) {
            }

        }
    }

    public JSONObject getItemDetails(String ItemName) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rstItemData = null;

        try {
            con = getJDBCConnection();
            pst = con.prepareStatement("SELECT  ITEM_UID,ITEM_NAME, ITEM_TYPE,PARENT_ITEM,ITEM_SUB_TYPE,MASTER_PRICE,TAILOR_PRICE,FINISHER_PRICE,ISNULL(ITEM_ORDER,'')AS ITEM_ORDER,ACTIVE,NOTE FROM ITEMS WHERE ITEM_NAME=?");
            pst.setString(1, ItemName);
            rstItemData = pst.executeQuery();
            JSONObject itemData = new JSONObject();
            if (rstItemData.next()) {
                itemData.put("ITEM_UID=NUM", rstItemData.getInt("ITEM_UID"));
                itemData.put("ITEM_NAME=STR", rstItemData.getString("ITEM_NAME"));
                itemData.put("ITEM_TYPE=STR", rstItemData.getString("ITEM_TYPE"));
                itemData.put("ITEM_SUB_TYPE=STR", rstItemData.getString("ITEM_SUB_TYPE"));
                itemData.put("PARENT_ITEM=STR", rstItemData.getString("PARENT_ITEM"));
                itemData.put("MASTER_PRICE=NUM", rstItemData.getInt("MASTER_PRICE"));
                itemData.put("TAILOR_PRICE=NUM", rstItemData.getInt("TAILOR_PRICE"));
                itemData.put("FINISHER_PRICE=NUM", rstItemData.getInt("FINISHER_PRICE"));
                itemData.put("ACTIVE=NUM", rstItemData.getString("ACTIVE"));
                itemData.put("NOTE=STR", rstItemData.getString("NOTE"));
            }
            return itemData;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                pst.close();
                rstItemData.close();
                con.close();
            } catch (Exception e) {
            }

        }

    }

    public Map<String, Object> getOrderPaymentData(String BILL_NO) {
        Connection con = null;
        PreparedStatement pstAdvance = null;
        PreparedStatement pstOrderData = null;
        ResultSet rstAdvance = null;
        ResultSet rstOrderData = null;

        Map<String, Object> orderData = new HashMap();
        int TotalAdvance = 0;
        try {
            ArrayList<String[]> billData = new ArrayList();
            con = this.getJDBCConnection();
            pstAdvance = con.prepareStatement("SELECT '20/05/16' AS TRANSACTION_DATE,PAYMENT_TYPE,AMOUNT FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO=?");
            pstAdvance.setString(1, BILL_NO);
            rstAdvance = pstAdvance.executeQuery();
            while (rstAdvance.next()) {
                String P_type = rstAdvance.getString("PAYMENT_TYPE");
//                for(int i =rstAdvance.getString("PAYMENT_TYPE").length();i<=10;i++){
//                P_type+="&nbsp;";
//                }
                String[] rows = new String[]{rstAdvance.getString("TRANSACTION_DATE"), P_type, rstAdvance.getString("AMOUNT")};
                TotalAdvance += rstAdvance.getInt("AMOUNT");
                billData.add(rows);
            }
            orderData.put("PAYMENT_DATA", billData);
            int Due = this.getOrderDue(BILL_NO);
            pstOrderData = con.prepareStatement("SELECT TOP 1 OD.ORDER_DATE, OD.DELIVERY_DATE,OD.PRICE,OM.MAIN_STATUS,OM.SUB_STATUS,CURRENT_LOCATION FROM ORDERS OD INNER JOIN ORDER_MOBILITY OM ON OD.BILL_NO=OM.BILL_NO WHERE OD.BILL_NO=? ORDER BY OM.MOBILITY_UID DESC");
            pstOrderData.setString(1, BILL_NO);
            rstOrderData = pstOrderData.executeQuery();
            while (rstOrderData.next()) {
                orderData.put("ORDER_DATE", getCustomFormatDate(rstOrderData.getTimestamp("ORDER_DATE")));
                orderData.put("DELIVERY_DATE", getCustomFormatDate(rstOrderData.getTimestamp("DELIVERY_DATE")));
                orderData.put("PRICE", rstOrderData.getString("PRICE"));
                orderData.put("MAIN_STATUS", rstOrderData.getString("MAIN_STATUS"));
                orderData.put("SUB_STATUS", rstOrderData.getString("SUB_STATUS"));
                orderData.put("CURRENT_LOCATION", rstOrderData.getString("CURRENT_LOCATION"));
                orderData.put("DUE", Due);
            }
            orderData.put("TOTAL_ADVANCE", Integer.toString(TotalAdvance));
            return orderData;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                pstAdvance.close();
                rstAdvance.close();
                pstOrderData.close();
                rstAdvance.close();
                con.close();
            } catch (Exception e) {
            }

        }
    }

    /**
     * To get all Values from table as Select Options for DHTMLX Forms , It
     * returns ResponseHandler JSON which only parsable in JavaScript USAGE : It
     * can be used to get Dynamic List of Select Options from Javascript in
     * ({"text":"name","value":"chanky"}) format
     *
     * @param TableName Table from where to fetch
     * @param ColumnName List will be fetched from this column
     * @param QueryColumn Condition Column name
     * @param QueryValue Condition Column value
     * @return ResponseHandle JSON String
     * @throws SQLException
     */
    public String getComboValues(String TableName, String ColumnName, String QueryColumn, String QueryValue) throws SQLException {
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement pst = null;
        ResponseJSONHandler rspj = new ResponseJSONHandler();
        try {
            String SQL = "SELECT DISTINCT " + ColumnName + " FROM " + TableName + " WHERE " + QueryColumn + " = ?";
            JSONArray jsonArray = new JSONArray();
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, QueryValue);
            rst = pst.executeQuery();
            JSONObject jsonObj2 = new JSONObject();
            jsonObj2.put("text", "");
            jsonObj2.put("value", "");
            jsonArray.put(jsonObj2);
            while (rst.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("text", rst.getString(ColumnName).replace("_", " "));
                jsonObj.put("value", rst.getString(ColumnName));
                jsonArray.put(jsonObj);
            }
            rspj.addResponseValue("VALUES", jsonArray.toString());
            generateSQLSuccessResponse(rspj, "Successfully Loaded");
            return rspj.getJSONResponse();
        } catch (Exception e) {
            generateSQLExceptionResponse(rspj, e, "Select options Loading Failed");
            return rspj.getJSONResponse();
        } finally {
            rst.close();
            pst.close();
            con.close();
        }

    }

    public String getComboValuesForCustomTag(String TableName, String ColumnName, boolean Blank) throws SQLException {
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement pst = null;
        ResponseJSONHandler rspj = new ResponseJSONHandler();
        try {
            String SQL = "SELECT DISTINCT " + ColumnName + " FROM " + TableName;
            JSONArray jsonArray = new JSONArray();
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            rst = pst.executeQuery();
            if (Blank) {
                JSONObject jsonObj2 = new JSONObject();
                jsonObj2.put("text", "");
                jsonObj2.put("value", "");
                jsonArray.put(jsonObj2);
            }
            while (rst.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("text", rst.getString(ColumnName).replace("_", " "));
                jsonObj.put("value", rst.getString(ColumnName));
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "";
        } finally {
            rst.close();
            pst.close();
            con.close();
        }
    }

    public String getComboValuesForCustomTagWithQuery(String TableName, String ColumnName, String QueryColumn, String QueryValue, boolean Blank) {
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement pst = null;
        try {
            String SQL = "SELECT DISTINCT " + ColumnName + " FROM " + TableName + " WHERE " + QueryColumn + "= ?";
            JSONArray jsonArray = new JSONArray();
            con = this.getJDBCConnection();
            pst = con.prepareStatement(SQL);
            pst.setString(1, QueryValue);
            rst = pst.executeQuery();
            if (Blank) {
                JSONObject jsonObj2 = new JSONObject();
                jsonObj2.put("text", "");
                jsonObj2.put("value", "");
                jsonArray.put(jsonObj2);
            }
            while (rst.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("text", rst.getString(ColumnName).replace("_", " "));
                jsonObj.put("value", rst.getString(ColumnName));
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "";
        } finally {
            try {
                rst.close();
                pst.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean isDbConnected123() {
        try {
            int state = this.getJdbcTemplate().queryForObject("SELECT 1", Integer.class);
            if (state == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public String createAndUploadDatabaseBackUp() {
        ResponseJSONHandler rsp = new ResponseJSONHandler();
        Map<String, String> stepResults = new LinkedHashMap<>();
        String FilePath = null;
        String FileName = "";
        String DatabseName = PropertyFileReader.getPropertiesFileValue("DB_DATABASE_NAME");
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                FilePath = PropertyFileReader.getPropertiesFileValue("DB_FILE_PATH");
            }
            FileName = DatabseName.concat("_").concat(this.getCustomFormatDate(this.getCurrentTimeStamp()).replace("/", "_").concat("_").concat(Long.toString(this.getCurrentTimeStamp().getTime()))).concat("_").concat(PropertyFileReader.getPropertiesFileValue("DB_SCHEMA_VERSION")).concat(".bak");
            String status = BackUpSQLServer.createBackUpFile(FilePath, FileName, DatabseName, this.getJdbcTemplate());
            stepResults.put("CREATING BACKUP FILE", status);
            this.generateSQLSuccessResponse(rsp, "BACK UP EVENT COMPLETED");
            rsp.addResponseValue("STEPS", new JSONObject(stepResults));
            return rsp.getJSONResponse();
        } catch (Exception e) {
            generateSQLExceptionResponse(rsp, e, "BACK UP FAILED");
            return rsp.getJSONResponse();
        }
    }

    public String setImage(String imageData) {
        ResponseJSONHandler response = new ResponseJSONHandler();
        TransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = this.getTransactionManager().getTransaction(txDef);
        try {            
            JSONObject paramData = new JSONObject(imageData);
            String Module = paramData.getString("MODULE");
            String ImageKey = paramData.getString("IMAGE_KEY");
            String Image = paramData.getString("IMAGE");
            String Note = paramData.getString("NOTE");
            if(Module.equals("") || ImageKey.equals("") || Image.equals("")){
            throw new Exception("Image data missing");
            }
            int UID = getColumnAutoIncrementValue("IMAGE_STORE", "IMAGE_ID");
            int InsertStatus = getJdbcTemplate().update("INSERT INTO IMAGE_STORE (IMAGE_ID,MODULE_NAME,IMAGE_KEY,IMAGE,NOTE ) VALUES (?,?,?,?,?)  ", new Object[]{UID, Module, ImageKey, Image, Note});
            mainAuditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.IMAGE_STORE, UID, "Image Saved Key=" + ImageKey);
            generateSQLSuccessResponse(response, " - Image saved Succesfully");
            this.getTransactionManager().commit(txStatus);
        } catch (Exception e) {
            this.getTransactionManager().rollback(txStatus);
            generateSQLExceptionResponse(response, e, "Exception ... see Logs");
        }
        return response.getJSONResponse();
    }

    public String getImage(String Module, String Key) {
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rst = null;
        ResponseJSONHandler rsp = new ResponseJSONHandler();
        try {
            con = getJDBCConnection();
            pst = con.prepareStatement("SELECT IMAGE_ID, MODULE_NAME,IMAGE_KEY, IMAGE ,NOTE,IMAGE_DATE FROM IMAGE_STORE WHERE MODULE_NAME = ? AND IMAGE_KEY= ? ORDER BY IMAGE_DATE ASC");
            pst.setString(1, Module);
            pst.setString(2, Key);
            rst = pst.executeQuery();

//            ArrayList<Map<String, String>> imageList = new ArrayList<Map<String, String>>();
            JSONArray imageArray = new JSONArray();
            while (rst.next()) {
                Map<String, String> imageData = new HashMap<String, String>();
                imageData.put("ID", rst.getString("IMAGE_ID"));
                imageData.put("MODULE_NAME", rst.getString("MODULE_NAME"));
                imageData.put("IMAGE_KEY", rst.getString("IMAGE_KEY"));
                imageData.put("IMAGE", rst.getString("IMAGE"));
                imageData.put("IMAGE_DATE", getCustomFormatDate(rst.getTimestamp("IMAGE_DATE")));
                imageData.put("NOTE", rst.getString("NOTE"));
                imageArray.put(imageData);
                //rsp.addResponseValue(rst.getString("IMAGE_ID"), new JSONObject(imageData));
            }
            // rsp.addResponseValue("ALL_IMAGES", new JSONObject(imageList).toString());
            //generateSQLSuccessResponse(rsp, "IMAGE FETCHED");
            return imageArray.toString();
        } catch (Exception e) {
            return null;
            //generateSQLExceptionResponse(rsp, e, "Image Fetch Error");
        } finally {
            try {
                rst.close();
                pst.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

}
