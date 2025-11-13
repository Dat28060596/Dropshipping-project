# React Frontend Integration Guide

## Overview

This guide shows how to integrate your Spring Boot RBAC system with a React frontend.

## Installation

### 1. Install Dependencies

```bash
cd my-react-app
npm install axios react-router-dom
```

## Setup

### 1. Create Auth Service

**src/services/authService.js**
```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api/auth';

const authService = {
  // Register new user
  register: async (userData) => {
    const response = await axios.post(`${API_BASE_URL}/register`, userData);
    return response.data;
  },

  // Login and get token
  login: async (username, password) => {
    const response = await axios.post(`${API_BASE_URL}/login`, {
      username,
      password
    });
    
    // Store token in localStorage
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    
    return response.data;
  },

  // Get current logged-in user
  getCurrentUser: async () => {
    const token = localStorage.getItem('token');
    if (!token) return null;
    
    try {
      const response = await axios.get(`${API_BASE_URL}/me`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      return null;
    }
  },

  // Logout
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  // Get stored token
  getToken: () => localStorage.getItem('token'),

  // Get stored user
  getUser: () => JSON.parse(localStorage.getItem('user') || 'null'),

  // Check if user is authenticated
  isAuthenticated: () => !!localStorage.getItem('token')
};

export default authService;
```

### 2. Create API Service

**src/services/apiService.js**
```javascript
import axios from 'axios';
import authService from './authService';

const API_BASE_URL = 'http://localhost:8081/api';

// Create axios instance with default headers
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add token to every request
apiClient.interceptors.request.use((config) => {
  const token = authService.getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

// Handle response errors
apiClient.interceptors.response.use((response) => {
  return response;
}, (error) => {
  // If 401, user is unauthorized - logout
  if (error.response?.status === 401) {
    authService.logout();
    window.location.href = '/login';
  }
  return Promise.reject(error);
});

export default apiClient;
```

## Components

### 1. Login Component

**src/components/Login.jsx**
```jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await authService.login(formData.username, formData.password);
      console.log('Login successful', response);
      
      // Redirect to dashboard based on role
      if (response.roles.includes('ADMIN')) {
        navigate('/admin/dashboard');
      } else if (response.roles.includes('STAFF')) {
        navigate('/staff/dashboard');
      } else if (response.roles.includes('SUPPLIER')) {
        navigate('/supplier/dashboard');
      } else {
        navigate('/user/dashboard');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Login</h2>
        
        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Enter your username"
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Enter your password"
              required
            />
          </div>

          <button type="submit" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <p className="signup-link">
          Don't have account? <a href="/register">Register here</a>
        </p>

        <div className="demo-users">
          <p>Demo Users:</p>
          <small>admin / admin123</small>
          <small>staff / staff123</small>
          <small>supplier / supplier123</small>
        </div>
      </div>
    </div>
  );
};

export default Login;
```

**src/components/Login.css**
```css
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 400px;
}

.login-card h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 600;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
  box-sizing: border-box;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

button[type="submit"] {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

button[type="submit"]:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
}

button[type="submit"]:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.alert {
  padding: 12px;
  border-radius: 5px;
  margin-bottom: 20px;
  font-size: 14px;
}

.alert-error {
  background-color: #fee;
  border: 1px solid #fcc;
  color: #c33;
}

.signup-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.signup-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
}

.demo-users {
  margin-top: 30px;
  padding: 15px;
  background: #f5f5f5;
  border-radius: 5px;
}

.demo-users p {
  margin: 0 0 10px 0;
  font-weight: 600;
  color: #333;
}

.demo-users small {
  display: block;
  color: #666;
  margin-bottom: 5px;
  font-family: monospace;
}
```

### 2. Register Component

**src/components/Register.jsx**
```jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import './Register.css';

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    fullName: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validate
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    setLoading(true);

    try {
      await authService.register({
        username: formData.username,
        email: formData.email,
        fullName: formData.fullName,
        password: formData.password
      });

      // Show success and redirect to login
      alert('Registration successful! Please login.');
      navigate('/login');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h2>Create Account</h2>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Full Name</label>
            <input
              type="text"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              placeholder="Your full name"
              required
            />
          </div>

          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Choose a username"
              required
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="your@email.com"
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Min 6 characters"
              required
            />
          </div>

          <div className="form-group">
            <label>Confirm Password</label>
            <input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="Confirm password"
              required
            />
          </div>

          <button type="submit" disabled={loading}>
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>

        <p className="login-link">
          Already have account? <a href="/login">Login here</a>
        </p>
      </div>
    </div>
  );
};

export default Register;
```

### 3. Protected Route Component

