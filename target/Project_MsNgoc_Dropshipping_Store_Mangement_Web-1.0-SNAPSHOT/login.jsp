<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Dropshipping Store</title>

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: "Poppins", sans-serif;
            /* Changed to a very light off-white/gray for contrast with the white card */
            background-color: #f4f7f6;
            color: #333; /* Dark text for light background */
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
            /* White background with a subtle border */
            background: #ffffff;
            border-bottom: 1px solid #e0e0e0;
            box-shadow: 0 2px 4px rgba(0,0,0,0.02);
            z-index: 50;
            box-sizing: border-box; /* Ensures padding doesn't break width */
        }

        .navbar .brand {
            font-size: 20px;
            font-weight: 600;
            color: #333; /* Dark text */
            text-decoration: none;
        }

        .navbar .nav-link {
            color: #555; /* Darker gray for links */
            font-size: 16px;
            text-decoration: none;
            font-weight: 500;
            padding: 8px 16px;
            border-radius: 8px;
            transition: 0.3s;
        }

        .navbar .nav-link:hover {
            background: #f0f0f0; /* Light gray hover */
            color: #0d6efd;
        }
        /* ------------------------------------------------ */

        .login-container {
            width: 100%;
            max-width: 420px;
            margin-top: 60px; /* push down so navbar doesn't overlap */
            padding: 20px;
        }

        .login-card {
            /* Pure white background */
            background: #ffffff;
            padding: 40px 35px;
            border-radius: 20px;
            /* Softer, modern shadow */
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
            animation: fadeIn 0.7s ease;
            color: #333;
            border: 1px solid #f1f1f1;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .login-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 700;
            text-align: center;
            color: #1a1a1a;
        }

        .login-header p {
            text-align: center;
            margin-top: 8px;
            color: #666; /* Muted gray text */
            font-size: 14px;
        }

        .form-group { margin-bottom: 20px; }

        .form-group label {
            font-weight: 500;
            margin-bottom: 8px;
            display: block;
            color: #444;
            font-size: 14px;
        }

        .form-control {
            width: 100%;
            padding: 12px;
            border-radius: 10px;
            /* Added border for visibility on white background */
            border: 1px solid #ddd;
            outline: none;
            font-size: 15px;
            background: #fff;
            box-sizing: border-box; /* Fixes padding issues */
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        .form-control:focus {
            border-color: #0d6efd;
            box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.15);
        }

        .error-message {
            color: #dc3545; /* Red for errors */
            font-size: 13px;
            margin-top: 5px;
            display: block;
        }

        .alert {
            padding: 12px;
            border-radius: 10px;
            background: #ffe5e5; /* Light red background */
            color: #d63384;
            border: 1px solid #ffcccc;
            margin-bottom: 20px;
            text-align: center;
            font-size: 14px;
        }

        .btn-primary {
            width: 100%;
            padding: 14px;
            background: #0d6efd;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            color: white;
            transition: 0.3s;
            box-shadow: 0 4px 12px rgba(13, 110, 253, 0.2);
        }

        .btn-primary:hover {
            background: #0b5ed7;
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(13, 110, 253, 0.3);
        }

        .form-footer {
            margin-top: 25px;
            text-align: center;
            font-size: 14px;
            color: #666;
        }

        .form-footer a {
            color: #0d6efd; /* Blue link */
            font-weight: 600;
            text-decoration: none;
        }
        
        .form-footer a:hover {
            text-decoration: underline;
        }

        .input-error {
            border: 1px solid #dc3545 !important;
            background-color: #fff8f8;
        }
    </style>
</head>
<body>

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
                <input type="text" id="username" name="username" class="form-control" placeholder="Enter your username" required />
                <span class="error-message" id="usernameError"></span>
            </div>

            <div class="form-group">
                <label for="password">Password *</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required />
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
                
                const usernameInput = document.getElementById('username');
                const passwordInput = document.getElementById('password');
                const usernameVal = usernameInput.value;
                const passwordVal = passwordInput.value;
                const usernameError = document.getElementById('usernameError');
                const passwordError = document.getElementById('passwordError');

                // Clear previous errors
                if(usernameError) usernameError.textContent = "";
                if(passwordError) passwordError.textContent = "";
                usernameInput.classList.remove('input-error');
                passwordInput.classList.remove('input-error');

                // Basic Validation
                if (usernameVal.trim().length < 3) {
                    if(usernameError) usernameError.textContent = "Username must be at least 3 characters";
                    usernameInput.classList.add('input-error');
                    return;
                }
                if (passwordVal.trim().length < 1) {
                    if(passwordError) passwordError.textContent = "Password is required";
                    passwordInput.classList.add('input-error');
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