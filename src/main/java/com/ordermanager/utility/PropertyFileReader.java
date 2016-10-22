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

    public static Map<String, String> languageMap = new HashMap<String, String>();

    public static String queryReader(String QueryName) {
        return "";
    }

    public static String getTranslation(String key, String Default) {
        try {
            return languageMap.getOrDefault(key.trim(), Default.trim());
        } catch (Exception e) {
            return Default;
        }
    }

    public static String getTranslation(String key) {
        try {
            return languageMap.get(key.trim());
        } catch (Exception e) {
            return key;
        }
    }

    public void loadLanguageProperties(HttpServletRequest request, ServletContext servletContext, ConstantContainer.LANGUAGES LanguangeName) {
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
    public String loadLanguagePropertiesForClient() {        
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
