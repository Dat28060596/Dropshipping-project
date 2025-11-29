<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head>
    <title>My Shopping Cart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shop.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <div class="main-header">
        <a href="${pageContext.request.contextPath}/home" class="brand">DSP Store</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/my-orders">My Orders</a>
            <a href="${pageContext.request.contextPath}/profile">Profile</a>
            <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;">Logout</a>
        </div>
    </div>

    <div class="cart-container">
        <h1 style="border-bottom: 2px solid #e3f2fd; padding-bottom: 10px; margin-bottom: 20px;">
            <i class="fa-solid fa-cart-shopping" style="color: #1565c0;"></i> Shopping Cart
        </h1>

        <c:if test="${param.error == 'stock'}">
            <div style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 6px; margin-bottom: 20px;">
                ⚠️ Some items in your cart are out of stock. Please remove them or reduce quantity.
            </div>
        </c:if>
        <c:if test="${param.error == 'missing_info'}">
            <div style="background-color: #fff3cd; color: #856404; padding: 15px; border-radius: 6px; margin-bottom: 20px;">
                ⚠️ Please provide both Phone Number and Address to checkout.
            </div>
        </c:if>

        <c:if test="${empty sessionScope.cart}">
            <div style="text-align: center; padding: 50px;">
                <i class="fa-solid fa-basket-shopping" style="font-size: 60px; color: #ccc;"></i>
                <p style="font-size: 18px; color: #666;">Your cart is empty.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Go Shopping</a>
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.cart}">
            <table>
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Total</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="grandTotal" value="0" />
                    <c:forEach var="item" items="${sessionScope.cart}">
                        <tr>
                            <td style="font-weight: 500;">${item.product.name}</td>
                            <td>$${item.product.price}</td>
                            <td>${item.quantity}</td>
                            <td style="color: #28a745; font-weight: bold;">
                                <fmt:formatNumber value="${item.totalPrice}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2" />
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="margin:0;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="${item.product.id}">
                                    <button type="submit" class="btn btn-danger"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <c:set var="grandTotal" value="${grandTotal + item.totalPrice}" />
                    </c:forEach>
                </tbody>
            </table>

            <div class="total-section">
                Grand Total: <span style="color: #1565c0;">
                    <fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2" />
                </span>
            </div>

            <div style="margin-top: 30px; background-color: #f8f9fa; padding: 25px; border-radius: 8px;">
                <h3 style="margin-top: 0; color: #333;">Shipping Information</h3>
                
                <form action="${pageContext.request.contextPath}/order/checkout-cart" method="post">
                    
                    <label style="display:block; margin-bottom:8px; font-weight:bold;">Contact Phone:</label>
                    <input type="tel" name="phone" value="${sessionScope.currentUser.phone}" required 
                           style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 15px;"
                           placeholder="090...">

                    <label style="display:block; margin-bottom:8px; font-weight:bold;">Delivery Address:</label>
                    <textarea name="address" rows="3" required 
                              style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 20px; font-family: inherit;" 
                              placeholder="123 Main St..."></textarea>
                    
                    <div style="display: flex; justify-content: center; gap: 15px;">
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-continue">Continue Shopping</a>
                        
                        <button type="submit" class="btn btn-checkout">Checkout All</button>
                    </div>
                </form>
            </div>
        </c:if>
    </div>

</body>
</html>