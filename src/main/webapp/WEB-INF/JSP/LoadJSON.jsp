<%-- 
    Document   : LoadForms
    Created on : Sep 26, 2016, 6:57:50 AM
    Author     : Maliick
--%>

<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>  
<c:if test="${FormType.equals('addNewOrder')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ORDER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "LOCATION", name: "LOCATION=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", maxLength: "30", required: true, tooltip: "Store Location", labelWidth: "130",options: [
    {text: "BAGNAN", value: "BAGNAN"},
    {text: "MECHEDA", value: "MECHEDA"}   
    ]},
    {type: "input", label: "BILL NO", value: "", name: "BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "password", label: "VERIFY BILL NO", value: "", name: "VERIFY_BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "calendar", label: "ORDER DATE", name: "ORDER_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter order date", icon: "icon-input", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "calendar", label: "DELIVERY DATE", name: "DELIVERY_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter delivery date", icon: "icon-calendar", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "input", label: "CUSTOMER NAME", name: "CUSTOMER_NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Enter customer name ", icon: "icon-select", labelWidth: "130", maxLength: "30"},
    {type: "input", label: "CONTACT NO", name: "CONTACT_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Enter customer name ", icon: "icon-input", labelWidth: "130", maxLength: "10"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ORDER TYPE/RATE</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "PIECE VENDOR", name: "PIECE_VENDOR=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    {text: "HOME", value: "HOME"},
    {text: "AWAY", value: "AWAY"}   
    ]},
    {type: "select", label: "ORDER TYPE", name: "ORDER_TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    {text: "CHURIDER", value: "CHURIDER"},
    {text: "SALWAR", value: "SALWAR"},
    {text: "LACHA", value: "LACHA"},
    {text: "PIECE SELL", value: "PIECE SALE"}
    ]},   
    {type: "input", label: "QUANTITY", name: "QUANTITY=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "1", tooltip: "Enter Password", icon: "icon-select", labelWidth: "120", value: "1"},
    {type: "input", label: "PRICE", name: "PRICE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "input", label: "ADVANCE", name: "ADVANCE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "btn2state", name: "CUSTOM_RATE=NUM", label: "CUSTOM RATE", checked: false, labelWidth: "120"},
    {type: "button", name: "ITEM_BUTTON=BUTTON",value: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bolder'>SELECT ITEMS</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", tooltip: "Enter Password"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", label: "PRODUCT TYPE", name: "PRODUCT_TYPE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "110", options: [
    {text: "COTTON WORK", value: "COTTON WORK"},
    {text: "SYNTHETIC WORK", value: "SYNTHETIC WORK"}
    ]},
    {type: "select", label: "MEASURED BY", name: "MEASURED_BY=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "MONI", value: "MONI"},
    {text: "SUNITA", value: "SUNITA"}   
    ]},
    {type: "select", label: "STATUS", name: "STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "NEW ORDER", value: "NEW ORDER"},
    {text: "PROBLEM HALT", value: "PROBLEM HALT"},
    {text: "REQUIREMENT HALT", value: "REQUIREMENT HALT"},
    {text: "IN PROCESS", value: "IN PROCESS"},
    {text: "ADVANCE LOW", value: "ADVANCE LOW"}   
    ]},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", rows: "3"}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals(('addNewOrder_withValue'))}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ORDER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "LOCATION", name: "LOCATION=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", validate: "isNotEmpty", maxLength: "30", required: true, tooltip: "Store Location", labelWidth: "130",options: [
    {text: "BAGNAN", value: "BAGNAN"},
    {text: "MECHEDA", value: "MECHEDA"}   
    ]},
    {type: "input", label: "BILL NO", value: "", name: "BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "password", label: "VERIFY BILL NO", value: "", name: "VERIFY_BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "calendar", label: "ORDER DATE", name: "ORDER_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter order date", icon: "icon-input", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "calendar", label: "DELIVERY DATE", name: "DELIVERY_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter delivery date", icon: "icon-calendar", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "input", label: "CUSTOMER NAME", name: "CUSTOMER_NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Enter customer name ", icon: "icon-select", labelWidth: "130", maxLength: "30"},
    {type: "input", label: "CONTACT NO", name: "CONTACT_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Enter customer name ", icon: "icon-input", labelWidth: "130", maxLength: "10"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ORDER TYPE/RATE</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "ORDER TYPE", name: "ORDER_TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    {text: "CHURIDER", value: "CHURIDER"},
    {text: "SALWAR", value: "SALWAR"},
    {text: "LACHA", value: "LACHA"},
    {text: "PIECE SELL", value: "PIECE SALE"},
    ]},
    {type: "select", label: "PRODUCT TYPE", name: "PRODUCT_TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    {text: "COTTON WORK", value: "COTTON WORK"},
    {text: "SYNTHETIC WORK", value: "SYNTHETIC WORK"}
    ]},
    {type: "input", label: "QUANTITY", name: "QUANTITY=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "1", tooltip: "Enter Password", icon: "icon-select", labelWidth: "120", value: "1"},
    {type: "input", label: "PRICE", name: "PRICE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "input", label: "ADVANCE", name: "ADVANCE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "button", name: "ITEM_BUTTON=BUTTON",value: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bolder'>SELECT ITEMS</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", tooltip: "Enter Password"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", label: "MEASURED BY", name: "MEASURED_BY=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "MONI", value: "MONI"},
    {text: "SUNITA", value: "SUNITA"}   
    ]},
    {type: "select", label: "STATUS", name: "STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "NEW ORDER", value: "NEW ORDER"},
    {text: "ADVANCE LOW", value: "ADVANCE LOW"}   
    ]},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", rows: "3"}
    ]
