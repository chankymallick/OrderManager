package com.ordermanager.utility;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponentJSONGenerator {
    @Autowired
     UtilityDAO UtilityDAO; 
    
    @RequestMapping("/loadNewOrderItemForm")
    public ModelAndView loadNewOrderItemForm(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "loadNewOrderItemForm"));
            if (defaultData.length()==0) {
                return new ModelAndView("LoadJSON", "FormType", "loadNewOrderItemForm");
            } else {
                requestMap.put("FormType", "loadNewOrderItemForm_withValue");
                requestMap.put("FormData",defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "loadNewOrderItemForm");
        }
    }

    @RequestMapping("/operationMenu")
    public ModelAndView operationMenu(Model model) {
        return new ModelAndView("LoadJSON", "FormType", "operationMenu");
    }

    @RequestMapping("/operationToolbar")
    public ModelAndView operationToolbar(Model model) {
        return new ModelAndView("LoadJSON", "FormType", "operationToolbar");
    }

}
