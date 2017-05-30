/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Maliick
 */
@Controller
public class UtilityController {

    @Autowired
    UtilityDAO UtilityDAO;

    @RequestMapping("/isValueUnique")
    public ModelAndView isValueUnique(@RequestParam("VALUE") String Value, @RequestParam("TABLE_NAME") String TableName, @RequestParam("COLUMN_NAME") String ColumnName) {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.isValueUnique(Value, TableName, ColumnName));
    }
    
    @RequestMapping("/isCompositeValueUnique")
    
    public ModelAndView isCompositeValueUnique(@RequestParam("VALUE1") String Value1, @RequestParam("VALUE2") String Value2,@RequestParam("TABLE_NAME") String TableName, @RequestParam("COLUMN_NAME1") String ColumnName1,@RequestParam("COLUMN_NAME2") String ColumnName2) {
        
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.isCompositeValueUnique(TableName, ColumnName1,ColumnName2,Value1,Value2));
    }

    @RequestMapping("/saveUpdateDefaultFormValue")
    public ModelAndView saveUpdateDefaultFormValue(@RequestParam("VALUE") String VALUE, @RequestParam("KEY") String KEY, @RequestParam("MODULE") String MODULE) {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.saveAndUpdateAppData(MODULE, KEY, VALUE));
    }

    @RequestMapping("/getLanguage")
    public ModelAndView getLanguageforClient(HttpServletRequest request) {
        return new ModelAndView("MakeResponse", "responseValue", new PropertyFileReader().loadLanguagePropertiesForClient((Map<String, String>) request.getSession(false).getAttribute("Language")));
    }

    @RequestMapping("/getComboValues")
    public ModelAndView getComboValue(@RequestParam("TableName") String TableName, @RequestParam("ColumnName") String ColumnName, @RequestParam("QueryColumn") String QueryColumn, @RequestParam("QueryValue") String QueryValue) throws SQLException {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.getComboValues(TableName, ColumnName, QueryColumn, QueryValue));
    }
    @RequestMapping("/getdBStatus")
    public ModelAndView getDBStatus() throws SQLException {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.isDbConnected123());
    }
}
