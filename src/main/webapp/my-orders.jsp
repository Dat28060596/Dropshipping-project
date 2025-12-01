<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head>
    <title>My Order History</title>
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

        a { text-decoration: none; }

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

        .nav-links a:hover, 
        .nav-links a.active {
            color: #1565c0;
        }

        .logout-link { color: #dc3545 !important; }

        /* ===========================
           3. Main Container
           =========================== */
        .container {
            max-width: 1100px;
            margin: 40px auto;
            background-color: #ffffff;
            padding: 35px;
            border-radius: 10px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.05);
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #e3f2fd;
            padding-bottom: 15px;
            margin-bottom: 25px;
        }

        .page-title {
            font-size: 26px;
            color: #333;
            margin: 0;
        }

        .page-title i { color: #1565c0; margin-right: 10px; }

        .btn-back {
            color: #1565c0;
            font-weight: 500;
            font-size: 15px;
            transition: transform 0.2s;
        }

        .btn-back:hover { text-decoration: underline; transform: translateX(-3px); }

        /* ===========================
           4. Table Styling
           =========================== */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
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
            font-size: 14px;
            text-transform: uppercase;
        }

        td {
            padding: 15px;
            border-bottom: 1px solid #e9ecef;
            vertical-align: top; /* Better for multi-line item lists */
            font-size: 15px;
        }

        /* Date Column Style */
        .date-col {
            color: #666;
            font-size: 14px;
            white-space: nowrap;
        }

        /* Total Price Style */
        .price-col {
            font-weight: 700;
            color: #28a745;
        }

        /* Items Column - subtle list styling */
        .items-col {
            line-height: 1.5;
            color: #444;
        }

        /* ===========================
           5. Status Badges
           =========================== */
        .badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 50px;
            font-size: 11px;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            text-align: center;
            min-width: 80px;
        }

        /* Status Colors */
        .badge-PENDING { 
            background-color: #fff3cd; 
            color: #856404; 
            border: 1px solid #ffeeba; 
        }

        .badge-SHIPPED { 
            background-color: #d4edda; 
            color: #155724; 
            border: 1px solid #c3e6cb; 
        }

        .badge-PAID { 
            background-color: #cce5ff; 
            color: #004085; 
            border: 1px solid #b8daff; 
        }
        
        .badge-CANCELLED {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        /* ===========================
           6. Empty State
           =========================== */
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }
        
        .empty-state i {
            font-size: 50px;
            color: #ddd;
            margin-bottom: 15px;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .main-header { flex-direction: column; gap: 10px; padding: 15px; }
            .container { margin: 15px; padding: 15px; }
            table { display: block; overflow-x: auto; white-space: nowrap; }
        }
    </style>
</head>
<body>

    <div class="main-header">
        <a href="${pageContext.request.contextPath}/home" class="brand">DSP Store</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/home">Home</a>
            <a href="${pageContext.request.contextPath}/my-orders" class="active">My Orders</a>
            <a href="${pageContext.request.contextPath}/profile">Profile</a>
            <a href="${pageContext.request.contextPath}/logout" class="logout-link">Logout</a>
        </div>
    </div>

    <div class="container">
        
        <div class="page-header">
            <h1 class="page-title"><i class="fa-solid fa-clock-rotate-left"></i> Order History</h1>
            <a href="${pageContext.request.contextPath}/home" class="btn-back">
                &larr; Back to Shop
            </a>
        </div>

        <c:if test="${empty orderList}">
            <div class="empty-state">
                <i class="fa-solid fa-box-open"></i>
                <p>You haven't placed any orders yet.</p>
            </div>
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
                            <td style="font-weight: bold; color: #555;">#${o.orderId}</td>
                            
                            <td class="date-col">
                                <i class="fa-regular fa-calendar" style="margin-right:5px; color:#999;"></i>
                                <fmt:formatDate value="${o.orderDate}" pattern="MMM dd, yyyy HH:mm" timeZone="Asia/Ho_Chi_Minh" />
                            </td>
                            
                            <td class="items-col">
                                <c:out value="${o.productName}" escapeXml="false" />
                            </td>
                            
                            <td class="price-col">
                                <c:if test="${o.totalPrice > 0}">
                                    <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2" />
                                </c:if>
                            </td>
                            
                            <td style="max-width: 200px; color: #666;">
                                ${o.shippingAddress}
                            </td>
                            
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