<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Registration</h2>
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
<form action="${pageContext.request.contextPath}/controller" method="post">
    <input type="hidden" name="command" value="registration"/>
    <label>Login: <input type="text" name="login" required minlength="3" maxlength="50"/></label><br/>
    <label>Password: <input type="password" name="password" required minlength="6"/></label><br/>
    <label>Email: <input type="email" name="email" required/></label><br/>
    <button type="submit">Sign Up</button>
</form>
</body>
</html>