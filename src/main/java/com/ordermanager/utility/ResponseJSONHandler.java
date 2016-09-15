package com.ordermanager.utility;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;

public class ResponseJSONHandler {

    private JSONObject ResponseJSON;
    private Map<String, String> ValueMap;
    private String Response_Type = "";
    private String Response_Status = "";
    private String Response_Message = "";
    private JSONObject Response_Value;

    public ResponseJSONHandler() {
        ResponseJSON = new JSONObject();
        Response_Value = new JSONObject();
        ValueMap = new HashMap<String, String>();
    }

    public void setResponse_Type(String Response_Type) {
        this.Response_Type = Response_Type;
    }

    public void setResponse_Status(String Response_Status) {
        this.Response_Status = Response_Status;
    }

    public void setResponse_Message(String Response_Message) {
        this.Response_Message = Response_Message;
    }

    public void setResponse_Value(JSONObject Response_Value) {
        this.Response_Value = Response_Value;
    }

    public void setResponseValueByResultSet(ResultSet ResultSet) {

    }

    public void addResponseValue(String key, String Value) {
        ValueMap.put(key, Value);
    }

    public String getJSONResponse() {
        ResponseJSON.put(ConstantContainer.ResponseJSONKeys.RESPONSE_TYPE.toString(), Response_Type);
        ResponseJSON.put(ConstantContainer.ResponseJSONKeys.RESPONSE_STATUS.toString(), Response_Status);
        ResponseJSON.put(ConstantContainer.ResponseJSONKeys.RESPONSE_MESSAGE.toString(), Response_Message);
        ResponseJSON.put(ConstantContainer.ResponseJSONKeys.RESPONSE_VALUE.toString(), Response_Value);
        if (ValueMap.size() > 0) {
            ResponseJSON.put(ConstantContainer.ResponseJSONKeys.RESPONSE_VALUE.toString(), new JSONObject(ValueMap));
        }
        return ResponseJSON.toString();
    }

    public static void main(String[] args) {
        ResponseJSONHandler rsv = new ResponseJSONHandler();
        rsv.setResponse_Status(ConstantContainer.ResponseJSONStatus.SUCCESS.toString());
        rsv.setResponse_Message("Succesfully Added");
        rsv.addResponseValue("ID", "450");
        rsv.addResponseValue("NAME", "CHANKY");
        System.out.println(rsv.getJSONResponse());
    }
}
