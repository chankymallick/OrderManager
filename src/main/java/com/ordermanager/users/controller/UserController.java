package com.ordermanager.users.controller;

import com.ordermanager.users.dao.UserDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    private static final String AppCtx = "C:\\Users\\Maliick\\Documents\\NetBeansProjects\\OrderManager\\src\\main\\webapp\\WEB-INF\\applicationContext.xml";
    @RequestMapping("/test")
    public ModelAndView getUserName(Model model){ 
     ApplicationContext ctx = new FileSystemXmlApplicationContext(AppCtx);     
    UserDAO uda = (UserDAO)ctx.getBean("UserDao");
    int ss = uda.StudentList();
    System.out.println("------------------------------------------"+ss); 
    return new ModelAndView("test");
    }
    
}
