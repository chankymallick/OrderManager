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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author Maliick
 */
public class DAOHelper extends ConstantContainer {

    private JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager txManager) {
        this.transactionManager = txManager;
    }

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

    public String isValueExistInTable(String Value, String TableName, String ColumnName) {
        ResponseJSONHandler rspJSON = new ResponseJSONHandler();
        try {

            String SQL = new StringBuilder("SELECT COUNT(").append(ColumnName).append(") FROM ").append(TableName).append(" WHERE ").append(ColumnName).append("=?").toString();
            int dataCount = this.jdbcTemplate.queryForObject(SQL, new Object[]{Value}, Integer.class);
            if (dataCount > 0) {
                this.generateSQLSuccessResponse(rspJSON, Value + " : Value already Exist");
                rspJSON.addResponseValue("UNIQUE", "FALSE");
            } else {
                this.generateSQLSuccessResponse(rspJSON, Value + " : Value is Unique");
                rspJSON.addResponseValue("UNIQUE", "TRUE");
            }

        } catch (DataAccessException e) {
            this.generateSQLExceptionResponse(rspJSON, e, "Operation Failed , Check Logs");
        }
        return rspJSON.getJSONResponse();

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

    public String getSimpleSQLInsert(JSONObject jsonParam, String tableName) throws Exception {
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
            if ("DATE".equalsIgnoreCase(ParamNameValue[1])) {
                ColumnNames.append(ParamNameValue[0]).append(",");
                ColumnValues.append("'").append(getParsedTimeStamp((String) jsonParam.get(Key))).append("'").append(",");
            }
            if ("DATE_AUTO".equalsIgnoreCase(ParamNameValue[1])) {
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
        List<Object> allRows = new ArrayList();
        List<String> columnNames = new ArrayList();
        List<Object> returnValues = new ArrayList();
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
                Map<String, String> row = new HashMap();
                for (int i = 1; i <= totalCoumn; i++) {
                    if (rsMetadata.getColumnType(i) == 93) {
                        row.put(rsMetadata.getColumnLabel(i), this.getStringTime(rst.getTimestamp(rsMetadata.getColumnLabel(i))));
                    } else {
                        row.put(rsMetadata.getColumnLabel(i), rst.getString(rsMetadata.getColumnLabel(i)));
                    }
                }
                allRows.add(row);
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

    public String saveAndUpdateAppData(String Module, String Key, String Value) {
        ResponseJSONHandler rsph = new ResponseJSONHandler();
        try {
            int valueExist = this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM APP_DATA WHERE APP_DATA_MODULE = ? AND APP_DATA_KEY=? ", new Object[]{Module, Key}, Integer.class);
            if (valueExist > 0) {
                int updatedRows = this.jdbcTemplate.update("UPDATE APP_DATA SET APP_DATA_VALUE=? WHERE APP_DATA_MODULE=? AND APP_DATA_KEY=?", new Object[]{Value, Module, Key});
                this.generateSQLSuccessResponse(rsph, "Default data updated Succesfully");
            } else {
                int savedRows = this.jdbcTemplate.update("INSERT INTO APP_DATA (APP_DATA_UID,APP_DATA_MODULE,APP_DATA_KEY,APP_DATA_VALUE) VALUES(?,?,?,?)", new Object[]{this.getColumnAutoIncrementValue("APP_DATA", "APP_DATA_UID"), Module, Key, Value});
                this.generateSQLSuccessResponse(rsph, "Default data added Succesfully");
            }
        } catch (Exception e) {
            this.generateSQLExceptionResponse(rsph, e, "Exception , see logs");
        }
        return rsph.getJSONResponse();
    }

    public String getAppData(String Module, String Key) {
        ResponseJSONHandler rsph = new ResponseJSONHandler();
        try {
            String Value = this.getJdbcTemplate().queryForObject("SELECT APP_DATA_VALUE FROM  APP_DATA WHERE APP_DATA_MODULE = ? AND APP_DATA_KEY=? ", new Object[]{Module, Key}, String.class);
            return Value;
        } catch (EmptyResultDataAccessException e) {
            return "";
        } catch (Exception e) {
            this.generateSQLExceptionResponse(rsph, e, "Exception , see logs");
            return "";
        }
    }

    public Timestamp getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        return timestamp;
    }

    public String getDatePartOfTimestamp(String valueFromDB) {
        try{
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");   
        Date d1 = sdf1.parse(valueFromDB);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateWithoutTime = sdf.format(d1);
        return dateWithoutTime;
        }
        catch(Exception e){
        return null;
        }
    }

    public Timestamp getParsedTimeStamp(String Date) throws Exception {
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd/MM/yy");
        Date lFromDate1 = datetimeFormatter1.parse(Date);
        Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());
        return fromTS1;
    }

    public String getStringTime(Timestamp Date) throws Exception {
        long date = Date.getTime();
        Date dateObj = new Date(date);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        String text = df.format(dateObj);
        return text;
    }

    public boolean auditor(ConstantContainer.AUDIT_TYPE Type, ConstantContainer.APP_MODULE Module, int AuditKey, String AuditHistory, String Note) {
        try {
            int autoUID = this.getColumnAutoIncrementValue("AUDIT", "AUDIT_UID");
            String currentUser = "Administrator";
            int insertedRows = this.jdbcTemplate.update("INSERT INTO AUDIT (AUDIT_UID,AUDIT_TYPE,AUDIT_MODULE,AUDIT_DATETIME,AUDITED_BY,AUDIT_KEY,AUDIT_HISTORY,NOTE) VALUES (?,?,?,?,?,?,?,?)", new Object[]{autoUID, Type.toString(), Module.toString(), this.getCurrentTimeStamp(), currentUser, AuditKey, AuditHistory, Note});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean mainAuditor(ConstantContainer.AUDIT_TYPE Type, ConstantContainer.APP_MODULE Module, int AuditKey, String AuditHistory) {
        try {
            int autoUID = this.getColumnAutoIncrementValue("AUDIT", "AUDIT_UID");
            String currentUser = "Administrator";
            int insertedRows = this.jdbcTemplate.update("INSERT INTO AUDIT (AUDIT_UID,AUDIT_TYPE,AUDIT_MODULE,AUDIT_DATETIME,AUDITED_BY,AUDIT_KEY,AUDIT_HISTORY,NOTE) VALUES (?,?,?,?,?,?,?,?)", new Object[]{autoUID, Type.toString(), Module.toString(), this.getCurrentTimeStamp(), currentUser, AuditKey, AuditHistory, ""});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean auditor(ConstantContainer.AUDIT_TYPE Type, ConstantContainer.APP_MODULE Module) {
        try {
            int autoUID = this.getColumnAutoIncrementValue("AUDIT", "AUDIT_UID");
            String currentUser = "Administrator";
            int insertedRows = this.jdbcTemplate.update("INSERT INTO AUDIT (AUDIT_UID,AUDIT_TYPE,AUDIT_MODULE,AUDIT_DATETIME,AUDITED_BY,AUDIT_KEY,AUDIT_HISTORY,NOTE) VALUES (?,?,?,?,?,?,?,?)", new Object[]{autoUID, Type.toString(), Module.toString(), this.getCurrentTimeStamp(), currentUser, -1, "", ""});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean auditor(ConstantContainer.AUDIT_TYPE Type, ConstantContainer.APP_MODULE Module, String Note) {
        try {
            int autoUID = this.getColumnAutoIncrementValue("AUDIT", "AUDIT_UID");
            String currentUser = "Administrator";
            int insertedRows = this.jdbcTemplate.update("INSERT INTO AUDIT (AUDIT_UID,AUDIT_TYPE,AUDIT_MODULE,AUDIT_DATETIME,AUDITED_BY,AUDIT_KEY,AUDIT_HISTORY,NOTE) VALUES (?,?,?,?,?,?,?,?)", new Object[]{autoUID, Type.toString(), Module.toString(), this.getCurrentTimeStamp(), currentUser, -1, "", Note});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("dd/MM/yy");
            Date lFromDate1 = datetimeFormatter1.parse("10/05/16");
            Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());
            Timestamp Date = new Timestamp(lFromDate1.getTime());
            long date = fromTS1.getTime();
            Date dateObj = new Date(date);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            String text = df.format(dateObj);
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