</c:if>
<c:if test="${FormType.equals('addNewUser') || ReqObject.get('FormType').equals('addNewUser_withValue')}">    
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER USER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "USER ID", name: "USER_ID=STR", inputWidth: 200, style: "font-weight:bold;background-color:#edeaea;", validate: "UserNameValidation", maxLength: "30", required: true, tooltip: "USER ID MUST BE UNIQUE", labelWidth: "100"},
    {type: "input", label: "FIRST NAME", value: "", name: "USER_FIRST_NAME=STR", inputWidth: "200", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "User First Name", icon: "icon-input", labelWidth: "100"},
    {type: "input", label: "LAST NAME", value: "", name: "USER_LAST_NAME=STR", inputWidth: "200", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "User last Name", icon: "icon-input", labelWidth: "100"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER USER SETTINGS</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "USER TYPE", name: "USER_TYPE=STR", inputWidth: "160", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select user type ", icon: "icon-select", labelWidth: "110", options: [
    {text: "ADMIN", value: "ADMIN"},
    {text: "CASH_MANAGER", value: "CASH_MANAGER"},
    {text: "ORDER_MANAGER", value: "ORDER_MANAGER"},
    {text: "ENQUIRER", value: "ENQUIRER"},
    {text: "STATUS_MANAGER", value: "STATUS_MANAGER"}
    ]},
    {type: "select", label: "LANGUAGE PREFERENCE", name: "LANGUAGE_PREFERENCE=STR", inputWidth: "160", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "110", options: [
    {text: "ENGLISH", value: "ENGLISH"},
    {text: "BENGALI", value: "BENGALI"}
    ]},
    {type: "password", label: "PASSWORD", name: "PASSWORD=STR", inputWidth: "160", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "Enter Password", icon: "icon-select", labelWidth: "110"},
    {type: "password", label: "CONFIRM PASSWORD", name: "REPASSWORD=STR", inputWidth: "160", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "Enter Password", icon: "icon-password", labelWidth: "110"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name: "ENABLED=NUM", label: "ENABLED", checked: true, labelWidth: "70"},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "70", rows: "3"}
    ]
