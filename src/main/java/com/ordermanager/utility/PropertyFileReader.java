package com.ordermanager.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

/**
 *
 * @author Maliick
 */
public class PropertyFileReader extends DAOHelper { 
    public static String queryReader(String QueryName) {
        return "";
    }

    public String getTranslation(String key, String Default,Map <String,String> langaugeMap) {      
        try {
            return langaugeMap.getOrDefault(key.trim(), Default.trim());
        } catch (Exception e) {
            return Default;
        }
    }

    public String getTranslation(String key,Map <String,String> langaugeMap) {
        try {
            return langaugeMap.get(key.trim());
        } catch (Exception e) {
            return key;
        }
    }

    public void loadLanguageProperties(HttpServletRequest request, ServletContext servletContext, ConstantContainer.LANGUAGES LanguangeName) {
        Map<String,String> languageMap = new HashMap();
        File rootDir =null;
        try {
            if (LanguangeName.equals(LanguangeName.BENGALI)) {
                rootDir = new File(servletContext.getRealPath("/WEB-INF/Language/Bengali_Language_Pack.properties"));
            } else {
                rootDir = new File(servletContext.getRealPath("/WEB-INF/Language/English_Language_Pack.properties"));
            }
            System.out.println(rootDir.getAbsolutePath());
            Properties LanguageProperties = new Properties();
            LanguageProperties.load(new FileInputStream(new File(rootDir.getAbsolutePath())));
            Enumeration<Object> keys = LanguageProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = LanguageProperties.getProperty(key);
                languageMap.put(key.trim(), value.trim());
            }

            request.getSession(false).setAttribute("Language", languageMap);
        } catch (Exception e) {
        }
    }
    public String loadLanguagePropertiesForClient(Map <String,String>languageMap) {        
        ResponseJSONHandler rsp =new ResponseJSONHandler();
        try {
            JSONObject obj = new JSONObject(languageMap);
            generateSQLSuccessResponse(rsp, "Language pack Recieved");
            rsp.addResponseValue("LANGUAGE_PACK",obj);
            return rsp.getJSONResponse();
        } catch (Exception e) {
            generateSQLExceptionResponse(rsp, e, "Exception Loading Language Properties");
            return rsp.getJSONResponse();
        }
    }
}
