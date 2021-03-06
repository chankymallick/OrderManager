﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿<%-- 
    Document   : LoadForms
    Created on : Sep 26, 2016, 6:57:50 AM
    Author     : Maliick
--%>

<%@page contentType="application/json" pageEncoding="UTF-16"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>
<c:if test="${FormType.equals('advanceReport_Form')}">
    [
    {type: "settings", position: "label-left", labelWidth:60, inputWidth: 100},
    {type: "block", width: "auto", list: [
    {type: "label", label: "<span style=\'color:white\'>Report Parameters</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "calendar", label: "REPORT DATE", name: "REPORT_DATE=DATE", inputWidth: "250", dateFormat: "%d/%m/%y",style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", validate: "OrderBillNoValidation", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "40"},
    {type: "button", label: "New Input", value: "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GET REPORT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>", name: "getreport", offsetLeft: "100", className: "button1"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('deliveryTransactionsReport_Form')}">
    [
    {type: "settings", position: "label-left", labelWidth:60, inputWidth: 100},
    {type: "block", width: "auto", list: [
    {type: "label", label: "<span style=\'color:white\'>Report Parameters</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "calendar", label: "REPORT DATE", name: "REPORT_DATE=DATE", inputWidth: "250", dateFormat: "%d/%m/%y",style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", validate: "OrderBillNoValidation", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "40"},
    {type: "button", label: "New Input", value: "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GET REPORT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>", name: "getreport", offsetLeft: "100", className: "button1"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('updateNewOrder_QueryForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", name: "BILL_NO=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", validate: "OrderBillNoValidation", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "32"}
    ]}    
    ]
</c:if>
<c:if test="${FormType.equals('updateItem_QueryForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>ITEM DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "ITEM NAME", name: "ITEM_NAME=STR", inputWidth: "350", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 24px;", required: true, tooltip: "Select Item", labelWidth: "100", inputHeight: "32",options:

    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="ITEMS" columnName="ITEM_NAME" queryColumn="ACTIVE" queryValue="1" emptyField="true"/>
    }, 

    ]}    
    ]
</c:if>
<c:if test="${FormType.equals('updateDeliveryCompleted_QueryForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", name: "BILL_NO=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", maxLength: "6",  tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "32"}
    ]},   
    {type: "newcolumn"},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>DATE</span>", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "calendar", label: "DELIVERY DATE", dateFormat: "%d/%m/%y" ,name: "DELIVERY_DATE=DATE", inputWidth: "200", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",  required: true, tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "32", icon: "icon-input"}
    ]},
    {type: "newcolumn"},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>OPTION</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", label: "ONLY READY TO DELIVER", name: "ONLY_READY_TO_DELIVER=NUM", inputWidth: "50", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", tooltip: "Enter Bill No", labelWidth: "200", inputHeight: "32", icon: "icon-calendar"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('assignmentStatusChange_QueryForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", name: "BILL_NO=STR", inputWidth: "160", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", maxLength: "6", tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "32"},
    {type: "select", label: "TASK",  name: "TASK=STR", inputWidth: "160", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "38", icon: "icon-input", required: true,options:[
    <mytags:SelectHandler listName="assignment_change_menu.type"  emptyField="true"/>
    ]}
    ]},
    {type: "newcolumn"},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>SELECT TYPE</span>", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", label: "TYPE",  name: "TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "38", icon: "icon-select", required: true,options:[
    <mytags:SelectHandler listName="assignment_type.type"  emptyField="true"/>
    ]},
    {type: "select", label: "NEW NAME",  name: "NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "38", icon: "icon-select", required: true}
    ]},
    {type: "newcolumn"},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>OPTION</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "calendar", label: "MODIFICATION DATE", dateFormat: "%d/%m/%y", name: "ASSIGNMENT_DATE=DATE", inputWidth: "200", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 24px;", tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "32", icon: "icon-calendar", required: true, calendarPosition: "bottom", minutesInterval: 1, labelAlign: "left"},
    {type: "select", label: "LOCATION", name: "LOCATION=STR", inputWidth: "200", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 24px;", tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "38", icon: "icon-select", required: true}
    ]},
    {type: "newcolumn"},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>STATUS</span>", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", label: "MAIN STATUS", name: "MAIN_STATUS=STR", inputWidth: "230", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey; font-size: 20px;", tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "38", icon: "icon-select", required: true,options:
    <mytags:SelectHandler  fromDB="true" listName="ORDER_STATUS_TYPES" emptyField="false"/>
    },
    {type: "select", label: "SUB STATUS",  name: "SUB_STATUS=STR", inputWidth: "230", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey; font-size: 20px;", tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "38", icon: "icon-select"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('updateBulkMasterTailor_QueryForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "170", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", name: "BILL_NO=STR", inputWidth: "100", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",  maxLength: "6",  tooltip: "Enter Bill No", labelWidth: "60", inputHeight: "32"},
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>MASTER NAME</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "MASTER", name: "MASTER_NAME=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",  maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "40", icon: "icon-input", offsetLeft: "10",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="EMPLOYEES" columnName="EMP_NAME" queryColumn="EMP_ROLE" queryValue="MASTER" emptyField="true"/>
    },
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>TAILOR NAME</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "TAILOR", name: "TAILOR_NAME=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "65", inputHeight: "40", icon: "icon-input", offsetLeft: "10",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="EMPLOYEES" columnName="EMP_NAME" queryColumn="EMP_ROLE" queryValue="TAILOR" emptyField="true"/>
    },
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>DATE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "calendar", label: "DATE", name: "ASSIGNMENT_DATE=DATE", inputWidth: "180", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",dateFormat: "%d/%m/%y",validate: "dateValidator", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "30", icon: "icon-input", offsetLeft: "10"},
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>LOCATION</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "", name: "LOCATION=STR", inputWidth: "220", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "40", icon: "icon-input", offsetLeft: "0",options:<mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="CURRENT_LOCATIONS" columnName="LOCATION_NAME" queryColumn="PARENT_STATUS" queryValue="IN_PROCESS" emptyField="false"/>}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('updateBulkToSingle_QueryForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "170", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", name: "BILL_NO=STR", inputWidth: "100", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size:24px;",  maxLength: "6",  tooltip: "Enter Bill No", labelWidth: "60", inputHeight: "32"},
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>TYPE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "TYPE", name: "TYPE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size:24px;",  maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "40", icon: "icon-input", offsetLeft: "10",options:[
    <mytags:SelectHandler listName="assignment_type.type"  emptyField="true"/>
    ]},
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>NAME</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "NAME", name: "NAME=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 24px;", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "65", inputHeight: "40", icon: "icon-input", offsetLeft: "10",options:[]
    },
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>DATE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "calendar", label: "DATE", name: "ASSIGNMENT_DATE=DATE", inputWidth: "180", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 24px;",dateFormat: "%d/%m/%y",validate: "dateValidator", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "30", icon: "icon-input", offsetLeft: "10"},
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>LOCATION</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "", name: "LOCATION=STR", inputWidth: "220", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 24px;", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "40", icon: "icon-input", offsetLeft: "0",options:<mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="CURRENT_LOCATIONS" columnName="LOCATION_NAME" queryColumn="PARENT_STATUS" queryValue="IN_PROCESS" emptyField="false"/>}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('updateBulkReadyToDeliver_QueryForm')}">
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "170", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", name: "BILL_NO=STR", inputWidth: "100", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",  maxLength: "6",  tooltip: "Enter Bill No", labelWidth: "60", inputHeight: "32"},
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>FINISHER NAME</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "FINISHER", name: "FINISHER_NAME=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",  maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "40", icon: "icon-input", offsetLeft: "10",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="EMPLOYEES" columnName="EMP_NAME" queryColumn="EMP_ROLE" queryValue="FINISHER" emptyField="true"/>
    },    
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>IRON BY</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "IRON", name: "IRON=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",  maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "40", icon: "icon-input", offsetLeft: "10",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="EMPLOYEES" columnName="EMP_NAME" queryColumn="EMP_ROLE" queryValue="IRON" emptyField="true"/>
    },    
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>DATE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "calendar", label: "DATE", name: "FINISHING_DATE=DATE", inputWidth: "180", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;",dateFormat: "%d/%m/%y",validate: "dateValidator", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "30", icon: "icon-input", offsetLeft: "10"},
    {type: "newcolumn",offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>LOCATION</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "", name: "LOCATION=STR", inputWidth: "280", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 30px;", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "40", icon: "icon-input", offsetLeft: "0",options:<mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="CURRENT_LOCATIONS" columnName="LOCATION_NAME" queryColumn="PARENT_STATUS" queryValue="READY_TO_DELIVER" emptyField="false"/>}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('quickNewOrder')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", name: "BILL_NO=STR", inputWidth: 200, style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", validate: "OrderBillNoValidation", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "40"},
    {type: "input", label: "PRICE", name: "PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", required: true, validate: "NotEmpty", maxLength: "4", tooltip: "User First Name", icon: "icon-input", labelWidth: "100", inputHeight: "30", offsetTop: "15"},
    {type: "input", label: "ADVANCE", name: "ADVANCE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", required: true, validate: "NotEmpty", maxLength: "4", tooltip: "User last Name", icon: "icon-input", labelWidth: "100", inputHeight: "30", offsetTop: "15"}
    ]}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals(('quickNewOrder_withValue'))}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BILL NO", value:"${ReqObject.get('FormData').get('BILL_NO=STR')}", name: "BILL_NO=STR", inputWidth: 200, style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", validate: "OrderBillNoValidation", maxLength: "6", required: true, tooltip: "Enter Bill No", labelWidth: "100", inputHeight: "40"},
    {type: "input", label: "PRICE", value:"${ReqObject.get('FormData').get('PRICE=NUM')}" ,name: "PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", required: true, validate: "NotEmpty", maxLength: "4", tooltip: "User First Name", icon: "icon-input", labelWidth: "100", inputHeight: "30", offsetTop: "15"},
    {type: "input", label: "ADVANCE", value:"${ReqObject.get('FormData').get('ADVANCE=NUM')}", name: "ADVANCE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#f2fffe;box-shadow: 1px 1px 2px 2px grey;     font-size: 40px;", required: true, validate: "NotEmpty", maxLength: "4", tooltip: "User last Name", icon: "icon-input", labelWidth: "100", inputHeight: "30", offsetTop: "15"}
    ]}
    ]
