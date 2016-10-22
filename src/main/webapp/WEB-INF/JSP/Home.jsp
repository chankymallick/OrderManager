<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <style>
            #Layout_Container,body,html
            {
                width:100%;
                height:100%;
                overflow: hidden !important;
                margin: 0px auto;        
               
            }
            .DHTMLX_LABEL1{
                background-color: #1faeff;               
                line-height: 0px;
                margin-top: 15px;
                height: 17px;
                text-transform: uppercase;
            }         
            .DHTMLX_LABEL2{
                background-color: #00c9f2;               
                line-height: 0px;
                margin-top: 15px;
                height: 17px;               
            }         
            .dhxform_obj_material div.dhxform_img.btn2state_0 {
                background-image: url("resources/Images/toggle_off.png");
                width: 42px;
                height: 24px;
            }

            .dhxform_obj_material div.dhxform_img.btn2state_1 {
                background-image: url("resources/Images/toggle_on.png");
                width: 42px;
                height: 24px;
            }
            .dhtmlx-SuccessNotification{
                font-weight:bold !important;
                color:blue !important;          
            }

        </style>
        <script src="<c:url value='/resources/Javascripts/Utility/main.js' />" ></script>
        <script src="<c:url value='/resources/Javascripts/Utility/shortcut.js' />" ></script>
        <link href="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlx.css' />" type="text/css" rel="stylesheet" />
        <script src="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlx.js' />" ></script>
        <script src="<c:url value='/resources/Javascripts/Utility/Utility.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Home/OrderManagerHome.js' />" ></script> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Manager v 1.0</title>
    </head>
    <body>        
        <div id="Layout_Container">            
        </div>
    </body>
    <script>
        var initializeObj = new com.ordermanager.home.OrderManagerHome();
    </script>
</html>
