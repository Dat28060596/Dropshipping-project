<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="config.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - Dropshipping</title>

    <!-- Google Font -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <style>
        body {
            font-family: "Poppins", sans-serif;
            margin: 0;
            padding: 0;
            min-height: 100vh;
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            display: flex;
            justify-content: center;
            align-items: center;
        }
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

        .register-wrapper {
            width: 100%;
            max-width: 900px;
            display: flex;
            gap: 30px;
            padding: 20px;
            animation: fadeIn 0.7s ease-in-out;
        }

        .register-card {
            flex: 2;
            background: rgba(255, 255, 255, 0.12);
            backdrop-filter: blur(12px);
            padding: 35px;
            color: #fff;
            border-radius: 18px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .info-section {
            flex: 1;
            color: #fff;
        }

        .info-box {
            background: rgba(255,255,255,0.12);
            backdrop-filter: blur(12px);
            border-radius: 14px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .form-control {
            border-radius: 10px;
            background: rgba(255,255,255,0.15);
            color: #fff;
            border: 1px solid rgba(255,255,255,0.25);
        }

        .form-control::placeholder {
            color: #eee;
        }

        .form-control:focus {
            background: rgba(255,255,255,0.2);
            box-shadow: none;
            border-color: #fff;
            color: #fff;
        }

        .btn-primary {
            padding: 12px;
            border-radius: 12px;
            font-size: 16px;
        }

        .error-message {
            color: #ffb3b3;
            font-size: 14px;
        }

        label {
            font-weight: 500;
            margin-bottom: 6px;
        }

        a {
            color: #fff;
            text-decoration: underline;
        }

        a:hover {
            opacity: 0.8;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(25px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>

</head>

<body>
<div class="navbar">
    <a href="<%= LINK_LANDING %>" class="brand">🛒 Dropshipping Store</a>
    <a href="<%= LINK_LANDING %>" class="nav-link">← Back to Home</a>
</div>
<div class="register-wrapper">

    <!-- Form Section -->
    <div class="register-card">

        <h1 class="mb-1 text-center">Create Account</h1>
        <p class="text-center mb-4">Join our dropshipping management system</p>

        <% 
            String error = request.getParameter("error");
            if (error != null && !error.isEmpty()) {
        %>
            <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <% 
            String success = request.getParameter("success");
            if (success != null && !success.isEmpty()) {
        %>
            <div class="alert alert-success">
                Account created! <a href="<%= request.getContextPath() %>/Login">Login here</a>
            </div>
        <% } %>

        <form id="registerForm" action="<%= API_AUTH_REGISTER %>" method="POST">

            <div class="mb-3">
                <label>Full Name *</label>
                <input type="text" name="fullName" id="fullName" class="form-control" placeholder="John Doe" required>
                <span id="fullNameError" class="error-message"></span>
            </div>

            <div class="mb-3">
                <label>Email *</label>
                <input type="email" name="email" id="email" class="form-control" placeholder="you@example.com" required>
                <span id="emailError" class="error-message"></span>
            </div>

            <div class="mb-3">
                <label>Username *</label>
                <input type="text" name="username" id="username" class="form-control" placeholder="yourusername" minlength="4" maxlength="20" required>
                <span id="usernameError" class="error-message"></span>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label>Password *</label>
                    <input type="password" name="password" id="password" class="form-control" minlength="6" placeholder="Create password" required>
                    <span id="passwordError" class="error-message"></span>
                </div>

                <div class="col-md-6 mb-3">
                    <label>Confirm Password *</label>
                    <input type="password" id="confirmPassword" class="form-control" placeholder="Confirm" required>
                    <span id="confirmPasswordError" class="error-message"></span>
                </div>
            </div>

            <div class="mb-3">
                <label>
                    <input type="checkbox" id="terms" required> I agree to the Terms & Privacy Policy
                </label>
                <span id="termsError" class="error-message d-block"></span>
            </div>

            <button type="submit" class="btn btn-primary w-100">Create Account</button>

            <p class="text-center mt-3">
                Already have an account?  
                <a href="<%= LINK_LOGIN %>">Sign in here</a>
            </p>

        </form>
    </div>



</div>

<script>
    document.getElementById('registerForm').addEventListener('submit', async function(e) {
        e.preventDefault(); // Always prevent default form submission

        // 1. Reset error messages
        document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
        let isValid = true;

        // 2. Get form values
        const fullName = document.getElementById('fullName').value.trim();
        const email = document.getElementById('email').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const terms = document.getElementById('terms').checked;

        // 3. Client-side Validation
        if (fullName.length < 2) {
            document.getElementById('fullNameError').textContent = "Full name must be at least 2 characters";
            isValid = false;
        }

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            document.getElementById('emailError').textContent = "Invalid email format";
            isValid = false;
        }

        const usernamePattern = /^[A-Za-z0-9_]{4,20}$/;
        if (!usernamePattern.test(username)) {
            document.getElementById('usernameError').textContent = "4-20 letters, numbers, underscores only";
            isValid = false;
        }

        if (password.length < 6) {
            document.getElementById('passwordError').textContent = "Password must be 6+ characters";
            isValid = false;
        }

        if (password !== confirmPassword) {
            document.getElementById('confirmPasswordError').textContent = "Passwords do not match";
            isValid = false;
        }

        if (!terms) {
            document.getElementById('termsError').textContent = "You must agree to continue";
            isValid = false;
        }

        if (!isValid) return;

        // 4. Send as JSON (The Fix)
        try {
            const response = await fetch('<%= API_AUTH_REGISTER %>', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    fullName: fullName,
                    email: email,
                    username: username,
                    password: password
                })
            });

            const data = await response.json();

            if (response.ok && data.success) {
                // Success: Redirect to login
                window.location.href = '<%= LINK_LOGIN %>?success=Account created successfully! Please login.';
            } else {
                // Error: Display message from backend
                alert(data.message || "Registration failed");
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        }
    });
</script>
</body>
</html>