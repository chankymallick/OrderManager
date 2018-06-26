<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Order Manager V 1.0 </title>    
    <link href="<c:url value='/resources/TemplateFiles/loginpagefiles/style.css' />" type="text/css" rel="stylesheet" />
    <link rel="shortcut icon"  href="<c:url value='/resources/Images/favicon.ico' />" type="image/x-icon"/>
    <script>
        
        (function (global) { 

    if(typeof (global) === "undefined") {
        throw new Error("window is undefined");
    }

    var _hash = "!";
    var noBackPlease = function () {
        global.location.href += "#";

        // making sure we have the fruit available for juice (^__^)
        global.setTimeout(function () {
            global.location.href += "!";
        }, 50);
    };

    global.onhashchange = function () {
        if (global.location.hash !== _hash) {
            global.location.hash = _hash;
        }
    };

    global.onload = function () {            
        noBackPlease();

        // disables backspace on page except on input fields and textarea..
        document.body.onkeydown = function (e) {
            var elm = e.target.nodeName.toLowerCase();
            if (e.which === 8 && (elm !== 'input' && elm  !== 'textarea')) {
                e.preventDefault();
            }
            // stopping event bubbling up the DOM tree..
            e.stopPropagation();
        };          
    }

})(window);
    </script>
</head>
<body>
    <div id="headerTab">       

    </div>  
    <!--    <form action="j_spring_security_check" class="login" method="POST">
            <h1>Login</h1>    
            <input type="text" name="j_username" class="login-input" placeholder="Username">
            <input type="password" name="j_password" class="login-input" placeholder="Password">
            <input type="submit" value="Login" class="login-submit">
            <p class="login-help"><a href="index.html">Forgot password?</a></p>
        </form> 
        <h1></h1>-->
    <form action="LoginProcess" class="login" method="POST">
        <h1>Login</h1>
        <input type="input" name="username" class="login-input" placeholder="Password">
        <input type="password" name="password" class="login-input" placeholder="Password">
        <input type="submit" value="Login" class="login-submit">
        <p class="login-help"><a href="index.html">Forgot password?</a></p>

        <c:if test="${param.message.equals('logout')}">
            <p style="color: yellow;text-align: center; font-weight: bold;">
                You have succesfully logged out !
            </p>
        </c:if>
        <c:if test="${param.message.equals('notfound')}">
            <p style="color: white;text-align: center; font-weight: bold;">
                Wrong Username & Password!
            </p>
        </c:if>
        <c:if test="${param.message.equals('unknown')}">
            <p style="color: white;text-align: center; font-weight: bold;">
                Cannot Login , Unknown Error !
            </p>
        </c:if>
        <c:if test="${param.message.equals('notenabled')}">
            <p style="color: orange;text-align: center; font-weight: bold;">
                You are not allowed to login ! Contact Admin  !
            </p>
        </c:if>

    </form>    
</body>
</html>
