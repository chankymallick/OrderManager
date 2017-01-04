/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. */
package com.ordermanager.utility;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SelectHandler extends SimpleTagSupport {
    private boolean emptyField;
    private String listName;    
    private String tableName;
    private String columnName;
    private String queryColumn;
    private String queryValue;
    private boolean fromDB;
    @Override
    public void doTag() throws JspException {
        Map<String, List<String>> selectMap = (Map) getJspContext().findAttribute("SelectList");
        JspWriter out = getJspContext().getOut();
        PropertyFileReader pfp = new PropertyFileReader();
        StringBuilder sb = new StringBuilder();
        try {
            if (fromDB == true) {
                if (listName.equalsIgnoreCase("ORDER_STATUS_TYPES")) {
                    ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
                    UtilityDAO utd = (UtilityDAO)ctx.getBean("UtilityDAO");
                    sb.append(utd.getComboValuesForCustomTagWithQuery("ORDER_STATUS_TYPES", "STATUS_NAME","STATUS_TYPE","MAIN_STATUS",true));
                }
                if (listName.equalsIgnoreCase("MAIN_ITEMS")) {
                    ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
                    UtilityDAO utd = (UtilityDAO)ctx.getBean("UtilityDAO");
                    sb.append(utd.getComboValuesForCustomTagWithQuery("ITEMS", "ITEM_NAME","ITEM_TYPE","MAIN",false));
                }              
                if (listName.equalsIgnoreCase("ORDER_SUB_STATUS_TYPES")) {
                    ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
                    UtilityDAO utd = (UtilityDAO)ctx.getBean("UtilityDAO");
                    sb.append(utd.getComboValuesForCustomTagWithQuery("ORDER_STATUS_TYPES", "STATUS_NAME","STATUS_PARENT_NAME",queryValue,false));
                }              
                if (listName.equalsIgnoreCase("CUSTOM_LIST")) {
                    ApplicationContext ctx = new FileSystemXmlApplicationContext(ConstantContainer.Application_Context_File_Path);
                    UtilityDAO utd = (UtilityDAO)ctx.getBean("UtilityDAO");
                    sb.append(utd.getComboValuesForCustomTagWithQuery(tableName, columnName,queryColumn,queryValue,false));
                }              
            } else {
                List<String> ComboValue = selectMap.get(this.listName);
                if (emptyField == true) {
                    sb.append("{text: \"\", value: \"\"},");
                    for (int i = 0; i < ComboValue.size(); i++) {
                        if (i == ComboValue.size() - 1) {
                            sb.append("{text: \"" + ComboValue.get(i).split(",")[1].trim() + "\", value: \"" + ComboValue.get(i).split(",")[0].trim() + "\"}");
                        } else {
                            sb.append("{text: \"" + ComboValue.get(i).split(",")[1].trim() + "\", value: \"" + ComboValue.get(i).split(",")[0].trim() + "\"},");
                        }
                    }
                } else {
                    for (int i = 0; i < ComboValue.size(); i++) {
                        if (i == ComboValue.size() - 1) {
                            sb.append("{text: \"" + ComboValue.get(i).split(",")[1].trim() + "\", value: \"" + ComboValue.get(i).split(",")[0].trim() + "\"}");
                        } else {
                            sb.append("{text: \"" + ComboValue.get(i).split(",")[1].trim() + "\", value: \"" + ComboValue.get(i).split(",")[0].trim() + "\"},");
                        }
                    }
                }
            }
            out.write(sb.toString());
            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }
        } catch (java.io.IOException ex) {
            throw new JspException("Error in CustomTagHandler tag", ex);
        }
    }

    public void setEmptyField(boolean emptyField) {
        this.emptyField = emptyField;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
     public void setFromDB(boolean fromDB) {
        this.fromDB = fromDB;
    }
     public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setQueryColumn(String queryColumn) {
        this.queryColumn = queryColumn;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

}
