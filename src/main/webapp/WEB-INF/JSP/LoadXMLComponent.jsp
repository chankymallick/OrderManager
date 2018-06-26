<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>  
<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<c:if test="${Type.equals('ordersandbills')}">
    <rows>
        <row id="addneworder">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addneworder"/></b>]]></cell>        
        </row>
        <row id="quickNewOrder">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="quickNewOrder"/></b>]]></cell>        
        </row>
        <row id="addadvance">
            <cell><![CDATA[<img src="resources/Images/advance.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addadvance"/></b>]]></cell>  
        </row>
        <row id="updateNewOrder">
            <cell><![CDATA[<img src="resources/Images/edit_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="updateNewOrder"/></b>]]></cell>  
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
        <row id="advanceReport">
            <cell><![CDATA[<img src="resources/Images/status.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="advanceReport"/></b>]]></cell>  
        </row>
        <row id="addNewStatusType">
            <cell><![CDATA[<img src="resources/Images/status.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addNewStatusType"/></b>]]></cell>  
        </row>
        <row id="addNewLocation">
            <cell><![CDATA[<img src="resources/Images/status.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addNewLocation"/></b>]]></cell>  
        </row>
    </rows>
</c:if>
<c:if test="${Type.equals('mastertailor')}">
    <rows>
        <row id="1">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Single Entry</b>]]></cell>        
        </row>
        <row id="2">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Master & Tailor Entry</b>]]></cell>  
        </row>
        <row id="3">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Iron & Finisher Entry</b>]]></cell>  
        </row>
        <row id="4">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Assignment Change & Cancel</b>]]></cell>  
        </row>
        <row id="5">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Production Enquiry</b>]]></cell>  
        </row>
        <row id="6">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Wage Payment</b>]]></cell>  
        </row>
        <row id="7">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Print Payment Details</b>]]></cell>  
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
<c:if test="${Type.equals('labourmanager')}">
    <rows>
        <row id="addNewEmployee">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b><mytags:getTranslation key="addNewEmployee" defaultValue="Add New Employee"/></b>]]></cell>        
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
                            <c:set var="contains" value="false" />
                            <c:forEach var="SavedItem" items="${SavedList}">
                                <c:if test="${SavedItem eq ITEM_NAME}">
                                    <c:set var="contains" value="true" />
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test = "${contains eq 'true' }">    
                                <cell><![CDATA[<img src='resources/Images/ok.png' width='30px' height='30px'/>]]></cell>   
                                </c:when>                 
                                <c:otherwise>           
                                <cell></cell>   
                                </c:otherwise>
                            </c:choose>                        
                    </row>     
                </c:if>
                <c:if test="${GroupEnd>0}">
                    <row id="${ID+1}">     
                        <userdata name="PARENT_TYPE">${Key}</userdata> 
                        <userdata name="ITEM_NAME">${ITEM_NAME}</userdata> 
                        <cell></cell>  
                        <cell><![CDATA[<a href="#" class="ITEM_BUTTON${CSSID}">${ITEM_NAME}</a> ]]></cell>
                            <c:set var="contains" value="false" />
                            <c:forEach var="SavedItem" items="${SavedList}">
                                <c:if test="${SavedItem eq ITEM_NAME}">
                                    <c:set var="contains" value="true" />
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test = "${contains eq 'true' }">    
                                <cell><![CDATA[<img src='resources/Images/ok.png' width='30px' height='30px'/>]]></cell>   
                                </c:when>                 
                                <c:otherwise>           
                                <cell></cell>   
                                </c:otherwise>
                            </c:choose>     
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
        <column width="90" type="ro" align="center" color="white" sort="str">PARENT</column>   
        <column width="90" type="ro" align="center" color="white" sort="str">SUB TYPE</column> 
        <column width="70" type="ro" align="center" color="white" sort="int">MASTER</column>   
        <column width="70" type="ro" align="center" color="white" sort="int">TAILOR</column>   
        <column width="70" type="ro" align="center" color="white" sort="int">FINISHER</column>   
        <column width="100" type="ch" align="center" color="white" sort="int">ACTIVE</column>   
        <column width="160" type="ro" align="center" color="white" >NOTE</column>        
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>&#160;,#text_search,#select_filter,#select_filter,#select_filter,#text_search,#text_search,#text_search,#text_search,#text_search</param>                      
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

