<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="config.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - Dropshipping</title>

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <style>
        body {
            font-family: "Poppins", sans-serif;
            margin: 0;
            padding: 0;
            min-height: 100vh;
            /* Changed to light grey to make the white card pop */
            background-color: #f8f9fa; 
            color: #333;
            display: flex;
            justify-content: center;
            align-items: center;
            padding-top: 60px; /* Space for fixed navbar */
        }

        /* Navbar Styling */
        .navbar {
            position: fixed;
            top: 0;
            width: 100%;
            padding: 15px 30px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            background: #ffffff; /* White background */
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); /* Subtle shadow */
            z-index: 50;
        }

        .navbar .brand {
            font-size: 20px;
            font-weight: 600;
            color: #0d6efd; /* Blue brand color */
            text-decoration: none;
        }

        .navbar .nav-link {
            color: #555; /* Dark grey text */
            font-size: 15px;
            text-decoration: none;
            font-weight: 500;
            padding: 8px 16px;
            border-radius: 8px;
            transition: 0.3s;
        }

        .navbar .nav-link:hover {
            background: #f1f3f5;
            color: #0d6efd;
        }

        /* Wrapper & Card */
        .register-wrapper {
            width: 100%;
            max-width: 600px; /* Made slightly narrower for a cleaner look */
            padding: 20px;
            animation: fadeIn 0.5s ease-in-out;
        }

        .register-card {
            background: #ffffff; /* Solid White */
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08); /* Soft shadow */
            border: 1px solid #eaeaea;
        }

        /* Form Controls */
        .form-control {
            border-radius: 8px;
            background: #ffffff;
            color: #333;
            border: 1px solid #ced4da; /* Standard grey border */
            padding: 10px 15px;
        }

        .form-control::placeholder {
            color: #aaa;
        }

        .form-control:focus {
            background: #fff;
            border-color: #0d6efd;
            box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.15);
        }

        /* Buttons & Links */
        .btn-primary {
            background-color: #0d6efd;
            border: none;
            padding: 12px;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 500;
            transition: all 0.3s;
        }

        .btn-primary:hover {
            background-color: #0b5ed7;
            transform: translateY(-1px);
        }

        .error-message {
            color: #dc3545; /* Standard Red */
            font-size: 13px;
            margin-top: 4px;
            display: block;
        }

        label {
            font-weight: 500;
            margin-bottom: 6px;
            color: #495057;
            font-size: 14px;
        }

        a {
            color: #0d6efd;
            text-decoration: none;
            font-weight: 500;
        }

        a:hover {
            text-decoration: underline;
        }

        h1 {
            color: #212529;
            font-weight: 600;
        }

        p.text-center {
            color: #6c757d;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
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
    <div class="register-card">

        <h1 class="mb-2 text-center">Create Account</h1>
        <p class="text-center mb-4">Start your dropshipping journey today</p>

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
                <label>Full Name</label>
                <input type="text" name="fullName" id="fullName" class="form-control" placeholder="e.g. John Doe" required>
                <span id="fullNameError" class="error-message"></span>
            </div>

            <div class="mb-3">
                <label>Email Address</label>
                <input type="email" name="email" id="email" class="form-control" placeholder="e.g. john@example.com" required>
                <span id="emailError" class="error-message"></span>
            </div>

            <div class="mb-3">
                <label>Username</label>
                <input type="text" name="username" id="username" class="form-control" placeholder="4-20 characters" minlength="4" maxlength="20" required>
                <span id="usernameError" class="error-message"></span>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label>Password</label>
                    <input type="password" name="password" id="password" class="form-control" minlength="6" placeholder="Min 6 chars" required>
                    <span id="passwordError" class="error-message"></span>
                </div>

                <div class="col-md-6 mb-3">
                    <label>Confirm Password</label>
                    <input type="password" id="confirmPassword" class="form-control" placeholder="Re-enter password" required>
                    <span id="confirmPasswordError" class="error-message"></span>
                </div>
            </div>

            <div class="mb-4">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="terms" required>
                    <label class="form-check-label" for="terms" style="font-weight: 400; font-size: 14px;">
                        I agree to the <a href="#">Terms</a> & <a href="#">Privacy Policy</a>
                    </label>
                </div>
                <span id="termsError" class="error-message"></span>
            </div>

            <button type="submit" class="btn btn-primary w-100">Create Account</button>

            <p class="text-center mt-4 mb-0" style="font-size: 14px;">
                Already have an account?  
                <a href="<%= LINK_LOGIN %>">Sign in here</a>
            </p>

        </form>
    </div>
</div>

<script>
    // Keep your existing script exactly the same, no changes needed for logic
    document.getElementById('registerForm').addEventListener('submit', async function(e) {
        e.preventDefault(); 

        document.querySelectorAll('.error-message').forEach(el => el.textContent = '');
        let isValid = true;

        const fullName = document.getElementById('fullName').value.trim();
        const email = document.getElementById('email').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const terms = document.getElementById('terms').checked;

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

        try {
            const response = await fetch('<%= API_AUTH_REGISTER %>', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    fullName: fullName,
                    email: email,
                    username: username,
                    password: password
                })
            });

            const data = await response.json();

            if (response.ok && data.success) {
                window.location.href = '<%= LINK_LOGIN %>?success=Account created successfully! Please login.';
            } else {
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