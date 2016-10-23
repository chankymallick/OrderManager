package com.ordermanager.users.controller;
import com.ordermanager.users.dao.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
   @Autowired 
    UserDAO UserDAO;

    @RequestMapping(value = "/addNewUser",method = RequestMethod.GET)                                              
    public ModelAndView addNewUser(@RequestParam("ParamData") JSONObject paramJson){ 
     return new ModelAndView("MakeResponse", "responseValue", UserDAO.addUser(paramJson, "Administrator"));
    }    
}