</c:if>

<c:if test="${ReqObject.get('FormType').equals(('addadvance_Form'))}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>BILL NO</span>", value: "", name: "LABEL_DETAILS", labelWidth: "100", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "template",disabled:"true", value: "${ReqObject.get('BiLL_NO=STR')}", name: "BILL_NO", labelWidth: "50", inputWidth: "60", style: "font-weight:bold;font-size:18px;color:blue;", required: true, validate: "NotEmpty", maxLength: "20", icon: "icon-input"},
    {type: "newcolumn", offset: "5"},
    {type: "label", label: "<span style=\'color:white\'>DATE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "150", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "template",disabled:"true", value: "AMOUNT", name: "ORDER_DATE", labelWidth: "120", inputWidth: "130", style: "font-weight:bold;font-size:24px;color:WHITE;background-color:blue;", required: true, validate: "NotEmpty", maxLength: "20", icon: "icon-template" },
    <c:forEach items="${ReqObject.get('FormData').get('PAYMENT_DATA')}" var="DATA_OBJECT">
        {type: "input",disabled:"true", value: "${DATA_OBJECT[0]}", name: "BILL_NO=STR", labelWidth: "0", inputWidth: "130", style: "font-weight:bold;font-size:18px;color:blue;", required: true, validate: "NotEmpty", icon: "icon-template"},
    </c:forEach>
    {type: "newcolumn", offset: "5"},
    {type: "label", label: "<span style=\'color:white\'>PAYMENTS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "160", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", labelLeft: "5", offsetLeft: "20"},
    {type: "input",disabled:"true", value: "${ReqObject.get('FormData').get('PRICE')}", name: "BILL_NO=STR", labelWidth: "0", inputWidth: "200", style: "font-weight:bold;font-size:24px;color:WHITE;background-color:blue;", required: true, validate: "NotEmpty", icon: "icon-template", offsetLeft: "20"},
    <c:forEach items="${ReqObject.get('FormData').get('PAYMENT_DATA')}" var="DATA_OBJECT">
        {type: "input",disabled:"true",label:"${DATA_OBJECT[1]}", value: "${DATA_OBJECT[2]}", name: "BILL_NO=STR", labelWidth: "0", inputWidth: "50", style: "font-weight:bold;font-size:18px;color:blue;", required: true, validate: "", icon: "icon-template",  offsetLeft: "20"},
    </c:forEach>
    {type: "input", disabled:"true",value: "DUE  :  ${ReqObject.get('FormData').get('DUE')}", name: "BILL_NO=STR", labelWidth: "120", inputWidth: "200", style: "font-weight:bold;font-size:24px;color:white;background-color:blue;", required: true, validate: "NotEmpty",  icon: "icon-template", offsetLeft: "20"},
    {type: "newcolumn", offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>ADVANCE AMOUNT </span>", value: "", name: "LABEL_DETAILS", labelWidth: "300", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "input", label: "ADVANCE AMOUNT", name: "ADVANCE_AMOUNT=NUM", labelWidth: "150", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;height:30px;font-size:20px;", maxLength: "4", tooltip: "LOCATION NAME", icon: "icon-input", required: true},
    {type: "label", label: "", value: "", name: "LABEL_DETAILS", labelWidth: "300", className: " ", labelHeight: "15", icon: "icon-label"},	
    {type: "label", label: "<span style=\'color:white\'>ORDER STATUS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "300", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "calendar", value:"${ReqObject.get('FormData').get('DELIVERY_DATE')}",label: "DELIVERY DATE", name: "DELIVERY_DATE=DATE", labelWidth: "150", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;height:30px;font-size:20px;", maxLength: "4", icon: "icon-select",calendarPosition: "bottom",validate: "dateValidator",dateFormat: "%d/%m/%y"},
    {type: "select", label: "STATUS", name: "ORDER_STATUS=STR", labelWidth: "150", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;height:25px;font-size:14px;", icon: "icon-input", value: "${ReqObject.get('FormData').get('MAIN_STATUS')}", required: true,options:
    <mytags:SelectHandler  fromDB="true" listName="ORDER_STATUS_TYPES" emptyField="false"/>
    },
    {type: "select", label: "SUB STATUS", name: "ORDER_SUB_STATUS=STR", labelWidth: "150", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;height:25px;font-size:14px;",  icon: "icon-input", value: "${ReqObject.get('FormData').get('SUB_STATUS')}",options:
    <mytags:SelectHandler  fromDB="true" listName="ORDER_SUB_STATUS_TYPES" queryValue="${ReqObject.get('FormData').get('MAIN_STATUS')}" emptyField="false"/>
    },
    {type: "select", label: "LOCATION", name: "CURRENT_LOCATION=STR", labelWidth: "150", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;height:25px;font-size:14px;", tooltip: "LOCATION NAME", icon: "icon-input", required: true, value: "${ReqObject.get('FormData').get('CURRENT_LOCATION')}",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="CURRENT_LOCATIONS" columnName="LOCATION_NAME" queryColumn="PARENT_STATUS" queryValue="${ReqObject.get('FormData').get('MAIN_STATUS')}" emptyField="false"/>
    }
    ]}  
    ]
</c:if>   

