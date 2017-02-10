/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.users.controller;

import org.springframework.stereotype.Controller;
import com.ordermanager.users.dao.EmployeeDAO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Maliick
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeDAO employeeDAO;

    @RequestMapping(value = "/addNewEmployee", method = RequestMethod.GET)
    public ModelAndView addNewEmployee(@RequestParam("ParamData") JSONObject paramJson) {
        return new ModelAndView("MakeResponse", "responseValue", employeeDAO.addNewEmployee(paramJson, "Administrator"));
    }
}
