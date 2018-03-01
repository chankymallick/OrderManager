<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <style type="text/css">
            ::-webkit-scrollbar {
                width: 6px;
                height: 6px;
            }
            ::-webkit-scrollbar-button {
                width: 0px;
                height: 0px;
            }
            ::-webkit-scrollbar-thumb {
                background: #e1e1e1;
                border: 0px none #ffffff;
                border-radius: 50px;
            }
            ::-webkit-scrollbar-thumb:hover {
                background: #ffffff;
            }
            ::-webkit-scrollbar-thumb:active {
                background: #000000;
            }
            ::-webkit-scrollbar-track {
                background: #666666;
                border: 0px none #ffffff;
                border-radius: 23px;
            }
            ::-webkit-scrollbar-track:hover {
                background: #666666;
            }
            ::-webkit-scrollbar-track:active {
                background: #333333;
            }
            ::-webkit-scrollbar-corner {
                background: transparent;
            }
        </style>
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
            .ITEM_BUTTON1 {
                -moz-box-shadow:inset 0px 1px 0px 0px #bee2f9;
                -webkit-box-shadow:inset 0px 1px 0px 0px #bee2f9;
                box-shadow:inset 0px 1px 0px 0px #bee2f9;
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #0068a8), color-stop(1, #000000));
                background:-moz-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-webkit-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-o-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-ms-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:linear-gradient(to bottom, #0068a8 5%, #000000 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#0068a8', endColorstr='#000000',GradientType=0);
                background-color:#0068a8;
                -moz-border-radius:6px;
                -webkit-border-radius:6px;
                border-radius:6px;
                border:1px solid #ffffff;
                display:inline-block;
                cursor:pointer;
                color:#ffffff;
                font-family:Arial;
                font-size:15px;
                font-weight:bold;
                height:40px;
                width:220px;
                text-decoration:none;
                text-align: center;
                line-height: 3;
                margin-top: 3px;    
            }
            .ITEM_BUTTON1:hover {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #000000), color-stop(1, #0068a8));
                background:-moz-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-webkit-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-o-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-ms-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:linear-gradient(to bottom, #000000 5%, #0068a8 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#000000', endColorstr='#0068a8',GradientType=0);
                background-color:#000000;
            }
            .ITEM_BUTTON1:active {
                position:relative;
                top:1px;
            }
            .ITEM_BUTTON2 {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ffad0a), color-stop(1, #d99100));
                background:-moz-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:-webkit-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:-o-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:-ms-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:linear-gradient(to bottom, #ffad0a 5%, #d99100 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffad0a', endColorstr='#d99100',GradientType=0);
                background-color:#ffad0a;
                -moz-border-radius:6px;
                -webkit-border-radius:6px;
                border-radius:6px;
                border:1px solid #ffe014;
                display:inline-block;
                cursor:pointer;
                color:#ffffff;
                font-family:Arial;
                font-size:15px;
                font-weight:bold;
                height:40px;
                width:220px;
                text-decoration:none;
                text-align: center;
                line-height: 3;
                margin-top: 3px;    

            }
            .ITEM_BUTTON2:hover {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #d99100), color-stop(1, #ffad0a));
                background:-moz-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:-webkit-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:-o-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:-ms-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:linear-gradient(to bottom, #d99100 5%, #ffad0a 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#d99100', endColorstr='#ffad0a',GradientType=0);
                background-color:#d99100;
            }
            .ITEM_BUTTON2:active {
                position:relative;
                top:1px;
            }
            .ITEM_BUTTON3 {     
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #0068a8), color-stop(1, #000000));
                background:-moz-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-webkit-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-o-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-ms-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:linear-gradient(to bottom, #0068a8 5%, #000000 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#0068a8', endColorstr='#000000',GradientType=0);
                background-color:#0068a8;
                -moz-border-radius:6px;
                -webkit-border-radius:6px;
                border-radius:6px;
                border:1px solid #ffffff;
                display:inline-block;
                cursor:pointer;
                color:#ffffff;
                font-family:Arial;
                font-size:15px;
                font-weight:bold;
                height:40px;
                width:220px;
                text-decoration:none;
                text-align: center;
                line-height: 3;
                margin-top: 3px;    


            }
            .ITEM_BUTTON3:hover {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #000000), color-stop(1, #0068a8));
                background:-moz-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-webkit-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-o-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-ms-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:linear-gradient(to bottom, #000000 5%, #0068a8 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#000000', endColorstr='#0068a8',GradientType=0);
                background-color:#000000;
            }
            .ITEM_BUTTON3:active {
                position:relative;
                top:1px;
            }
            .SUB_ITEMCSS{              
                background: #45484d;
                background: -moz-linear-gradient(top,  #45484d 0%, #000000 100%);
                background: -webkit-linear-gradient(top,  #45484d 0%,#000000 100%);
                background: linear-gradient(to bottom,  #45484d 0%,#000000 100%);
                filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#45484d', endColorstr='#000000',GradientType=0 ); 
                color:white;
                text-align: center;
                height: 40px;
                border-radius: 5px;
                line-height: 40px;
            }
            .GridHover{
                background-color: #bee2f9;
                color:white;
                cursor: pointer;
            }
            .hdrcell{
                text-align:center;
            }
            .dhx_cal_event_clear{
                height: 18px !important;
            }
            .dhx_month_head{
                font-weight: bolder !important;
                font-size: 16px !important;
            }
            .dhx_month_body{                
                background: #feffff; /* Old browsers */
                background: -moz-linear-gradient(top, #feffff 0%, #d2ebf9 100%); /* FF3.6-15 */
                background: -webkit-linear-gradient(top, #feffff 0%,#d2ebf9 100%); /* Chrome10-25,Safari5.1-6 */
                background: linear-gradient(to bottom, #feffff 0%,#d2ebf9 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
                filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#feffff', endColorstr='#d2ebf9',GradientType=0 );
            }
            .dhx_cal_event_clear {
                color:  rgba(200, 54, 54, 0) !important;

            }

        </style>   
        <link rel="shortcut icon"  href="<c:url value='/resources/Images/favicon.ico' />" type="image/x-icon"/>
        <script src="<c:url value='http://code.jquery.com/jquery-1.9.1.js' />" ></script>
        <script src="<c:url value='http://code.jquery.com/ui/1.10.3/jquery-ui.js' />" ></script>
        <link href="<c:url value='http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css' />" type="text/css" rel="stylesheet" /> 
        <script src="<c:url value='/resources/Javascripts/Utility/main.js' />" ></script>       
        <script src="<c:url value='/resources/Javascripts/Utility/shortcut.js' />" ></script>
        <link href="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlxscheduler.css' />" type="text/css" rel="stylesheet" />           
        <link href="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlx.css' />" type="text/css" rel="stylesheet" />          
        <script src="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlx.js' />" ></script>
        <script src="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlxscheduler.js' />" ></script>
        <script src="<c:url value='/resources/Javascripts/Utility/Utility.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Utility/ReportingUtility.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Utility/UpdateUtility.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Home/OrderManagerHome.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Utility/BulkUpdate.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Utility/OrderScheduler.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Utility/KeyNavigation.js' />" ></script> 

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script>
            loadLanguagePack();
        </script>
        <title>Order Manager v 1.0</title>
    </head>
    <body>        
        <div id="Layout_Container">            
        </div>
    </body>
    <script>
        var Commands;
        var initializeObj = new com.ordermanager.home.OrderManagerHome();
    </script>
    <script>    
      
    </script>
</html>
