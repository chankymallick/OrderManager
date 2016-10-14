package com.ordermanager.order.controller;

import com.ordermanager.order.dao.OrderDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import com.ordermanager.utility.ResponseJSONHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ordermanager.utility.ConstantContainer;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author Maliick
 */
@Controller
public class OrderController {

    @RequestMapping(value = "/addItem", method = RequestMethod.GET)
    public ModelAndView addItem(@RequestParam("ParamData") JSONObject paramJson) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
        OrderDAO orderDAO = (OrderDAO)ctx.getBean("OrderDAO");
        return new ModelAndView("MakeResponse", "responseValue", orderDAO.addItem(paramJson));
    }
    

}
