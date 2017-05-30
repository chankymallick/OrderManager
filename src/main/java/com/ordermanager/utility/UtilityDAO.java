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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean isDbConnected123(){
        try {
            int state = this.getJdbcTemplate().queryForObject("SELECT 1", Integer.class);
            if(state == 1)
            return true;
            else
            return false;            
        } catch (Exception e) {
            return false;
        }
    
    }
}
   