<c:if test="${ReqObject.get('FormType').equals(('updateNewOrder_Form'))}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ORDER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select",disabled:"true", value:"${ReqObject.get('FormData').get('LOCATION=STR')}", label: "SHOP", name: "LOCATION=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "30", required: true, tooltip: "Store Location", labelWidth: "130",options: [
    <mytags:SelectHandler emptyField="true" listName="addneworder.location"/>
    ]},
    {type: "input", disabled:"true", label: "BILL NO", value:"${ReqObject.get('FormData').get('BILL_NO=STR')}", name: "BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "OrderBillNoValidation", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "input", disabled:"true",  label: "VERIFY BILL NO", value:"${ReqObject.get('FormData').get('VERIFY_BILL_NO=STR')}", name: "VERIFY_BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "calendar", disabled:"true",  value:"${ReqObject.get('FormData').get('ORDER_DATE=DATE')}", label: "ORDER DATE", name: "ORDER_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter order date", icon: "icon-input", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "calendar", value:"${ReqObject.get('FormData').get('DELIVERY_DATE=DATE')}", label: "DELIVERY DATE", name: "DELIVERY_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#fff7db;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter delivery date", icon: "icon-calendar", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "input", value:"${ReqObject.get('FormData').get('CUSTOMER_NAME=STR')}", label: "CUSTOMER NAME", name: "CUSTOMER_NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff7db;", tooltip: "Enter customer name ", icon: "icon-select", labelWidth: "130", maxLength: "30"},
    {type: "input",value:"${ReqObject.get('FormData').get('CONTACT_NO=STR')}", label: "CONTACT NO", name: "CONTACT_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff7db;", tooltip: "Enter customer name ", icon: "icon-input", labelWidth: "130", maxLength: "10"},
    {type: "newcolumn", offset: "20"},
    {type: "label", disabled:"true",  label: "<span style=\'color:white\'>ORDER TYPE/RATE</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", value:"${ReqObject.get('FormData').get('PIECE_VENDOR=STR')}",label: "PIECE VENDOR", name: "PIECE_VENDOR=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff7db;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    <mytags:SelectHandler emptyField="true" listName="addneworder.vendor"/>
    ]},
    {type: "select",value:"${ReqObject.get('FormData').get('ORDER_TYPE=STR')}", label: "ORDER TYPE", name: "ORDER_TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff7db;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: 
    <mytags:SelectHandler emptyField="false" fromDB="true" listName="MAIN_ITEMS"/>
    },   
    {type: "input",value:"${ReqObject.get('FormData').get('QUANTITY=NUM')}", label: "QUANTITY", name: "QUANTITY=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#fff7db;", required: true, validate: "ValidNumeric", maxLength: "1", tooltip: "Enter Password", icon: "icon-select", labelWidth: "120", value: "1"},
    {type: "input", disabled:"true", value:"${ReqObject.get('FormData').get('PRICE=NUM')}", label: "PRICE", name: "PRICE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "input",disabled:"true", value:"${ReqObject.get('FormData').get('ADVANCE=NUM')}", label: "ADVANCE", name: "ADVANCE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "btn2state", name: "CUSTOM_RATE=NUM", label: "CUSTOM RATE", checked: ${ReqObject.get('FormData').get('CUSTOM_RATE=NUM')==0 ? false:true}, labelWidth: "120"},
    {type: "button", name: "ITEM_BUTTON=BUTTON",value: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bolder'>SELECT ITEMS</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", tooltip: "Enter Password"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", value:"${ReqObject.get('FormData').get('PRODUCT_TYPE=STR')}",label: "TYPE", name: "PRODUCT_TYPE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff7db;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "110", options: [
    {text: "", value: ""},
    <mytags:SelectHandler emptyField="true" listName="addneworder.producttype"/>
    ]},
    {type: "select",value:"${ReqObject.get('FormData').get('MEASURED_BY=STR')}", label: "MEASURED BY", name: "MEASURED_BY=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff7db;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "", value: ""},
    {text: "MONI", value: "MONI"},
    {text: "SUNITA", value: "SUNITA"}   
    ]},
    {type: "select", value:"${ReqObject.get('FormData').get('ORDER_STATUS=STR')}", label: "STATUS", name: "ORDER_STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff7db;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    <mytags:SelectHandler  fromDB="true" listName="ORDER_STATUS_TYPES" emptyField="false"/>
    },
    {type: "select", value:"${ReqObject.get('FormData').get('ORDER_SUB_STATUS=STR')}", label: "SUB STATUS", name: "ORDER_SUB_STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    <mytags:SelectHandler  fromDB="true" listName="ORDER_SUB_STATUS_TYPES" queryValue="${ReqObject.get('FormData').get('ORDER_STATUS=STR')}" emptyField="false"/>
    },
    {type: "select", value:"${ReqObject.get('FormData').get('CURRENT_LOCATION=STR')}", label: "LOCATION", name: "CURRENT_LOCATION=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="CURRENT_LOCATIONS" columnName="LOCATION_NAME" queryColumn="PARENT_STATUS" queryValue="${ReqObject.get('FormData').get('ORDER_STATUS=STR')}" emptyField="false"/>
    },
    {type: "input",value:"${ReqObject.get('FormData').get('NOTE=STR')}", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#fff7db;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", rows: "3"}
    ]
</c:if>
<c:if test="${FormType.equals('addNewOrder')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ORDER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "SHOP", name: "LOCATION=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "30", required: true, tooltip: "Store Location", labelWidth: "130",options: [
    <mytags:SelectHandler emptyField="false" listName="addneworder.location"/>
    ]},
    {type: "button", offsetLeft:"130",name: "AUTO_BILL=BUTTON",value: "<span style='font-weight: bolder'><mytags:getTranslation key="GETNEXTBILLNO" defaultValue="GET NEXT BILL NO"/></span>", tooltip: "Auto Bill"},
    {type: "input", label: "BILL NO", value: "", name: "BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "OrderBillNoValidation", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "input", label: "VERIFY BILL NO", value: "", name: "VERIFY_BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "calendar", label: "ORDER DATE", name: "ORDER_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter order date", icon: "icon-input", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "calendar", label: "DELIVERY DATE", name: "DELIVERY_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter delivery date", icon: "icon-calendar", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "input", label: "CUSTOMER NAME", name: "CUSTOMER_NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Enter customer name ", icon: "icon-select", labelWidth: "130", maxLength: "30"},
    {type: "input", label: "CONTACT NO", name: "CONTACT_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Enter customer name ", icon: "icon-input", labelWidth: "130", maxLength: "10"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ORDER TYPE/RATE</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "PIECE VENDOR", name: "PIECE_VENDOR=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    <mytags:SelectHandler emptyField="false" listName="addneworder.vendor"/>
    ]},
    {type: "select", label: "ORDER TYPE", name: "ORDER_TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: 
    <mytags:SelectHandler emptyField="false" fromDB="true" listName="MAIN_ITEMS"/>
    },   
    {type: "input", label: "QUANTITY", name: "QUANTITY=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "1", tooltip: "Enter Password", icon: "icon-select", labelWidth: "120", value: "1"},
    {type: "input", label: "PRICE", name: "PRICE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "input", label: "ADVANCE", name: "ADVANCE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "btn2state", name: "CUSTOM_RATE=NUM", label: "CUSTOM RATE", checked: false, labelWidth: "120"},
    {type: "button", name: "ITEM_BUTTON=BUTTON",value: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bolder'>SELECT ITEMS</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", tooltip: "Enter Password"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", label: "TYPE", name: "PRODUCT_TYPE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "110", options: [
    <mytags:SelectHandler emptyField="true" listName="addneworder.producttype"/>
    ]},
    {type: "select", label: "MEASURED BY", name: "MEASURED_BY=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "", value: ""},
    {text: "SUPARNA", value: "SUPARNA"},
    {text: "RAMA", value: "RAMA"}   
    ]},
    {type: "select", label: "STATUS", name: "ORDER_STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    <mytags:SelectHandler  fromDB="true" listName="ORDER_STATUS_TYPES" emptyField="false"/>
    },
    {type: "select", label: "SUB STATUS", name: "ORDER_SUB_STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    []
    },
    {type: "select", label: "LOCATION", name: "CURRENT_LOCATION=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    [
    ]
    },
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", rows: "3"}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals(('addNewOrder_withValue'))}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ORDER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", value:"${ReqObject.get('FormData').get('LOCATION=STR')}", label: "SHOP", name: "LOCATION=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "30", required: true, tooltip: "Store Location", labelWidth: "130",options: [
    <mytags:SelectHandler emptyField="false" listName="addneworder.location"/>
    ]},
    {type: "button", offsetLeft:"130",name: "AUTO_BILL=BUTTON",value: "<span style='font-weight: bolder'><mytags:getTranslation key="GETNEXTBILLNO" defaultValue="GET NEXT BILL NO"/></span>", tooltip: "Auto Bill"},
    {type: "input", label: "BILL NO", value:"${ReqObject.get('FormData').get('BILL_NO=STR')}", name: "BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "OrderBillNoValidation", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "input", label: "VERIFY BILL NO", value:"${ReqObject.get('FormData').get('VERIFY_BILL_NO=STR')}", name: "VERIFY_BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "calendar", value:"${ReqObject.get('FormData').get('ORDER_DATE=DATE')}", label: "ORDER DATE", name: "ORDER_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter order date", icon: "icon-input", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "calendar", value:"${ReqObject.get('FormData').get('DELIVERY_DATE=DATE')}", label: "DELIVERY DATE", name: "DELIVERY_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter delivery date", icon: "icon-calendar", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "input", value:"${ReqObject.get('FormData').get('CUSTOMER_NAME=STR')}", label: "CUSTOMER NAME", name: "CUSTOMER_NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Enter customer name ", icon: "icon-select", labelWidth: "130", maxLength: "30"},
    {type: "input",value:"${ReqObject.get('FormData').get('CONTACT_NO=STR')}", label: "CONTACT NO", name: "CONTACT_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Enter customer name ", icon: "icon-input", labelWidth: "130", maxLength: "10"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ORDER TYPE/RATE</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", value:"${ReqObject.get('FormData').get('PIECE_VENDOR=STR')}",label: "PIECE VENDOR", name: "PIECE_VENDOR=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    <mytags:SelectHandler emptyField="false" listName="addneworder.vendor"/>
    ]},
    {type: "select",value:"${ReqObject.get('FormData').get('ORDER_TYPE=STR')}", label: "ORDER TYPE", name: "ORDER_TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: 
    <mytags:SelectHandler emptyField="false" fromDB="true" listName="MAIN_ITEMS"/>
    },   
    {type: "input",value:"${ReqObject.get('FormData').get('QUANTITY=NUM')}", label: "QUANTITY", name: "QUANTITY=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "1", tooltip: "Enter Password", icon: "icon-select", labelWidth: "120", value: "1"},
    {type: "input",value:"${ReqObject.get('FormData').get('PRICE=NUM')}", label: "PRICE", name: "PRICE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "input",value:"${ReqObject.get('FormData').get('ADVANCE=NUM')}", label: "ADVANCE", name: "ADVANCE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "btn2state", name: "CUSTOM_RATE=NUM", label: "CUSTOM RATE", checked: ${ReqObject.get('FormData').get('CUSTOM_RATE=NUM')==0 ? false:true}, labelWidth: "120"},
    {type: "button", name: "ITEM_BUTTON=BUTTON",value: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bolder'>SELECT ITEMS</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", tooltip: "Enter Password"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", value:"${ReqObject.get('FormData').get('PRODUCT_TYPE=STR')}",label: "TYPE", name: "PRODUCT_TYPE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "110", options: [
    <mytags:SelectHandler emptyField="true" listName="addneworder.producttype"/>
    ]},
    {type: "select",value:"${ReqObject.get('FormData').get('MEASURED_BY=STR')}", label: "MEASURED BY", name: "MEASURED_BY=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "", value: ""},
    {text: "SUPARNA", value: "SUPARNA"},
    {text: "RAMA", value: "RAMA"}   
    ]},
    {type: "select", value:"${ReqObject.get('FormData').get('ORDER_STATUS=STR')}", label: "STATUS", name: "ORDER_STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options:
    <mytags:SelectHandler  fromDB="true" listName="ORDER_STATUS_TYPES" emptyField="false"/>
    },
    {type: "select", value:"${ReqObject.get('FormData').get('ORDER_SUB_STATUS=STR')}", label: "SUB STATUS", name: "ORDER_SUB_STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    <mytags:SelectHandler  fromDB="true" listName="ORDER_SUB_STATUS_TYPES" queryValue="${ReqObject.get('FormData').get('ORDER_STATUS=STR')}" emptyField="false"/>
    },
    {type: "select", value:"${ReqObject.get('FormData').get('CURRENT_LOCATION=STR')}", label: "LOCATION", name: "CURRENT_LOCATION=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: 
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="CURRENT_LOCATIONS" columnName="LOCATION_NAME" queryColumn="PARENT_STATUS" queryValue="${ReqObject.get('FormData').get('ORDER_STATUS=STR')}" emptyField="false"/>
    },
    {type: "input",value:"${ReqObject.get('FormData').get('NOTE=STR')}", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", rows: "3"}
    ]
