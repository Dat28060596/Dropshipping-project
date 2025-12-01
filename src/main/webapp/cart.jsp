<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head>
    <title>My Shopping Cart</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        /* ===========================
           1. Global Reset & Body
           =========================== */
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f6f9;
            color: #333;
            line-height: 1.6;
        }

        a {
            text-decoration: none;
        }

        /* ===========================
           2. Header / Navigation
           =========================== */
        .main-header {
            background-color: #ffffff;
            padding: 15px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .brand {
            font-size: 24px;
            font-weight: 700;
            color: #1565c0;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .nav-links a {
            margin-left: 25px;
            font-size: 15px;
            font-weight: 500;
            color: #555;
            transition: color 0.3s ease;
        }

        .nav-links a:hover {
            color: #1565c0;
        }

        .logout-link {
            color: #dc3545 !important;
        }

        /* ===========================
           3. Main Container
           =========================== */
        .cart-container {
            max-width: 1000px;
            margin: 40px auto;
            background-color: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05);
        }

        .cart-title {
            border-bottom: 2px solid #e3f2fd;
            padding-bottom: 10px;
            margin-bottom: 20px;
            font-size: 28px;
            color: #333;
        }

        .cart-title i {
            color: #1565c0;
            margin-right: 10px;
        }

        /* ===========================
           4. Alerts & Messages
           =========================== */
        .alert {
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-weight: 500;
        }

        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .alert-warning {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }

        .empty-cart-view {
            text-align: center;
            padding: 50px;
        }

        .empty-cart-view i {
            font-size: 60px;
            color: #ccc;
            margin-bottom: 15px;
        }

        .empty-cart-view p {
            font-size: 18px;
            color: #666;
            margin-bottom: 20px;
        }

        /* ===========================
           5. Cart Table Styling
           =========================== */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
            margin-bottom: 20px;
        }

        thead {
            background-color: #f8f9fa;
            border-bottom: 2px solid #dee2e6;
        }

        th {
            text-align: left;
            padding: 15px;
            font-weight: 600;
            color: #495057;
        }

        td {
            padding: 15px;
            border-bottom: 1px solid #e9ecef;
            vertical-align: middle;
        }

        .product-name {
            font-weight: 500;
            color: #333;
        }

        .total-price {
            color: #28a745;
            font-weight: bold;
        }

        /* ===========================
           6. Buttons
           =========================== */
        .btn {
            display: inline-block;
            padding: 10px 20px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s ease;
            text-align: center;
        }

        .btn:hover {
            opacity: 0.9;
            transform: translateY(-1px);
        }

        .btn-primary { background-color: #1565c0; color: white; }
        .btn-danger { background-color: #dc3545; color: white; padding: 8px 14px; }
        .btn-continue { background-color: #6c757d; color: white; }
        
        .btn-checkout {
            background-color: #28a745;
            color: white;
            font-weight: bold;
            font-size: 16px;
            padding: 12px 30px;
            box-shadow: 0 4px 6px rgba(40, 167, 69, 0.2);
        }

        /* ===========================
           7. Forms & Footer
           =========================== */
        .total-section {
            text-align: right;
            font-size: 20px;
            font-weight: 600;
            padding-top: 20px;
            color: #333;
        }

        .grand-total-highlight {
            color: #1565c0;
        }

        .shipping-section {
            margin-top: 30px;
            background-color: #f8f9fa;
            padding: 25px;
            border-radius: 8px;
            border: 1px solid #e9ecef;
        }

        .shipping-section h3 {
            margin-top: 0;
            margin-bottom: 15px;
            color: #333;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .form-input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-bottom: 15px;
            font-family: inherit;
            transition: border-color 0.3s;
        }

        .form-input:focus {
            outline: none;
            border-color: #1565c0;
            box-shadow: 0 0 0 3px rgba(21, 101, 192, 0.1);
        }

        .action-buttons {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 20px;
        }

        /* Mobile Responsiveness */
        @media (max-width: 768px) {
            .main-header { padding: 15px; flex-direction: column; gap: 10px; }
            .cart-container { margin: 15px; padding: 15px; }
            table { display: block; overflow-x: auto; white-space: nowrap; }
            .action-buttons { flex-direction: column; }
            .btn { width: 100%; }
        }
    </style>
</head>
<body>

    <div class="main-header">
        <a href="${pageContext.request.contextPath}/home" class="brand">DSP Store</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/my-orders">My Orders</a>
            <a href="${pageContext.request.contextPath}/profile">Profile</a>
            <a href="${pageContext.request.contextPath}/logout" class="logout-link">Logout</a>
        </div>
    </div>

    <div class="cart-container">
        <h1 class="cart-title">
            <i class="fa-solid fa-cart-shopping"></i> Shopping Cart
        </h1>

        <c:if test="${param.error == 'stock'}">
            <div class="alert alert-error">
                ⚠️ Some items in your cart are out of stock. Please remove them or reduce quantity.
            </div>
        </c:if>
        <c:if test="${param.error == 'missing_info'}">
            <div class="alert alert-warning">
                ⚠️ Please provide both Phone Number and Address to checkout.
            </div>
        </c:if>

        <c:if test="${empty sessionScope.cart}">
            <div class="empty-cart-view">
                <i class="fa-solid fa-basket-shopping"></i>
                <p>Your cart is empty.</p>
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
                            <td class="product-name">${item.product.name}</td>
                            <td>$${item.product.price}</td>
                            <td>${item.quantity}</td>
                            <td class="total-price">
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
                Grand Total: <span class="grand-total-highlight">
                    <fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2" />
                </span>
            </div>

            <div class="shipping-section">
                <h3>Shipping Information</h3>
                
                <form action="${pageContext.request.contextPath}/order/checkout-cart" method="post">
                    
                    <label class="form-label">Contact Phone:</label>
                    <input type="tel" name="phone" class="form-input" 
                           value="${sessionScope.currentUser.phone}" required 
                           placeholder="090...">

                    <label class="form-label">Delivery Address:</label>
                    <textarea name="address" rows="3" class="form-input" 
                              required placeholder="123 Main St..."></textarea>
                    
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-continue">Continue Shopping</a>
                        <button type="submit" class="btn btn-checkout">Checkout All</button>
                    </div>
                </form>
            </div>
        </c:if>
    </div>

</body>
</html>