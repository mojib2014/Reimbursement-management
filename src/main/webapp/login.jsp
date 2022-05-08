<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<html>
    <head>Login Page</head>
    <body>
        <form action="login" method="POST">
            <input type="hidden" name="origin" value="${origin}">
            <c:if test="${not empty error}">
                * error: ${error}
            </c:if>
            <input type="email" name="email" placeholder="Your email here...">
            <input type="password" name="password" placeholder="Your password here...">
            <input type="submit">
        </form>
    </body>
</html>