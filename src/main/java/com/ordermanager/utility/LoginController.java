package com.ordermanager.utility;

import com.ordermanager.security.SecurityModule;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {

    @Autowired
    ServletContext servletContext;
    @Autowired
    UtilityDAO UtilityDAO;

    @RequestMapping(path = "/LoginProcess", method = RequestMethod.POST)
    public ModelAndView Login(HttpServletRequest request, HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String Password) {

        Connection con = null;
        PreparedStatement mainQuery = null;
        ResultSet userResult = null;
        String FullName = "";

        try {
            con = UtilityDAO.getJDBCConnection();
            mainQuery = con.prepareStatement("SELECT USER_FIRST_NAME , USER_LAST_NAME,LANGUAGE_PREFERENCE,USER_TYPE,ENABLED FROM USERS WHERE USER_ID = ? AND PASSWORD= ?  ");
            mainQuery.setString(1, username);
            mainQuery.setString(2, SecurityModule.encrypt(Password, Password));
            userResult = mainQuery.executeQuery();
            if (userResult.next()) {
                String USER_FIRST_NAME = userResult.getString("USER_FIRST_NAME");
                String USER_LAST_NAME = userResult.getString("USER_LAST_NAME");
                FullName = USER_FIRST_NAME + " " + USER_LAST_NAME;
                String LANGUAGE_PREFERENCE = userResult.getString("LANGUAGE_PREFERENCE");
                String USER_TYPE = userResult.getString("USER_TYPE");
                String ENABLED = userResult.getString("ENABLED");
                if (ENABLED.equalsIgnoreCase("1")) {
                    request.getSession().setAttribute("LOGIN_STATUS", "TRUE");
                    request.getSession().setAttribute("USERNAME", USER_FIRST_NAME + " " + USER_LAST_NAME);
                    request.getSession().setAttribute("LANGUAGE", LANGUAGE_PREFERENCE);
                    request.getSession().setAttribute("USER_TYPE", USER_TYPE);
                    request.getSession().setAttribute("USER_ID", username);

                    Map<String, String> UserDetail = new HashMap<String, String>();
                    UserDetail.put("LOGIN_STATUS", "TRUE");
                    UserDetail.put("USERNAME", USER_FIRST_NAME + " " + USER_LAST_NAME);
                    UserDetail.put("LANGUAGE", LANGUAGE_PREFERENCE);
                    UserDetail.put("USER_TYPE", USER_TYPE);
                    request.getSession().setAttribute("USER_DETAILS", UserDetail);

                    this.postSuccesLoginJob(LANGUAGE_PREFERENCE, request);
                    UtilityDAO.auditor(ConstantContainer.AUDIT_TYPE.SECURITY, ConstantContainer.APP_MODULE.LOGIN, 0, FullName, "");
                    return new ModelAndView(new RedirectView("Home"));

                } else {
                    UtilityDAO.SecurityAuditor(ConstantContainer.AUDIT_TYPE.SECURITY, ConstantContainer.APP_MODULE.FAILED_LOGIN, 0, FullName, "", username);
                    return new ModelAndView(new RedirectView("Login.html")).addObject("message", "notenabled");
                }

            } else {
                UtilityDAO.SecurityAuditor(ConstantContainer.AUDIT_TYPE.SECURITY, ConstantContainer.APP_MODULE.FAILED_LOGIN, 0, FullName, "", username);
                return new ModelAndView(new RedirectView("Login.html")).addObject("message", "notfound");
            }
        } catch (Exception e) {
        }
        UtilityDAO.SecurityAuditor(ConstantContainer.AUDIT_TYPE.SECURITY, ConstantContainer.APP_MODULE.FAILED_LOGIN, 0, FullName, "", username);
        return new ModelAndView(new RedirectView("Login.html")).addObject("message", "unknown");
    }

    @RequestMapping("/Logout")
    public ModelAndView Logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            UtilityDAO.auditor(ConstantContainer.AUDIT_TYPE.SECURITY, ConstantContainer.APP_MODULE.LOGOUT, 0, request.getSession(false).getAttribute("USERNAME").toString(), "");
            request.getSession().removeAttribute("LOGIN_STATUS");
            request.getSession().removeAttribute("USERNAME");
            request.getSession().removeAttribute("LANGUAGE");
            request.getSession().removeAttribute("USER_TYPE");
            request.getSession(false).invalidate();
            return new ModelAndView(new RedirectView("Login.html")).addObject("message", "logout");
        } catch (Exception e) {
            return new ModelAndView(new RedirectView("Login.html")).addObject("message", "logout");
        }

    }

    @RequestMapping("/Home")
    public ModelAndView reloadHome(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (UtilityDAO.getCurrentUserId() == null) {
                return new ModelAndView(new RedirectView("Login.html")).addObject("message", "reload");
            } else {
                return new ModelAndView("Home");
            }
        } catch (Exception e) {
            return new ModelAndView(new RedirectView("Login.html")).addObject("message", "logout");
        }

    }

    public boolean checkLogin(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            return false;
        } else if (request.getSession(false).getAttribute("LOGIN_STATUS").equals("TRUE")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean roleLoginCheck(String Role) {

        if (UtilityDAO.getCurrentUserId() != null) {
            if (UtilityDAO.getCurrentUserRole().equals(Role)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @RequestMapping(value = {"/", "/index.html", "/Login.jsp", "/Login.html", "/index.jsp"})
    public ModelAndView WelcomePageRedirection(Model model) {
        return new ModelAndView("Login");
    }

    public void postSuccesLoginJob(String LanguageType, HttpServletRequest request) {
        try {
            File rootDir = new File(servletContext.getRealPath("/WEB-INF/applicationContext.xml"));
            File temp = new File(servletContext.getRealPath("/WEB-INF/"));
            System.out.println(rootDir.getAbsolutePath());
            ConstantContainer.Application_Context_File_Path = rootDir.getAbsolutePath();
            ConstantContainer.WEB_INF_PATH = temp.getAbsolutePath();
        } catch (Exception e) {
        }
        if (LanguageType.equals("BENGALI")) {
            new PropertyFileReader().loadLanguageProperties(request, servletContext, ConstantContainer.LANGUAGES.BENGALI);
            new PropertyFileReader().loadSelectItemProperties(request, servletContext, ConstantContainer.LANGUAGES.BENGALI);
        } else {
            new PropertyFileReader().loadLanguageProperties(request, servletContext, ConstantContainer.LANGUAGES.ENGLISH);
            new PropertyFileReader().loadSelectItemProperties(request, servletContext, ConstantContainer.LANGUAGES.ENGLISH);
        }

    }

}
