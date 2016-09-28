package com.ordermanager.utility;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponentXMLGenarator {
    @RequestMapping("/LoadMenuItems")
    public ModelAndView loadMenuItems(@RequestParam("menutype") String Type){    
    return new ModelAndView("LoadXMLComponent","Type",Type);
    }
    @RequestMapping("/LoadDataViewGrid")
    public ModelAndView dataViewGrid(@RequestParam("gridname") String Type){    
    return new ModelAndView("LoadXMLComponent","Type",Type);
    }
    
}
