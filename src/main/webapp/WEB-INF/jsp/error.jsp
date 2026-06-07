<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Error Occurred</h2>
<p>${error}</p>
<a href="${pageContext.request.contextPath}/controller?command=viewProducts">Home</a>
</body>
</html>