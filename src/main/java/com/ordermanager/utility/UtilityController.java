/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

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
public class UtilityController{
    @Autowired
    UtilityDAO UtilityDAO;    
    @RequestMapping("/isValueUnique")   
    public ModelAndView isValueUnique(@RequestParam("VALUE") String Value,@RequestParam("TABLE_NAME") String TableName,@RequestParam("COLUMN_NAME") String ColumnName){        
    return new ModelAndView("MakeResponse","responseValue",UtilityDAO.isValueUnique(Value, TableName, ColumnName));
    }
    @RequestMapping("/saveUpdateDefaultFormValue")   
    public ModelAndView saveUpdateDefaultFormValue(@RequestParam("VALUE") String VALUE,@RequestParam("KEY") String KEY,@RequestParam("MODULE") String MODULE){        
    return new ModelAndView("MakeResponse","responseValue",UtilityDAO.saveAndUpdateAppData(MODULE, KEY,VALUE));
    }
    @RequestMapping("/getLanguage")   
    public ModelAndView getLanguageforClient(HttpServletRequest request){        
    return new ModelAndView("MakeResponse","responseValue",new PropertyFileReader().loadLanguagePropertiesForClient((Map <String,String>)request.getSession(false).getAttribute("Language")));
    }
    
}
