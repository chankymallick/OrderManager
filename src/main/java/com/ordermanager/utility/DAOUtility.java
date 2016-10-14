/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Maliick
 */
public class DAOUtility {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getColumnAutoIncrementValue(String tableName, String ColumnName) {
        int maxNumber = jdbcTemplate.queryForObject("SELECT ISNULL(MAX(" + ColumnName + "),0) FROM " + tableName, Integer.class);
        return ++maxNumber;
    }

    public Connection getJDBCConnection() {
        try {
            return this.jdbcTemplate.getDataSource().getConnection();
        } catch (Exception e) {
            return null;
        }

    }

    public ResponseJSONHandler generateSQLExceptionResponse(ResponseJSONHandler responseJSON, Exception ex, String Message) {
        responseJSON.setResponse_Status(ConstantContainer.ResponseJSONStatus.FAILED.toString());
        responseJSON.setResponse_Message(Message);
        responseJSON.addResponseValue("EXCEPTION_MESSAGE", ex.getMessage());
        return responseJSON;
    }

    public ResponseJSONHandler generateSQLSuccessResponse(ResponseJSONHandler responseJSON, String Message) {
        responseJSON.setResponse_Status(ConstantContainer.ResponseJSONStatus.SUCCESS.toString());
        responseJSON.setResponse_Message(Message);
        return responseJSON;
    }

    public String getSimpleSQLInsert(JSONObject jsonParam, String tableName) {
        StringBuffer InsertQuery = new StringBuffer();
        StringBuffer ColumnNames = new StringBuffer();
        StringBuffer ColumnValues = new StringBuffer();
        InsertQuery.append("INSERT INTO ").append(tableName).append(" ");
        ColumnNames.append("(");
        ColumnValues.append("(");
        for (String Key : jsonParam.keySet()) {
            String ParamNameValue[] = Key.split("=");
            if ("NUM".equalsIgnoreCase(ParamNameValue[1])) {
                ColumnNames.append(ParamNameValue[0]).append(",");
                ColumnValues.append(jsonParam.get(Key)).append(",");
            }
            if ("STR".equalsIgnoreCase(ParamNameValue[1])) {
                ColumnNames.append(ParamNameValue[0]).append(",");
                ColumnValues.append("'").append(jsonParam.get(Key)).append("'").append(",");
            }
        }
        if (',' == ColumnNames.charAt(ColumnNames.length() - 1)) {
            ColumnNames.deleteCharAt(ColumnNames.length() - 1);
        }
        if (',' == ColumnValues.charAt(ColumnValues.length() - 1)) {
            ColumnValues.deleteCharAt(ColumnValues.length() - 1);
        }
        ColumnNames.append(")");
        ColumnValues.append(")");
        InsertQuery.append(ColumnNames).append(" ").append("VALUES").append(" ").append(ColumnValues);
        return InsertQuery.toString();
    }

    public List<Object> getJSONDataForGrid(String SQLQuery) {          
        List <Object> allRows = new ArrayList();
        List <String> columnNames = new ArrayList();
        List <Object> returnValues = new ArrayList();
        int totalCoumn = 0;
        try {
            Connection con = this.jdbcTemplate.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(SQLQuery);
            ResultSet rst = pst.executeQuery();
            ResultSetMetaData rsMetadata = rst.getMetaData();
            totalCoumn = rsMetadata.getColumnCount();
            for (int i = 1; i <= totalCoumn; i++) {
                columnNames.add(rsMetadata.getColumnLabel(i));
            }
            while (rst.next()) {
                JSONObject jsonRow = new JSONObject();
                for (int i = 1; i <= totalCoumn; i++) {
                    jsonRow.put(rsMetadata.getColumnLabel(i), rst.getString(rsMetadata.getColumnLabel(i)));
                }
                allRows.add(jsonRow);
            }          
            returnValues.add(allRows);
            returnValues.add(columnNames);
            rst.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return returnValues;
    }

}
