package com.ordermanager.users.controller;

import com.ordermanager.users.dao.UserDAO;
import com.ordermanager.utility.PropertyFileReader;
import java.util.Locale;
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

System.out.println(ctx);
      String name = ctx.getMessage("cname",null,null,null);
        System.out.println(name);
    return new ModelAndView("Home");
    }
    
}