</c:if>
<c:if test="${FormType.equals('addNewItem')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},    
    {type: "input", label: "ITEM NAME", name:"ITEM_NAME=STR",labelWidth: "120", inputWidth:"170",style: "font-weight:bold;background-color:#edeaea;", validate:"ItemName",maxLength:20,required:true,validate: "ItemNameValidation", tooltip: "ITEM NAME MUST BE UNIQUE"},
    {type: "select", label: "ITEM TYPE", value: "", name: "ITEM_TYPE=STR",labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "MAIN", value: "MAIN"},    
    {text: "EXTRA", value: "EXTRA"}   
    ]},
    {type: "select", label: "ITEM SUB TYPE",  name: "ITEM_SUB_TYPE=STR", labelWidth: "120",inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;",  maxLength: "20", tooltip: "SELECT ITEM PARENT", icon: "icon-input",options:[
    {text: "", value: ""},    
    {text: "UPPER", value: "UPPER"} ,  
    {text: "LOWER", value: "LOWER"},   
    {text: "OTHERS", value: "OTHERS"},   
    ]},
    {type: "select", label: "PARENT ITEM", name: "PARENT_ITEM=STR", labelWidth: "120",inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", maxLength: "20", tooltip: "SELECT ITEM SUB TYPE", icon: "icon-input",options:[
    {text: "", value: ""},    
    {text: "LACHA", value: "LACHA"},
    {text: "CHURIDER", value: "CHURIDER"},
    {text: "SALWAR", value: "SALWAR"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER RATES</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "MASTER PRICE", name: "MASTER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-select", labelWidth: "150"},
    {type: "input", label: "TAILOR PRICE", name: "TAILOR_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "FINISHER PRICE", name: "FINISHER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: true, labelWidth: "100"},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "180", style: "font-weight:bold;background-color:#edeaea;",  tooltip: "Extra Note", icon: "icon-select", labelWidth: "100", rows: "3"}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('addNewItem_withValue')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15"},    
    {type: "input", value:"${ReqObject.get('FormData').get('ITEM_NAME=STR')}",label: "ITEM NAME", name:"ITEM_NAME=STR",inputWidth:"170",labelWidth: "120",style: "font-weight:bold;background-color:#edeaea;", validate:"ItemName",maxLength:20,required:true,validate: "ItemNameValidation", tooltip: "ITEM NAME MUST BE UNIQUE"},
    {type: "select",value:"${ReqObject.get('FormData').get('ITEM_TYPE=STR')}" , label: "ITEM TYPE", name: "ITEM_TYPE=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "MAIN", value: "MAIN"},    
    {text: "EXTRA", value: "EXTRA"}   
    ]},
     {type: "select", label: "ITEM SUB TYPE", value:"${ReqObject.get('FormData').get('ITEM_SUB_TYPE=STR')}", name: "ITEM_SUB_TYPE=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM PARENT", icon: "icon-input",options:[
    {text: "NONE", value: "NONE"},    
    {text: "LOWER", value: "LOWER"}   
    ]},
    {type: "select", label: "PARENT ITEM", value:"${ReqObject.get('FormData').get('PARENT_ITEM=STR')}", name: "PARENT_ITEM=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM SUB TYPE", icon: "icon-input",options:[
    {text: "NONE", value: "NONE"},    
    {text: "LACHA", value: "LACHA"}   
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER RATES</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15"},
    {type: "input",value:"${ReqObject.get('FormData').get('MASTER_PRICE=NUM')}", label: "MASTER PRICE", name: "MASTER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-select", labelWidth: "150"},
    {type: "input",value:"${ReqObject.get('FormData').get('TAILOR_PRICE=NUM')}", label: "TAILOR PRICE", name: "TAILOR_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"},
    {type: "input",value:"${ReqObject.get('FormData').get('FINISHER_PRICE=NUM')}",label: "FINISHER PRICE", name: "FINISHER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: ${ReqObject.get('FormData').get('ACTIVE=NUM')==0 ? false:true}, labelWidth: "100"}, 
    {type: "input", value:"${ReqObject.get('FormData').get('NOTE=STR')}", label: "NOTE", name: "NOTE=STR", inputWidth: "180", style: "font-weight:bold;background-color:#edeaea;",  tooltip: "Extra Note", icon: "icon-select", labelWidth: "100", rows: "3"}
    ]
</c:if>
<c:if test="${FormType.equals('operationToolbar')}">   
    [
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'><mytags:getTranslation key="${FormName}" defaultValue="Not Found"/> - </span><span style='font-weight: normal;color: #0096eb'><mytags:getTranslation key="Click" defaultValue="Click"/></span> "},
    { id:"save",type:"button",text:"SAVE - F8 ",  img:"resources/Images/save.png", imgdis: "resources/Images/save_dis.png", title:   "Click to save record"},
    {id:"sep1",type:"separator"},
    { id:"new",type:"button",text:"NEW - F4",  img:"resources/Images/new.png", imgdis: "resources/Images/new_dis.png",title:   "Click to add new record",enabled:false},
    {id:"sep2",type:"separator"},
    { id:"update",type:"button",text:"UPDATE - F7",  img:"resources/Images/new.png", imgdis: "resources/Images/new_dis.png",title:   "Click to add new record",enabled:false},
    {id:"sep3",type:"separator"},
    { id:"clear",type:"button",text:"CLEAR",  img:"resources/Images/refresh.png", imgdis: "resources/Images/refresh_dis.png",title:   "Click to clear record"},
    {id:"sep4",type:"separator"},
    {id:"default",type:"buttonTwoState",  img:"resources/Images/defualt.png", imgdis: "resources/Images/defualt.png",text:"Default ON/OFF",title: "Click to on default"},
    {id:"sep5",type:"separator"},
    {id:"save_default",type:"button",  img:"resources/Images/save.png", imgdis: "resources/Images/save.png",text:"Save Default",title: "Click to save default"}
    ]
</c:if>
