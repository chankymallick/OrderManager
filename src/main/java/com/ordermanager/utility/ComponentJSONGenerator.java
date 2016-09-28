
package com.ordermanager.utility;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ComponentJSONGenerator {
    @RequestMapping("/loadNewOrderItemForm")
    public ModelAndView loadNewOrderItemForm(Model model){  
    return new ModelAndView("LoadJSON","FormType","loadNewOrderItemForm");
    }
    @RequestMapping("/operationMenu")
    public ModelAndView operationMenu(Model model){
    return new ModelAndView("LoadJSON","FormType","operationMenu");
    }
    @RequestMapping("/operationToolbar")
    public ModelAndView operationToolbar(Model model){
    return new ModelAndView("LoadJSON","FormType","operationToolbar");
    }
    
}
