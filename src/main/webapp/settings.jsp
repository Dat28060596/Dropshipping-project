<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="config.jsp" %>
<%@ page import="com.security.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(LINK_LOGIN);
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Settings - Dropshipping Store Management</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        /* Similar styles as other pages */
        :root {
            --primary-color: #6366f1;
            --secondary-color: #8b5cf6;
            --success-color: #10b981;
            --danger-color: #ef4444;
            --warning-color: #f59e0b;
            --info-color: #3b82f6;
            --dark-bg: #0f172a;
            --card-bg: #1e293b;
            --input-bg: #0f172a;
            --border-color: #334155;
            --text-primary: #f1f5f9;
            --text-secondary: #cbd5e1;
            --shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            --shadow-sm: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
            background-attachment: fixed;
            color: var(--text-primary);
            min-height: 100vh;
        }

        .navbar {
            padding: 20px;
            margin-bottom: 20px;
        }

        .navbar a {
            color: var(--text-secondary);
            text-decoration: none;
            font-weight: 500;
        }

        .navbar a:hover { color: var(--primary-color); }

        .settings-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .settings-header {
            text-align: center;
            margin-bottom: 40px;
        }

        .settings-header h1 {
            font-size: 42px;
            font-weight: 700;
            margin-bottom: 10px;
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .settings-form {
            background: var(--card-bg);
            padding: 30px;
            border-radius: 16px;
            box-shadow: var(--shadow);
            border: 1px solid var(--border-color);
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: var(--text-secondary);
            font-weight: 500;
        }

        .form-control {
            width: 100%;
            padding: 12px;
            background: var(--input-bg);
            border: 1px solid var(--border-color);
            border-radius: 8px;
            color: var(--text-primary);
            font-size: 16px;
            transition: border-color 0.3s;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary-color);
        }

        .submit-btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s;
            margin-top: 10px;
        }

        .submit-btn:hover {
            transform: translateY(-2px);
        }

        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            display: none;
        }

        .alert-success {
            background: rgba(16, 185, 129, 0.2);
            color: #a7f3d0;
            border: 1px solid rgba(16, 185, 129, 0.3);
        }

        .alert-error {
            background: rgba(239, 68, 68, 0.2);
            color: #fecaca;
            border: 1px solid rgba(239, 68, 68, 0.3);
        }
    </style>
</head>
<body>
    <div class="navbar">
        <a href="<%= LINK_DASHBOARD %>">← Back to Dashboard</a>
    </div>

    <div class="settings-container">
        <div class="settings-header">
            <h1>Account Settings</h1>
            <p>Update your profile and preferences</p>
        </div>

        <div id="successMessage" class="alert alert-success"></div>
        <div id="errorMessage" class="alert alert-error"></div>

        <form id="settingsForm" class="settings-form">
            <div class="form-group">
                <label for="username">Username (Read-only)</label>
                <input type="text" id="username" class="form-control" value="<%= user.getUsername() %>" disabled 
                       style="opacity: 0.7; cursor: not-allowed;">
            </div>
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" class="form-control" 
                       value="<%= user.getFullName() != null ? user.getFullName() : "" %>" placeholder="Enter your full name" required>
            </div>
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" 
                       value="<%= user.getEmail() %>" placeholder="Enter your email" required>
            </div>
            <div class="form-group">
                <label for="password">New Password (Leave blank to keep current)</label>
                <input type="password" id="password" name="newPassword" class="form-control" 
                       placeholder="Enter new password (min 6 chars)">
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm New Password</label>
                <input type="password" id="confirmPassword" class="form-control" 
                       placeholder="Confirm new password">
            </div>
            
            <button type="submit" class="submit-btn">
                <i class="fas fa-save"></i> Save Changes
            </button>
        </form>
    </div>

    <script>
        const form = document.getElementById('settingsForm');
        const successMsg = document.getElementById('successMessage');
        const errorMsg = document.getElementById('errorMessage');

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            // Reset messages
            successMsg.style.display = 'none';
            errorMsg.style.display = 'none';

            const fullName = document.getElementById('fullName').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            // Validation
            if (password && password.length < 6) {
                showError("Password must be at least 6 characters long.");
                return;
            }

            if (password !== confirmPassword) {
                showError("Passwords do not match.");
                return;
            }

            const data = {
                fullName: fullName,
                email: email,
                newPassword: password
            };

            try {
                const response = await fetch('<%= request.getContextPath() %>/api/settings/update', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                const result = await response.json();

                if (response.ok && result.success) {
                    showSuccess(result.message);
                    // Clear password fields
                    document.getElementById('password').value = '';
                    document.getElementById('confirmPassword').value = '';
                } else {
                    showError(result.message || "Failed to update settings.");
                }
            } catch (error) {
                console.error('Error:', error);
                showError("An unexpected error occurred.");
            }
        });

        function showSuccess(msg) {
            successMsg.textContent = msg;
            successMsg.style.display = 'block';
            setTimeout(() => successMsg.style.display = 'none', 5000);
        }

        function showError(msg) {
            errorMsg.textContent = msg;
            errorMsg.style.display = 'block';
        }
    </script>
</body>
</html>