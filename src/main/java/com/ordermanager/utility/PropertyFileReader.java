package com.ordermanager.utility;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 *
 * @author Maliick
 */
public class PropertyFileReader {
    public static String queryReader(String QueryName){
        ResourceBundle rb = ResourceBundle.getBundle("com.ordermanager.utility.properties.Language_Bengali");
        Enumeration <String>  keys = rb.getKeys();
        while(keys.hasMoreElements()){
        String key = keys.nextElement();
        String value = rb.getString(key);
            System.out.println(value);
           
        }
    return "";
    }      
    public void loadLanguageProperties(){
    
    }   
}
