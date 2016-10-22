package com.ordermanager.users.controller;

import com.ordermanager.order.dao.OrderDAO;
import com.ordermanager.users.dao.UserDAO;
import com.ordermanager.utility.ConstantContainer;
import com.ordermanager.utility.PropertyFileReader;
import java.io.File;
import java.net.URLDecoder;
import java.util.Locale;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletContext;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
   @Autowired 
    UserDAO UserDAO;

    @RequestMapping(value="/addNewUser",method = RequestMethod.POST)
    public ModelAndView addNewUser(@RequestParam("ParamData") JSONObject paramJson){       
    System.out.println(paramJson.toString());
     return new ModelAndView("MakeResponse", "responseValue", UserDAO.addUser(paramJson, "Administrator"));
    }
    
    public ModelAndView getUserName(Model model){   
    return new ModelAndView("Home");
    }
    
}
