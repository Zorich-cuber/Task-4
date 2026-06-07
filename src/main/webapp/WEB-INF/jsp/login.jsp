<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Login</h2>
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
<c:if test="${not empty message}">
    <p style="color:green">${message}</p>
</c:if>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="login"/>
    <label>Login: <input type="text" name="login" required/></label><br/>
    <label>Password: <input type="password" name="password" required/></label><br/>
    <button type="submit">Sign In</button>
</form>
<a href="${pageContext.request.contextPath}/controller?command=registration">Register</a>
</body>
</html>