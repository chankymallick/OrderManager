<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<c:if test="${Type.equals('ordersandbills')}">
    <rows>
        <row id="1">
            <cell><![CDATA[<img src="resources/Images/new_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Add New Order</b>]]></cell>        
        </row>
        <row id="2">
            <cell><![CDATA[<img src="resources/Images/advance.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Add Advance</b>]]></cell>  
        </row>
        <row id="3">
            <cell><![CDATA[<img src="resources/Images/edit_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Edit Order Details</b>]]></cell>  
        </row>
        <row id="4">
            <cell><![CDATA[<img src="resources/Images/alert.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Add Order Halt Alert </b>]]></cell>  
        </row>
        <row id="5">
            <cell><![CDATA[<img src="resources/Images/cancel_order.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Cancel Order</b>]]></cell>  
        </row>
        <row id="6">
            <cell><![CDATA[<img src="resources/Images/search2.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Search Order</b>]]></cell>  
        </row>
        <row id="7">
            <cell><![CDATA[<img src="resources/Images/status.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Add Status</b>]]></cell>  
        </row>
        <row id="8">
            <cell><![CDATA[<img src="resources/Images/settings.png" width="30px" height="30px"/>]]></cell>
            <cell><![CDATA[<b>Order Settings</b>]]></cell>  
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
<c:if test="${Type.equals('loadNewOrderItemForm')}">
    <rows>
        <row id="1">
            <cell>1</cell>
            <cell>ASTRA</cell>   
            <cell>EXTRA</cell>
            <cell>5</cell>
            <cell>15</cell>   
            <cell>0</cell>
            <cell>YES</cell>
            <cell>DONE</cell>
        </row> 
        <row id="2">
            <cell>1</cell>
            <cell>Half Stitch</cell>   
            <cell>EXTRA</cell>
            <cell>55</cell>
            <cell>45</cell>   
            <cell>5</cell>
            <cell>YES</cell>
            <cell>DONE</cell>
        </row>     
        <row id="3">
            <cell>1</cell>
            <cell>DOUBLE LESS LACHA</cell>   
            <cell>EXTRA</cell>
            <cell>50</cell>
            <cell>120</cell>   
            <cell>0</cell>
            <cell>YES</cell>
            <cell>DONE</cell>
        </row> 
        <row id="4">
            <cell>1</cell>
            <cell>CHURI PA</cell>   
            <cell>EXTRA</cell>
            <cell>0</cell>
            <cell>10</cell>   
            <cell>3</cell>
            <cell>YES</cell>
            <cell>DONE</cell>
        </row>       
    </rows>
</c:if>

 