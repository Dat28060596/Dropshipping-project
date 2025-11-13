# RBAC Quick Testing Guide

## Test Users

```
ADMIN:
  Username: admin
  Password: admin123
  Email: admin@example.com

STAFF:
  Username: staff
  Password: staff123
  Email: staff@example.com

SUPPLIER:
  Username: supplier
  Password: supplier123
  Email: supplier@example.com

USER:
  Username: user
  Password: user123
  Email: user@example.com
```

## Using Postman

### 1. Create Login Request

```
Method: POST
URL: http://localhost:8081/api/auth/login

Body (JSON):
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "email": "admin@example.com",
  "fullName": "Admin User",
  "roles": ["ADMIN"]
}
```

### 2. Copy Token to Environment Variable

In Postman:
1. Go to **Manage Environments**
2. Create new environment: `RBAC_Testing`
3. Add variable:
   ```
   Key: token
   Value: {paste your token here}
   ```

### 3. Test Protected Endpoints

#### Test Admin Endpoint

```
Method: GET
URL: http://localhost:8081/api/admin/dashboard

Headers:
Authorization: Bearer {{token}}
```

✅ Should return 200 with admin dashboard

#### Test Staff Endpoint (with Admin token)

```
Method: GET
URL: http://localhost:8081/api/staff/dashboard

Headers:
Authorization: Bearer {{token}}
```

✅ Should return 200 (Admin has access to Staff endpoints too)

#### Test Supplier Endpoint (with Admin token)

```
Method: GET
URL: http://localhost:8081/api/supplier/dashboard

Headers:
Authorization: Bearer {{token}}
```

✅ Should return 200 (Admin has access to Supplier endpoints too)

### 4. Test Role Restrictions

#### Login as STAFF

```
Method: POST
URL: http://localhost:8081/api/auth/login

Body:
{
  "username": "staff",
  "password": "staff123"
}
```

Copy the new token.

#### Try to Access Admin Endpoint (should FAIL)

```
Method: GET
URL: http://localhost:8081/api/admin/dashboard

Headers:
Authorization: Bearer {{token}}  # Staff token
```

❌ Should return 403 Forbidden

#### Try to Access Staff Endpoint (should SUCCEED)

```
Method: GET
URL: http://localhost:8081/api/staff/dashboard

Headers:
Authorization: Bearer {{token}}  # Staff token
```

✅ Should return 200

## Using cURL

### Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Access Protected Endpoint

```bash
# Save token from login response
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Access admin endpoint
curl -X GET http://localhost:8081/api/admin/dashboard \
  -H "Authorization: Bearer $TOKEN"
```

## Using Thunder Client (VS Code Extension)

1. Create new request
2. Set method: `POST`
3. URL: `http://localhost:8081/api/auth/login`
4. Body tab → Raw JSON:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
5. Send
6. Copy token from response
7. Create new request for protected endpoint
8. Set method: `GET`
9. URL: `http://localhost:8081/api/admin/dashboard`
10. Headers tab → Add:
    - Key: `Authorization`
    - Value: `Bearer {token}`
11. Send

## Using Insomnia

1. Create HTTP Request
2. Method: `POST`
3. URL: `http://localhost:8081/api/auth/login`
4. JSON Body:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
5. Send
6. Copy token
7. Create new request (GET)
8. URL: `http://localhost:8081/api/admin/dashboard`
9. Add Header: `Authorization: Bearer {token}`
10. Send

## Test Cases

### Test Case 1: Admin Can Access All
| User | Endpoint | Expected |
|------|----------|----------|
| admin | /api/admin/dashboard | ✅ 200 |
| admin | /api/staff/dashboard | ✅ 200 |
| admin | /api/supplier/dashboard | ✅ 200 |

### Test Case 2: Staff Can't Access Admin
| User | Endpoint | Expected |
|------|----------|----------|
| staff | /api/admin/dashboard | ❌ 403 |
| staff | /api/staff/dashboard | ✅ 200 |
| staff | /api/supplier/dashboard | ❌ 403 |

### Test Case 3: Supplier Can't Access Admin/Staff
| User | Endpoint | Expected |
|------|----------|----------|
| supplier | /api/admin/dashboard | ❌ 403 |
| supplier | /api/staff/dashboard | ❌ 403 |
| supplier | /api/supplier/dashboard | ✅ 200 |

### Test Case 4: Regular User Can't Access Protected Endpoints
| User | Endpoint | Expected |
|------|----------|----------|
| user | /api/admin/dashboard | ❌ 403 |
| user | /api/staff/dashboard | ❌ 403 |
| user | /api/supplier/dashboard | ❌ 403 |

## Common Response Codes

| Code | Meaning | Solution |
|------|---------|----------|
| 200 | Success | Request authorized and executed |
| 400 | Bad Request | Check JSON format, missing fields |
| 401 | Unauthorized | Token missing or invalid |
| 403 | Forbidden | User doesn't have required role |
| 404 | Not Found | Endpoint doesn't exist |
| 500 | Server Error | Check server logs |

## Debugging

### 1. Check JWT Token Validity
Use [jwt.io](https://jwt.io) to decode token and verify:
- Signature is valid
- Expiration hasn't passed
- Roles are correct

### 2. Enable Debug Logging
Add to `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.example.demo.security=DEBUG
```

### 3. Verify Token Format
Token should always be:
```
Authorization: Bearer {token}
```

NOT:
```
Authorization: {token}  ❌
Authorization: Bearer {token}extra  ❌
```

### 4. Check Role Prefix
Roles are stored as:
```
ROLE_ADMIN
ROLE_STAFF
ROLE_SUPPLIER
ROLE_USER
```

The `ROLE_` prefix is added automatically by Spring Security.

## Performance Tips

1. **Cache Token**: Don't request new token for every API call
2. **Set Reasonable Expiration**: Not too short (annoying), not too long (insecure)
3. **Use Refresh Tokens**: For better security without constant re-login
4. **Implement Token Revocation**: For logout functionality

## Security Checklist

- [ ] Changed default JWT secret in production
- [ ] Using HTTPS in production
- [ ] Token expiration set appropriately
- [ ] Password hashing with BCrypt
- [ ] CORS configured correctly
- [ ] No sensitive data in JWT payload
- [ ] Implementing role hierarchy
- [ ] User input validation
- [ ] Rate limiting on auth endpoints
- [ ] Audit logging for sensitive operations

---

**Happy Testing!** 🚀
