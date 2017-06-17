package com.ordermanager.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
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

    public String getTranslation(String key, String Default, Map<String, String> langaugeMap) {
        try {
            return langaugeMap.getOrDefault(key.trim(), Default.trim());
        } catch (Exception e) {
            return Default;
        }
    }

    public String getTranslation(String key, Map<String, String> langaugeMap) {
        try {
            return langaugeMap.get(key.trim());
        } catch (Exception e) {
            return key;
        }
    }

    public void loadLanguageProperties(HttpServletRequest request, ServletContext servletContext, ConstantContainer.LANGUAGES LanguangeName) {
        Map<String, String> languageMap = new HashMap();
        File rootDir = null;
        try {
            if (LanguangeName.equals(LanguangeName.BENGALI)) {
                rootDir = new File(servletContext.getRealPath("/WEB-INF/Properties/Bengali_Language_Pack.properties"));
            } else {
                rootDir = new File(servletContext.getRealPath("/WEB-INF/Properties/English_Language_Pack.properties"));
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

    public static String getPropertiesFileValue(String Key) {
        try {
            File rootDir = new File(ConstantContainer.WEB_INF_PATH + "/Properties/AppSettings.properties");
            Properties LanguageProperties = new Properties();
            LanguageProperties.load(new FileInputStream(rootDir.getAbsolutePath()));
            System.out.println(LanguageProperties.getProperty(Key));
            return LanguageProperties.getProperty(Key);
        } catch (Exception e) {
            return null;
        }        
    }

    public void loadSelectItemProperties(HttpServletRequest request, ServletContext servletContext, ConstantContainer.LANGUAGES LanguangeName) {
        Map<String, List<String>> selectItemMap = new HashMap();
        File rootDir = null;
        boolean isBengali = false;
        try {
            rootDir = new File(servletContext.getRealPath("/WEB-INF/Properties/SelecItemContainer.properties"));;
            if (LanguangeName.equals(LanguangeName.BENGALI)) {
                isBengali = true;
            }
            Properties SelectItemProperties = new Properties();
            SelectItemProperties.load(new FileInputStream(new File(rootDir.getAbsolutePath())));
            Enumeration<Object> keys = SelectItemProperties.keys();
            List<String> selectValues;
            Set<String> uniqueSelects = new LinkedHashSet();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String selectItem[] = key.split("\\.");
                uniqueSelects.add(selectItem[0] + "." + selectItem[1]);
            }

            for (String value : uniqueSelects) {
                keys = SelectItemProperties.keys();
                selectValues = new ArrayList();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    String selectItem[] = key.split("\\.");
                    if ((value + "." + selectItem[2]).equals(selectItem[0] + "." + selectItem[1] + "." + selectItem[2])) {
                        if (isBengali) {
                            selectValues.add(SelectItemProperties.getProperty(key));
                        } else {
                            selectValues.add(SelectItemProperties.getProperty(key).split(",")[0] + "," + SelectItemProperties.getProperty(key).split(",")[0]);
                        }
                    }
                }
                selectItemMap.put(value, selectValues);
            }
            request.getSession(false).setAttribute("SelectList", selectItemMap);
        } catch (Exception e) {

        }
    }

    public String loadLanguagePropertiesForClient(Map<String, String> languageMap) {
        ResponseJSONHandler rsp = new ResponseJSONHandler();
        try {
            JSONObject obj = new JSONObject(languageMap);
            generateSQLSuccessResponse(rsp, "Language pack Recieved");
            rsp.addResponseValue("LANGUAGE_PACK", obj);
            return rsp.getJSONResponse();
        } catch (Exception e) {
            generateSQLExceptionResponse(rsp, e, "Exception Loading Language Properties");
            return rsp.getJSONResponse();
        }
    }
}
