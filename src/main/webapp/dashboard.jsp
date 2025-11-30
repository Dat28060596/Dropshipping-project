<%-- 
    Document   : dashboard
    Created on : 19 Nov 2025, 04:25:30
    Author     : Admin
--%>

<%@page import="com.security.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="config.jsp" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Dropshipping Store Management</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        /* ============================================
           DASHBOARD PAGE STYLES - LIGHT THEME
           ============================================ */

        :root {
            /* Brand Colors (Kept same) */
            --primary-color: #6366f1;
            --secondary-color: #8b5cf6;
            --success-color: #10b981;
            --danger-color: #ef4444;
            --warning-color: #f59e0b;
            --info-color: #3b82f6;

            /* Light Theme Variables */
            --main-bg: #f8fafc;        /* Very light blue-grey */
            --card-bg: #ffffff;        /* Pure white */
            --input-bg: #f1f5f9;       /* Light grey */
            --border-color: #e2e8f0;   /* Light border */
            
            /* Text Colors */
            --text-primary: #1e293b;   /* Dark slate for main text */
            --text-secondary: #64748b; /* Medium slate for secondary text */
            
            /* Shadows for light background */
            --shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            --shadow-sm: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
            --shadow-hover: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html, body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background-color: var(--main-bg);
            /* Subtle light gradient */
            background-image: linear-gradient(135deg, #f8fafc 0%, #eff6ff 100%); 
            background-attachment: fixed;
            color: var(--text-primary);
            min-height: 100vh;
        }

        /* ============================================
           DASHBOARD CONTAINER
           ============================================ */

        .dashboard-container {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        /* ============================================
           HEADER
           ============================================ */

        .dashboard-header {
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(8px);
            border-bottom: 1px solid var(--border-color);
            padding: 20px 40px;
            box-shadow: var(--shadow-sm);
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .header-content {
            max-width: 1400px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
        }

        .logo-section h1 {
            font-size: 28px;
            font-weight: 800;
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            letter-spacing: -0.5px;
        }

        .user-section {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .welcome-text {
            font-size: 14px;
            color: var(--text-secondary);
            font-weight: 500;
        }

        .btn-logout {
            padding: 8px 16px;
            background: white;
            color: var(--danger-color);
            border: 1px solid #fee2e2;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: all 0.3s ease;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-logout:hover {
            background: #fef2f2;
            border-color: var(--danger-color);
            transform: translateY(-1px);
        }

        /* ============================================
           MAIN CONTENT
           ============================================ */

        .dashboard-main {
            flex: 1;
            max-width: 1400px;
            width: 100%;
            margin: 0 auto;
            padding: 40px 20px;
        }

        /* ============================================
           PROFILE SECTION
           ============================================ */

        .profile-section {
            margin-bottom: 50px;
        }

        .profile-card {
            background: var(--card-bg);
            border: 1px solid var(--border-color);
            border-radius: 16px;
            padding: 30px;
            box-shadow: var(--shadow);
        }

        .profile-header {
            display: flex;
            gap: 25px;
            align-items: flex-start;
            margin-bottom: 30px;
            padding-bottom: 30px;
            border-bottom: 1px solid var(--border-color);
        }

        .profile-avatar {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            font-weight: 700;
            color: white;
            flex-shrink: 0;
            box-shadow: 0 4px 6px -1px rgba(99, 102, 241, 0.3);
        }

        .profile-info h2 {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 5px;
            color: var(--text-primary);
        }

        .profile-username {
            font-size: 14px;
            color: var(--text-secondary);
            margin-bottom: 8px;
        }

        .profile-email {
            font-size: 14px;
            color: var(--primary-color);
            font-weight: 500;
        }

        .profile-roles h3 {
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: var(--text-secondary);
            margin-bottom: 15px;
            font-weight: 700;
        }

        .roles-list {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .role-badge {
            display: inline-block;
            padding: 6px 14px;
            background: #e0e7ff;
            color: #4338ca;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        /* ============================================
           FEATURES SECTION
           ============================================ */

        .features-section {
            margin-bottom: 50px;
        }

        .features-section h2 {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 25px;
            color: var(--text-primary);
            letter-spacing: -0.5px;
        }

        .features-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
        }

        .feature-card {
            background: var(--card-bg);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 25px;
            text-decoration: none;
            color: inherit;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
            cursor: pointer;
            box-shadow: var(--shadow-sm);
        }

        .feature-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #6366f1, #8b5cf6);
            transform: translateX(-100%);
            transition: transform 0.3s ease;
        }

        .feature-card:hover {
            transform: translateY(-5px);
            border-color: var(--primary-color);
            box-shadow: var(--shadow-hover);
        }

        .feature-card:hover::before {
            transform: translateX(0);
        }

        /* Specific gradient lines for cards */
        .payment-card::before { background: linear-gradient(90deg, #6366f1, #3b82f6); }
        .analytics-card::before { background: linear-gradient(90deg, #10b981, #6366f1); }
        .settings-card::before { background: linear-gradient(90deg, #8b5cf6, #6366f1); }

        .feature-icon {
            font-size: 32px;
            margin-bottom: 20px;
            display: block;
            background: #f8fafc;
            width: 60px;
            height: 60px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 12px;
        }

        .feature-card h3 {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 10px;
            color: var(--text-primary);
        }

        .feature-card p {
            font-size: 14px;
            color: var(--text-secondary);
            line-height: 1.6;
            margin-bottom: 20px;
        }

        .feature-arrow {
            display: inline-block;
            font-size: 18px;
            transition: transform 0.3s ease;
            color: var(--primary-color);
        }

        .feature-card:hover .feature-arrow {
            transform: translateX(5px);
        }

        /* ============================================
           QUICK STATS SECTION
           ============================================ */

        .quick-stats {
            margin-bottom: 50px;
        }

        .quick-stats h2 {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 25px;
            color: var(--text-primary);
            letter-spacing: -0.5px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            gap: 25px;
        }

        .stat-card {
            background: var(--card-bg);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 25px;
            display: flex;
            gap: 20px;
            transition: all 0.3s ease;
            box-shadow: var(--shadow-sm);
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: var(--shadow-hover);
        }

        .stat-icon {
            font-size: 28px;
            flex-shrink: 0;
            width: 56px;
            height: 56px;
            background: #f1f5f9;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .stat-content {
            flex: 1;
        }

        .stat-content h4 {
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            color: var(--text-secondary);
            margin-bottom: 8px;
            font-weight: 600;
        }

        .stat-value {
            font-size: 28px;
            font-weight: 700;
            color: var(--text-primary);
            margin-bottom: 5px;
            letter-spacing: -0.5px;
        }

        .stat-change {
            font-size: 13px;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
        }

        .stat-change.positive { color: var(--success-color); }
        .stat-change.negative { color: var(--danger-color); }

        /* ============================================
           RECENT ACTIVITY SECTION
           ============================================ */

        .recent-activity {
            margin-bottom: 30px;
        }

        .recent-activity h2 {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 25px;
            color: var(--text-primary);
            letter-spacing: -0.5px;
        }

        .activity-list {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .activity-item {
            background: var(--card-bg);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 20px;
            display: flex;
            gap: 20px;
            align-items: flex-start;
            transition: all 0.3s ease;
        }

        .activity-item:hover {
            border-color: var(--primary-color);
            box-shadow: var(--shadow);
            transform: translateX(5px);
        }

        .activity-icon {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            background: #e0e7ff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            color: var(--primary-color);
            flex-shrink: 0;
            font-size: 20px;
        }

        .activity-icon.success {
            background: #d1fae5;
            color: var(--success-color);
        }

        .activity-icon.info {
            background: #dbeafe;
            color: var(--info-color);
        }

        .activity-content h4 {
            font-size: 16px;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: 4px;
        }

        .activity-content p {
            font-size: 14px;
            color: var(--text-secondary);
            margin-bottom: 6px;
        }

        .activity-time {
            font-size: 12px;
            color: #94a3b8;
            font-weight: 500;
        }

        /* ============================================
           FOOTER
           ============================================ */

        .dashboard-footer {
            background: white;
            border-top: 1px solid var(--border-color);
            padding: 25px 40px;
            text-align: center;
            color: var(--text-secondary);
            font-size: 14px;
            margin-top: auto;
        }

        .dashboard-footer a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
        }

        .dashboard-footer a:hover {
            text-decoration: underline;
        }

        /* ============================================
           RESPONSIVE DESIGN (Kept from original)
           ============================================ */

        @media (max-width: 1024px) {
            .dashboard-main { padding: 30px 15px; }
            .features-grid { grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); }
            .profile-header { flex-direction: column; }
            .profile-avatar { align-self: center; }
            .profile-info { text-align: center; }
        }

        @media (max-width: 768px) {
            .dashboard-header { padding: 15px 20px; }
            .header-content { flex-direction: column; align-items: flex-start; }
            .user-section { width: 100%; justify-content: space-between; margin-top: 15px; }
            .dashboard-main { padding: 20px 15px; }
            .logo-section h1 { font-size: 20px; }
            .features-grid { grid-template-columns: 1fr; }
            .stats-grid { grid-template-columns: repeat(2, 1fr); }
            .profile-card { padding: 20px; }
            .profile-header { margin-bottom: 20px; padding-bottom: 20px; }
        }

        @media (max-width: 480px) {
            .dashboard-header { padding: 10px 15px; }
            .stats-grid { grid-template-columns: 1fr; }
            .profile-avatar { width: 60px; height: 60px; font-size: 24px; }
            .profile-info h2 { font-size: 18px; }
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <!-- Header -->
        <header class="dashboard-header">
            <div class="header-content">
                <div class="logo-section">
                    <h1>🛍️ Dropshipping Store Management</h1>
                </div>
                <div class="user-section">
                    <span class="welcome-text">Welcome, <%= user.getFullName() %>!</span>
                    <a href="<%= LINK_LOGOUT %>" class="btn-logout">Logout</a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="dashboard-main">
            <!-- User Profile Card -->
            <section class="profile-section">
                <div class="profile-card">
                    <div class="profile-header">
                        <div class="profile-avatar">
                            <%= user.getFullName().charAt(0) %>
                        </div>
                        <div class="profile-info">
                            <h2><%= user.getFullName() %></h2>
                            <p class="profile-username">@<%= user.getUsername() %></p>
                            <p class="profile-email">📧 <%= user.getEmail() %></p>
                        </div>
                    </div>
                    <div class="profile-roles">
                        <h3>Assigned Roles:</h3>
                        <div class="roles-list">
                            <% for (String role : user.getRoles()) { %>
                                <span class="role-badge"><%= role %></span>
                            <% } %>
                        </div>
                    </div>
                </div>
            </section>

            <!-- Features Section -->
            <section class="features-section">
                <h2>Core Features</h2>
                <div class="features-grid">
                    
                    <!-- Payment Processing -->
                    <a href="<%= LINK_PAYMENT %>" class="feature-card payment-card">
                        <div class="feature-icon">🔒</div>
                        <h3>Secure Payment</h3>
                        <p>Process transactions with multiple payment gateways (VN Pay, Stripe, PayPal, Momo)</p>
                        <div class="feature-arrow">→</div>
                    </a>

                    <!-- Analytics & Reports -->
                    <a href="<%= LINK_ANALYTICS %>" class="feature-card analytics-card">
                        <div class="feature-icon">📊</div>
                        <h3>Analytics & Reports</h3>
                        <p>Sales reports, inventory trends, customer insights with PDF export</p>
                        <div class="feature-arrow">→</div>
                    </a>

                    <!-- Settings -->
                    <a href="<%= LINK_SETTINGS %>" class="feature-card settings-card">
                        <div class="feature-icon">⚙️</div>
                        <h3>Settings</h3>
                        <p>Configure store settings and preferences</p>
                        <div class="feature-arrow">→</div>
                    </a>
                        
                     <!-- Payment Processing -->
                    <a href="${pageContext.request.contextPath}/home_servlet" class="feature-card payment-card">
                        <div class="feature-icon">🛒</div>
                        <h3>Product</h3>
                        <p>Cart</p>
                        <div class="feature-arrow">→</div>
                    </a>
                </div>
            </section>

            <!-- Quick Stats -->
            <section class="quick-stats">
                <h2>Quick Statistics</h2>
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">💰</div>
                        <div class="stat-content">
                            <h4>Total Revenue</h4>
                            <p class="stat-value">₫ 125,500,000</p>
                            <span class="stat-change positive">↑ 23.5% this month</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">📦</div>
                        <div class="stat-content">
                            <h4>Total Orders</h4>
                            <p class="stat-value">1,542</p>
                            <span class="stat-change positive">↑ 12.3% this month</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">👥</div>
                        <div class="stat-content">
                            <h4>Total Customers</h4>
                            <p class="stat-value">2,904</p>
                            <span class="stat-change positive">↑ 8.7% this month</span>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">📊</div>
                        <div class="stat-content">
                            <h4>Conversion Rate</h4>
                            <p class="stat-value">3.45%</p>
                            <span class="stat-change positive">↑ 0.5% this month</span>
                        </div>
                    </div>
                </div>
            </section>

            <!-- Recent Activity -->
            <section class="recent-activity">
                <h2>Recent Activity</h2>
                <div class="activity-list">
                    <div class="activity-item">
                        <div class="activity-icon success">✓</div>
                        <div class="activity-content">
                            <h4>Payment Processed</h4>
                            <p>Order #TXN-001 - ₫ 5,000,000</p>
                            <span class="activity-time">2 hours ago</span>
                        </div>
                    </div>
                    <div class="activity-item">
                        <div class="activity-icon">📦</div>
                        <div class="activity-content">
                            <h4>New Order Received</h4>
                            <p>Order #ORD-1542 from John Doe</p>
                            <span class="activity-time">4 hours ago</span>
                        </div>
                    </div>
                    <div class="activity-item">
                        <div class="activity-icon info">ℹ</div>
                        <div class="activity-content">
                            <h4>Product Stock Updated</h4>
                            <p>Wireless Earbuds Pro - 50 units added</p>
                            <span class="activity-time">1 day ago</span>
                        </div>
                    </div>
                </div>
            </section>
        </main>

        <!-- Footer -->
        <footer class="dashboard-footer">
            <p>&copy; 2025 Dropshipping Store Management. All rights reserved.</p>
        </footer>
    </div>
</body>
</html>