<c:if test="${OBJECT_MAP.get('Type').equals('addNewAccount')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="str">UID</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">MODULE</column>   
        <column width="250" type="ro" align="center" color="white" sort="str">ACCOUNT NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">MOBILE</column>   
        <column width="250" type="ro" align="center" color="white" sort="str">ADDRES</column>   
        <column width="250" type="ro" align="center" color="white" sort="str">BANK DETAILS</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">DATE OF CREATION</column>   
        <column width="100" type="ro" align="center" color="white" sort="int">VIEW ORDER</column>   
        <column width="50" type="ro" align="center" color="white" sort="int">ACTIVE</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">NOTE</column>      
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#select_filter,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewAccountTransaction')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="int">UID</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">DATE</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">MODULE</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">SHOP</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">TYPE</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">AC NAME</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">TR DETAILS</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">SUB TYPE</column>   
        <column width="70" type="ro" align="center" color="white" sort="int">AMOUNT</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">NOTE</column>      
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#select_filter,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewAccountSubType')}">
    <rows>
        <head>            
        <column width="100" type="ro" align="center" color="white" sort="int">UID</column>      
        <column width="200" type="ro" align="center" color="white" sort="str">MODULE</column>       
        <column width="200" type="ro" align="center" color="white" sort="str">SUB TYPE</column>   
        <column width="200" type="ro" align="center" color="white" sort="int">ACTIVE</column>         
        <column width="200" type="ro" align="center" color="white" sort="str">NOTE</column>      
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#select_filter,#text_search,#select_filter,#text_search</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewTask')}">
    <rows>
        <head>            
        <column width="100" type="ro" align="center" color="white" sort="int">UID</column>      
        <column width="200" type="ro" align="center" color="white" sort="str">TASK NAME</column>       
        <column width="100" type="ro" align="center" color="white" sort="str">TYPE</column>   
        <column width="60" type="ro" align="center" color="white" sort="str">PRIORITY</column>      
        <column width=200 type="ro" align="center" color="white" sort="str">TIMING</column>      
        <column width="100" type="ro" align="center" color="white" sort="str" >STATUS</column>      
        <column width="100" type="ro" align="center" color="white" sort="str" >NOTE</column>         
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#select_filter,#text_search,#select_filter,#text_search,#text_search,#text_search</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('taskList')}">
    <rows>
        <head>            
        <column width="100" type="ro" align="left" color="#b5e2ff" sort="int">UID</column>      
        <column width="300" type="ro" align="left" color="#b5e2ff" sort="str">DATA</column> 
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>                 
            <call command="setColumnHidden"><param>0</param><param>true</param></call>        
        </afterInit>     
    </head> 

    <c:set var="ID" value="${0}"/>
    <c:forEach items="${OBJECT_MAP.get('ALL_ROWS_LIST')}" var="DATA_OBJECT">
        <row id="${ID+1}">
            <c:forEach items="${OBJECT_MAP.get('COLUMN_NAME_LIST')}" var="COLUMN_NAME">
                <cell><![CDATA[  ${DATA_OBJECT.get(COLUMN_NAME)}  ]]></cell>
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
        <column width="100" type="ro" align="center" color="white" sort="dhxCalendar">ORDER DATE</column>   
        <column width="100" type="ro" align="center" color="white" sort="dhxCalendar">DELIVERY</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">ORDER STATUS</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">PIECE VENDOR</column>   
        <column width="100" type="ro" align="center" color="white" sort="int" >PRICE</column>   
        <column width="120" type="ro" align="center" color="white" sort="str">ORDER TYPE</column>   
        <column width="150" type="ro" align="center" color="white" sort="str">PRODUCT TYPE</column>   
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
<c:if test="${OBJECT_MAP.get('Type').equals('quickNewOrder')}">
    <rows>
        <head>            
        <column width="200" type="ro" align="center" color="white" sort="str">BILL NO</column>       
        <column width="200" type="ro" align="center" color="white" sort="str">ORDER DATE</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">PRICE</column>  
        <column width="200" type="ro" align="center" color="white" sort="str">ADVANCE</column>    
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewStatusType')}">
    <rows>
        <head>            
        <column width="50" type="ro" align="center" color="white" sort="str">UID</column>       
        <column width="150" type="ro" align="center" color="white" sort="str">STATUS TYPE</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">STATUS NAME</column>  
        <column width="200" type="ro" align="center" color="white" sort="str">PARENT</column>    
        <column width="80" type="ro" align="center" color="white" sort="str">ORDER</column>    
        <column width="200" type="ro" align="center" color="white" sort="str">NOTE</column>    
        <column width="80" type="ch" align="center" color="white" sort="int">ACTIVE</column>    
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#select_filter,#text_search,#select_filter,#text_search,#text_search,#select_filter</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewLocation')}">
    <rows>
        <head>            
        <column width="150" type="ro" align="center" color="white" sort="str">UID</column>       
        <column width="200" type="ro" align="center" color="white" sort="str">LOCATION NAME</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">PARENT STATUS</column>     
        <column width="200" type="ro" align="center" color="white" sort="str">NOTE</column>     
        <column width="180" type="ch" align="center" color="white" sort="int">ACTIVE</column>    
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#text_search,#select_filter,#text_search,#select_filter</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('addNewEmployee')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="int">UID</column>       
        <column width="180" type="ro" align="center" color="white" sort="str">NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="str">MOBILE1</column>     
        <column width="100" type="ro" align="center" color="white" sort="str">MOBILE2</column>     
        <column width="150" type="ro" align="center" color="white" sort="str">ADDRESS</column>    
        <column width="100" type="ro" align="center" color="white" sort="str">ROLE</column>
        <column width="100" type="ro" align="center" color="white" sort="str">PAY</column>
        <column width="120" type="ro" align="center" color="white" sort="str">HOLIDAY</column>
        <column width="60" type="ch" align="center" color="white" sort="str">ACTIVE</column>
        <column width="150" type="ro" align="center" color="white" sort="str">NOTE</column>
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#text_search,#text_search,#text_search,#text_search,#select_filter,#select_filter,#select_filter,#select_filter,#text_search</param>                      
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
<c:if test="${OBJECT_MAP.get('Type').equals('advanceReport')}">
    <rows>
        <head>            
        <column width="200" type="ro" align="center" color="white" sort="str">BILL NO</column>       
        <column width="200" type="ro" align="center" color="white" sort="str">ORDER DATE</column>   
        <column width="200" type="ro" align="center" color="white" sort="str">PRICE</column>  
        <column width="200" type="ro" align="center" color="white" sort="str">ADVANCE</column>    
        <column width="200" type="ro" align="center" color="white" sort="str">PAYMENT TYPE</column>    
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#text_search,#text_search,#text_search,#text_search</param>                      
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