</c:if>
<c:if test="${FormType.equals('addNewUser') || ReqObject.get('FormType').equals('addNewUser_withValue')}">    
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER USER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "USER ID", name: "USER_ID=STR", inputWidth: 200, style: "font-weight:bold;background-color:#8cf9ff;", validate: "UserNameValidation", maxLength: "30", required: true, tooltip: "USER ID MUST BE UNIQUE", labelWidth: "100"},
    {type: "input", label: "FIRST NAME", value: "", name: "USER_FIRST_NAME=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "User First Name", icon: "icon-input", labelWidth: "100"},
    {type: "input", label: "LAST NAME", value: "", name: "USER_LAST_NAME=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "User last Name", icon: "icon-input", labelWidth: "100"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER USER SETTINGS</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "USER TYPE", name: "USER_TYPE=STR", inputWidth: "160", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select user type ", icon: "icon-select", labelWidth: "110", options: [
    {text: "ADMIN", value: "ADMIN"},
    {text: "CASH_MANAGER", value: "CASH_MANAGER"},
    {text: "ORDER_MANAGER", value: "ORDER_MANAGER"},
    {text: "ENQUIRER", value: "ENQUIRER"},
    {text: "STATUS_MANAGER", value: "STATUS_MANAGER"}
    ]},
    {type: "select", label: "LANGUAGE PREFERENCE", name: "LANGUAGE_PREFERENCE=STR", inputWidth: "160", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "110", options: [
    {text: "ENGLISH", value: "ENGLISH"},
    {text: "BENGALI", value: "BENGALI"}
    ]},
    {type: "password", label: "PASSWORD", name: "PASSWORD=STR", inputWidth: "160", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "Enter Password", icon: "icon-select", labelWidth: "110"},
    {type: "password", label: "CONFIRM PASSWORD", name: "REPASSWORD=STR", inputWidth: "160", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "30", tooltip: "Enter Password", icon: "icon-password", labelWidth: "110"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name: "ENABLED=NUM", label: "ENABLED", checked: true, labelWidth: "70"},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "70", rows: "3"}
    ]
