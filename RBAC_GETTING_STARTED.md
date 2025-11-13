# 🚀 RBAC Getting Started - 5 Minutes to Working Authentication

## What You Got

A complete **Role-Based Access Control (RBAC)** system for your dropshipping platform with:
- ✅ User registration & login
- ✅ JWT token authentication
- ✅ Role-based permissions (Admin, Staff, Supplier, User)
- ✅ Protected API endpoints
- ✅ 4 test users ready to use

## Quick Start (5 Minutes)

### Step 1: Build Project (1 min)
```powershell
cd d:\year 3\java\demo\demo
mvn clean install
```

### Step 2: Set Environment Variables (1 min)
```powershell
# Copy your Supabase connection URL
$env:DB_CONNECTION_URL="postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres"

# JWT secret (already has default, but change in production)
$env:APP_JWT_SECRET="your_very_long_random_secret_key_with_at_least_32_chars"
```

### Step 3: Start Server (1 min)
```powershell
mvn spring-boot:run
```

### Step 4: Test Login (1 min)
Open Postman or PowerShell and run:

```powershell
# Login as admin
$body = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "http://localhost:8081/api/auth/login" `
  -Method Post `
  -Headers @{"Content-Type"="application/json"} `
  -Body $body

$response.Content | ConvertFrom-Json | Select-Object token, username, roles
```

### Step 5: Access Protected Endpoint (1 min)
```powershell
# Copy token from previous response
$token = "your_token_from_above"

$headers = @{
    "Authorization" = "Bearer $token"
}

Invoke-WebRequest -Uri "http://localhost:8081/api/admin/dashboard" `
  -Headers $headers | Select-Object StatusCode, Content
```

**Done! You have working authentication!** ✅

## Test Credentials

Copy and use these to test:

```
┌─────────┬───────────┬──────────────┬─────────────────────────┐
│ Role    │ Username  │ Password     │ Email                   │
├─────────┼───────────┼──────────────┼─────────────────────────┤
│ ADMIN   │ admin     │ admin123     │ admin@example.com       │
│ STAFF   │ staff     │ staff123     │ staff@example.com       │
│ SUPPLIER│ supplier  │ supplier123  │ supplier@example.com    │
│ USER    │ user      │ user123      │ user@example.com        │
└─────────┴───────────┴──────────────┴─────────────────────────┘
```

## 🎯 What Each Role Can Do

### ADMIN (Full Access)
```
✅ /api/admin/dashboard           - View admin dashboard
✅ /api/admin/users/assign-role   - Assign roles to users
✅ /api/admin/users/{id}          - Delete users
✅ /api/staff/*                   - Access staff endpoints
✅ /api/supplier/*                - Access supplier endpoints
```

### STAFF (Order Management)
```
❌ /api/admin/*                   - NO admin access
✅ /api/staff/dashboard           - View staff dashboard
✅ /api/staff/orders              - View orders
✅ /api/staff/orders/{id}/status  - Update order status
```

### SUPPLIER (Product Management)
```
❌ /api/admin/*                   - NO admin access
❌ /api/staff/*                   - NO staff access
✅ /api/supplier/dashboard        - View supplier dashboard
✅ /api/supplier/products         - View/add/update products
```

### USER (Basic Access)
```
❌ /api/admin/*                   - NO admin access
❌ /api/staff/*                   - NO staff access
❌ /api/supplier/*                - NO supplier access
✅ /api/auth/me                   - View own profile
```

## 📚 Documentation

If you need more details:

| File | Purpose | Read Time |
|------|---------|-----------|
| `RBAC_QUICK_REFERENCE.md` | Quick commands & endpoints | 5 min |
| `RBAC_TESTING_GUIDE.md` | How to test with Postman | 10 min |
| `RBAC_IMPLEMENTATION_GUIDE.md` | Complete technical guide | 20 min |
| `REACT_INTEGRATION_GUIDE.md` | Connect React frontend | 15 min |

## 🔐 Security Notes

⚠️ **For Production:**
1. Change `APP_JWT_SECRET` to a random 32+ character string
2. Use HTTPS (not HTTP)
3. Set short token expiration (15 minutes)
4. Implement refresh tokens
5. Add rate limiting to login endpoint

## 🧪 Testing the System

