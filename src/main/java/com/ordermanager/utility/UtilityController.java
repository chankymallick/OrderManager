/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

import com.ordermanager.backupmanager.BackUpSQLServer;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @ModelAttribute
    public void MemoryMonitor(Model mvc) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory(); // current heap allocated to the VM process
        long freeMemory = runtime.freeMemory(); // out of the current heap, how much is free
        long maxMemory = runtime.maxMemory(); // Max heap VM can use e.g. Xmx setting
        long usedMemory = totalMemory - freeMemory; // how much of the current heap the VM is using
        long availableMemory = maxMemory - usedMemory;
        
         System.out.println("Total : "+totalMemory);
        System.out.println("freeMemory : "+freeMemory);
        System.out.println("usedMemory : "+usedMemory);

             
    }

    @RequestMapping("/isValueUnique")
    public ModelAndView isValueUnique(@RequestParam("VALUE") String Value, @RequestParam("TABLE_NAME") String TableName, @RequestParam("COLUMN_NAME") String ColumnName) {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.isValueUnique(Value, TableName, ColumnName));
    }

    @RequestMapping("/isCompositeValueUnique")

    public ModelAndView isCompositeValueUnique(@RequestParam("VALUE1") String Value1, @RequestParam("VALUE2") String Value2, @RequestParam("TABLE_NAME") String TableName, @RequestParam("COLUMN_NAME1") String ColumnName1, @RequestParam("COLUMN_NAME2") String ColumnName2) {

        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.isCompositeValueUnique(TableName, ColumnName1, ColumnName2, Value1, Value2));
    }

    @RequestMapping("/saveUpdateDefaultFormValue")
    public ModelAndView saveUpdateDefaultFormValue(@RequestParam("VALUE") String VALUE, @RequestParam("KEY") String KEY, @RequestParam("MODULE") String MODULE) {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.saveAndUpdateAppData(MODULE, KEY, VALUE));
    }

    @RequestMapping("/getLanguage")
    public ModelAndView getLanguageforClient(HttpServletRequest request) {
        return new ModelAndView("MakeResponse", "responseValue", new PropertyFileReader().loadLanguagePropertiesForClient(request));
    }

    @RequestMapping("/getComboValues")
    public ModelAndView getComboValue(@RequestParam("TableName") String TableName, @RequestParam("ColumnName") String ColumnName, @RequestParam("QueryColumn") String QueryColumn, @RequestParam("QueryValue") String QueryValue) throws SQLException {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.getComboValues(TableName, ColumnName, QueryColumn, QueryValue));
    }

    @RequestMapping("/getdBStatus")
    public ModelAndView getDBStatus() throws SQLException {
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.isDbConnected123());
    }
    @RequestMapping("/databaseBackUP")
    public ModelAndView databaseBackUP() throws SQLException {            
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.createAndUploadDatabaseBackUp());
    }
    
    @RequestMapping(value="/setImage",method = RequestMethod.POST)
    public ModelAndView setImage(@RequestBody String imageData) throws SQLException {           
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.setImage(imageData));       
    }
    @RequestMapping(value="/getImage",method = RequestMethod.GET)
    public ModelAndView getAllImage(@RequestParam String MODULE,@RequestParam String KEY) throws SQLException {           
        return new ModelAndView("MakeResponse", "responseValue", UtilityDAO.getImage(MODULE,KEY));       
    }

    
}
