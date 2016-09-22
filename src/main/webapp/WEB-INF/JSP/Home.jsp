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
                overflow: hidden;
                margin: 0px auto;                
            }
        </style>
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