### Option 1: Using Postman
1. Download [Postman](https://www.postman.com/)
2. Create new request: `POST http://localhost:8081/api/auth/login`
3. Body (JSON):
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
4. Send → Copy the token
5. Create another request: `GET http://localhost:8081/api/admin/dashboard`
6. Add header: `Authorization: Bearer {token}`
7. Send → Should see admin dashboard!

### Option 2: Using Thunder Client (VS Code)
1. Install Thunder Client extension
2. Create POST request to `http://localhost:8081/api/auth/login`
3. Body: `{"username":"admin","password":"admin123"}`
4. Send
5. Copy token
6. Create GET request to `http://localhost:8081/api/admin/dashboard`
7. Add header: `Authorization: Bearer {token}`
8. Send

### Option 3: Using curl
```bash
# Login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Copy token from response

# Access admin dashboard
curl -X GET http://localhost:8081/api/admin/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## ✨ Key Features

```
✅ Register new users
✅ Login with username/password
✅ Get JWT token (1 hour validity)
✅ Access protected endpoints
✅ Role-based permissions
✅ Token validation
✅ Automatic test data
✅ CORS for React frontend
✅ BCrypt password hashing
✅ Stateless API
```

## 📦 What's Included

### Backend Files
- 15 Java implementation files
- 4 security/JWT components
- 4 protected API endpoints
- 3 role groups

### Documentation
- Quick reference guide
- Testing guide
- Complete implementation guide
- React integration examples
- File manifest

### Database
- User table with roles
- Auto-seeded test users
- PostgreSQL ready (Supabase)

## 🚫 Troubleshooting

### ❌ "Invalid token"
**Solution**: 
- Check format: `Authorization: Bearer {token}`
- Token might be expired (1 hour default)
- Verify JWT secret in environment

### ❌ "Access Denied (403)"
**Solution**:
- User doesn't have required role
- Check role assignment
- Verify @PreAuthorize annotation

### ❌ "User not found"
**Solution**:
- User doesn't exist in database
- Check username spelling
- Verify test data was seeded

### ❌ "Can't connect to database"
**Solution**:
- Check DB_CONNECTION_URL in .env
- Verify Supabase credentials
- Test connection in application.properties

## 🔄 API Flow Diagram

```
┌──────────────┐
│  React App   │
└──────┬───────┘
       │ 1. POST /api/auth/login
       │    (username, password)
       ▼
┌──────────────────────────────┐
│  Login Endpoint              │
│  - Validate credentials      │
│  - Generate JWT token       │
└──────┬───────────────────────┘
       │ 2. Return token
       ▼
┌──────────────────────────────┐
│  Client (React)              │
│  - Store token in localStorage│
│  - Add to Authorization header│
└──────┬───────────────────────┘
       │ 3. GET /api/admin/dashboard
       │    Authorization: Bearer {token}
       ▼
┌──────────────────────────────┐
│  JWT Filter                  │
│  - Extract token             │
│  - Validate signature        │
│  - Check expiration          │
└──────┬───────────────────────┘
       │ 4. Check authorization
       ▼
┌──────────────────────────────┐
│  @PreAuthorize Check         │
│  - User has ADMIN role?      │
│  - Grant/Deny access         │
└──────┬───────────────────────┘
       │ 5. Execute or reject
       ▼
┌──────────────────────────────┐
│  Protected Endpoint          │
│  - Return data               │
│  - or 403 Forbidden          │
└──────────────────────────────┘
```

## 🎓 Learning Path

1. **Start Here**: Test login with admin user
2. **Try This**: Login as staff and try to access `/api/admin/dashboard` (should fail)
3. **Add Endpoint**: Create new controller method with `@PreAuthorize`
4. **Connect React**: Use `REACT_INTEGRATION_GUIDE.md`
5. **Deploy**: Set proper JWT secret and use HTTPS

## 💡 Tips

**Tip 1**: Token expires in 1 hour. For testing, you can:
- Change expiration in `application.properties`
- Or login again to get new token

**Tip 2**: Test multiple roles:
```
Login as admin    → Can access /api/admin/*
Login as staff    → Cannot access /api/admin/*
Login as supplier → Cannot access /api/staff/*
```

**Tip 3**: Frontend integration:
```javascript
// Store token after login
localStorage.setItem('token', response.token);

// Use in API calls
fetch('/api/admin/dashboard', {
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  }
});
```

## 🚀 Next Steps

1. ✅ **Test Login** - Use credentials above
2. ✅ **Test Protected Endpoints** - Access role-based URLs
3. ✅ **Add Custom Roles** - Extend Role enum
4. ✅ **Create User Admin UI** - Manage users and roles
5. ✅ **Connect React** - Follow React integration guide
6. ✅ **Deploy to Production** - Change secrets, use HTTPS

## 📞 Quick Reference Commands

```bash
# Start server
mvn spring-boot:run

# Build project
mvn clean install

# Test endpoint with token
curl -H "Authorization: Bearer TOKEN" http://localhost:8081/api/admin/dashboard

# View logs
# See console output from mvn spring-boot:run
```

## ✅ Success Checklist

- [ ] Built project with `mvn clean install`
- [ ] Set environment variables
- [ ] Started server with `mvn spring-boot:run`
- [ ] Logged in as admin
- [ ] Got JWT token back
- [ ] Accessed protected endpoint with token
- [ ] Got 403 when accessing without token
- [ ] Got 403 when staff tries to access admin endpoint

**All checked?** You're all set! 🎉

---

## Need Help?

See these files:
- **Quick commands?** → `RBAC_QUICK_REFERENCE.md`
- **How to test?** → `RBAC_TESTING_GUIDE.md`
- **All details?** → `RBAC_IMPLEMENTATION_GUIDE.md`
- **React code?** → `REACT_INTEGRATION_GUIDE.md`

**Happy coding!** 🚀
