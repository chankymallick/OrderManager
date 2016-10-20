/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

import com.ordermanager.order.dao.OrderDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Maliick
 */
@Controller
public class UtilityController{
    @ResponseBody
    @RequestMapping("/isValueUnique")   
    public ModelAndView isValueUnique(@RequestParam("VALUE") String Value,@RequestParam("TABLE_NAME") String TableName,@RequestParam("COLUMN_NAME") String ColumnName){        
     ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
     UtilityDAO utilityDAO = (UtilityDAO)ctx.getBean("UtilityDAO");
     return new ModelAndView("MakeResponse","responseValue",utilityDAO.isValueUnique(Value, TableName, ColumnName));
    }
    
}
