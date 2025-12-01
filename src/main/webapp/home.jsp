<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>DSP Dropshipping Store - Product Catalog</title>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        /* ==========================
           GLOBAL
        ========================== */
        body {
            font-family: "Segoe UI", sans-serif;
            background: #f5f7fa;
            margin: 0;
            padding: 0;
            color: #333;
        }

        a {
            text-decoration: none;
            color: inherit;
        }

        img {
            user-select: none;
        }

        /* ==========================
           HEADER
        ========================== */
        .main-header {
            background: #1565c0;
            padding: 15px 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
        }

        .main-header .brand {
            font-size: 20px;
            font-weight: bold;
            color: white;
        }

        .nav-links a {
            margin-left: 15px;
            color: white;
            font-size: 14px;
            transition: opacity .2s;
        }

        .nav-links a:hover {
            opacity: .8;
        }

        .cart-badge {
            background: #f44336;
            color: white;
            font-size: 11px;
            padding: 2px 6px;
            border-radius: 12px;
            margin-left: 5px;
        }

        /* ==========================
           SUCCESS MESSAGE
        ========================== */
        .alert-success {
            background: #e8f5e9;
            color: #1b5e20;
            padding: 12px;
            font-weight: 500;
            border-left: 4px solid #2e7d32;
            margin: 15px auto;
            width: 95%;
            border-radius: 6px;
        }

        /* ==========================
           PRODUCT GRID
        ========================== */
        .product-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(270px, 1fr));
            gap: 25px;
            padding: 30px;
        }

        .product-card {
            background: white;
            border: 1px solid #e3e7ec;
            border-radius: 10px;
            padding: 15px;
            transition: .25s ease;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .product-card:hover {
            box-shadow: 0 6px 18px rgba(0,0,0,.08);
        }

        .product-card img {
            width: 100%;
            height: auto;
            object-fit: cover;
            border-radius: 8px;
        }

        .product-name {
            font-size: 17px;
            font-weight: 600;
            margin-top: 10px;
            text-align: center;
        }

        .product-category {
            margin-top: 5px;
            font-size: 13px;
            color: #777;
        }

        .product-description {
            text-align: center;
            font-size: 13px;
            margin: 10px 0;
            color: #555;
        }

        .product-price {
            font-size: 18px;
            font-weight: 700;
            margin: 8px 0;
        }

        /* ==========================
           TAGS
        ========================== */
        .product-tags {
            margin: 10px 0;
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
        }

        .product-tag {
            background: #e3f2fd;
            color: #1565c0;
            padding: 5px 10px;
            font-size: 11px;
            margin: 3px;
            border-radius: 10px;
        }

        /* ==========================
           STOCK BADGES
        ========================== */
        .stock-badge {
            padding: 4px 10px;
            font-size: 12px;
            border-radius: 4px;
            font-weight: 600;
        }

        .stock-badge.in-stock {
            background: #d4edda;
            color: #155724;
        }

        .stock-badge.low-stock {
            background: #fff3cd;
            color: #856404;
        }

        .stock-badge.out-of-stock {
            background: #f8d7da;
            color: #721c24;
        }

        /* ==========================
           VARIANT SELECT
        ========================== */
        .variant-selector {
            padding: 10px;
            border-radius: 6px;
            background: #f1f5f9;
            margin-top: 8px;
            text-align: center;
        }

        .variant-option {
            display: inline-block;
            padding: 8px 12px;
            margin: 6px;
            border: 1.8px solid #ced4da;
            border-radius: 6px;
            transition: .2s;
            cursor: pointer;
            font-size: 13px;
            white-space: nowrap;
        }

        .variant-option:hover {
            border-color: #1565c0;
            background: #e8f3ff;
        }

        .variant-option.selected {
            background: #1565c0;
            color: white;
            border-color: #1565c0;
        }

        .variant-option.out-of-stock {
            opacity: 0.5;
            color: #aaa;
            border-color: #ccc;
            cursor: not-allowed;
            text-decoration: line-through;
        }

        /* ==========================
           BUTTONS
        ========================== */
        button.btn-primary {
            background: #1565c0;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            transition: .2s;
        }

        button.btn-primary:hover {
            background: #0d47a1;
        }

        button.btn-primary:disabled {
            opacity: 0.4;
            cursor: not-allowed;
        }

        /* ==========================
           DETAILS LINK
        ========================== */
        .product-card a {
            color: #1565c0;
            margin-top: 10px;
            font-size: 14px;
            text-align: center;
        }

        .product-card a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

<!-- ==============================
     HEADER NAVBAR (no login/logout/profile)
================================= -->
<div class="main-header">
    <a href="${pageContext.request.contextPath}/home_servlet" class="brand">
        <i class="fa-solid fa-store"></i> DSP Store
    </a>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/cart">
            <i class="fa-solid fa-cart-shopping"></i> Cart
            <c:if test="${sessionScope.cartItemCount > 0}">
                <span class="cart-badge">${sessionScope.cartItemCount}</span>
            </c:if>
        </a>
    </div>
</div>

<!-- ==============================
     ALERT
================================= -->
<c:if test="${param.msg == 'added'}">
    <div class="alert-success">
        ✅ Item added to cart!
        <a href="${pageContext.request.contextPath}/cart">View Cart</a>
    </div>
</c:if>

<!-- ==============================
     PRODUCT LIST
================================= -->
<div class="product-container">

    <c:forEach var="p" items="${productList}">
        <div class="product-card">

            <!-- Image: product or fallback -->
            <c:choose>
    <c:when test="${not empty p.imageUrl}">
      
    </c:when>
    <c:otherwise>
        <img src="https://cdn.freecodecamp.org/curriculum/cat-photo-app/relaxing-cat.jpg"
             alt="default image">
    </c:otherwise>
</c:choose>


            <div class="product-name">Cat</div>

            <div class="product-category">
                <i class="fa-solid fa-tag"></i> Pet
            </div>

            <!-- Tags -->
            <c:if test="${not empty p.tags}">
                <div class="product-tags">
                    <c:forEach var="tag" items="${p.tags}">
                        <span class="product-tag">${tag}</span>
                    </c:forEach>
                </div>
            </c:if>

            <!-- Description -->
            <div class="product-description">${p.description}</div>

            <c:choose>
                <c:when test="${p.hasVariants && not empty p.variants}">
                    <div class="product-price">
                        From 100$<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/>
                    </div>

                    <div class="variant-selector">
                        <strong>Select Options:</strong>
                        <div id="variants-${p.id}">
                            <c:forEach var="variant" items="${p.variants}">
                                <div class="variant-option ${variant.stockQuantity == 0 ? 'out-of-stock' : ''}"
                                     data-variant-id="${variant.id}"
                                     data-product-id="${p.id}"
                                     data-price="${variant.price}"
                                     data-stock="${variant.stockQuantity}"
                                     onclick="selectVariant(this)">
                                    ${variant.displayName}
                                    <br>
                                    <small>
                                        $<fmt:formatNumber value="${variant.price}" pattern="#,##0.00"/>
                                    </small>

                                    <c:if test="${variant.stockQuantity == 0}">
                                        <br><small>(Out of Stock)</small>
                                    </c:if>

                                    <c:if test="${variant.lowStock && variant.stockQuantity > 0}">
                                        <br><strong>Only ${variant.stockQuantity} left!</strong>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <form id="cartForm-${p.id}"
                          action="${pageContext.request.contextPath}/cart.jsp" method="post"
                          style="margin-top: 15px;">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${p.id}">
                        <input type="hidden" name="variantId" id="selectedVariantId-${p.id}">
                        <div style="display:flex;gap:10px;justify-content:center;">
                            <input type="number" id="quantity-${p.id}"
                                   name="quantity" value="1" min="1" max="99"
                                   style="width:60px;padding:5px;text-align:center;">
                            <button type="submit" id="addBtn-${p.id}"
                                    class="btn-primary" disabled>
                                <i class="fa-solid fa-cart-plus"></i> Add
                            </button>
                        </div>
                    </form>
                </c:when>

                <c:otherwise>
                    <div class="product-price">
                        $<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/>
                    </div>

                    <c:choose>
                        <c:when test="${p.stock > 10}">
                            <span class="stock-badge in-stock">In Stock</span>
                        </c:when>
                        <c:when test="${p.stock > 0 && p.stock <= 10}">
                            <span class="stock-badge low-stock">Only ${p.stock} left!</span>
                        </c:when>
                        <c:otherwise>
                            <span class="stock-badge out-of-stock">Out of Stock</span>
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${p.stock > 0}">
                        <form action="${pageContext.request.contextPath}/cart.jsp" method="post"
                              style="margin-top: 15px;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${p.id}">
                            <div style="display:flex;gap:10px;justify-content:center;">
                                <input type="number" name="quantity"
                                       value="1" min="1" max="${p.stock}"
                                       style="width:60px;padding:5px;text-align:center;">
                                <button type="submit" class="btn-primary">
                                    <i class="fa-solid fa-cart-plus"></i> Add
                                </button>
                            </div>
                        </form>
                    </c:if>
                </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/product-details?id=${p.id}">
                <i class="fa-solid fa-circle-info"></i> View Details
            </a>
        </div>
    </c:forEach>
</div>

<script>
    function selectVariant(el) {
        if (el.classList.contains("out-of-stock")) return;

        const pid = el.dataset.productId;
        const box = el.parentElement;

        box.querySelectorAll(".variant-option").forEach(v =>
            v.classList.remove("selected")
        );
        el.classList.add("selected");

        document.getElementById("selectedVariantId-" + pid).value =
            el.dataset.variantId;

        const qty = document.getElementById("quantity-" + pid);
        qty.max = el.dataset.stock;
        qty.value = 1;

        document.getElementById("addBtn-" + pid).disabled = false;
    }
</script>

</body>
</html>
