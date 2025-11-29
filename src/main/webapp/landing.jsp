<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dropshipping Store Management - Home</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            color: #333;
        }

        header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1rem 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        header h1 {
            font-size: 24px;
            font-weight: 700;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin-left: 2rem;
            font-weight: 500;
            transition: opacity 0.3s;
        }

        nav a:hover {
            opacity: 0.8;
        }

        .hero {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 6rem 2rem;
            text-align: center;
        }

        .hero h2 {
            font-size: 48px;
            margin-bottom: 1rem;
        }

        .hero p {
            font-size: 20px;
            margin-bottom: 2rem;
            opacity: 0.9;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: white;
            color: #667eea;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 600;
            margin: 0.5rem;
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }

        .features {
            padding: 4rem 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }

        .features h3 {
            text-align: center;
            font-size: 32px;
            margin-bottom: 3rem;
            color: #333;
        }

        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
        }

        .feature-card {
            background: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: transform 0.3s;
        }

        .feature-card:hover {
            transform: translateY(-5px);
        }

        .feature-card h4 {
            color: #667eea;
            margin: 1rem 0;
            font-size: 20px;
        }

        .feature-card p {
            color: #666;
            font-size: 14px;
        }

        .icon {
            font-size: 40px;
            color: #667eea;
        }

        footer {
            background: #333;
            color: white;
            text-align: center;
            padding: 2rem;
            margin-top: 4rem;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
    </style>
</head>
<body>
    <header>
        <h1>🛒 Dropshipping Store</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/Login">Login</a>
            <a href="<%= request.getContextPath() %>/login.jsp">Register</a>
        </nav>
    </header>

    <section class="hero">
        <h2>Welcome to Dropshipping Management</h2>
        <p>Manage your orders, products, and payments all in one place</p>
        <a href="<%= request.getContextPath() %>/Login" class="btn">Get Started</a>
        <a href="#features" class="btn" style="background: rgba(255, 255, 255, 0.2); color: white;">Learn More</a>
    </section>

    <section id="features" class="features">
        <h3>Our Features</h3>
        <div class="feature-grid">
            <div class="feature-card">
                <div class="icon">📦</div>
                <h4>Order Management</h4>
                <p>Easily manage all your orders in one centralized dashboard</p>
            </div>
            <div class="feature-card">
                <div class="icon">🛍️</div>
                <h4>Product Catalog</h4>
                <p>Manage your product inventory with real-time updates</p>
            </div>
            <div class="feature-card">
                <div class="icon">💳</div>
                <h4>Payment Gateway</h4>
                <p>Secure payments with VN Pay integration</p>
            </div>
            <div class="feature-card">
                <div class="icon">📊</div>
                <h4>Analytics</h4>
                <p>Track sales, revenue, and performance metrics</p>
            </div>
            <div class="feature-card">
                <div class="icon">👥</div>
                <h4>User Management</h4>
                <p>Manage team members with role-based access control</p>
            </div>
            <div class="feature-card">
                <div class="icon">🔒</div>
                <h4>Security</h4>
                <p>Enterprise-grade security with JWT authentication</p>
            </div>
        </div>
    </section>

    <footer>
        <p>&copy; 2024 Dropshipping Store Management System. All rights reserved.</p>
    </footer>
</body>
</html>
