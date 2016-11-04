<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>  
<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<c:if test="${Type.equals('ordersandbills')}">
    <rows>
        <row id="addneworder">
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
<c:if test="${Type.equals('usermanager')}">
    <rows>
        <row id="addnewuser">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addnewuser" defaultValue="Add New User"/></b>]]></cell>        
        </row>
    </rows>
</c:if>
<c:if test="${Type.equals('ItemSelectionList')}">   
    <rows>
        <head>                
        <column width="250" type="ro" align="left" color="white">SUB_ITEM</column>       
        <column width="240" type="ro" align="right" color="white">ITEM_BUTTON</column>     
        <column width="35" type="ro" align="left" color="white"></column>
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit>           
    </head> 
    <c:set var="ID" value="${0}"/>    
    <c:set var="CSSID" value="${1}"/>   
    <c:forEach items="${DataMap.get('Key_Name')}" var="Key">
        <c:set var="GroupEnd" value="${0}"/>   
        <c:forEach items="${DataMap.get(Key)}" var="DATA_OBJECT">
            <c:forEach items="${DATA_OBJECT}" var="ITEM_NAME">
                <c:if test="${GroupEnd==0}">
                    <row id="${ID+1}">          
                        <userdata name="PARENT_TYPE">${Key}</userdata> 
                        <userdata name="ITEM_NAME">${ITEM_NAME}</userdata> 
                        <cell><![CDATA[<div class='SUB_ITEMCSS'><b>${Key}</b></div>]]></cell>  
                        <cell><![CDATA[<a href="#" class="ITEM_BUTTON${CSSID}">${ITEM_NAME}</a> ]]></cell>
                        <cell></cell>   
                    </row>     
                </c:if>
                <c:if test="${GroupEnd>0}">
                    <row id="${ID+1}">     
                        <userdata name="PARENT_TYPE">${Key}</userdata> 
                        <userdata name="ITEM_NAME">${ITEM_NAME}</userdata> 
                        <cell></cell>  
                        <cell><![CDATA[<a href="#" class="ITEM_BUTTON${CSSID}">${ITEM_NAME}</a> ]]></cell>
                        <cell></cell>   
                    </row>     
                </c:if>              
                <c:set var="ID" value="${ID+1}"/>      
                <c:set var="GroupEnd" value="${GroupEnd+1}"/>
            </c:forEach>            
        </c:forEach>                
        <c:set var="CSSID" value="${CSSID+1}"/>
    </c:forEach>

</rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('addNewItem')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="str">UID</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">ITEM NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">TYPE</column>          
        <column width="70" type="ro" align="center" color="white" sort="int">MASTER</column>   
        <column width="70" type="ro" align="center" color="white" sort="int">TAILOR</column>   
        <column width="70" type="ro" align="center" color="white" sort="int">FINISHER</column>   
        <column width="100" type="ch" align="center" color="white" sort="int">ACTIVE</column>   
        <column width="160" type="ro" align="center" color="white" >NOTE</column>   
        <column width="90" type="ro" align="center" color="white" sort="str">SUB TYPE</column>   
        <column width="90" type="ro" align="center" color="white" sort="str">PARENT</column>   
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>&#160;,#text_search,#select_filter,#text_search,#text_search,#text_search,#text_search,#text_search,#select_filter,#select_filter</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewUser')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="str">UID</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">USER ID</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">FIRST NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">LAST NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">LANGUAGE</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">TYPE</column>   
        <column width="100" type="ch" align="center" color="white" sort="int">ENABLED</column>   
        <column width="160" type="ro" align="center" color="white" >PASSWORD</column>   
        <column width="160" type="ro" align="center" color="white" >NOTE</column>   
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>&#160;,#text_search,#text_search,#text_search,#select_filter,#select_filter,#select_filter,#text_search,#text_search</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('getItemSelectionList')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="str">UID</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">USER ID</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">FIRST NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">LAST NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">LANGUAGE</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">TYPE</column>   
        <column width="100" type="ch" align="center" color="white" sort="int">ENABLED</column>   
        <column width="160" type="ro" align="center" color="white" >PASSWORD</column>   
        <column width="160" type="ro" align="center" color="white" >NOTE</column>   
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>&#160;,#text_search,#text_search,#text_search,#select_filter,#select_filter,#select_filter,#text_search,#text_search</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewOrder')}">
    <rows>
        <head>            
        <column width="100" type="ro" align="center" color="white" sort="str">BILL NO</column>       
        <column width="200" type="ro" align="center" color="white" sort="dhxCalendar">ORDER_DATE</column>   
        <column width="200" type="ro" align="center" color="white" sort="dhxCalendar">DELIVERY_DATE</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">ORDER_STATUS</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">PIECE_VENDOR</column>   
        <column width="100" type="ro" align="center" color="white" sort="int" >PRICE</column>   
        <column width="120" type="ro" align="center" color="white" sort="str">ORDER_TYPE</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">PRODUCT_TYPE</column>   
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search</param>                      
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


