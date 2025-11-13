# Supabase Configuration Guide

## Step 1: Get Your Supabase Credentials

1. Go to [Supabase](https://supabase.com) and create a free account
2. Create a new project (or use an existing one)
3. Navigate to **Project Settings** → **Database**
4. You'll find:
   - **Host**: `db.YOUR_PROJECT_ID.supabase.co`
   - **Port**: `5432`
   - **Database**: `postgres`
   - **User**: `postgres`
   - **Password**: (The password you set during project creation)

## Step 2: Update application.properties

In your `src/main/resources/application.properties`, replace the placeholder values:

```properties
# Replace YOUR_PROJECT_ID with your actual Supabase project ID
spring.datasource.url=jdbc:postgresql://db.YOUR_PROJECT_ID.supabase.co:5432/postgres

# Replace YOUR_DATABASE_PASSWORD with your Supabase password
spring.datasource.password=YOUR_DATABASE_PASSWORD
```

**Example:**
```properties
spring.datasource.url=jdbc:postgresql://db.abcdefg123xyz.supabase.co:5432/postgres
spring.datasource.password=my_secure_password_123
```

## Step 3: Build & Test Your Connection

Run this Maven command to download dependencies and verify the setup:

```bash
mvn clean install -DskipTests
```

Then start your Spring Boot application:

```bash
mvn spring-boot:run
```

## Step 4: Verify in Supabase

After running your app:
1. Go to your Supabase dashboard
2. Click **SQL Editor** on the left
3. You should see the `orders` and `order_items` tables created automatically!

## Troubleshooting

### "Connection refused" error
- Verify your project ID is correct
- Check that Supabase project is active
- Make sure your password is correct

### "Database does not exist" error
- Ensure you're connecting to the `postgres` database (default)
- Check your URL doesn't have typos

### SSL Connection Issues
If you get SSL errors, add this to `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://db.YOUR_PROJECT_ID.supabase.co:5432/postgres?sslmode=require
```

## Security Notes

⚠️ **NEVER** commit your password to Git!

For production, use environment variables:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}:5432/postgres
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

Then set these in your deployment environment.

---

## Your Project is Now Ready!

Your backend will now:
✅ Connect to Supabase PostgreSQL  
✅ Auto-create `orders` and `order_items` tables  
✅ Serve analytics APIs on `/api/analytics`  
✅ Ready for your React frontend to consume!
