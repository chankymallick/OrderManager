<%-- 
    Document   : LoadForms
    Created on : Sep 26, 2016, 6:57:50 AM
    Author     : Maliick
--%>

<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>  
<c:if test="${FormType.equals('loadNewOrderItemForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},    
    {type: "input", label: "ITEM NAME", name:"ITEM_NAME=STR",inputWidth:200,style: "font-weight:bold;background-color:#edeaea;", validate:"ItemName",maxLength:20,required:true,validate: "ItemNameValidation", tooltip: "ITEM NAME MUST BE UNIQUE"},
    {type: "select", label: "ITEM TYPE", value: "", name: "ITEM_TYPE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "MAIN", value: "MAIN"},    
    {text: "EXTRA", value: "EXTRA"}   
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
<c:if test="${ReqObject.get('FormType').equals('loadNewOrderItemForm_withValue')}">
 [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15"},    
    {type: "input", value:"${ReqObject.get('FormData').get('ITEM_NAME=STR')}",label: "ITEM NAME", name:"ITEM_NAME=STR",inputWidth:200,style: "font-weight:bold;background-color:#edeaea;", validate:"ItemName",maxLength:20,required:true,validate: "ItemNameValidation", tooltip: "ITEM NAME MUST BE UNIQUE"},
    {type: "select",value:"${ReqObject.get('FormData').get('ITEM_TYPE=STR')}" , label: "ITEM TYPE", name: "ITEM_TYPE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "MAIN", value: "MAIN"},    
    {text: "EXTRA", value: "EXTRA"}   
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
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'><mytags:getTranslation key="addnewitem"/> - </span><span style='font-weight: normal;color: #0096eb'><mytags:getTranslation key="Click" defaultValue="Click"/></span> "},
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
