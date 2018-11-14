package com.ordermanager.utility;

import com.ordermanager.order.dao.OrderDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponentXMLGenarator {

    @ModelAttribute
    public void MemoryMonitor(Model mvc) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory(); // current heap allocated to the VM process
        long freeMemory = runtime.freeMemory(); // out of the current heap, how much is free
        long maxMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx setting
        long usedMemory = totalMemory - freeMemory; // how much of the current heap the VM is using
        long availableMemory = maxMemory - usedMemory;

        System.out.println("Total : " + totalMemory);
        System.out.println("freeMemory : " + freeMemory);
        System.out.println("usedMemory : " + usedMemory);

    }
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
            List temp = OrderDAO.getGridData("ITEMS", "ITEM_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("addNewUser")) {
            List temp = OrderDAO.getGridData("USERS", "USER_UID");
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
            List temp = OrderDAO.getGridData("EMPLOYEES", "EMPLOYEE_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("addNewAccount")) {
            List temp = OrderDAO.getGridData("ACCOUNT_REGISTER", "TRANSACTION_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("addNewAccountTransaction")) {
            List temp = OrderDAO.getGridData("ACCOUNTS_BOOK", "TRANSACTION_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("addNewAccountSubType")) {
            List temp = OrderDAO.getGridData("ACCOUNT_BOOK_SUBTYPES", "TRANSACTION_UID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("taskList")) {
            List temp = OrderDAO.getGridDataForTaskList();
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("addNewTask")) {
            List temp = OrderDAO.getGridData("TASK_LIST", "TASK_ID");
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        mvc.put("Type", Type);
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/getDataExplorer")
    public ModelAndView getDataExplorer(@RequestParam("modulename") String Module, @RequestParam("keyname") String KeyName) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        List temp=null;
        if (Module.equalsIgnoreCase("AUDIT")) {
               temp = OrderDAO.getGridDataForAudit();
        } else {
            temp = OrderDAO.getGridData(Module, KeyName);
        }
        mvc.put("ALL_ROWS_LIST", temp.get(0));
        mvc.put("COLUMN_NAME_LIST", temp.get(1));
        mvc.put("Type", "DataExplore" + Module);
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/LoadSearchGrid")
    public ModelAndView dataViewGrid(@RequestParam("gridname") String Type, @RequestParam("BillNo") String BillNo) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        if (Type.equals("paymentGrid")) {
            List temp = OrderDAO.getGridDataForPaymentSearchQuery(BillNo);
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("assignmentGrid")) {
            List temp = OrderDAO.getGridDataForOrderAssignmentSearchQuery(BillNo);
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("orderItemsGrid")) {
            List temp = OrderDAO.getGridDataForOrderItemsSearchQuery(BillNo);
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        mvc.put("Type", Type);
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/LoadAssignmentGrid")
    public ModelAndView assignmentUpdateGrid(@RequestParam("gridname") String Type) {

        Map<String, Object> mvc = new HashMap<String, Object>();
        mvc.put("Type", Type);
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/LoadReportViewGrid")
    public ModelAndView reportViewGrid(@RequestParam("gridname") String Type, @RequestParam(name = "ParamJson", required = false) JSONObject ParamJson) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        if (Type.equals("advanceReport")) {
            List temp = OrderDAO.getGridDataForQuickOrdersWithDate(ParamJson.get("REPORT_DATE=DATE").toString());
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        if (Type.equals("deliveryTransactionsReport")) {
            List temp = OrderDAO.getGridDataForDeliveryTransactions(ParamJson.get("REPORT_DATE=DATE").toString());
            mvc.put("ALL_ROWS_LIST", temp.get(0));
            mvc.put("COLUMN_NAME_LIST", temp.get(1));
        }
        mvc.put("Type", Type);
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/loadScheduleOrderList")
    public ModelAndView loadScheduleOrderList(@RequestParam("ORDER_STATUS") String Type, @RequestParam("DELIVERY_DATE") String DELIVERY_DATE) throws Exception {
        Map<String, Object> mvc = new HashMap<String, Object>();
        List temp = OrderDAO.getGridDataForSchedulerOrderList(Type, DELIVERY_DATE);
        mvc.put("ALL_ROWS_LIST", temp.get(0));
        mvc.put("COLUMN_NAME_LIST", temp.get(1));
        mvc.put("Type", "SCHEDULER_ORDER_LIST");
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/loadChartOrderList")
    public ModelAndView loadChartOrderList(@RequestParam("TYPE") String Type, @RequestParam("PARAM_DATA") String param) throws Exception {
        Map<String, Object> mvc = new HashMap<String, Object>();
        List temp = OrderDAO.getGridDataForChartOrderList(Type, param);
        mvc.put("ALL_ROWS_LIST", temp.get(0));
        mvc.put("COLUMN_NAME_LIST", temp.get(1));
        mvc.put("Type", "CHART_ORDER_LIST");
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

}