</c:if>
<c:if test="${FormType.equals('addNewTask') || ReqObject.get('FormType').equals('addNewTask_withValue')}">    
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER TASK DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "TASK NAME", value: "", name: "TASK_NAME=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "300", tooltip: "User First Name", icon: "icon-input", labelWidth: "100", rows: "3"},
    {type: "select", label: "TASK TYPE", name: "TASK_TYPE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select user type ", icon: "icon-select", labelWidth: "100", options: [
    {text: "DELIVERY_FAILED", value: "DELIVERY_FAILED"},
    {text: "ALTER", value: "ALTER"},
    {text: "EARLY_DELIVERY", value: "EARLY_DELIVERY"},
    {text: "OTHERS", value: "OTHERS"}   
    ]},
    {type: "calendar", label: "DATE  & TIME", name: "SCHEDULE_DATE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", tooltip: "User First Name", icon: "icon-input", labelWidth: "100", rows: "3", calendarPosition: "bottom", minutesInterval: 1, weekStart: "1", enableTime: true,dateFormat: "%d/%m/%y %g:%i %A"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER USER SETTINGS</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "PRIORITY", name: "PRIORITY=NUM", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select user type ", icon: "icon-select", labelWidth: "100", options: [
    {text: "NORMAL", value: "1"},
    {text: "URGENT", value: "2"},
    {text: "EMERGENCY", value: "3"} 
    ]},
    {type: "select", label: "STATUS", name: "TASK_STATUS=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", required: true, tooltip: "Select user type ", icon: "icon-select", labelWidth: "100", options: [
    {text: "PENDING", value: "PENDING"},
    {text: "FINISHED", value: "FINISHED"},
    {text: "HOLD", value: "HOLD"}
    ]},
    {type: "newcolumn", offset: "20"},
    {_idd: "150", type: "label", label: "<span style=\'color:white\'>EXTRA</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {_idd: "502", type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "160", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Select user type ", icon: "icon-select", labelWidth: "50", rows: "3"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('addNewItem')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},    
    {type: "input", label: "ITEM NAME", name:"ITEM_NAME=STR",labelWidth: "120", inputWidth:"170",style: "font-weight:bold;background-color:#8cf9ff;", validate:"ItemName",maxLength:20,required:true,validate: "ItemNameValidation", tooltip: "ITEM NAME MUST BE UNIQUE"},
    {type: "select", label: "ITEM TYPE", value: "", name: "ITEM_TYPE=STR",labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "MAIN", value: "MAIN"},    
    {text: "EXTRA", value: "EXTRA"}   
    ]},
    {type: "select", label: "ITEM SUB TYPE",  name: "ITEM_SUB_TYPE=STR", labelWidth: "120",inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;",  maxLength: "20", tooltip: "SELECT ITEM PARENT", icon: "icon-input",options:[
    {text: "", value: ""},    
    {text: "UPPER", value: "UPPER"} ,  
    {text: "LOWER", value: "LOWER"},   
    {text: "OTHERS", value: "OTHERS"}
    ]},
    {type: "select", label: "PARENT ITEM", name: "PARENT_ITEM=STR", labelWidth: "120",inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "20", tooltip: "SELECT ITEM SUB TYPE", icon: "icon-input",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="ITEMS" columnName="ITEM_NAME" queryColumn="ITEM_TYPE" queryValue="MAIN" emptyField="true"/>

    },
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER RATES</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "MASTER PRICE", name: "MASTER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-select", labelWidth: "150"},
    {type: "input", label: "TAILOR PRICE", name: "TAILOR_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "FINISHER PRICE", name: "FINISHER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: true, labelWidth: "100"},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "180", style: "font-weight:bold;background-color:#8cf9ff;",  tooltip: "Extra Note", icon: "icon-select", labelWidth: "100", rows: "3"}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('addNewItem_withValue')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15"},    
    {type: "input", value:"${ReqObject.get('FormData').get('ITEM_NAME=STR')}",label: "ITEM NAME", name:"ITEM_NAME=STR",inputWidth:"170",labelWidth: "120",style: "font-weight:bold;background-color:#8cf9ff;", validate:"ItemName",maxLength:20,required:true,validate: "ItemNameValidation", tooltip: "ITEM NAME MUST BE UNIQUE"},
    {type: "select",value:"${ReqObject.get('FormData').get('ITEM_TYPE=STR')}" , label: "ITEM TYPE", name: "ITEM_TYPE=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "MAIN", value: "MAIN"},    
    {text: "EXTRA", value: "EXTRA"}   
    ]},
    {type: "select", label: "ITEM SUB TYPE", value:"${ReqObject.get('FormData').get('ITEM_SUB_TYPE=STR')}", name: "ITEM_SUB_TYPE=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM PARENT", icon: "icon-input",options:[
    {text: "", value: ""},    
    {text: "UPPER", value: "UPPER"} ,  
    {text: "LOWER", value: "LOWER"},   
    {text: "OTHERS", value: "OTHERS"}  
    ]},
    {type: "select", label: "PARENT ITEM", value:"${ReqObject.get('FormData').get('PARENT_ITEM=STR')}", name: "PARENT_ITEM=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM SUB TYPE", icon: "icon-input",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="ITEMS" columnName="ITEM_NAME" queryColumn="ITEM_TYPE" queryValue="MAIN" emptyField="false"/>

    },
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER RATES</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15"},
    {type: "input",value:"${ReqObject.get('FormData').get('MASTER_PRICE=NUM')}", label: "MASTER PRICE", name: "MASTER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-select", labelWidth: "150"},
    {type: "input",value:"${ReqObject.get('FormData').get('TAILOR_PRICE=NUM')}", label: "TAILOR PRICE", name: "TAILOR_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"},
    {type: "input",value:"${ReqObject.get('FormData').get('FINISHER_PRICE=NUM')}",label: "FINISHER PRICE", name: "FINISHER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: ${ReqObject.get('FormData').get('ACTIVE=NUM')==0 ? false:true}, labelWidth: "100"}, 
    {type: "input", value:"${ReqObject.get('FormData').get('NOTE=STR')}", label: "NOTE", name: "NOTE=STR", inputWidth: "180", style: "font-weight:bold;background-color:#8cf9ff;",  tooltip: "Extra Note", icon: "icon-select", labelWidth: "100", rows: "3"}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('updateItem_Form')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15"},    
    {type: "input", value:"${ReqObject.get('FormData').get('ITEM_UID=NUM')}",label: "ITEM UID", name:"ITEM_UID=NUM",inputWidth:"170",labelWidth: "120",style: "font-weight:bold;background-color:#8cf9ff;",maxLength:20,required:true, disabled:"true"},
    {type: "input", value:"${ReqObject.get('FormData').get('ITEM_NAME=STR')}",label: "ITEM NAME", name:"ITEM_NAME=STR",inputWidth:"170",labelWidth: "120",style: "font-weight:bold;background-color:#8cf9ff;",maxLength:20,required:true, tooltip: "ITEM NAME MUST BE UNIQUE"},
    {type: "select",value:"${ReqObject.get('FormData').get('ITEM_TYPE=STR')}" , label: "ITEM TYPE", name: "ITEM_TYPE=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT ITEM TYPE", icon: "icon-input",options:[
    {text: "MAIN", value: "MAIN"},    
    {text: "EXTRA", value: "EXTRA"}   
    ]},
    {type: "select", label: "ITEM SUB TYPE", value:"${ReqObject.get('FormData').get('ITEM_SUB_TYPE=STR')}", name: "ITEM_SUB_TYPE=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "20", tooltip: "SELECT ITEM PARENT", icon: "icon-input",options:[
    {text: "", value: ""},    
    {text: "LOWER", value: "LOWER"}   
    ]},
    {type: "select", label: "PARENT ITEM", value:"${ReqObject.get('FormData').get('PARENT_ITEM=STR')}", name: "PARENT_ITEM=STR", inputWidth: "170",labelWidth: "120", style: "font-weight:bold;background-color:#8cf9ff;",  maxLength: "20", tooltip: "SELECT ITEM SUB TYPE", icon: "icon-input",options:[
    {text: "", value: ""},    
    {text: "LACHA", value: "LACHA"}   
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ENTER RATES</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15"},
    {type: "input",value:"${ReqObject.get('FormData').get('MASTER_PRICE=NUM')}", label: "MASTER PRICE", name: "MASTER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-select", labelWidth: "150"},
    {type: "input",value:"${ReqObject.get('FormData').get('TAILOR_PRICE=NUM')}", label: "TAILOR PRICE", name: "TAILOR_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"},
    {type: "input",value:"${ReqObject.get('FormData').get('FINISHER_PRICE=NUM')}",label: "FINISHER PRICE", name: "FINISHER_PRICE=NUM", inputWidth: "100", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "ValidNumeric", maxLength: "3", tooltip: "Enter rate for master", icon: "icon-input", labelWidth: "150"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "200", className: "DHTMLX_LABEL2", labelHeight: "15", icon: "icon-label"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: ${ReqObject.get('FormData').get('ACTIVE=NUM')==0 ? false:true}, labelWidth: "100"}, 
    {type: "input", value:"${ReqObject.get('FormData').get('NOTE=STR')}", label: "NOTE", name: "NOTE=STR", inputWidth: "180", style: "font-weight:bold;background-color:#8cf9ff;",  tooltip: "Extra Note", icon: "icon-select", labelWidth: "100", rows: "3"}
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
<c:if test="${FormType.equals('wagePaymentUnpaidToolbar')}">   
    [
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'><mytags:getTranslation key="${FormName}" defaultValue="Not Found"/> - </span><span style='font-weight: normal;color: #0096eb'><mytags:getTranslation key="Click" defaultValue="Click"/></span> "},
    { id:"pay",type:"button",text:"PAY - F8 ",  img:"resources/Images/save.png", imgdis: "resources/Images/save_dis.png", title:   "Click to save record"},
    {id:"sep1",type:"separator"},
    { id:"new",type:"button",text:"NEW - F4",  img:"resources/Images/new.png", imgdis: "resources/Images/new_dis.png",title:   "Click to add new record",enabled:false},
    {id:"sep2",type:"separator"},
    { id:"print",type:"button",text:"PRINT PAYMENT DETAILS- F9",  img:"resources/Images/print.png", imgdis: "resources/Images/print.png",title:   "Click to add new record"},
    {id:"sep3",type:"separator"},
    { id:"clear",type:"button",text:"CLEAR ALL",  img:"resources/Images/refresh.png", imgdis: "resources/Images/refresh_dis.png",title:   "Click to clear record"}
    ]
</c:if>
<c:if test="${FormType.equals('printModuleToolbar')}">   
    [
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'><mytags:getTranslation key="${FormName}" defaultValue="Not Found"/> - </span><span style='font-weight: normal;color: #0096eb'><mytags:getTranslation key="Click" defaultValue="Click"/></span> "},
    { id:"new_search",type:"button",text:"NEW SEARCH - F2 ",  img:"resources/Images/search.png", imgdis: "resources/Images/save_dis.png", title:   "Click to save record"},
    {id:"sep1",type:"separator"},
    { id:"close",type:"button",text:"CLOSE- ESC ",  img:"resources/Images/fail.png", imgdis: "resources/Images/fail.png", title:   "Click to save record"},
    ]
</c:if>
<c:if test="${FormType.equals('reportToolbar')}">   
    [
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'><mytags:getTranslation key="${FormName}" defaultValue="Not Found"/> - </span><span style='font-weight: normal;color: #0096eb'><mytags:getTranslation key="Click" defaultValue="Click"/></span> "},
    { id:"print",type:"button",text:"PRINT - F2 ",  img:"resources/Images/print.png", imgdis: "resources/Images/print.png", title:   "Click to save record"},
    {id:"sep1",type:"separator"},
    { id:"refresh",type:"button",text:"REFRESH - F4 ",  img:"resources/Images/loading.gif", imgdis: "resources/Images/fail.png", title:   "Click to save record"},
    ]
</c:if>
<c:if test="${FormType.equals('operationToolbarUpdate')}">   
    [
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'><mytags:getTranslation key="${FormName}" defaultValue="Not Found"/> - </span><span style='font-weight: normal;color: #0096eb'><mytags:getTranslation key="Click" defaultValue="Click"/></span> "},
    { id:"update",type:"button",text:"UPDATE - F8 ",  img:"resources/Images/save.png", imgdis: "resources/Images/save_dis.png", title:   "Click to save record"},
    {id:"sep1",type:"separator"},
    { id:"new_search",type:"button",text:"NEW SEARCH - F4 ",  img:"resources/Images/search2.png", imgdis: "resources/Images/search2.png", title:   "Click to search record"},
    {id:"sep2",type:"separator"},
    { id:"clear",type:"button",text:"CLEAR",  img:"resources/Images/refresh.png", imgdis: "resources/Images/refresh_dis.png",title:   "Click to clear record"},
    {id:"sep4",type:"separator"},
    {id:"default",type:"buttonTwoState",  img:"resources/Images/defualt.png", imgdis: "resources/Images/defualt.png",text:"Default ON/OFF",title: "Click to on default"},
    {id:"sep5",type:"separator"},
    {id:"save_default",type:"button",  img:"resources/Images/save.png", imgdis: "resources/Images/save.png",text:"Save Default",title: "Click to save default"}
    ]
</c:if>
<c:if test="${FormType.equals('operationToolbarBulkUpdate')}">   
    [
    {id:"label",type:"text", text:"<span style='font-weight: bold;color: #262626'><mytags:getTranslation key="${FormName}" defaultValue="Not Found"/> - </span><span style='font-weight: normal;color: #0096eb'><mytags:getTranslation key="Click" defaultValue="Click"/></span> "},
    { id:"update",type:"button",text:"ALL UPDATE - F8 ",  img:"resources/Images/save.png", imgdis: "resources/Images/save_dis.png", title:   "Click to save record"},
    {id:"sep1",type:"separator"},
    { id:"new_search",type:"button",text:"NEW TASK - F4 ",  img:"resources/Images/search2.png", imgdis: "resources/Images/search2.png", title:   "Click to search record",disabled:"true"},
    {id:"sep2",type:"separator"}, 
    {id:"print",type:"button",  img:"resources/Images/save.png", imgdis: "resources/Images/save.png",text:"PRINT - F2 ",title: "Click to save default",disabled:"true"}
    ]
</c:if>
<c:if test="${FormType.equals('addNewStatusType')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER STATUS DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "STATUS TYPE", value: "", name: "STATUS_TYPE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT STATUS_TYPE", icon: "icon-input", options: [
    {text: "MAIN STATUS", value: "MAIN_STATUS"},
    {text: "SUB STATUS", value: "SUB_STATUS"}
    ]},
    {type: "select", label: "STATUS PARENT NAME ", name: "STATUS_PARENT_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "20", tooltip: "STATUS_PARENT_NAME", icon: "icon-input", options: [
    {text: "", value: ""},    
    ]},
    {type: "input", label: "STATUS NAME", name: "STATUS_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "STATUS_NAME", icon: "icon-input",  required: true, validate: "NotEmpty"},
    {type: "input", label: "STATUS POSITION", name: "STATUS_ORDER=NUM", labelWidth: "120", inputWidth: "50", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "4", tooltip: "STATUS_POSITION", icon: "icon-input",  required: true},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>EXTRAS</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: true, labelWidth: "120"}, 
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "3"},
    ]}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('addNewStatusType_withValue')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER STATUS DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select",value:"${ReqObject.get('FormData').get('STATUS_TYPE=STR')}", label: "STATUS TYPE", value: "", name: "STATUS_TYPE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT STATUS_TYPE", icon: "icon-input", options: [
    {text: "MAIN STATUS", value: "MAIN_STATUS"},
    {text: "SUB STATUS", value: "SUB_STATUS"}
    ]},
    {type: "select",value:"${ReqObject.get('FormData').get('STATUS_PARENT_NAME=STR')}", label: "STATUS PARENT NAME ", name: "STATUS_PARENT_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "20", tooltip: "STATUS_PARENT_NAME", icon: "icon-input", options: [
    {text: "", value: ""},    
    ], required: true},
    {type: "input", value:"${ReqObject.get('FormData').get('STATUS_NAME=NUM')}",label: "STATUS NAME", name: "STATUS_NAME=NUM", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "STATUS_NAME", icon: "icon-input",  required: true, validate: "NotEmpty"},
    {type: "input", value:"${ReqObject.get('FormData').get('STATUS_ORDER=NUM')}",label: "STATUS POSITION", name: "STATUS_ORDER=NUM", labelWidth: "120", inputWidth: "50", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "2", tooltip: "STATUS_POSITION", icon: "icon-input",  required: true},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>EXTRAS</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: ${ReqObject.get('FormData').get('ACTIVE=NUM')==0 ? false:true}, labelWidth: "120"}, 
    {type: "input", value:"${ReqObject.get('FormData').get('NOTE=STR')}",label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "3"},
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('addNewLocation')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER LOCATION DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "PARENT STATUS", value: "", name: "PARENT_STATUS=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT PARENT_STATUS", icon: "icon-input", options: 
    <mytags:SelectHandler  fromDB="true" listName="ORDER_STATUS_TYPES" emptyField="false"/>
    },
    {type: "input", label: "LOCATION NAME", name: "LOCATION_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-input",  required: true, validate: "NotEmpty"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>EXTRAS</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: true, labelWidth: "120"}, 
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "3"},
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('addNewAccount')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER ACCOUNT DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "ACCOUNT MODULE", name: "ACCOUNT_MODULE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-input", required: true, validate: "NotEmpty",options:[
    <mytags:SelectHandler emptyField="false" listName="accountregister.module"/>
    ]},
    {type: "input", label: "ACCOUNT NAME", name: "ACCOUNT_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "input", label: "MOBILE", name: "MOBILE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "input", label: "ADDRESS", rows: "3", name: "ADDRESS=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},



    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>EXTRAS</span>", value: "", name: "EXTRA", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BANK DETAILS", rows: "3",name: "BANK_DETAILS=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "btn2state", name: "ACTIVE=NUM", label: "ACTIVE", checked: true, labelWidth: "120"},
    {type: "input", name: "VIEW_ORDER=NUM", label: "VIEW ORDER", checked: true, labelWidth: "120",inputWidth: "170",style: "font-weight:bold;background-color:#8cf9ff;", icon: "icon-btn2state", value: "0"},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "2"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('addNewAccount_withValue')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER ACCOUNT DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "ACCOUNT MODULE", name: "ACCOUNT_MODULE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-input", required: true, validate: "NotEmpty",options:[
    <mytags:SelectHandler emptyField="false" listName="accountregister.module"/>
    ]},
    {type: "input", label: "ACCOUNT NAME", name: "ACCOUNT_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "input", label: "MOBILE", name: "MOBILE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "input", label: "ADDRESS", rows: "3", name: "ADDRESS=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},



    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>EXTRAS</span>", value: "", name: "EXTRA", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "BANK DETAILS", rows: "3",name: "BANK_DETAILS=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "btn2state", name: "ACTIVE=NUM", label: "ACTIVE", checked: true, labelWidth: "120"},
    {type: "input", name: "VIEW_ORDER=NUM", label: "VIEW ORDER", checked: true, labelWidth: "120",inputWidth: "170",style: "font-weight:bold;background-color:#8cf9ff;", icon: "icon-btn2state", value: "0"},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "2"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('addNewAccountSubType')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER ACCOUNT DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "ACCOUNT MODULE", name: "ACCOUNT_MODULE=STR", labelWidth: "120", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-input", required: true, validate: "NotEmpty", options: [
    <mytags:SelectHandler emptyField="false" listName="accountregister.module"/>
    ]},
    {type: "input", label: "SUBTYPE", rows: "1", name: "ACCOUNT_SUBTYPE=STR", labelWidth: "120", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>EXTRAS</span>", value: "", name: "EXTRA", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "btn2state", name: "ACTIVE=NUM", label: "ACTIVE", checked: true, labelWidth: "120"},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "2"}
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('addNewAccountTransaction')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER ACCOUNT DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "TR TYPE", name: "TR_TYPE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty", options:[
    <mytags:SelectHandler emptyField="false" listName="accounttransaction.type" />
    ]},    
    {type: "select", label: "AC TYPE", rows: "1", name: "TR_MODULE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty",options:[
    <mytags:SelectHandler emptyField="true" listName="accountregister.module"/>
    ]},
    {type: "calendar", label: "DATE", rows: "1", name: "TR_DATE=DATE", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", dateFormat: "%d/%m/%y", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "select", label: "AC NAME", name: "ACCOUNT_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true,  validate: "NotEmpty",options:[]},
    {type: "select", name: "MODULE_TRANSACTION_TYPE=STR", label: "TR SUB TYPE", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;",options:[]},
    {type: "select", name: "PAID_FROM=STR", label: "PAID FROM", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;",options:[
    <mytags:SelectHandler emptyField="true" listName="accounttransaction.funds"/>
    ]},

    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>AMOUNT DETAILS</span>", value: "", name: "EXTRA", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", name: "AMOUNT=NUM", label: "AMOUNT", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:yellow;font-size:20px;", icon: "icon-btn2state"},
    {type: "input", name: "TR_DETAILS=STR", label: "DETAILS", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", rows:"2", icon: "icon-btn2state"},
    {type: "newcolumn", offset: "20"},
    {_idd: "150", type: "label", label: "<span style=\'color:white\'>EXTRA</span>", value: "", name: "EXTRA", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {_idd: "154", type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "2"}
    ]}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('addNewAccountTransaction_withValue')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER ACCOUNT DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", value:"${ReqObject.get('FormData').get('TR_TYPE=STR')}", label: "TR TYPE", name: "TR_TYPE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty", options:[
    <mytags:SelectHandler emptyField="false" listName="accounttransaction.type" />
    ]},    
    {type: "select",  value:"${ReqObject.get('FormData').get('TR_MODULE=STR')}", label: "AC TYPE", rows: "1", name: "TR_MODULE=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true, validate: "NotEmpty",options:[
    <mytags:SelectHandler emptyField="true" listName="accountregister.module"/>
    ]},
    {type: "calendar", value:"${ReqObject.get('FormData').get('TR_DATE=DATE')}", label: "DATE", rows: "1", name: "TR_DATE=DATE", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", dateFormat: "%d/%m/%y", icon: "icon-select", required: true, validate: "NotEmpty"},
    {type: "select", value:"${ReqObject.get('FormData').get('ACCOUNT_NAME=STR')}", label: "AC NAME", name: "ACCOUNT_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-select", required: true,  validate: "NotEmpty",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="ACCOUNT_REGISTER" columnName="ACCOUNT_NAME" queryColumn="ACCOUNT_MODULE" queryValue="${ReqObject.get('FormData').get('TR_MODULE=STR')}" emptyField="false"/>
    },
    {type: "select", value:"${ReqObject.get('FormData').get('MODULE_TRANSACTION_TYPE=STR')}", name: "MODULE_TRANSACTION_TYPE=STR", label: "TR SUB TYPE", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;",options:
    <mytags:SelectHandler  fromDB="true" listName="CUSTOM_LIST" tableName="ACCOUNT_BOOK_SUBTYPES" columnName="ACCOUNT_SUBTYPE" queryColumn="ACCOUNT_MODULE" queryValue="${ReqObject.get('FormData').get('TR_MODULE=STR')}" emptyField="false"/>
    },
    {type: "select", value:"${ReqObject.get('FormData').get('PAID_FROM=STR')}", name: "PAID_FROM=STR", label: "PAID FROM", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;",options:[
    <mytags:SelectHandler emptyField="true" listName="accounttransaction.funds"/>
    ]},    
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>AMOUNT DETAILS</span>", value: "", name: "EXTRA", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", value:"${ReqObject.get('FormData').get('AMOUNT=NUM')}", name: "AMOUNT=NUM", label: "AMOUNT", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:yellow;font-size:20px;", icon: "icon-btn2state"},
    {type: "input", value:"${ReqObject.get('FormData').get('TR_DETAILS=STR')}", name: "TR_DETAILS=STR", label: "DETAILS", checked: true, labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", rows:"2", icon: "icon-btn2state"},
    {type: "newcolumn", offset: "20"},
    {_idd: "150", type: "label", label: "<span style=\'color:white\'>EXTRA</span>", value: "", name: "EXTRA", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {_idd: "154", value:"${ReqObject.get('FormData').get('NOTE=STR')}", type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "2"}
    ]}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('addNewLocation_withValue')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ENTER LOCATION DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "PARENT STATUS", value: "", name: "PARENT_STATUS=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", required: true, validate: "NotEmpty", maxLength: "20", tooltip: "SELECT PARENT_STATUS", icon: "icon-input", options: 
    <mytags:SelectHandler  fromDB="true" listName="ORDER_STATUS_TYPES" emptyField="false"/>
    },
    {type: "input", label: "LOCATION NAME", name: "LOCATION_NAME=STR", labelWidth: "120", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", tooltip: "LOCATION NAME", icon: "icon-input",  required: true, validate: "NotEmpty"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>EXTRAS</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "btn2state", name:"ACTIVE=NUM",label: "ACTIVE", checked: true, labelWidth: "120"}, 
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "3"},
    ]}
    ]
