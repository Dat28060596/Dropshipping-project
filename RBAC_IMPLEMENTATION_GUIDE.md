# Role-Based Access Control (RBAC) Implementation Guide

## Overview

Your Spring Boot application now has a complete **Role-Based Access Control (RBAC)** system with JWT authentication. This guide explains the implementation and how to use it.

## Architecture

```
┌─────────────┐
│   React     │  (Client)
│  Frontend   │
└──────┬──────┘
       │ POST /api/auth/login
       │ Authorization: Bearer {token}
       ▼
┌─────────────────────────────────────┐
│  SecurityConfig (Filter Chain)      │
│  - CSRF Disabled                    │
│  - JWT Authentication Filter        │
│  - Session Stateless (Stateless)    │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────┐
│  JwtAuthenticationFilter            │
│  - Extract token from header        │
│  - Validate token signature         │
│  - Load user authorities            │
│  - Set authentication context       │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────┐
│  @PreAuthorize Annotations          │
│  - Method-level role checks         │
│  - Deny access if unauthorized      │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────┐
│  Protected Endpoints                │
│  - AdminController                  │
│  - StaffController                  │
│  - SupplierController               │
└─────────────────────────────────────┘
```

## Files Created

### 1. Security Files

| File | Purpose |
|------|---------|
| `JwtUtil.java` | Generate and validate JWT tokens |
| `JwtAuthenticationFilter.java` | Extract and process JWT from requests |
| `SecurityConfig.java` | Configure Spring Security with JWT |
| `CustomUserDetailsService.java` | Load user details from database |

### 2. Model Files

| File | Purpose |
|------|---------|
| `User.java` | JPA entity for users (implements UserDetails) |
| `Role.java` | Enum with ADMIN, STAFF, SUPPLIER, USER roles |
| `UserRepository.java` | Database operations for users |

### 3. DTO Files

| File | Purpose |
|------|---------|
| `AuthRequest.java` | Login request (username, password) |
| `AuthResponse.java` | Login response (token, user info) |
| `RegisterRequest.java` | Registration request |

### 4. Controller Files

| File | Purpose |
|------|---------|
| `AuthController.java` | Register, login, get current user |
| `AdminController.java` | Admin-only endpoints |
| `StaffController.java` | Admin + Staff endpoints |
| `SupplierController.java` | Admin + Supplier endpoints |

## Role-Based Permissions

### ADMIN
- Full system access
- Manage users
- Assign roles
- View all data
- Endpoints: `/api/admin/**`

### STAFF
- Manage orders
- Update order status
- View analytics
- Generate reports
- Endpoints: `/api/staff/**`

### SUPPLIER
- Manage own products
- View orders
- Track shipments
- Update product info
- Endpoints: `/api/supplier/**`

### USER
- Basic access
- View own profile
- Make purchases
- View order history

## Setup Instructions

### Step 1: Update Environment Variables

Edit `.env` file with your Supabase credentials:

```env
DB_CONNECTION_URL=postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres
APP_JWT_SECRET=your_very_long_random_secret_key_with_at_least_32_characters
APP_JWT_EXPIRATION_MS=3600000
```

### Step 2: Start the Application

```bash
# Set environment variable and run
$env:DB_CONNECTION_URL="postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres"
$env:APP_JWT_SECRET="your_secret_key_here"

mvn spring-boot:run
```

### Step 3: Verify Database Seed

The application automatically creates these test users:

| Username | Password | Email | Role |
|----------|----------|-------|------|
| admin | admin123 | admin@example.com | ADMIN |
| staff | staff123 | staff@example.com | STAFF |
| supplier | supplier123 | supplier@example.com | SUPPLIER |
| user | user123 | user@example.com | USER |

## API Endpoints

### Authentication Endpoints

#### 1. Register
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@example.com",
  "fullName": "New User",
  "password": "password123"
}

Response (201):
{
  "message": "User registered successfully"
}
```

#### 2. Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response (200):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "email": "admin@example.com",
  "fullName": "Admin User",
  "roles": ["ADMIN"]
}
```

#### 3. Get Current User
```bash
GET /api/auth/me
Authorization: Bearer {token}

Response (200):
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "fullName": "Admin User",
  "roles": ["ADMIN"]
}
```

### Protected Endpoints

#### Admin Endpoints

```bash
# Get Admin Dashboard (ADMIN only)
GET /api/admin/dashboard
Authorization: Bearer {token}

# Assign Role (ADMIN only)
POST /api/admin/users/assign-role?username=user&role=STAFF
Authorization: Bearer {token}

# Delete User (ADMIN only)
DELETE /api/admin/users/1
Authorization: Bearer {token}
```

#### Staff Endpoints

```bash
# Get Staff Dashboard (ADMIN, STAFF)
GET /api/staff/dashboard
Authorization: Bearer {token}

# Get Orders (ADMIN, STAFF)
GET /api/staff/orders
Authorization: Bearer {token}

# Update Order Status (ADMIN, STAFF)
POST /api/staff/orders/1/status?status=SHIPPED
Authorization: Bearer {token}
```