<c:if test="${OBJECT_MAP.get('Type').equals('deliveryTransactionsReport')}">
    <rows>
        <head>            
        <column width="100" type="ro" align="center" color="white" sort="str">SR</column>       
        <column width="100" type="ro" align="center" color="white" sort="str">BILL NO</column>       
        <column width="250" type="ro" align="center" color="white" sort="str">NAME</column>   
        <column width="100" type="ro" align="center" color="white" sort="int">PRICE</column>  
        <column width="100" type="ro" align="center" color="white" sort="int">ADVANCE</column>    
        <column width="100" type="ro" align="center" color="white" sort="int">DISCOUNT </column>    
        <column width="100" type="ro" align="center" color="white" sort="int">PAID</column>    
        <column width="150" type="ro" align="center" color="white" sort="str">NOTE</column>    
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





<c:if test="${OBJECT_MAP.get('Type').equals('updateBulkMasterTailor')}">
    <rows>
        <head>            
        <column width="40" type="ro" align="center" color="white" >NO</column>       
        <column width="80" type="ro" align="center" color="white" >BILL NO</column>       
        <column width="100" type="ro" align="center" color="white" >ORDER</column>
        <column width="100" type="ro" align="center" color="white" >DELIVERY</column>
        <column width="40" type="ro" align="center" color="white" >QTY</column>
        <column width="90" type="ro" align="center" color="white" >DAY</column>
        <column width="90" type="ro" align="center" color="white" >REMAINING</column>  
        <column width="100" type="ro" align="center" color="white" >STATUS</column>
        <column width="100" type="ro" align="center" color="white" >ITEMS</column>
        <column width="80" type="ro" align="center" color="white" >STARS</column> 
        <column width="80" type="ro" align="center" color="white" >REMOVE</column> 
        <column width="100" type="ro" align="center" color="white" >RESULT</column> 
    </head> 
</rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('assignmentStatusChange')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" >NO</column>       
        <column width="60" type="ro" align="center" color="white" >BILL NO</column>       
        <column width="30" type="ro" align="center" color="white" >QTY</column>
        <column width="70" type="ro" align="center" color="white" >DELIVERY DATE</column>
        <column width="80" type="ro" align="center" color="white" >NAME</column>
        <column width="70" type="ro" align="center" color="white" >ASSIGNED DATE</column>
        <column width="130" type="ro" align="center" color="white" >MAIN STATUS</column>  
        <column width="130" type="ro" align="center" color="white" >SUB STATUS</column>
        <column width="80" type="ro" align="center" color="white" >CURRENT LOCATION</column>        
        <column width="70" type="ro" align="center" color="white" >WAGE STATUS</column>        
        <column width="60" type="ro" align="center" color="white" >WAGE AMOUNT</column>        
        <column width="70" type="ro" align="center" color="white" >ORDER TYPE</column>        
        <column width="60" type="ro" align="center" color="white" >REMOVE</column>      
        <column width="60" type="ro" align="center" color="white" >RESULT</column>     
    </head> 
</rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('updateBulkToSingle')}">
    <rows>
        <head>            
        <column width="40" type="ro" align="center" color="white" >NO</column>       
        <column width="80" type="ro" align="center" color="white" >BILL NO</column>       
        <column width="100" type="ro" align="center" color="white" >ORDER</column>
        <column width="100" type="ro" align="center" color="white" >DELIVERY</column>
        <column width="40" type="ro" align="center" color="white" >QTY</column>
        <column width="90" type="ro" align="center" color="white" >DAY</column>
        <column width="90" type="ro" align="center" color="white" >REMAINING</column>  
        <column width="100" type="ro" align="center" color="white" >STATUS</column>
        <column width="100" type="ro" align="center" color="white" >ITEMS</column>
        <column width="80" type="ro" align="center" color="white" >STARS</column> 
        <column width="80" type="ro" align="center" color="white" >REMOVE</column> 
        <column width="100" type="ro" align="center" color="white" >RESULT</column> 
    </head> 
</rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('updateBulkReadyToDeliver')}">
    <rows>
        <head>            
        <column width="40" type="ro" align="center" color="white" >NO</column>       
        <column width="80" type="ro" align="center" color="white" >BILL NO</column>        
        <column width="100" type="ro" align="center" color="white" >SHOP</column>
        <column width="120" type="ro" align="center" color="white" >DELIVERY</column>
        <column width="40" type="ro" align="center" color="white" >QTY</column>        
        <column width="130" type="ro" align="center" color="white" >STATUS</column>
        <column width="100" type="ro" align="center" color="white" >ITEM</column>
        <column width="300" type="ro" align="center" color="white" >EXTRAS</column> 
        <column width="80" type="ro" align="center" color="white" >REMOVE</column>      
    </head> 
</rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('updateDeliveryCompleted')}">
    <rows>
        <head>            
        <column width="40" type="ro" align="center" color="white" >NO</column>       
        <column width="100" type="ro" align="center" color="white" >BILL NO</column>        
        <column width="40" type="ro" align="center" color="white" >QTY</column>
        <column width="80" type="ro" align="center" color="white" >PRICE</column>
        <column width="100" type="ro" align="center" color="white" >ADVANCE</column>
        <column width="70" type="edn" align="center" color="white" >DISCOUNT</column>        
        <column width="60" type="ro" align="center" color="white" >DUE</column>
        <column width="150" type="ro" align="center" color="white" >ORDER TYPE</column>
        <column width="150" type="ro" align="center" color="white" >STATUS</column> 
        <column type="ro" align="center" color="white" >NOTE</column>     
        <column type="ro" align="center" color="white" >REMOVE</column>
        <column  type="ro" align="center" color="white" >RESULT</column> 
    </head> 
</rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('SchedulerData')}">
    <data>
        <c:set var="ID" value="${0}"/>
        <c:forEach items="${OBJECT_MAP.get('DATA')}" var="DATA_OBJECT" >
            <event id="${ID}">
                <text><![CDATA[<b style='display:inline-block;width:120px;background-color: #00c9f2;color:white;font-size: 14px;'>${DATA_OBJECT[0]}</b>]]></text>
                <start_date>${DATA_OBJECT[3]}</start_date>
                <end_date>${DATA_OBJECT[3]}</end_date>
            </event>            
            <c:set var="ID" value="${ID+1}"/>
            <event id="${ID}">
                <text><![CDATA[<b style='display:inline-block;width:120px;background-color: yellowgreen;color:white;font-size: 14px;'>${DATA_OBJECT[1]}</b>]]>></text>
                <start_date>${DATA_OBJECT[3]}</start_date>
                <end_date>${DATA_OBJECT[3]}</end_date>
            </event>        
            <c:set var="ID" value="${ID+1}"/>
            <event id="${ID}">
                <text><![CDATA[<b style='display:inline-block;width:120px;background-color: red;color:white;font-size: 14px;'>${DATA_OBJECT[2]}</b>]]></text>
                <start_date>${DATA_OBJECT[3]}</start_date>
                <end_date>${DATA_OBJECT[3]}</end_date>
            </event>   
            <c:set var="ID" value="${ID+1}"/>
        </c:forEach>
    </data>
</c:if>  
<c:if test="${OBJECT_MAP.get('Type').equals('DayWiseProduction')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="int">NO</column>       
        <column width="50" type="ro" align="center" color="white" sort="str">BILL NO</column>        
        <column width="200" type="ro" align="center" color="white" sort="str">MAIN STATUS</column>            
        <column width="150" type="ro" align="center" color="white" sort="str">LOCATION</column>    
        <column width="50" type="ro" align="center" color="white" sort="int">WAGE</column>    
        <column width="100" type="ro" align="center" color="white" sort="str">WAGE STATUS</column>    
        <column width="100" type="ro" align="center" color="white" sort="str">ITEMS</column>    
        <column width="35" type="ro" align="center" color="white" sort="int">QTY</column>    
        <column width="100" type="ro" align="center" color="white" sort="str">ORDER TYPE</column>    
        <column width="70" type="ro" align="center" color="white" sort="str">PIECE VENDOR</column>
        <column width="100" type="ro" align="center" color="white" sort="str">PIECE</column> 
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search</param>
            </call> 
        </afterInit>     
    </head> 
    <c:set var="ID" value="${0}"/>    
    <c:forEach items="${OBJECT_MAP.get('DATA')}" var="DATE_WISE_LIST_MAP">
        <row id="${ID+1}" style="font-weight: bold;background-color:orangered;color:white;font-size: 20px;text-align: left;">            
            <cell colspan='11'><![CDATA[ ${DATE_WISE_LIST_MAP.key}]]></cell>               

        </row>    
        <c:forEach items="${DATE_WISE_LIST_MAP.value}" var="ARRAY_LIST_OF_ALL_BILL">
            <c:set var="ID" value="${ID+1}"/>   
            <row id="${ID+1}">    
                <cell></cell>        
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('BILL_NO')}</cell>                     
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('MAIN_STATUS')}</cell> 
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('LOCATION')}</cell>        
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('WAGE_AMOUNT')}</cell>        
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('WAGE_STATUS')}</cell>        
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('ITEMS')}</cell>        
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('QUANTITY')}</cell>        
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('ORDER_TYPE')}</cell>        
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('PIECE_VENDOR')}</cell>     
                <cell>${ARRAY_LIST_OF_ALL_BILL.get('PIECE_VENDOR')}</cell>        
            </row>                
        </c:forEach>        
        <c:set var="ID" value="${ID+1}"/>   
    </c:forEach>