</c:if>
<c:if test="${FormType.equals('addNewEmployee')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: "0", offsetLeft: "20", list: [
    {type: "label", label: "<span style=\'color:white\'>EMPLOYEE DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "EMPLOYEE NAME", name: "EMP_NAME=STR", inputWidth: 200, style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", required: true, labelWidth: "150"},
    {type: "input", label: "MOBILE NO 1", value: "", name: "EMP_MOBILE1=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "MOBILE NO 2", name: "EMP_MOBILE2=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;",  maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "ADDRESS", name: "EMP_ADDRESS=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "100", tooltip: "2nd  Mobile Number", icon: "icon-input", labelWidth: "150", rows: "3"}
    ]},
    {type: "newcolumn"},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>EMPLOYEE WORK DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "ROLE", name: "EMP_ROLE=STR", inputWidth: 200, style: "font-weight:bold;background-color:#8cf9ff;", validate: "NotEmpty", maxLength: "50", required: true, labelWidth: "150",options:[
    <mytags:SelectHandler emptyField="false" listName="addnewemployee.role"/>
    ]},
    {type: "select", label: "PAY TYPE", value: "", name: "PAY_TYPE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", validate: "NotEmpty", maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150", required: true,options:[
    <mytags:SelectHandler emptyField="false" listName="addnewemployee.paytype"/>
    ]},
    {type: "select", label: "HOLIDAY", value: "", name: "EMP_HOLIDAY=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", validate: "NotEmpty", maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150",options:[
    <mytags:SelectHandler emptyField="false" listName="addnewemployee.holiday"/>
    ]},
    {type: "btn2state", label: "IN SERVICE", checked:true, name: "IN_SERVICE=NUM", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;",tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "NOTE", value: "", name: "NOTE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", labelWidth: "150", rows: "3"}
    ]}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('addNewEmployee_withValue')}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: "0", offsetLeft: "20", list: [
    {type: "label", label: "<span style=\'color:white\'>EMPLOYEE DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "input", label: "EMPLOYEE NAME", value:"${ReqObject.get('FormData').get('EMP_NAME=STR')}", name: "EMP_NAME=STR", inputWidth: 200, style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "50", required: true, labelWidth: "150"},
    {type: "input", label: "MOBILE NO 1", value:"${ReqObject.get('FormData').get('EMP_MOBILE1=STR')}", name: "EMP_MOBILE1=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "MOBILE NO 2", value:"${ReqObject.get('FormData').get('EMP_MOBILE2=STR')}",  name: "EMP_MOBILE2=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;",  maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "ADDRESS",value:"${ReqObject.get('FormData').get('EMP_ADDRESS=STR')}", name: "EMP_ADDRESS=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", maxLength: "100", tooltip: "2nd  Mobile Number", icon: "icon-input", labelWidth: "150", rows: "3"}
    ]},
    {type: "newcolumn"},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>EMPLOYEE WORK DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", label: "ROLE",  value:"${ReqObject.get('FormData').get('EMP_ROLE=STR')}",  name: "EMP_ROLE=STR", inputWidth: 200, style: "font-weight:bold;background-color:#8cf9ff;", validate: "NotEmpty", maxLength: "50", required: true, labelWidth: "150",options:[
    <mytags:SelectHandler emptyField="false" listName="addnewemployee.role"/>
    ]},
    {type: "select", label: "PAY TYPE", value:"${ReqObject.get('FormData').get('PAY_TYPE=STR')}", name: "PAY_TYPE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", validate: "NotEmpty", maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150", required: true,options:[
    <mytags:SelectHandler emptyField="false" listName="addnewemployee.paytype"/>
    ]},
    {type: "select", label: "HOLIDAY",  value:"${ReqObject.get('FormData').get('EMP_HOLIDAY=STR')}",  name: "EMP_HOLIDAY=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;", validate: "NotEmpty", maxLength: "10", tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150",options:[
    <mytags:SelectHandler emptyField="false" listName="addnewemployee.holiday"/>
    ]},
    {type: "btn2state", label: "IN SERVICE", checked: ${ReqObject.get('FormData').get('IN_SERVICE=NUM')==0 ? false:true},  name: "IN_SERVICE=NUM", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;",  tooltip: "1st Mobile Number", icon: "icon-input", labelWidth: "150"},
    {type: "input", label: "NOTE",  value:"${ReqObject.get('FormData').get('NOTE=STR')}", name: "NOTE=STR", inputWidth: "200", style: "font-weight:bold;background-color:#8cf9ff;",  maxLength: "100", tooltip: "1st Mobile Number", icon: "icon-btn2state", labelWidth: "150", rows: "3"}
    ]}
    ]
