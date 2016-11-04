package com.ordermanager.utility;

import java.io.File;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    ServletContext servletContext;
    @Autowired
    UtilityDAO UtilityDAO;

    @RequestMapping("/Login")
    public ModelAndView Login(HttpServletRequest request, HttpServletResponse response, @RequestParam("password") String LanguageType) {

        try {
            File rootDir = new File(servletContext.getRealPath("/WEB-INF/applicationContext.xml"));          
            System.out.println(rootDir.getAbsolutePath());
            ConstantContainer.Application_Context_File_Path = rootDir.getAbsolutePath();
        } catch (Exception e) {
        }
        if (LanguageType.equals("bng")) {           
            new PropertyFileReader().loadLanguageProperties(request, servletContext, ConstantContainer.LANGUAGES.BENGALI);
            new PropertyFileReader().loadSelectItemProperties(request, servletContext, ConstantContainer.LANGUAGES.BENGALI);
        } else {
            new PropertyFileReader().loadLanguageProperties(request, servletContext, ConstantContainer.LANGUAGES.ENGLISH);
            new PropertyFileReader().loadSelectItemProperties(request, servletContext, ConstantContainer.LANGUAGES.ENGLISH);
        }
        return new ModelAndView("Home");
    }

    @RequestMapping(value = {"/", "/index.html", "/Login.jsp", "/Login.html", "/index.jsp"}, method = RequestMethod.GET)
    public ModelAndView WelcomePageRedirection(Model model) {
        return new ModelAndView("Login");
    }
}
