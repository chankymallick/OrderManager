package com.ordermanager.utility;

import com.ordermanager.order.dao.OrderDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponentXMLGenarator {

    @Autowired
    OrderDAO OrderDAO;

    @RequestMapping("/LoadMenuItems")
    public ModelAndView loadMenuItems(@RequestParam("menutype") String Type) {
        return new ModelAndView("LoadXMLComponent", "Type", Type);
    }

    @RequestMapping("/LoadDataViewGrid")
    public ModelAndView dataViewGrid(@RequestParam("gridname") String Type) {

        Map<String, Object> mvc = new HashMap<String, Object>();
        if (Type.equals("addNewItem")) {
            List temp = OrderDAO.getGridData("ITEMS","ITEM_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("addNewUser")) {
            List temp = OrderDAO.getGridData("USERS","USER_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }       
        if (Type.equals("addNewOrder")) {
            List temp = OrderDAO.getGridDataForOrders();
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }       
        if (Type.equals("quickNewOrder")) {
            List temp = OrderDAO.getGridDataForQuickOrders();
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }       
        if (Type.equals("addNewStatusType")) {
            List temp = OrderDAO.getGridDataForStatusTypes();
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }       
        if (Type.equals("addNewLocation")) {
            List temp = OrderDAO.getGridDataForNewLocation();
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }       
        if (Type.equals("addNewEmployee")) {
            List temp = OrderDAO.getGridData("EMPLOYEES","EMPLOYEE_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }       
        mvc.put("Type", Type);
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }
    @RequestMapping("/LoadReportViewGrid")
    public ModelAndView reportViewGrid(@RequestParam("gridname") String Type,@RequestParam(name="ParamJson",required = false) JSONObject ParamJson) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        if (Type.equals("advanceReport")) {           
            List temp = OrderDAO.getGridDataForQuickOrdersWithDate(ParamJson.get("ORDER_DATE=DATE").toString());
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));            
        }         
        mvc.put("Type", Type);
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

}
