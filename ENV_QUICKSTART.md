# ⚡ QUICK START WITH .env FILE

## 3 Simple Steps

### Step 1: Copy Your Supabase URL
```
Go to: https://supabase.com/dashboard
→ Your Project → Settings → Database
→ Connection String → URI
→ Copy the URL
```

### Step 2: Update `.env` File
```
File: .env

Change:
  DB_CONNECTION_URL=postgresql://postgres:[YOUR_PASSWORD]@db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres

To:
  DB_CONNECTION_URL=postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres
```

### Step 3: Set Environment Variable & Run

**Windows PowerShell:**
```powershell
$env:DB_CONNECTION_URL="postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres"
mvn spring-boot:run
```

**Windows CMD:**
```cmd
set DB_CONNECTION_URL=postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres
mvn spring-boot:run
```

**macOS/Linux:**
```bash
export DB_CONNECTION_URL="postgresql://postgres:YOUR_PASSWORD@db.YOUR_PROJECT_ID.supabase.co:5432/postgres"
mvn spring-boot:run
```

✅ **Done!** Your application is running!

---

## 🧪 Test It

```bash
curl "http://localhost:8081/api/analytics/sales-by-day?startDate=2024-01-01&endDate=2024-12-31"
```

Should return JSON (possibly empty `[]`)

---

## 📌 Important
- ✅ `.env` is in `.gitignore` - Your credentials are safe!
- ✅ Never commit `.env` to Git
- ✅ Never share your `.env` file
- ✅ Keep your password secure

---

**Read more:** See `ENV_SETUP.md` for detailed configuration options
