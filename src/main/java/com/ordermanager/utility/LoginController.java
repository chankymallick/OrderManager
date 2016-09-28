package com.ordermanager.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    ServletContext servletContext;

    @RequestMapping("/Login")
    public ModelAndView Login(Model model) {
        try {
            File rootDir = new File(servletContext.getRealPath("/WEB-INF/Language/Bengali_Language_Pack.properties"));
            System.out.println(rootDir.getAbsolutePath());
            Properties LanguageProperties = new Properties();
            LanguageProperties.load(new FileInputStream(new File(rootDir.getAbsolutePath())));
            System.out.println(LanguageProperties.getProperty("name"));
        } catch (Exception e) {
        }
        return new ModelAndView("Home");
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView WelcomePageRedirection(Model model) {
        return new ModelAndView("Login");
    } 
}