#### Supplier Endpoints

```bash
# Get Supplier Dashboard (ADMIN, SUPPLIER)
GET /api/supplier/dashboard
Authorization: Bearer {token}

# Get Products (ADMIN, SUPPLIER)
GET /api/supplier/products
Authorization: Bearer {token}

# Add Product (ADMIN, SUPPLIER)
POST /api/supplier/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Product Name",
  "price": 100000,
  "quantity": 50
}
```

## How to Test with Postman

### 1. Login
```
POST http://localhost:8081/api/auth/login

{
  "username": "admin",
  "password": "admin123"
}
```

Copy the `token` from the response.

### 2. Use Token in Protected Endpoint
```
GET http://localhost:8081/api/admin/dashboard

Headers:
Authorization: Bearer {paste_token_here}
```

## JWT Token Structure

Your JWT token contains:
- **Header**: Algorithm (HS256)
- **Payload**: 
  - `sub` (subject): username
  - `roles`: array of user roles
  - `iat` (issued at): token creation time
  - `exp` (expiration): token expiration time
- **Signature**: HMAC-SHA256 encrypted with your secret

Example:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNjk1MDAwMDAwLCJleHAiOjE2OTUwMDM2MDB9.
signature_here
```

## Adding Role-Based Access to Endpoints

### Example 1: Admin Only
```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/users")
public ResponseEntity<?> getAllUsers() {
    // Only ADMIN can access
}
```

### Example 2: Multiple Roles
```java
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@GetMapping("/orders")
public ResponseEntity<?> getOrders() {
    // ADMIN and STAFF can access
}
```

### Example 3: Complex Rules
```java
@PreAuthorize("hasRole('ADMIN') or (@userService.isOwner(#userId, authentication))")
@GetMapping("/users/{userId}")
public ResponseEntity<?> getUser(@PathVariable Long userId) {
    // ADMIN or the user themselves
}
```

## Security Best Practices

### 1. Change JWT Secret
⚠️ **CRITICAL**: Change the default JWT secret in production!

```env
# NEVER use this in production
APP_JWT_SECRET=very_long_random_secret_at_least_32_chars_here_for_testing_only

# Use a strong random secret:
APP_JWT_SECRET=aB#mK9$pL@2xQ&wE%rT^yU*iO(pS)dFg
```

### 2. Use HTTPS in Production
- All communication should be encrypted
- Token exposed via HTTP

### 3. Short Token Expiration
```env
# 15 minutes (in milliseconds)
APP_JWT_EXPIRATION_MS=900000
```

### 4. Implement Refresh Tokens
- Access token: Short-lived (15 min)
- Refresh token: Long-lived (7 days)

### 5. Password Security
- Passwords are hashed with BCrypt
- Never send passwords in plain text
- Always use HTTPS

### 6. Validate User Input
- Check token expiration
- Validate user permissions
- Sanitize database inputs

## Frontend Integration (React)

### 1. Login Component
```javascript
const handleLogin = async (username, password) => {
  const response = await fetch('http://localhost:8081/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  
  const data = await response.json();
  localStorage.setItem('token', data.token);
  localStorage.setItem('user', JSON.stringify(data));
};
```

### 2. Protected API Calls
```javascript
const callProtectedAPI = async (endpoint) => {
  const token = localStorage.getItem('token');
  
  const response = await fetch(`http://localhost:8081${endpoint}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  
  return response.json();
};
```

### 3. Check User Role
```javascript
const isAdmin = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  return user?.roles?.includes('ADMIN');
};

// Use in component
{isAdmin() && <AdminPanel />}
```

## Troubleshooting

### Issue: "Invalid token" Error
**Solution**: 
1. Verify token format: `Bearer {token}`
2. Check token expiration
3. Verify JWT secret matches

### Issue: Access Denied (403)
**Solution**:
1. Check user has required role
2. Verify JWT is valid
3. Check @PreAuthorize annotation

### Issue: "User not found"
**Solution**:
1. Verify user exists in database
2. Check username spelling
3. Verify JWT subject matches username

## Database Schema

```sql
-- Users table
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(255),
  enabled BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT NOW()
);

-- User Roles junction table
CREATE TABLE user_roles (
  user_id INT REFERENCES users(id) ON DELETE CASCADE,
  role VARCHAR(50),
  PRIMARY KEY (user_id, role)
);
```

## Next Steps

1. **Customize Roles**: Add more roles as needed
2. **Add User Management**: Create endpoints to manage users
3. **Implement Refresh Tokens**: For better security
4. **Add Audit Logging**: Track user actions
5. **Implement 2FA**: Two-factor authentication
6. **Add Role Hierarchy**: Determine role precedence

## Support

For more information on Spring Security and JWT:
- [Spring Security Docs](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [JJWT Documentation](https://github.com/jwtk/jjwt)

---

**Last Updated**: November 14, 2025
**Version**: 1.0
