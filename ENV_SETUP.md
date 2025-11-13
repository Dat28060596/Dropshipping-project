# 🔐 ENVIRONMENT CONFIGURATION GUIDE

## Using the `.env` File

Your project now uses environment variables for secure credential management!

---

## 📋 How to Set Up

### Step 1: Get Your Supabase Connection URL

1. Go to https://supabase.com/dashboard
2. Click on your project
3. Go to **Settings** → **Database**
4. Look for **Connection String** section
5. Select **URI** tab
6. Copy the entire connection string

**Example URL:**
```
postgresql://postgres:YOUR_PASSWORD@db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres
```

---

### Step 2: Update `.env` File

Edit the `.env` file in your project root and replace:

```env
DB_CONNECTION_URL=postgresql://postgres:[YOUR_PASSWORD]@db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres
```

**With your actual URL:**
```env
DB_CONNECTION_URL=postgresql://postgres:YOUR_ACTUAL_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres
```

---

### Step 3: Load Environment Variables

#### Option A: Using Maven Plugin (Recommended)
```bash
# Install dotenv-maven-plugin
mvn clean install

# Run with env variables loaded automatically
mvn spring-boot:run
```

#### Option B: Manual Environment Variable (Windows PowerShell)
```powershell
# Set environment variable in PowerShell
$env:DB_CONNECTION_URL="postgresql://postgres:YOUR_PASSWORD@db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres"

# Then run
mvn spring-boot:run
```

#### Option C: Manual Environment Variable (Windows CMD)
```cmd
# Set environment variable in Command Prompt
set DB_CONNECTION_URL=postgresql://postgres:YOUR_PASSWORD@db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres

# Then run
mvn spring-boot:run
```

#### Option D: Manual Environment Variable (macOS/Linux)
```bash
# Set environment variable
export DB_CONNECTION_URL="postgresql://postgres:YOUR_PASSWORD@db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres"

# Then run
mvn spring-boot:run
```

---

## 🔒 Security Notes

### ✅ DO:
- ✅ Update `.env` with your real password
- ✅ Keep `.env` in `.gitignore` (already done)
- ✅ Never share your `.env` file
- ✅ Use different passwords for dev/staging/prod
- ✅ Rotate passwords regularly

### ❌ DON'T:
- ❌ Commit `.env` to Git
- ❌ Share your connection URL
- ❌ Use the same password everywhere
- ❌ Leave `.env` in shared folders
- ❌ Hardcode credentials in code

---

## 📝 Environment Variable Format

Your connection URL should follow this format:

```
postgresql://postgres:[PASSWORD]@db.[PROJECT_ID].supabase.co:5432/postgres
```

Breaking it down:
- `postgresql://` - Protocol
- `postgres` - Username (default)
- `:` - Password separator
- `[PASSWORD]` - Your Supabase password
- `@` - Host separator
- `db.[PROJECT_ID].supabase.co` - Supabase host
- `:5432` - Port
- `/postgres` - Database name

---

## ✅ Verification

After setting up, verify the connection:

```bash
# Start the application
mvn spring-boot:run

# Look for this in the console:
# Started DemoApplication in X.XXX seconds

# Test an endpoint
curl "http://localhost:8081/api/analytics/sales-by-day?startDate=2024-01-01&endDate=2024-12-31"

# Should return JSON (even if empty)
# If you get "Connection refused", check your .env file
```

---

## 🐛 Troubleshooting

### Error: "Connection refused"
- Check your `.env` file has correct URL
- Verify your Supabase project ID is correct
- Confirm your password is correct
- Make sure Supabase project is active

### Error: "Access denied for user 'postgres'"
- Your password in `.env` is incorrect
- Copy password again from Supabase dashboard
- Make sure there are no extra spaces

### Error: "database postgres does not exist"
- Your connection URL is malformed
- Copy the exact URL from Supabase
- Make sure URL ends with `/postgres`

---

## 📂 File Structure

```
demo/
├── .env ........................ ⚠️ Your credentials (NEVER commit!)
├── .gitignore ................. Already ignores .env
├── pom.xml
└── src/main/resources/
    └── application.properties .. Uses ${DB_CONNECTION_URL}
```

---

## 🔄 For Different Environments

### Development
```env
DB_CONNECTION_URL=postgresql://postgres:DEV_PASSWORD@db.dev-project.supabase.co:5432/postgres
```

### Staging
```env
DB_CONNECTION_URL=postgresql://postgres:STAGING_PASSWORD@db.staging-project.supabase.co:5432/postgres
```

### Production
```env
DB_CONNECTION_URL=postgresql://postgres:PROD_PASSWORD@db.prod-project.supabase.co:5432/postgres
```

**Use different `.env` files for each environment!**

---

## 💡 Pro Tips

1. **Keep `.env` out of Git** - It's in `.gitignore` already ✅
2. **Test connection before deploying** - Use the curl command above
3. **Use strong passwords** - Supabase generates secure ones by default
4. **Rotate passwords** - Do this regularly in production
5. **Document your setup** - Share setup instructions, not `.env` files

---

## 🚀 Next Steps

1. ✅ Update your `.env` with Supabase connection URL
2. ✅ Set environment variable (choose option A, B, C, or D above)
3. ✅ Run `mvn spring-boot:run`
4. ✅ Test API endpoint
5. ✅ Check Supabase dashboard for auto-created tables

---

## 📞 Quick Reference

| Task | Command |
|------|---------|
| Set env var (PowerShell) | `$env:DB_CONNECTION_URL="..."` |
| Set env var (CMD) | `set DB_CONNECTION_URL=...` |
| Set env var (Linux/Mac) | `export DB_CONNECTION_URL="..."` |
| Run application | `mvn spring-boot:run` |
| Test connection | `curl "http://localhost:8081/api/analytics/sales-by-day?startDate=2024-01-01&endDate=2024-12-31"` |

---

## ✨ All Set!

Your `.env` file is ready. Just update it with your Supabase connection URL and you're good to go!

**Remember:** The `.env` file is ignored by Git, so your credentials are safe! 🔐
