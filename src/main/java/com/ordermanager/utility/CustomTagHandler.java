/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.util.Map;

/**
 *
 * @author Maliick
 */
public class CustomTagHandler extends SimpleTagSupport {

    private String defaultValue;
    private String key;
    

    @Override
    public void doTag() throws JspException {
        Map <String,String> langaugeMap = (Map)getJspContext().findAttribute("Language");   
        JspWriter out = getJspContext().getOut();
        PropertyFileReader pfp = new PropertyFileReader();
        try {
            if (defaultValue == null) {
                out.write(pfp.getTranslation(key,langaugeMap));
            } else {
                out.write(pfp.getTranslation(key, defaultValue,langaugeMap));
            }
            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in CustomTagHandler tag", ex);
        }
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
