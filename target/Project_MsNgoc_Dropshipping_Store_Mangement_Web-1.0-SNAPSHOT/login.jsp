<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login -  Dropshipping Store</title>

    <!-- GOOGLE FONTS -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: "Poppins", sans-serif;
            background: linear-gradient(135deg, #3a7bd5, #3a6073);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        /* ------------------ NAVBAR ------------------ */
        .navbar {
            position: fixed;
            top: 0;
            width: 100%;
            padding: 15px 30px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(10px);
            -webkit-backdrop-filter: blur(10px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            z-index: 50;
        }

        .navbar .brand {
            font-size: 20px;
            font-weight: 600;
            color: white;
            text-decoration: none;
        }

        .navbar .nav-link {
            color: white;
            font-size: 16px;
            text-decoration: none;
            font-weight: 500;
            padding: 6px 14px;
            border-radius: 8px;
            transition: 0.3s;
        }

        .navbar .nav-link:hover {
            background: rgba(255, 255, 255, 0.3);
        }
        /* ------------------------------------------------ */

        .login-container {
            width: 100%;
            max-width: 420px;
            margin-top: 80px; /* push down so navbar doesn't overlap */
        }

        .login-card {
            background: rgba(255, 255, 255, 0.15);
            padding: 40px 35px;
            border-radius: 20px;
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
            animation: fadeIn 0.7s ease;
            color: #fff;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .login-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
            text-align: center;
        }

        .login-header p {
            text-align: center;
            margin-top: 5px;
            opacity: 0.85;
        }

        .form-group { margin-bottom: 18px; }

        .form-group label {
            font-weight: 500;
            margin-bottom: 6px;
            display: block;
        }

        .form-control {
            width: 100%;
            padding: 12px;
            border-radius: 10px;
            border: none;
            outline: none;
            font-size: 15px;
            background: rgba(255,255,255,0.85);
        }

        .form-control:focus {
            background: #fff;
            box-shadow: 0 0 0 2px rgba(0,123,255,0.4);
        }

        .error-message {
            color: #ffdddd;
            font-size: 13px;
            margin-top: 4px;
            display: block;
        }

        .alert {
            padding: 12px;
            border-radius: 10px;
            background: rgba(255, 0, 0, 0.4);
            margin-bottom: 15px;
            text-align: center;
        }

        .btn-primary {
            width: 100%;
            padding: 12px;
            background: #0d6efd;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            color: white;
            transition: 0.3s;
        }

        .btn-primary:hover {
            background: #0b5ed7;
            transform: translateY(-2px);
        }

        .form-footer {
            margin-top: 15px;
            text-align: center;
        }

        .form-footer a {
            color: #fff;
            font-weight: 500;
        }

        .input-error {
            border: 2px solid #ff4d4d !important;
        }
    </style>
</head>
<body>

<!-- NAVIGATION BAR -->
<div class="navbar">
    <a href="<%= request.getContextPath() %>/landing.jsp" class="brand">🛒 Dropshipping Store</a>
    <a href="<%= request.getContextPath() %>/landing.jsp" class="nav-link">← Back to Home</a>
</div>

<div class="login-container">
    <div class="login-card">
        <div class="login-header">
            <h1>Welcome Back</h1>
            <p>Sign in to your dropshipping dashboard</p>
        </div>

        <form id="loginForm" action="<%= request.getContextPath() %>/Login" method="POST">

            <div class="form-group">
                <label for="username">Username *</label>
                <input type="text" id="username" name="username" class="form-control" required />
                <span class="error-message" id="usernameError"></span>
            </div>

            <div class="form-group">
                <label for="password">Password *</label>
                <input type="password" id="password" name="password" class="form-control" required />
                <span class="error-message" id="passwordError"></span>
            </div>

            <% 
                String error = request.getParameter("error");
                if (error != null && !error.isEmpty()) {
            %>
                <div class="alert">
                    <strong>Login Failed:</strong> <%= error %>
                </div>
            <% } %>

            <button type="submit" class="btn-primary">Sign In</button>

            <div class="form-footer">
                <p>Don't have an account?
                    <a href="<%= request.getContextPath() %>/register.jsp">Create one now</a>
                </p>
            </div>

        </form>
    </div>
</div>

<script>
    // Wait for DOM to load
    document.addEventListener("DOMContentLoaded", function() {
        
        const loginForm = document.getElementById('loginForm');
        
        if (loginForm) {
            loginForm.addEventListener('submit', async function(e) {
                // 1. Prevent default submission to handle Token fetch first
                e.preventDefault(); 
                
                const usernameVal = document.getElementById('username').value;
                const passwordVal = document.getElementById('password').value;
                const usernameError = document.getElementById('usernameError');
                const passwordError = document.getElementById('passwordError');

                // Clear previous errors
                if(usernameError) usernameError.textContent = "";
                if(passwordError) passwordError.textContent = "";

                // Basic Validation
                if (usernameVal.trim().length < 3) {
                    if(usernameError) usernameError.textContent = "Username must be at least 3 characters";
                    return;
                }
                if (passwordVal.trim().length < 1) {
                    if(passwordError) passwordError.textContent = "Password is required";
                    return;
                }

                try {
                    // 2. Call the API to get the JWT Token
                    const response = await fetch('<%= request.getContextPath() %>/api/auth/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ 
                            username: usernameVal, 
                            password: passwordVal 
                        })
                    });

                    // 3. If API login succeeds, save the token
                    if (response.ok) {
                        const data = await response.json();
                        if (data.token) {
                            localStorage.setItem('authToken', data.token);
                            console.log("Token saved successfully");
                        }
                    } else {
                        console.warn("API Login failed, proceeding to form submit to handle error display");
                    }
                } catch (error) {
                    console.error("Login API error:", error);
                } finally {
                    // 4. Always submit the form to the Server (LoginController) 
                    // This sets the Session needed for Dashboard access
                    loginForm.submit();
                }
            });
        }
    });
</script>

</body>
</html>
