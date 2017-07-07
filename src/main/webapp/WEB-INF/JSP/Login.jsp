<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Order Manager V 1.0 </title>    
    <link href="<c:url value='/resources/TemplateFiles/loginpagefiles/style.css' />" type="text/css" rel="stylesheet" />
    <link rel="shortcut icon"  href="<c:url value='/resources/Images/favicon.ico' />" type="image/x-icon"/>
</head>
<body>
    <div id="headerTab">       
        
    </div>  
    <form action="Login" class="login">
        <h1>Login</h1>
        <div class="dropdown dropdown-dark">
             <select name="two" class="dropdown-select">
               <option value="">Select User</option>
               <option value="1">Chanky</option>
               <option value="2">skumarpal</option>
               <option value="3">uttam</option>
               <option value="3">Jackie</option>
             </select>
           </div>
        <input type="password" name="password" class="login-input" placeholder="Password">
        <input type="submit" value="Login" class="login-submit">
        <p class="login-help"><a href="index.html">Forgot password?</a></p>
    </form>    
</body>
</html>
