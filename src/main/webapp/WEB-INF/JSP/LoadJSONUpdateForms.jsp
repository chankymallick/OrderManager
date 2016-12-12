<%-- 
    Document   : LoadJsonUpdateForms
    Created on : Dec 9, 2016, 11:16:39 PM
    Author     : Maliick
--%>
<%@page contentType="application/json" pageEncoding="UTF-16"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>
<c:if test="${ReqObject.get('FormType').equals(('updateNewOrder_Form'))}">
    [
    {type: "settings", position: "label-left", labelWidth: 90, inputWidth: 130},
    {type: "block", width: "auto", blockOffset: 40, list: [
    {type: "label", label: "<span style=\'color:white\'>ORDER DETAILS</span>", value: "", name: "LABEL_DETAILS", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", value:"${ReqObject.get('FormData').get('LOCATION=STR')}", label: "LOCATION", name: "LOCATION=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", maxLength: "30", required: true, tooltip: "Store Location", labelWidth: "130",options: [
    <mytags:SelectHandler emptyField="false" listName="addneworder.location"/>
    ]},
    {type: "input", label: "BILL NO", value:"${ReqObject.get('FormData').get('BILL_NO=STR')}", name: "BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "OrderBillNoValidation", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "input", label: "VERIFY BILL NO", value:"${ReqObject.get('FormData').get('VERIFY_BILL_NO=STR')}", name: "VERIFY_BILL_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "NotEmpty", maxLength: "10", tooltip: "Enter bill number", icon: "icon-input", labelWidth: "130"},
    {type: "calendar", value:"${ReqObject.get('FormData').get('ORDER_DATE=DATE')}", label: "ORDER DATE", name: "ORDER_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter order date", icon: "icon-input", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "calendar", value:"${ReqObject.get('FormData').get('DELIVERY_DATE=DATE')}", label: "DELIVERY DATE", name: "DELIVERY_DATE=DATE", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "dateValidator", maxLength: "30", tooltip: "Enter delivery date", icon: "icon-calendar", labelWidth: "130", dateFormat: "%d/%m/%y", enableTime: true, calendarPosition: "bottom"},
    {type: "input", value:"${ReqObject.get('FormData').get('CUSTOMER_NAME=STR')}", label: "CUSTOMER NAME", name: "CUSTOMER_NAME=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Enter customer name ", icon: "icon-select", labelWidth: "130", maxLength: "30"},
    {type: "input",value:"${ReqObject.get('FormData').get('CONTACT_NO=STR')}", label: "CONTACT NO", name: "CONTACT_NO=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Enter customer name ", icon: "icon-input", labelWidth: "130", maxLength: "10"},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>ORDER TYPE/RATE</span>", value: "", name: "LABEL_RATE", labelWidth: "200", className: "DHTMLX_LABEL1", labelHeight: "15"},
    {type: "select", value:"${ReqObject.get('FormData').get('PIECE_VENDOR=STR')}",label: "PIECE VENDOR", name: "PIECE_VENDOR=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    <mytags:SelectHandler emptyField="false" listName="addneworder.vendor"/>
    ]},
    {type: "select",value:"${ReqObject.get('FormData').get('ORDER_TYPE=STR')}", label: "ORDER TYPE", name: "ORDER_TYPE=STR", inputWidth: "170", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "120", options: [
    {text: "CHURIDER", value: "CHURIDER"},
    {text: "SALWAR", value: "SALWAR"},
    {text: "LACHA", value: "LACHA"},
    {text: "PIECE SELL", value: "PIECE SALE"}
    ]},   
    {type: "input",value:"${ReqObject.get('FormData').get('QUANTITY=NUM')}", label: "QUANTITY", name: "QUANTITY=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "1", tooltip: "Enter Password", icon: "icon-select", labelWidth: "120", value: "1"},
    {type: "input",value:"${ReqObject.get('FormData').get('PRICE=NUM')}", label: "PRICE", name: "PRICE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "input",value:"${ReqObject.get('FormData').get('ADVANCE=NUM')}", label: "ADVANCE", name: "ADVANCE=NUM", inputWidth: "60", style: "font-weight:bold;background-color:#edeaea;", required: true, validate: "ValidNumeric", maxLength: "4", tooltip: "Enter Password", icon: "icon-password", labelWidth: "120"},
    {type: "btn2state", name: "CUSTOM_RATE=NUM", label: "CUSTOM RATE", checked: ${ReqObject.get('FormData').get('CUSTOM_RATE=NUM')==0 ? false:true}, labelWidth: "120"},
    {type: "button", name: "ITEM_BUTTON=BUTTON",value: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bolder'>SELECT ITEMS</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", tooltip: "Enter Password"}
    ]},
    {type: "newcolumn", offset: "20"},
    {type: "label", label: "<span style=\'color:white\'>OTHER OPTION</span>", value: "", name: "OTHERS", labelWidth: "180", className: "DHTMLX_LABEL1", labelHeight: "15", icon: "icon-label"},
    {type: "select", value:"${ReqObject.get('FormData').get('PRODUCT_TYPE=STR')}",label: "PRODUCT TYPE", name: "PRODUCT_TYPE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", required: true, tooltip: "Select Preferred Language ", icon: "icon-select", labelWidth: "110", options: [
    {text: "COTTON WORK", value: "COTTON WORK"},
    {text: "SYNTHETIC WORK", value: "SYNTHETIC WORK"}
    ]},
    {type: "select",value:"${ReqObject.get('FormData').get('MEASURED_BY=STR')}", label: "MEASURED BY", name: "MEASURED_BY=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "MONI", value: "MONI"},
    {text: "SUNITA", value: "SUNITA"}   
    ]},
    {type: "select", value:"${ReqObject.get('FormData').get('ORDER_STATUS=STR')}", label: "STATUS", name: "ORDER_STATUS=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", options: [
    {text: "NEW ORDER", value: "NEW ORDER"},
    {text: "PROBLEM HALT", value: "PROBLEM HALT"},
    {text: "REQUIREMENT HALT", value: "REQUIREMENT HALT"},
    {text: "IN PROCESS", value: "IN PROCESS"},
    {text: "ADVANCE LOW", value: "ADVANCE LOW"}   
    ]},
    {type: "input",value:"${ReqObject.get('FormData').get('NOTE=STR')}", label: "NOTE", name: "NOTE=STR", inputWidth: "150", style: "font-weight:bold;background-color:#edeaea;", tooltip: "Extra Note", icon: "icon-select", labelWidth: "110", rows: "3"}
    ]
</c:if>