<%-- 
    Document   : StatisticsContainer
    Created on : Nov 8, 2016, 2:12:00 PM
    Author     : Maliick
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/CustomTags.tld" prefix="mytags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <style>
        html,body{
            margin:0px;
            overflow: hidden;
        }
        .statContainer1{
            height:70px;
            width:180px;
            margin:1px;
            padding:5px;
            float:left;
            border-radius:5px;
            line-height:30px;
        }
        .template_color1{
            background-color:#00bfff;
        }
        .template_color2{
            background-color:green;
        }
        .template_color3{
            background-color:blue;
        }
        .template_color4{
            background-color:orange;
        }
        .template_color5{
            background-color:black;
        }
        .template_color6{
            background-color:red;
        }
        .Heading{
            font-weignt:bold;
            font-size:20px;
            font-family:sans-serif;
            text-align:left;
            color:white;
        }
        .Value{
            font-weignt:bolder;
            font-size:38px;
            font-family:Impact;
            text-align:left;
            color:yellow;
        }
    </style>   
    <body>
    <c:set var="ID" value="${1}"/>
    <c:forEach items="${OBJECT_MAP.get('STAT_VALUES')}" var="STAT_OBJECT">    
        <div id='templateContainer' class='statContainer1 template_color${ID}'>
            <span class='Heading'>${STAT_OBJECT[1]}</span><br>
            <span class='Value'>${STAT_OBJECT[2]}</span>
        </div>         
        <c:set var="ID" value="${ID+1}"/>   
    </c:forEach>    
    </body>
</html>
