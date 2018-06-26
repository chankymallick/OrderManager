package com.ordermanager.order.controller;

import com.ordermanager.order.dao.OrderDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ordermanager.utility.ConstantContainer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Maliick
 */
@Controller
public class OrderController {

    @Autowired
    OrderDAO orderDAO;

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

    @RequestMapping("/addNewItem")
    public ModelAndView addItem(@RequestParam("ParamData") JSONObject paramJson) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
        OrderDAO orderDAO = (OrderDAO) ctx.getBean("OrderDAO");
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addItem(paramJson));
    }

    @RequestMapping("/addNewOrder")
    public ModelAndView addNewOrder(@RequestParam("ParamData") JSONObject paramJson) {
        if (!orderDAO.roleLoginCheck(new String[]{"ADMIN", "ORDER_MANAGER", "ENQUIRER"})) {
            return new ModelAndView("Login");
        }
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewOrder(paramJson));
    }

    @RequestMapping("/updateBulkMasterTailor_Query")
    public ModelAndView getOrderDetails(Model map, @RequestParam("ParamData") JSONObject Params) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getOrderDetails(Params));
    }

    @RequestMapping("/updateNewOrder")
    public ModelAndView updateOrderForm(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.updateNewOrder(paramJson));
    }

    @RequestMapping("/updateItem")
    public ModelAndView updateItem(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.updateItem(paramJson));
    }

    @RequestMapping("/addadvance")
    public ModelAndView addAdvance(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addAdvance(paramJson));
    }

    @RequestMapping("/quickNewOrder")
    public ModelAndView addNewQuickOrder(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewQuickOrder(paramJson));
    }

    @RequestMapping("/addNewStatusType")
    public ModelAndView addNewStatusType(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewStatusType(paramJson));
    }

    @RequestMapping("/addNewAccount")
    public ModelAndView addNewAccount(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewAccount(paramJson));
    }

    @RequestMapping("/addNewAccountTransaction")
    public ModelAndView addNewAccountTransaction(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewAccountTransaction(paramJson));
    }

    @RequestMapping("/addNewAccountSubType")
    public ModelAndView addNewAccountSubType(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewAccountSubType(paramJson));
    }

    @RequestMapping("/addNewLocation")
    public ModelAndView addNewLocation(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewLocation(paramJson));
    }

    @RequestMapping("/getExtraItems")
    public String getItemSelectionList(Model map, @RequestParam("ITEM_TYPE") String ITEM_TYPE, @RequestParam("BILL_NO") String Bill_No) {
        Map<String, Object> dataMap = orderDAO.getItemSelectionList(ITEM_TYPE);
        map.addAttribute("Type", "ItemSelectionList");
        map.addAttribute("DataMap", dataMap);
        map.addAttribute("SavedList", orderDAO.getSavedItemSelectionList(Bill_No));
        return "LoadXMLComponent";
    }

    @RequestMapping("/getStatisticsJSON")
    public ModelAndView getStatisticsJSON(@RequestParam("StatisticsName") String Statistics_Name, @RequestParam("ReportParams") JSONObject jsonParam) {
        Map<String, Object> requestMap = new HashMap();
        if (Statistics_Name.equals("advanceReport")) {
            String OrderDate = jsonParam.getString("REPORT_DATE=DATE");
            String Location = jsonParam.getString("LOCATION=STR");
            List<String[]> statList = orderDAO.getAdvanceStatistics(OrderDate, Location);
            requestMap.put("STAT_VALUES", statList);
        }
        if (Statistics_Name.equals("deliveryTransactionsReport")) {
            String OrderDate = jsonParam.getString("REPORT_DATE=DATE");
            String Location = jsonParam.getString("LOCATION=STR");
            List<String[]> statList = orderDAO.getDeliveryPaymentStatistics(OrderDate, Location);
            requestMap.put("STAT_VALUES", statList);
        }

        return new ModelAndView("MakeResponse", "responseValue", new JSONObject(requestMap).toString());
    }

    @RequestMapping("/getStatistics")
    public ModelAndView getStatistics(@RequestParam("StatisticsName") String Statistics_Name, @RequestParam("ReportParams") JSONObject jsonParam) {
        Map<String, Object> requestMap = new HashMap();
        if (Statistics_Name.equals("advanceReport")) {
            String OrderDate = jsonParam.getString("REPORT_DATE=DATE");
            String Location = jsonParam.getString("LOCATION=STR");
            List<String[]> statList = orderDAO.getAdvanceStatistics(OrderDate, Location);
            requestMap.put("STAT_VALUES", statList);
        }
        if (Statistics_Name.equals("deliveryTransactionsReport")) {
            String OrderDate = jsonParam.getString("REPORT_DATE=DATE");
            String Location = jsonParam.getString("LOCATION=STR");
            List<String[]> statList = orderDAO.getDeliveryPaymentStatistics(OrderDate, Location);
            requestMap.put("STAT_VALUES", statList);
        }
        return new ModelAndView("StatisticsContainer", "OBJECT_MAP", requestMap);
    }

    @RequestMapping("/updateBulkMasterTailor_BulkUpdate")
    public ModelAndView updateBulkMasterTailor(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.orderAssignmentMasterTailor(paramJson, "Administrator"));
    }

    @RequestMapping("/updateBulkReadyToDeliver_BulkUpdate")
    public ModelAndView updateBulkReadyToDeliver(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.orderAssignmentReadyToDeliver(paramJson, "Administrator"));
    }

    @RequestMapping("/updateBulkToSingle_BulkUpdate")
    public ModelAndView updateBulkToSingle(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.orderSingleAssignment(paramJson, "Administrator"));
    }

    @RequestMapping("/updateDeliveryCompleted_BulkUpdate")
    public ModelAndView updateBulkDeliveryCompleted(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.updateDeliveryCompletedBulk(paramJson, "Administrator"));
    }

    @RequestMapping("/assignmentStatusChange_BulkUpdate")
    public ModelAndView assignmentStatusChange(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.assignmentStatusChangeUpdate(paramJson, "Administrator"));
    }

    @RequestMapping("/updateBulkReadyToDeliver_Query")
    public ModelAndView getOrderDetailsReadyToDeliver(Model map, @RequestParam("ParamData") JSONObject Params) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getOrderDetailsForReadyToDeliver(Params));
    }

    @RequestMapping("/updateBulkToSingle_Query")
    public ModelAndView getOrderDetailsSingleAssignment(Model map, @RequestParam("ParamData") JSONObject Params) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getOrderDetailsSingleAssignment(Params));
    }

    @RequestMapping("/updateDeliveryCompleted_Query")
    public ModelAndView getOrderDetailsForDelivery(Model map, @RequestParam("ParamData") JSONObject Params) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getOrderDetailsForDelivery(Params));
    }

    @RequestMapping("/assignmentStatusChange_Query")
    public ModelAndView getOrderDetailsForassignmentStatusChange(Model map, @RequestParam("ParamData") JSONObject Params) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getOrderDetailsForAssignmentChange(Params));
    }

    @RequestMapping("/LoadScheduleOrderData")
    public ModelAndView orderScheduler(Model map, @RequestParam("from") String fromDate, @RequestParam("to") String toDate) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        mvc.put("Type", "SchedulerData");
        mvc.put("DATA", orderDAO.getOrderScheduleStatusByMonth(fromDate, toDate));
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/getDayWiseProductionDetails")
    public ModelAndView getDayWiseProductionDetails(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("ReportType") String ReportType) {

        Map<String, Object> mvc = new HashMap<String, Object>();
        mvc.put("Type", "DayWiseProduction");
        mvc.put("DATA", orderDAO.getProductionData(ProductionType, Name, ReportType, "", ""));
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/getDayWiseProductionDetails2")
    public ModelAndView getDayWiseProductionDetails2(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("ReportType") String ReportType, @RequestParam("fromDate") String FromDate, @RequestParam("toDate") String ToDate) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        mvc.put("Type", "DayWiseProduction2");
        mvc.put("DATA", orderDAO.getProductionData2(ProductionType, Name, ReportType, FromDate, ToDate));
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/getDayWiseProductionStats")
    public ModelAndView getDayWiseProductionStats(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("ReportType") String ReportType, @RequestParam("fromDate") String FromDate, @RequestParam("toDate") String ToDate) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getDayWiseProductionStats(ProductionType, Name, ReportType, FromDate, ToDate));
    }

    @RequestMapping("/getDayWiseProductionDetailsUnpaidWage")
    public ModelAndView getDayWiseProductionDetailsUnpaidWage(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("ReportType") String ReportType, @RequestParam("fromDate") String FromDate, @RequestParam("toDate") String ToDate) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        mvc.put("Type", "DayWiseProductionUnpaid");
        mvc.put("DATA", orderDAO.getProductionDataNonPaidWage(ProductionType, Name, ReportType, FromDate, ToDate));
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/getDayWisePaidWagePaymentOrders")
    public ModelAndView getDayWisePaidWagePaymentOrders(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("PaymentDate") String PaymentDate) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        mvc.put("Type", "DayWiseProductionUnpaid");
        mvc.put("DATA", orderDAO.getDayWisePaidWagePaymentOrders(ProductionType, Name, PaymentDate));
        return new ModelAndView("LoadXMLComponent", "OBJECT_MAP", mvc);
    }

    @RequestMapping("/getDayWiseProductionStatsUnpaid")
    public ModelAndView getDayWiseProductionStatsUnpaid(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("ReportType") String ReportType, @RequestParam("fromDate") String FromDate, @RequestParam("toDate") String ToDate, @RequestParam("removedBills") String removedBills) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getDayWiseProductionStatsUnpaid(ProductionType, Name, ReportType, FromDate, ToDate, removedBills));
    }

    @RequestMapping("/getDayWisePaidWagePaymentStats")
    public ModelAndView getDayWisePaidWagePaymentStats(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("PaymentDate") String PaymentDate) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getDayWisePaidWagePaymentStats(ProductionType, Name, PaymentDate));
    }

    @RequestMapping("/wagePayment")
    public ModelAndView wagePayment(Model map, @RequestParam("ProductionType") String ProductionType, @RequestParam("Name") String Name, @RequestParam("BILLS") JSONObject jsonParams) {
        Map<String, Object> mvc = new HashMap<String, Object>();
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.wagePaymentModule(jsonParams, "", ProductionType, Name));
    }

    @RequestMapping("/getNextBillNo")
    public ModelAndView getNextBillNO(Model map) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.getNextBillNo());
    }

    @RequestMapping("/getChartData")
    public ModelAndView getChartData(Model map,@RequestParam String chartName, @RequestParam JSONObject chartParams) throws Exception {

        if (chartName.equals("orderStatus")) {
            return new ModelAndView("MakeResponse", "responseValue", orderDAO.getChartDataOrderStatus(chartParams));
        }
        if (chartName.equals("locationStatus")) {
            return new ModelAndView("MakeResponse", "responseValue", orderDAO.getChartDataLocationStatus(chartParams));
        }
        if (chartName.equals("")) {

        }
        if (chartName.equals("")) {

        }
        return new ModelAndView("MakeResponse", "responseValue", "");
    }

}
