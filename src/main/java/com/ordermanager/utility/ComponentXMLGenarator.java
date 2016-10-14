package com.ordermanager.utility;

import com.ordermanager.order.dao.OrderDAO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponentXMLGenarator {

    @RequestMapping("/LoadMenuItems")
    public ModelAndView loadMenuItems(@RequestParam("menutype") String Type) {
        return new ModelAndView("LoadXMLComponent", "Type", Type);
    }

    @RequestMapping("/LoadDataViewGrid")
    public ModelAndView dataViewGrid(@RequestParam("gridname") String Type) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
        OrderDAO orderDAO = (OrderDAO) ctx.getBean("OrderDAO");
        Map<String,Object> mvc = new HashMap<String,Object>();
        mvc.put("ALL_ROWS_LIST",orderDAO.getGridData("ITEMS").get(0));
        mvc.put("COLUMN_NAME_LIST",orderDAO.getGridData("ITEMS").get(1));
        mvc.put("Type",Type);   
        return new ModelAndView("LoadXMLComponent","OBJECT_MAP",mvc);
    }

}
