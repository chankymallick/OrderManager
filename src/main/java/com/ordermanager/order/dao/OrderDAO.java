package com.ordermanager.order.dao;

import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.order.model.Order;
import com.ordermanager.utility.DAOHelper;
import com.ordermanager.utility.ResponseJSONHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

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
            this.auditor(ConstantContainer.AUDIT_TYPE.INSERT, ConstantContainer.APP_MODULE.ITEMS,"","");
        } catch (Exception ex) {
          this.generateSQLExceptionResponse(responseJSON, ex, "Failed to Save Item Data");
        }
        return responseJSON.getJSONResponse();
    }
    public List<Object> getGridData(String TableName,String Order_Column){
        String SQL = "SELECT *FROM "+TableName+" ORDER BY "+Order_Column+" DESC";
        return this.getJSONDataForGrid(SQL);    
    }
    

}
