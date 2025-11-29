<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head>
    <title>My Order History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .badge { padding: 5px 10px; border-radius: 20px; font-size: 12px; font-weight: bold; text-transform: uppercase; }
        .badge-PENDING { background-color: #fff3cd; color: #856404; border: 1px solid #ffeeba; }
        .badge-SHIPPED { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .badge-PAID { background-color: #cce5ff; color: #004085; border: 1px solid #b8daff; }
    </style>
</head>
<body>

    <div class="main-header">
        <a href="${pageContext.request.contextPath}/home" class="brand">DSP Store</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/my-orders" style="color: #1565c0;">My Orders</a>
            <a href="${pageContext.request.contextPath}/profile">Profile</a>
            <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;">Logout</a>
        </div>
    </div>

    <div class="cart-container">
        <h1><i class="fa-solid fa-clock-rotate-left" style="color: #1565c0;"></i> Order History</h1>
        <a href="${pageContext.request.contextPath}/home" style="text-decoration: none; color: #1565c0; margin-bottom: 20px; display: inline-block;">
            &larr; Back to Shop
        </a>

        <c:if test="${empty orderList}">
            <p style="text-align: center; color: #666; margin-top: 30px;">
                You haven't placed any orders yet.
            </p>
        </c:if>

        <c:if test="${not empty orderList}">
            <table>
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Date</th>
                        <th>Items</th>
                        <th>Total</th>
                        <th>Address</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="o" items="${orderList}">
                        <tr>
                            <td>#${o.orderId}</td>
                            <td style="color: #666; font-size: 14px;">
                                <fmt:formatDate value="${o.orderDate}" pattern="MMM dd, yyyy HH:mm" timeZone="Asia/Ho_Chi_Minh" />
                            </td>
                            <td>
                                <c:out value="${o.productName}" escapeXml="false" />
                            </td>
                            <td style="font-weight: bold;">
                                <c:if test="${o.totalPrice > 0}">
                                    <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2" />
                                </c:if>
                            </td>
                            <td>${o.shippingAddress}</td>
                            <td>
                                <span class="badge badge-${o.status}">${o.status}</span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>

</body>
</html>