/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Maliick
 */
public class UtilityDAO extends DAOHelper {

    public String isValueUnique(String Value, String TableName, String ColumnName) {
        return this.isValueExistInTable(Value, TableName, ColumnName);
    }
    public String isCompositeValueUnique(String TableName,String  ColumnName1,String  ColumnName2,String Value1,String Value2) {
        return this.isCompositeValueExistInTable(TableName, ColumnName1,ColumnName2,Value1,Value2);
    }

    public String saveUpdateDefaultFormValue(String Value, String Key, String Module) {
        return this.saveUpdateDefaultFormValue(Module, Key, Value);
    }

    public String getApplicationData(String Module, String Key) {
        String appdata = this.getAppData(Module, Key);
        if (appdata.equals("")) {
            return "{}";
        } else {
            return appdata;
        }
    }

    public JSONObject getdefaultDataOrderFormWithExistingData(String BILL_NO) {
        try {
            JSONObject defaultData = new JSONObject(this.getApplicationData("FORM_DEFAULT_VALUE", "addNewOrder"));
            Connection con = this.getJDBCConnection();

            PreparedStatement pst = con.prepareStatement("SELECT *FROM ORDERS WHERE BILL_NO =?");
            pst.setString(1, BILL_NO);
            ResultSet rstOrder = pst.executeQuery();
            PreparedStatement pst1 = con.prepareStatement("SELECT SUM(AMOUNT) AS TOTAL_PAID FROM PAYMENT_TRANSACTIONS WHERE ORDER_BILL_NO =? AND PAYMENT_TYPE=? OR PAYMENT_TYPE=? ");
            pst1.setString(1, BILL_NO);
            pst1.setString(2, ConstantContainer.PAYMENT_TYPE.ADVANCE.toString());
            pst1.setString(3, ConstantContainer.PAYMENT_TYPE.RE_ADVANCE.toString());
            ResultSet rstPayment = pst1.executeQuery();

            PreparedStatement pst2 = con.prepareStatement("SELECT MAIN_STATUS FROM ORDER_MOBILITY WHERE BILL_NO = ? AND  PROCESS_DATE = (SELECT MAX(PROCESS_DATE) FROM ORDER_MOBILITY WHERE BILL_NO = ?)");
            pst2.setString(1, BILL_NO);
            pst2.setString(2, BILL_NO);
            ResultSet rstStatus = pst2.executeQuery();
            if (rstPayment.next()) {
                defaultData.put("ADVANCE=NUM", rstPayment.getInt("TOTAL_PAID"));
            }
            if (rstStatus.next()) {
                defaultData.put("ORDER_STATUS=STR", rstStatus.getString("MAIN_STATUS"));
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
        }
    }

    public String getComboValues(String TableName, String ColumnName, String QueryColumn, String QueryValue)throws SQLException {
         Connection con = null;
         ResponseJSONHandler rspj = new ResponseJSONHandler();
        try {            
            String SQL = "SELECT DISTINCT " + ColumnName + ", STATUS_ORDER FROM " + TableName + " WHERE " + QueryColumn + " = ? ORDER BY STATUS_ORDER ASC";
            JSONArray jsonArray = new JSONArray();
            con = this.getJDBCConnection();
            PreparedStatement pst = con.prepareStatement(SQL);
            pst.setString(1, QueryValue);
            ResultSet rst = pst.executeQuery();
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
            rspj.addResponseValue("VALUES",jsonArray.toString());
            generateSQLSuccessResponse(rspj, "Successfully Loaded");
            return rspj.getJSONResponse();
        } catch (Exception e) {         
            generateSQLExceptionResponse(rspj, e, "Select options Loading Failed");
            return rspj.getJSONResponse();
        }
        finally{
        con.close();
        }
      
    }
    public String getComboValuesForCustomTag(String TableName, String ColumnName,boolean Blank){
         Connection con = null;
         ResponseJSONHandler rspj = new ResponseJSONHandler();
        try {            
            String SQL = "SELECT DISTINCT " + ColumnName + " FROM " + TableName;           
            JSONArray jsonArray = new JSONArray();
            con = this.getJDBCConnection();
            PreparedStatement pst = con.prepareStatement(SQL);           
            ResultSet rst = pst.executeQuery();
             if(Blank){
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
        }
        finally{
            try {
                con.close();
            } catch (Exception e) {
            }   
        }      
    }
    public String getComboValuesForCustomTagWithQuery(String TableName, String ColumnName,String QueryColumn,String QueryValue, boolean Blank){
         Connection con = null;
         ResponseJSONHandler rspj = new ResponseJSONHandler();
        try {            
            String SQL = "SELECT DISTINCT " + ColumnName + " FROM " + TableName+ " WHERE "+ QueryColumn+"= ?";           
            JSONArray jsonArray = new JSONArray();
            con = this.getJDBCConnection();
            PreparedStatement pst = con.prepareStatement(SQL);     
            pst.setString(1, QueryValue);
            ResultSet rst = pst.executeQuery();
             if(Blank){
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
        }
        finally{
            try {
                con.close();
            } catch (Exception e) {
            }   
        }      
    }
}
