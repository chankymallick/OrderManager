package com.ordermanager.users.controller;

import com.ordermanager.users.dao.UserDAO;
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
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class UserController {
    @Autowired
     ServletContext servletContext;
    private static final String AppCtx = "C:\\Users\\Maliick\\Documents\\NetBeansProjects\\OrderManager\\src\\main\\webapp\\WEB-INF\\applicationContext.xml";
    @RequestMapping("/test")
    public ModelAndView getUserName(Model model){ 
    ApplicationContext ctx = new FileSystemXmlApplicationContext(AppCtx);    
        try {
           File rootDir = new File( servletContext.getRealPath("/WEB-INF/Language/") );
            System.out.println(rootDir.getAbsolutePath());
        } catch (Exception e) {
        }
   

      String name = ctx.getMessage("cname",null,null,null);
        System.out.println(name);
    return new ModelAndView("Home");
    }
    
}
