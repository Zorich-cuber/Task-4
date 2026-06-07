<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Product Catalog</h2>
<c:if test="${not empty sessionScope.user}">
    <p>Welcome, ${sessionScope.user.login}!</p>
    <a href="${pageContext.request.contextPath}/controller?command=viewOrders">My Orders</a> |
    <a href="${pageContext.request.contextPath}/controller?command=logout">Logout</a>
</c:if>
<c:if test="${empty sessionScope.user}">
    <a href="${pageContext.request.contextPath}/controller?command=login">Login</a>
</c:if>
<table border="1">
    <tr>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Action</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>${product.name}</td>
            <td>${product.description}</td>
            <td>${product.price}</td>
            <td>${product.quantity}</td>
            <td>
                <c:if test="${not empty sessionScope.user}">
                    <form action="${pageContext.request.contextPath}/controller" method="post">
                        <input type="hidden" name="command" value="createOrder"/>
                        <input type="hidden" name="productId" value="${product.id}"/>
                        <input type="number" name="quantity" value="1" min="1" max="${product.quantity}"/>
                        <button type="submit">Order</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${currentPage > 0}">
    <a href="?command=viewProducts&page=${currentPage - 1}">Previous</a>
</c:if>
Page ${currentPage + 1} of ${totalPages}
<c:if test="${currentPage + 1 < totalPages}">
    <a href="?command=viewProducts&page=${currentPage + 1}">Next</a>
</c:if>
</body>
</html>