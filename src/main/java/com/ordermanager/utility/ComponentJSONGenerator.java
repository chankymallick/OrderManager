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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponentJSONGenerator {
    @Autowired
     UtilityDAO UtilityDAO; 
    
    
    @RequestMapping("/addNewOrder_Form")
    public ModelAndView addNewOrder(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewOrder"));
            if (defaultData.length()==0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewOrder");
            } else {
                requestMap.put("FormType", "addNewOrder_withValue");
                requestMap.put("FormData",defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewOrder");
        }
    }
    @RequestMapping("/quickNewOrder_Form")
    public ModelAndView addNewQuickOrder(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "quickNewOrder"));
            if (defaultData.length()==0) {
                return new ModelAndView("LoadJSON", "FormType", "quickNewOrder");
            } else {
                requestMap.put("FormType", "quickNewOrder_withValue");
                requestMap.put("FormData",defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "quickNewOrder");
        }
    }
    @RequestMapping("/addNewItem_Form")
    public ModelAndView loadNewOrderItemForm(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewItem"));
            if (defaultData.length()==0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewItem");
            } else {
                requestMap.put("FormType", "addNewItem_withValue");
                requestMap.put("FormData",defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewItem");
        }
    }
    @RequestMapping("/addNewUser_Form")
    public ModelAndView loadaddNewUser(@RequestParam("Default") boolean isDefaultOn) {
        Map<String, Object> requestMap = new HashMap();
        if (isDefaultOn) {
            JSONObject defaultData = new JSONObject(UtilityDAO.getApplicationData("FORM_DEFAULT_VALUE", "addNewUser"));
            if (defaultData.length()==0) {
                return new ModelAndView("LoadJSON", "FormType", "addNewUser");
            } else {
                requestMap.put("FormType", "addNewUser_withValue");
                requestMap.put("FormData",defaultData);
                return new ModelAndView("LoadJSON", "ReqObject", requestMap);
            }

        } else {
            return new ModelAndView("LoadJSON", "FormType", "addNewUser");
        }
    }
    @RequestMapping("/advanceReport_Form")
    public ModelAndView dailyAdvanceReport() {    
            return new ModelAndView("LoadJSON", "FormType", "advanceReport_Form");
        
    }

    @RequestMapping("/operationMenu")
    public ModelAndView operationMenu(Model model) {
        return new ModelAndView("LoadJSON", "FormType", "operationMenu");
    }

    @RequestMapping("/operationToolbar")
    public String operationToolbar(Model map,@RequestParam("formname") String FormName) {
       map.addAttribute("FormType", "operationToolbar");
       map.addAttribute("FormName",FormName);
       return "LoadJSON";
    }

}
