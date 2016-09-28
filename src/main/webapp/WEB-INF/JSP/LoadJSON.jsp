<%-- 
    Document   : LoadForms
    Created on : Sep 26, 2016, 6:57:50 AM
    Author     : Maliick
--%>

<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${FormType.equals('loadNewOrderItemForm')}">

    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},    
    {type: "combo", label: "ITEM NAME", name:"ITEM_NAME",inputWidth:200, filtering: true, filterCache: true,style: "font-weight:bold;background-color:#edeaea;", validate:"ItemName",required:true, tooltip: "ITEM NAME MUST BE UNIQUE",options:[
    {text: "Astra", value: "Astra"},
    {text: "Top", value: "Top"}  
    ]},
    {type: "select", label: "ITEM TYPE", value: "", name: "ITEM_TYPE", inputWidth: "200", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "Admin", value: "admin"},    
    {text: "Organiser", value: "org"},
    {text: "Power User", value: "poweruser"},
    {text: "User", value: "user", selected:true}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER RATES</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "MASTER PRICE", name: "MASTER_PRICE", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-select", labelWidth: "150"},
    {type: "input", label: "TAILOR PRICE", name: "TAILOR_PRICE", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "FINISHER PRICE", name: "FINISHER_PRICE", inputWidth: "100", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name:"ACTIVE",label: "ACTIVE", checked: true, labelWidth: "150"}
    ]
</c:if>
<c:if test="${FormType.equals('operationMenu')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "button", label: "New Input", value: "SAVE (F8)", className: "BUTTON1", name: "save"},
    {type: "newcolumn"},
    {type: "button", label: "New Input", value: "NEW (F4)", className: "BUTTON1", name: "new", icon: "icon-button"},
    {type: "newcolumn"},
    {type: "button", label: "New Input", value: "CLEAR (DELETE)", className: "BUTTON1", name: "clear", icon: "icon-button"}
    ]
</c:if>
<c:if test="${FormType.equals('operationToolbar')}">   
    [
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'>ADD NEW ITEM -  </span><span style='font-weight: normal;color: #0096eb'>Click on Control to Perform Operation >> </span> "},
    { id:"save",type:"button",text:"SAVE RECORD - F8 ",  img:"resources/Images/save.png", imgdis: "resources/Images/save_dis.png", title:   "Click to save record"},
    {id:"sep1",type:"separator"},
    { id:"new",type:"button",text:"NEW RECORD - F4",  img:"resources/Images/new.png", imgdis: "resources/Images/new_dis.png",title:   "Click to add new record",enabled:false},
    {id:"sep1",type:"separator"},
    { id:"clear",type:"button",text:"CLEAR FORM DATA",  img:"resources/Images/refresh.png", imgdis: "resources/Images/refresh_dis.png",title:   "Click to clear record"}
    ]
</c:if>