</c:if>
<c:if test="${ReqObject.get('FormType').equals('LoadScheduler')}">   
    [
    <c:forEach items="${ReqObject.get('FormData')}" var="DATA_OBJECT" varStatus="loop" >
        {end_date:"${DATA_OBJECT[3]}",text:"${DATA_OBJECT[0]}",start_date:"${DATA_OBJECT[3]}"}, 
        {end_date:"${DATA_OBJECT[3]}",text:"${DATA_OBJECT[1]}",start_date:"${DATA_OBJECT[3]}"}, 
        {end_date:"${DATA_OBJECT[3]}",text:"${DATA_OBJECT[2]}",start_date:"${DATA_OBJECT[3]}"}<c:if test="${!loop.last}">,</c:if>     
    </c:forEach>
    ]
</c:if>
<c:if test="${FormType.equals('daily_productionReport_Form')}">   
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [
    {type: "label", label: "<span style=\'color:white\'>TYPE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "125", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select",  label: "TYPE", name: "TYPE=STR", inputWidth: "115", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", required: true, tooltip: "SELECT TYPE", labelWidth: "40", inputHeight: "40",options:
    [<mytags:SelectHandler emptyField="true" listName="addnewemployee.role"/>]
    },
    {type: "newcolumn", offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>NAME</span>", value: "", name: "LABEL_DETAILS", labelWidth: "100", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "NAME", name: "NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "40", icon: "icon-input", offsetLeft: "10", options: []},
    {type: "newcolumn", offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>REPORT TYPE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "190", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "select", label: "REPORT TYPE", name: "REPORT_TYPE=STR", inputWidth: "160", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", required: true, tooltip: "Enter Bill No", labelWidth: "70", inputHeight: "40", icon: "icon-input", offsetLeft: "10", options: [
    <mytags:SelectHandler emptyField="true" listName="reports.production_report_type"/>]},
    {type: "newcolumn", offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>FROM DATE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "130", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "calendar", label: "FROM DATE",disabled:"true", name: "FROM_DATE=DATE", inputWidth: "100", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", dateFormat: "%d/%m/%y", validate: "dateValidator", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "30", icon: "icon-input", offsetLeft: "10"},
    {type: "newcolumn", offset: "10"},
    {type: "label", label: "<span style=\'color:white\'>TO DATE</span>", value: "", name: "LABEL_DETAILS", labelWidth: "130", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label", offsetLeft: "10"},
    {type: "calendar", label: "TO DATE", disabled:"true",name: "TO_DATE=DATE", inputWidth: "100", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", dateFormat: "%d/%m/%y", validate: "dateValidator", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "30", icon: "icon-calendar", offsetLeft: "10"}
    ]},
    {type: "newcolumn", offset: "10"},

    {type: "button", label: "New Input", value: "<b>GET--- REPORT</b>", name: "getreport", className: "button1"},
    {type: "button", label: "New Input", value: "<b>PRINT REPORT</b>", name: "reportPrint", className: "button1"}
    ]
