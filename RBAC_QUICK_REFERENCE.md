# RBAC Quick Reference

## 🚀 Quick Start

### Start Server
```bash
$env:DB_CONNECTION_URL="postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres"
$env:APP_JWT_SECRET="your_secret_key_at_least_32_chars"
mvn spring-boot:run
```

## 🔐 Test Credentials

```
Admin:    admin / admin123
Staff:    staff / staff123
Supplier: supplier / supplier123
User:     user / user123
```

## 📝 Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

## 🔓 Protected Request

```bash
curl -X GET http://localhost:8081/api/admin/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 🎯 Endpoints

### Auth
| Endpoint | Method | Auth | Purpose |
|----------|--------|------|---------|
| /api/auth/register | POST | ❌ | Register new user |
| /api/auth/login | POST | ❌ | Get JWT token |
| /api/auth/me | GET | ✅ | Get current user |
| /api/auth/logout | POST | ✅ | Logout |

### Admin (ADMIN only)
| Endpoint | Method | Purpose |
|----------|--------|---------|
| /api/admin/dashboard | GET | Admin dashboard |
| /api/admin/users/assign-role | POST | Assign role to user |
| /api/admin/users/{id} | DELETE | Delete user |

### Staff (ADMIN, STAFF)
| Endpoint | Method | Purpose |
|----------|--------|---------|
| /api/staff/dashboard | GET | Staff dashboard |
| /api/staff/orders | GET | Get orders |
| /api/staff/orders/{id}/status | POST | Update order status |

### Supplier (ADMIN, SUPPLIER)
| Endpoint | Method | Purpose |
|----------|--------|---------|
| /api/supplier/dashboard | GET | Supplier dashboard |
| /api/supplier/products | GET | Get products |
| /api/supplier/products | POST | Add product |
| /api/supplier/products/{id} | PUT | Update product |

## 👥 Roles & Permissions

```
ADMIN     → Can do everything
STAFF     → Manage orders, view analytics
SUPPLIER  → Manage products, view orders
USER      → Basic access only
```

## 🛡️ Security Annotations

```java
// Admin only
@PreAuthorize("hasRole('ADMIN')")

// Multiple roles
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")

// Complex rules
@PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id)")
```

## 📦 Files Created

```
src/main/java/com/example/demo/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── AdminController.java
│   ├── StaffController.java
│   └── SupplierController.java
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   └── RegisterRequest.java
├── enums/
│   └── Role.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── security/
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
└── service/
    └── CustomUserDetailsService.java
```

## 🔧 Configuration (.env)

```env
# Database
DB_CONNECTION_URL=postgresql://postgres:PASSWORD@db.PROJECT_ID.supabase.co:5432/postgres

# JWT Security
APP_JWT_SECRET=your_secret_key_here_at_least_32_chars
APP_JWT_EXPIRATION_MS=3600000

# Server
SERVER_PORT=8081
```

## ✅ What's Working

- ✅ User registration
- ✅ User login with JWT
- ✅ Role-based access control
- ✅ Protected endpoints
- ✅ User details loading
- ✅ CORS configured
- ✅ Database user storage
- ✅ Test data seeding

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| 401 Unauthorized | Check token format: `Bearer {token}` |
| 403 Forbidden | User doesn't have required role |
| Token expires too fast | Increase `APP_JWT_EXPIRATION_MS` |
| Can't connect to DB | Check `DB_CONNECTION_URL` in .env |
| User not found | Verify username in database |

## 📚 Documentation

- Full guide: `RBAC_IMPLEMENTATION_GUIDE.md`
- Testing guide: `RBAC_TESTING_GUIDE.md`
- Spring Security: https://spring.io/projects/spring-security
- JWT: https://jwt.io

## 🚦 Next Steps

1. **Test Login**: Use credentials above
2. **Try Endpoints**: Access /api/admin, /api/staff, /api/supplier
3. **Customize**: Add more endpoints with @PreAuthorize
4. **Implement**: Connect with React frontend
5. **Secure**: Change JWT secret in production

---

**Quick Links:**
- Login: `POST http://localhost:8081/api/auth/login`
- Admin: `GET http://localhost:8081/api/admin/dashboard`
- Staff: `GET http://localhost:8081/api/staff/dashboard`
- Supplier: `GET http://localhost:8081/api/supplier/dashboard`

Remember: Always use `Authorization: Bearer {token}` header!
