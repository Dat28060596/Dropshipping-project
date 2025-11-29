<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>DSP Dropshipping Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="main-header">
        <a href="${pageContext.request.contextPath}/home" class="brand">DSP Store</a>
        <div class="nav-links">
            <c:if test="${sessionScope.currentUser != null}">
                <span>Welcome, ${sessionScope.currentUser.username}</span>
                <a href="${pageContext.request.contextPath}/cart"><i class="fa-solid fa-cart-shopping"></i> My Cart</a>
                <a href="${pageContext.request.contextPath}/my-orders"><i class="fa-solid fa-box"></i> My Orders</a>
                <a href="${pageContext.request.contextPath}/profile"><i class="fa-solid fa-user"></i> Profile</a>
                <a href="${pageContext.request.contextPath}/logout" style="color: red;">Logout</a>
            </c:if>
            <c:if test="${sessionScope.currentUser == null}">
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </c:if>
        </div>
    </div>

    <c:if test="${param.msg == 'added'}">
        <div class="alert-success">
            ✅ Item added to cart! <a href="${pageContext.request.contextPath}/cart">View Cart</a>
        </div>
    </c:if>

    <div class="product-container">
        <c:forEach var="p" items="${productList}">
            <div class="product-card">
                <img src="https://via.placeholder.com/200" alt="Product">
                <div class="product-name">${p.name}</div>
                <div class="product-category">${p.categoryName}</div>
                <div class="product-price">$${p.price}</div>
                
                <form action="${pageContext.request.contextPath}/cart" method="post" style="margin-top: 15px;">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="${p.id}">
                    <div style="display: flex; justify-content: center; align-items: center; gap: 10px;">
                        <input type="number" name="quantity" value="1" min="1" max="${p.stock}" style="width: 60px; padding: 5px; text-align: center; border: 1px solid #ddd; border-radius: 4px;">
                        <button type="submit" class="btn btn-primary">Add to Cart</button>
                    </div>
                </form>
            </div>
        </c:forEach>
    </div>

</body>
</html>