</c:if>
<c:if test="${FormType.equals('print_module_form')}">   
    [
    {type: "settings", position: "label-left", labelWidth: 60, inputWidth: 100},
    {type: "block", width: "auto", blockOffset: 20, list: [    
    {type: "select",  label: "TYPE", name: "TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", required: true, tooltip: "SELECT TYPE", labelWidth: "40", inputHeight: "40",options:
    [<mytags:SelectHandler emptyField="true" listName="addnewemployee.role"/>]
    },
    {type: "newcolumn", offset: "10"},   
    {type: "select", label: "NAME", name: "NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", required: true, tooltip: "Enter Bill No", labelWidth: "50", inputHeight: "40", icon: "icon-input", offsetLeft: "10", options: []},
    {type: "newcolumn", offset: "10"},    
    {type: "calendar", label: "PAYMENT DATE", name: "PAY_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#fff9c4;box-shadow: 1px 1px 2px 2px grey;     font-size: 20px;", dateFormat: "%d/%m/%y", validate: "dateValidator", required: true, tooltip: "Enter Bill No", labelWidth: "120", inputHeight: "30", icon: "icon-input", offsetLeft: "10"},
    ]},
    {type: "newcolumn", offset: "10"},     
    {type: "button", label: "New Input", value: "<b>VIEW-</b>", name: "getreport", className: "button1"},
    {type: "newcolumn", offset: "10"},     
    {type: "button", label: "New Input", value: "<b>PRINT</b>", name: "printReport", className: "button1"}
    ]
</c:if>
<c:if test="${FormType.equals('Delivery_Completed')}">   
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "input", label: "QUANTITY", name: "QUANTITY=STR", labelWidth: "120", inputWidth: "120", style: "color:GREY;font-weight:bold;font-size:24px;background-color:#E8FFFF;", maxLength: "1", tooltip: "QUANTITY", icon: "icon-input", validate: "NotEmpty", inputHeight: "30", readonly: true},
    {type: "input", label: "TOTAL AMOUNT", name: "PRICE=STR", labelWidth: "120", inputWidth: "120", style: "color:GREY;font-weight:bold;font-size:24px;background-color:#E8FFFF;", validate: "NotEmpty", maxLength: "20", tooltip: "SELECT PARENT_STATUS", icon: "icon-input", options: [], inputHeight: "30", readonly: true},
    {type: "input", label: "TOTAL ADVANCE", name: "ADVANCE=STR", labelWidth: "120", inputWidth: "120", style: "color:GREY;font-weight:bold;font-size:24px;background-color:#E8FFFF;", maxLength: "50", tooltip: "ADVANCE", icon: "icon-input", validate: "NotEmpty", inputHeight: "30", readonly: true},
    {type: "input", label: "DUE", name: "DUE=STR", labelWidth: "120", inputWidth: "180", style: "color:black;font-weight:bold;font-size:36px;background-color:#FFFAAE;", maxLength: "50", tooltip: "DUE", icon: "icon-input", validate: "NotEmpty", inputHeight: "40", readonly: true},
    {type: "input", label: "ACTUAL PAYMENT", name: "ACTUAL_PAYMENT=STR", labelWidth: "120", inputWidth: "180", style: "font-weight:bold;font-size:36px;background-color:#8cf9ff;", maxLength: "50", tooltip: "ACTUAL_PAYMENT", icon: "icon-input", required: true, validate: "NotEmpty", inputHeight: "40"},
    {type: "newcolumn", offset: "20"},
    {type: "calendar", label: "DELIVERY DATE", name: "DATE_OF_DELIVERY=DATE", labelWidth: "120", inputWidth: "170", style: "color:GREY;font-weight:bold;font-size:24px;background-color:#E8FFFF;", maxLength: "50", tooltip: "DATE OF DELIVERY", icon: "icon-input", validate: "NotEmpty", inputHeight: "30", value: "null", required: true},
    {type: "input", label: "NOTE", name: "NOTE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#8cf9ff;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "120", rows: "3"}
    ]}
    ]
</c:if>