**src/components/ProtectedRoute.jsx**
```jsx
import { Navigate } from 'react-router-dom';
import authService from '../services/authService';

const ProtectedRoute = ({ children, requiredRole = null }) => {
  const isAuthenticated = authService.isAuthenticated();
  const user = authService.getUser();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && user && !user.roles.includes(requiredRole)) {
    return <Navigate to="/unauthorized" />;
  }

  return children;
};

export default ProtectedRoute;
```

### 4. Admin Dashboard

**src/components/AdminDashboard.jsx**
```jsx
import React, { useState, useEffect } from 'react';
import apiClient from '../services/apiService';
import authService from '../services/authService';

const AdminDashboard = () => {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const user = authService.getUser();

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    try {
      const response = await apiClient.get('/admin/dashboard');
      setDashboard(response.data);
    } catch (err) {
      setError('Failed to load dashboard');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div className="alert alert-error">{error}</div>;

  return (
    <div className="dashboard">
      <h1>Admin Dashboard</h1>
      <p>Welcome, {user?.fullName}!</p>

      {dashboard && (
        <div className="features">
          <h2>Features Available:</h2>
          <ul>
            {dashboard.features.map((feature, index) => (
              <li key={index}>{feature}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
```

### 5. Navigation Component

**src/components/Navbar.jsx**
```jsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import './Navbar.css';

const Navbar = () => {
  const navigate = useNavigate();
  const user = authService.getUser();
  const isAuthenticated = authService.isAuthenticated();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/">MyApp</Link>
      </div>

      <div className="navbar-menu">
        {isAuthenticated ? (
          <>
            <span className="navbar-user">
              {user?.fullName} ({user?.roles.join(', ')})
            </span>
            <button onClick={handleLogout} className="logout-btn">
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
```

## App.js Setup

```jsx
import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Register from './components/Register';
import ProtectedRoute from './components/ProtectedRoute';
import AdminDashboard from './components/AdminDashboard';
import StaffDashboard from './components/StaffDashboard';
import SupplierDashboard from './components/SupplierDashboard';
import authService from './services/authService';
import './App.css';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is authenticated on mount
    const user = authService.getCurrentUser();
    setIsAuthenticated(authService.isAuthenticated());
    setLoading(false);
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected Routes */}
        <Route
          path="/admin/dashboard"
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/staff/dashboard"
          element={
            <ProtectedRoute requiredRole="STAFF">
              <StaffDashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/supplier/dashboard"
          element={
            <ProtectedRoute requiredRole="SUPPLIER">
              <SupplierDashboard />
            </ProtectedRoute>
          }
        />

        {/* Redirect */}
        <Route path="/" element={<Navigate to={isAuthenticated ? "/admin/dashboard" : "/login"} />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
```

## Testing in React

### 1. Login Test
```bash
# Start your React app
npm start

# Go to http://localhost:3000/login
# Use test credentials: admin / admin123
# You should be redirected to admin dashboard
```

### 2. Check Token
```javascript
// In browser console
console.log(localStorage.getItem('token'));
console.log(JSON.parse(localStorage.getItem('user')));
```

### 3. Test Protected Routes
```javascript
// Try accessing /admin/dashboard with staff token
// Should redirect to /login
```

## Error Handling

### Network Error
```javascript
try {
  const response = await apiClient.get('/admin/dashboard');
} catch (error) {
  if (error.response?.status === 401) {
    // Token expired or invalid
    authService.logout();
  } else if (error.response?.status === 403) {
    // User doesn't have permission
    navigate('/unauthorized');
  } else if (error.response?.status === 500) {
    // Server error
    setError('Server error');
  }
}
```

## Best Practices

1. **Never store sensitive data in localStorage except token**
   ```javascript
   // Good
   localStorage.setItem('token', token);
   
   // Bad
   localStorage.setItem('password', password);
   localStorage.setItem('creditCard', cardNumber);
   ```

2. **Always use HTTPS in production**
   - Prevents token theft
   - Encrypts all communication

3. **Set appropriate token expiration**
   ```javascript
   // In backend
   APP_JWT_EXPIRATION_MS=900000  // 15 minutes
   ```

4. **Handle logout properly**
   ```javascript
   const handleLogout = () => {
     authService.logout();  // Clear localStorage
     navigate('/login');     // Redirect to login
     // Optionally: Call backend logout endpoint
   };
   ```

5. **Refresh token on expiration**
   - Implement refresh token mechanism
   - Auto-refresh before expiration

## Next Steps

1. Add Staff Dashboard
2. Add Supplier Dashboard
3. Implement role-specific UI
4. Add user profile management
5. Implement refresh tokens
6. Add logout confirmation
7. Add error boundaries
8. Implement loading states

---

**Happy Coding!** 🚀
