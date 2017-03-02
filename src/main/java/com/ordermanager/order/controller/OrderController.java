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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Maliick
 */
@Controller
public class OrderController {

    @Autowired
    OrderDAO orderDAO;

    @RequestMapping(value = "/addNewItem", method = RequestMethod.GET)
    public ModelAndView addItem(@RequestParam("ParamData") JSONObject paramJson) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
        OrderDAO orderDAO = (OrderDAO) ctx.getBean("OrderDAO");
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addItem(paramJson));
    }

    @RequestMapping(value = "/addNewOrder", method = RequestMethod.GET)
    public ModelAndView addNewOrder(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewOrder(paramJson));
    }
    
    @RequestMapping("/updateNewOrder")
    public ModelAndView updateOrderForm(@RequestParam("ParamData") JSONObject paramJson) {
      return new ModelAndView("MakeResponse", "responseValue", orderDAO.updateNewOrder(paramJson));
    }
    @RequestMapping("/addadvance")
    public ModelAndView addAdvance(@RequestParam("ParamData") JSONObject paramJson) {
      return new ModelAndView("MakeResponse", "responseValue", orderDAO.addAdvance(paramJson));
    }
    
    @RequestMapping(value = "/quickNewOrder", method = RequestMethod.GET)
    public ModelAndView addNewQuickOrder(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewQuickOrder(paramJson));
    }
    @RequestMapping(value = "/addNewStatusType", method = RequestMethod.GET)
    public ModelAndView addNewStatusType(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewStatusType(paramJson));
    }
    @RequestMapping(value = "/addNewLocation", method = RequestMethod.GET)
    public ModelAndView addNewLocation(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewLocation(paramJson));
    }

    @RequestMapping("/getExtraItems")
    public String getItemSelectionList(Model map, @RequestParam("ITEM_TYPE") String ITEM_TYPE) {
        Map<String, Object> dataMap = orderDAO.getItemSelectionList(ITEM_TYPE);
        map.addAttribute("Type", "ItemSelectionList");
        map.addAttribute("DataMap", dataMap);
        return "LoadXMLComponent";
    }
    
    @RequestMapping("/updateBulkMasterTailor_Query")
    public ModelAndView getOrderDetails(Model map, @RequestParam("ParamData") JSONObject Params) {
    return new ModelAndView("MakeResponse", "responseValue",orderDAO.getOrderDetails(Params));    
    }
    @RequestMapping("/getStatistics")
    public ModelAndView getStatistics(@RequestParam("StatisticsName") String Statistics_Name, @RequestParam("ReportParams") JSONObject jsonParam) {
        Map<String, Object> requestMap = new HashMap();
        if (Statistics_Name.equals("advanceReport")) {
            String OrderDate =  jsonParam.getString("ORDER_DATE=DATE");
            String Location = jsonParam.getString("LOCATION=STR");
            List<String[]> statList = orderDAO.getAdvanceStatistics(OrderDate, Location);
            requestMap.put("STAT_VALUES", statList);
        }
        return new ModelAndView("StatisticsContainer", "OBJECT_MAP", requestMap);
    }
        
   @RequestMapping("/updateBulkMasterTailor_BulkUpdate")                                              
    public ModelAndView updateBulkMasterTailor(@RequestParam("ParamData") JSONObject paramJson){ 
     return new ModelAndView("MakeResponse", "responseValue", orderDAO.orderAssignmentMasterTailor(paramJson, "Administrator"));
    }   
   @RequestMapping("/updateBulkReadyToDeliver_BulkUpdate")                                              
    public ModelAndView updateBulkReadyToDeliver(@RequestParam("ParamData") JSONObject paramJson){ 
     return new ModelAndView("MakeResponse", "responseValue", orderDAO.orderAssignmentReadyToDeliver(paramJson, "Administrator"));
    }   
   @RequestMapping("/updateBulkReadyToDeliver_Query")
    public ModelAndView getOrderDetailsReadyToDeliver(Model map, @RequestParam("ParamData") JSONObject Params) {
    return new ModelAndView("MakeResponse", "responseValue",orderDAO.getOrderDetailsForReadyToDeliver(Params));    
    }
    
}
