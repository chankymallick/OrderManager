<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>  
<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<c:if test="${Type.equals('ordersandbills')}">
    <rows>
        <row id="1">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addneworder"/></b>]]></cell>        
        </row>
        <row id="2">
            <cell><![CDATA[<img src="resources/Images/advance.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addadvance"/></b>]]></cell>  
        </row>
        <row id="3">
            <cell><![CDATA[<img src="resources/Images/edit_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="editorderdetails"/></b>]]></cell>  
        </row>
        <row id="4">
            <cell><![CDATA[<img src="resources/Images/alert.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addorderhalt"/></b>]]></cell>  
        </row>
        <row id="5">
            <cell><![CDATA[<img src="resources/Images/cancel_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="cancelorder"/></b>]]></cell>  
        </row>
        <row id="6">
            <cell><![CDATA[<img src="resources/Images/search2.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="searchorder"/></b>]]></cell>  
        </row>
        <row id="7">
            <cell><![CDATA[<img src="resources/Images/status.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addstatus"/></b>]]></cell>  
        </row>
        <row id="8">
            <cell><![CDATA[<img src="resources/Images/settings.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="ordersettings"/></b>]]></cell>  
        </row>
        <row id="AddNewItem">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addnewitem"/></b>]]></cell>  
        </row>
    </rows>
</c:if>
<c:if test="${Type.equals('mastertailor')}">
    <rows>
        <row id="1">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Master Entry</b>]]></cell>        
        </row>
        <row id="2">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Tailor Entry</b>]]></cell>  
        </row>
        <row id="3">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Single Master/Tailor Entry</b>]]></cell>  
        </row>
    </rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('loadNewOrderItemForm')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="left" color="white" sort="str">UID</column>   
        <column width="200" type="ro" align="left" color="white" sort="str">ITEM NAME</column>   
        <column width="100" type="ro" align="left" color="white" sort="str">ITEM TYPE</column>   
        <column width="100" type="ro" align="left" color="white" sort="int">MASTER PRICE</column>   
        <column width="100" type="ro" align="left" color="white" sort="int">TAILOR PRICE</column>   
        <column width="150" type="ro" align="left" color="white" sort="int">FINISHER PRICE</column>   
        <column width="100" type="ch" align="left" color="white" sort="int">ACTIVE</column>   
        <column width="160" type="ro" align="left" color="white" >NOTE</column>   
          <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>&#160;,#text_search,#select_filter,#text_search,#text_search,#text_search,#text_search,#text_search</param>                      
            </call> 
        </afterInit>     
    </head> 

    <c:set var="ID" value="${0}"/>
    <c:forEach items="${OBJECT_MAP.get('ALL_ROWS_LIST')}" var="DATA_OBJECT">
        <row id="${ID+1}">
            <c:forEach items="${OBJECT_MAP.get('COLUMN_NAME_LIST')}" var="COLUMN_NAME">
                <cell>${DATA_OBJECT.get(COLUMN_NAME)}</cell>
                </c:forEach>
        </row>     
        <c:set var="ID" value="${ID+1}"/>   
    </c:forEach>
</rows>
</c:if>


