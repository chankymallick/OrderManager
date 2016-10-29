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
        OrderDAO orderDAO = (OrderDAO)ctx.getBean("OrderDAO");
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addItem(paramJson));
    }
    
    @RequestMapping(value = "/addNewOrder", method = RequestMethod.GET)
    public ModelAndView addNewOrder(@RequestParam("ParamData") JSONObject paramJson) {     
    return new ModelAndView("MakeResponse", "responseValue", orderDAO.addNewOrder(paramJson));
    }
    
   @RequestMapping("/getExtraItems")
    public String getItemSelectionList(Model map,@RequestParam("ITEM_TYPE") String ITEM_TYPE) {
       Map <String,Object> dataMap = orderDAO.getItemSelectionList(ITEM_TYPE);
       map.addAttribute("Type", "ItemSelectionList");
       map.addAttribute("DataMap",dataMap);
       return "LoadXMLComponent";
    }
}