</rows>
</c:if>
<c:if test="${OBJECT_MAP.get('Type').equals('DayWiseProduction2')}">
    <rows>
        <head>            
        <column width="30" type="ro" align="center" color="white" sort="int">NO</column>       
        <column width="50" type="ro" align="center" color="white" sort="str">BILL</column>        
        <column width="200" type="ro" align="center" color="white" sort="str">MAIN STATUS</column>            
        <column width="150" type="ro" align="center" color="white" sort="str">LOCATION</column>    
        <column width="50" type="ro" align="center" color="white" sort="int">WAGE</column>    
        <column width="100" type="ro" align="center" color="white" sort="str">WAGE STATUS</column>    
        <column width="100" type="ro" align="center" color="white" sort="str">ITEMS</column>    
        <column width="35" type="ro" align="center" color="white" sort="int">QTY</column>    
        <column width="100" type="ro" align="center" color="white" sort="str">ORDER TYPE</column>    
        <column width="70" type="ro" align="center" color="white" sort="str"> VENDOR</column>
        <column width="100" type="ro" align="center" color="white" sort="str">PIECE</column> 
        <beforeInit> 
            <call command="setImagePath"> 
                <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
            </call>             
        </beforeInit> 
        <afterInit>  
            <call command="attachHeader">
                <param>#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search</param>
            </call> 
        </afterInit>     
    </head> 
    <c:set var="ID" value="${0}"/>    
    <c:set var="DATE" value=""/>
    <c:forEach items="${OBJECT_MAP.get('DATA')}" var="LIST_OF_ALL_BII_OBJECT">  
        <c:if test="${!DATE.equals(LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')) || DATE.equals('')}">   
            <row id="${ID*1000}" style="font-weight: bold;background-color:#06b;color:white;font-size: 20px;text-align: left;">            
                <cell colspan='11'><![CDATA[ ${LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')}]]> - ${LIST_OF_ALL_BII_OBJECT.get('WEEKDAY')}</cell> 
            </row>  
        </c:if>
        <c:set var="DATE" value="${LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')}"/>   
        <c:choose>
            <c:when test="${!LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS').equals('READY_TO_DELIVER')}">
                <row id="${ID+1}" style='font-weight: bold; background-color:#ffa35e;'>    
                </c:when>
                <c:when test="${LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS').equals('READY_TO_DELIVER')}">
                    <row id="${ID+1}" style='font-weight: bold; background-color: palegreen;'>    
                    </c:when>
                    <c:otherwise>
                        <row id="${ID+1}">    
                        </c:otherwise>
                    </c:choose>  
                    <cell>${ID+1}</cell>        
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('BILL_NO')}</cell>                     
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS')}</cell> 
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('LOCATION')}</cell>        
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('WAGE_AMOUNT')}</cell>        
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('WAGE_STATUS')}</cell>        
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('ITEMS')}</cell>        
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('QUANTITY')}</cell>        
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('ORDER_TYPE')}</cell>        
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('PIECE_VENDOR')}</cell>     
                    <cell>${LIST_OF_ALL_BII_OBJECT.get('PIECE_VENDOR')}</cell>        
                </row>
                <c:set var="ID" value="${ID+1}"/>   
            </c:forEach>
            </rows>
        </c:if>
        <c:if test="${OBJECT_MAP.get('Type').equals('DayWiseProductionUnpaid')}">
            <rows>
                <head>            
                <column width="30" type="ro" align="center" color="white" sort="int">NO</column>       
                <column width="50" type="ro" align="center" color="white" sort="str">BILL</column>        
                <column width="200" type="ro" align="center" color="white" sort="str">MAIN STATUS</column>            
                <column width="150" type="ro" align="center" color="white" sort="str">LOCATION</column>    
                <column width="50" type="ro" align="center" color="white" sort="int">WAGE</column>    
                <column width="100" type="ro" align="center" color="white" sort="str">WAGE STATUS</column>    
                <column width="100" type="ro" align="center" color="white" sort="str">ITEMS</column>    
                <column width="35" type="ro" align="center" color="white" sort="int">QTY</column>    
                <column width="100" type="ro" align="center" color="white" sort="str">ORDER TYPE</column>    
                <column width="70" type="ro" align="center" color="white" sort="str"> VENDOR</column>
                <column width="100" type="ro" align="center" color="white" sort="str">REMOVE</column> 
                <beforeInit> 
                    <call command="setImagePath"> 
                        <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
                    </call>             
                </beforeInit> 
                <afterInit>  
                    <call command="attachHeader">
                        <param>#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search,#text_search</param>
                    </call> 
                </afterInit>     
                </head> 
                <c:set var="ID" value="${0}"/>    
                <c:set var="DATE" value=""/>
                <c:forEach items="${OBJECT_MAP.get('DATA')}" var="LIST_OF_ALL_BII_OBJECT">  
                    <c:if test="${!DATE.equals(LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')) || DATE.equals('')}">   
                        <row id="${ID*1000}" style="font-weight: bold;background-color:#06b;color:white;font-size: 20px;text-align: left;">            
                            <cell colspan='11'><![CDATA[ ${LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')}]]> - ${LIST_OF_ALL_BII_OBJECT.get('WEEKDAY')}</cell> 
                        </row>  
                    </c:if>
                    <c:set var="DATE" value="${LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')}"/>   
                    <c:choose>
                        <c:when test="${!LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS').equals('READY_TO_DELIVER')}">
                            <row id="${ID+1}" style='font-weight: bold; background-color:#ffa35e;'>    
                            </c:when>
                            <c:when test="${LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS').equals('READY_TO_DELIVER')}">
                                <row id="${ID+1}" style='font-weight: bold; background-color: palegreen;'>    
                                </c:when>
                                <c:otherwise>
                                    <row id="${ID+1}">    
                                    </c:otherwise>
                                </c:choose>  
                                <cell>${ID+1}</cell>        
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('BILL_NO')}</cell>                     
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS')}</cell> 
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('LOCATION')}</cell>        
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('WAGE_AMOUNT')}</cell>        
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('WAGE_STATUS')}</cell>        
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('ITEMS')}</cell>        
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('QUANTITY')}</cell>        
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('ORDER_TYPE')}</cell>        
                                <cell>${LIST_OF_ALL_BII_OBJECT.get('PIECE_VENDOR')}</cell>     
                                <cell><![CDATA[ <img height='20px' width='20px' src='resources/Images/cancel_order.png'/> ]]></cell>        
                            </row>
                            <c:set var="ID" value="${ID+1}"/>   
                        </c:forEach>
                        </rows>
                    </c:if>
                    <c:if test="${OBJECT_MAP.get('Type').equals('DayWiseProductionPaid')}">
                        <rows>
                            <head>            
                            <column width="30" type="ro" align="center" color="white" sort="int">NO</column>       
                            <column width="50" type="ro" align="center" color="white" sort="str">BILL</column>        
                            <column width="200" type="ro" align="center" color="white" sort="str">MAIN STATUS</column>            
                            <column width="150" type="ro" align="center" color="white" sort="str">LOCATION</column>    
                            <column width="50" type="ro" align="center" color="white" sort="int">WAGE</column>    
                            <column width="100" type="ro" align="center" color="white" sort="str">WAGE STATUS</column>    
                            <column width="100" type="ro" align="center" color="white" sort="str">ITEMS</column>    
                            <column width="35" type="ro" align="center" color="white" sort="int">QTY</column>    
                            <column width="100" type="ro" align="center" color="white" sort="str">ORDER TYPE</column>    
                            <column width="70" type="ro" align="center" color="white" sort="str"> VENDOR</column>

                            <beforeInit> 
                                <call command="setImagePath"> 
                                    <param>resources/Javascripts/Dhtmlx/codebase/imgs/</param> 
                                </call>             
                            </beforeInit> 
                            <afterInit>             
                            </afterInit>     
                            </head> 
                            <c:set var="ID" value="${0}"/>    
                            <c:set var="DATE" value=""/>
                            <c:forEach items="${OBJECT_MAP.get('DATA')}" var="LIST_OF_ALL_BII_OBJECT">  
                                <c:if test="${!DATE.equals(LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')) || DATE.equals('')}">   
                                    <row id="${ID*1000}" style="font-weight: bold;background-color:#06b;color:white;font-size: 20px;text-align: left;">            
                                        <cell colspan='11'><![CDATA[ ${LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')}]]> - ${LIST_OF_ALL_BII_OBJECT.get('WEEKDAY')}</cell> 
                                    </row>  
                                </c:if>
                                <c:set var="DATE" value="${LIST_OF_ALL_BII_OBJECT.get('ASSIGNMENT_DATE')}"/>   
                                <c:choose>
                                    <c:when test="${!LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS').equals('READY_TO_DELIVER')}">
                                        <row id="${ID+1}" style='font-weight: bold; background-color:#ffa35e;'>    
                                        </c:when>
                                        <c:when test="${LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS').equals('READY_TO_DELIVER')}">
                                            <row id="${ID+1}" style='font-weight: bold; background-color: palegreen;'>    
                                            </c:when>
                                            <c:otherwise>
                                                <row id="${ID+1}">    
                                                </c:otherwise>
                                            </c:choose>  
                                            <cell>${ID+1}</cell>        
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('BILL_NO')}</cell>                     
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('MAIN_STATUS')}</cell> 
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('LOCATION')}</cell>        
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('WAGE_AMOUNT')}</cell>        
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('WAGE_STATUS')}</cell>        
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('ITEMS')}</cell>        
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('QUANTITY')}</cell>        
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('ORDER_TYPE')}</cell>        
                                            <cell>${LIST_OF_ALL_BII_OBJECT.get('PIECE_VENDOR')}</cell>
                                        </row>
                                        <c:set var="ID" value="${ID+1}"/>   
                                    </c:forEach>
                                    </rows>
                                </c:if>

