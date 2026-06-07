<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>My Orders</h2>
<a href="${pageContext.request.contextPath}/controller?command=viewProducts">Back to Products</a>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Product ID</th>
        <th>Quantity</th>
        <th>Total</th>
        <th>Status</th>
        <th>Date</th>
        <th>Action</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>${order.id}</td>
            <td>${order.productId}</td>
            <td>${order.quantity}</td>
            <td>${order.totalAmount}</td>
            <td>${order.status}</td>
            <td>${order.createdAt}</td>
            <td>
                <c:if test="${order.status == 'NEW'}">
                    <a href="${pageContext.request.contextPath}/controller?command=cancelOrder&orderId=${order.id}">Cancel</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>