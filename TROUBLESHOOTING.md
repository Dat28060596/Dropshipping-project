# Supabase Troubleshooting Guide

## Common Issues & Solutions

### 1. "Cannot find symbol: class Order" or "class OrderItem"

**Error Message:**
```
[ERROR] cannot find symbol
  symbol: class Order
```

**Causes:**
- Maven dependencies not downloaded
- Classes not compiled yet

**Solution:**
```bash
# Clean and rebuild
mvn clean install -DskipTests

# Then run
mvn spring-boot:run
```

---

### 2. "org.postgresql.util.PSQLException: Connection refused"

**Error Message:**
```
Connection refused to host:db.abc123.supabase.co Port: 5432
```

**Causes:**
- Wrong project ID in `application.properties`
- Supabase project is paused
- Network firewall blocking connection

**Solution:**
1. **Check your Supabase Project ID**
   - Open https://supabase.com/dashboard
   - Click on your project
   - Go to **Settings** → **General**
   - Copy the **Project ID**

2. **Update application.properties**
   ```properties
   spring.datasource.url=jdbc:postgresql://db.YOUR_CORRECT_PROJECT_ID.supabase.co:5432/postgres
   ```

3. **Verify project is active**
   - Dashboard should show project as "Active"
   - If paused, click "Resume"

4. **Check internet connection**
   - Verify you can reach supabase.co from terminal:
   ```bash
   ping supabase.co
   ```

---

### 3. "FATAL: password authentication failed for user 'postgres'"

**Error Message:**
```
org.postgresql.util.PSQLException: FATAL: password authentication failed for user 'postgres'
```

**Causes:**
- Wrong password in `application.properties`
- Password contains special characters that need escaping

**Solution:**
1. **Get correct password from Supabase**
   - Dashboard → Settings → Database
   - Click "Reveal" next to password
   - Copy the **actual password** (not the masked version)

2. **Update application.properties**
   ```properties
   spring.datasource.password=YOUR_EXACT_PASSWORD
   ```

3. **If password has special characters**
   - Use URL encoding if needed
   - Or use environment variables instead:
   ```bash
   export DB_PASSWORD=YourPassword123!
   ```
   Then in properties:
   ```properties
   spring.datasource.password=${DB_PASSWORD}
   ```

4. **Test password directly** (optional)
   ```bash
   psql -h db.YOUR_ID.supabase.co -U postgres -d postgres -W
   # It will prompt for password - paste your password
   ```

---

### 4. "database "postgres" does not exist"

**Error Message:**
```
ERROR: database "postgres" does not exist
```

**Causes:**
- Using wrong database name
- Database was deleted
- Typo in connection URL

**Solution:**
1. **Verify in application.properties**
   ```properties
   # Should be:
   spring.datasource.url=jdbc:postgresql://db.YOUR_ID.supabase.co:5432/postgres
   #                                                                       ^^^^^^^^
   #                                                         Keep this as "postgres"
   ```

2. **Check Supabase has default database**
   - Go to Supabase Dashboard
   - Click on your project
   - Should see database with name "postgres"
   - If not, you may need to create a new project

---

### 5. "The dialect org.hibernate.dialect.PostgreSQLDialect is deprecated"

**Warning Message:**
```
WARN org.hibernate.orm.deprecated.dialect : The dialect org.hibernate.dialect.PostgreSQLDialect 
is deprecated; use PostgreSQL10Dialect or PostgreSQL92Dialect instead
```

**Solution:**
Update `application.properties`:
```properties
# Change from:
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# To one of these:
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect
# or
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL92Dialect
```

---

### 6. Tables not created in Supabase

**Symptoms:**
- No `orders` table in Supabase SQL Editor
- No `order_items` table
- Application runs but no data is persisted

**Causes:**
- `spring.jpa.hibernate.ddl-auto=validate` (too strict)
- Hibernate not configured properly
- Application failed silently

**Solution:**
1. **Check application.properties**
   ```properties
   spring.jpa.hibernate.ddl-auto=update  # Should be "update" not "validate"
   ```

2. **Enable SQL logging**
   ```properties
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   logging.level.org.hibernate.SQL=DEBUG
   ```

3. **Restart application**
   ```bash
   mvn spring-boot:run
   ```

4. **Check logs for CREATE TABLE statements**
   ```
   Hibernate: create table orders (...)
   Hibernate: create table order_items (...)
   ```

5. **Manually check tables in Supabase**
   - Dashboard → SQL Editor
   - Run: `SELECT * FROM information_schema.tables WHERE table_name = 'orders';`

---

### 7. "org.springframework.beans.factory.NoSuchBeanDefinitionException: OrderRepository"

**Error Message:**
```
No qualifying bean of type 'com.example.demo.repository.OrderRepository' available
```

**Causes:**
- Repository not in correct package
- Repository package not scanned
- `@Repository` annotation missing

**Solution:**
1. **Verify repository is in correct package**
   ```
   src/main/java/com/example/demo/repository/OrderRepository.java
   ✓ Correct location
   ```

2. **Ensure @Repository annotation exists**
   ```java
   @Repository  // This is required!
   public interface OrderRepository extends JpaRepository<Order, Long> {
   ```

3. **Check component scan in main application class**
   ```java
   @SpringBootApplication // This enables component scanning
   public class DemoApplication {
   ```

---

### 8. "Caused by: java.sql.SQLException: JDBC Connection refused"

**Error Message:**
```
java.sql.SQLException: Cannot get a connection, pool error Timeout waiting for an idle object
```

**Causes:**
- Connection pool exhausted
- Too many connections
- Connection timeout too short

**Solution:**
1. **Add connection pool settings to application.properties**
   ```properties
   spring.datasource.hikari.maximum-pool-size=20
   spring.datasource.hikari.minimum-idle=5
   spring.datasource.hikari.connection-timeout=20000
   spring.datasource.hikari.idle-timeout=300000
   spring.datasource.hikari.max-lifetime=1200000
   ```

2. **Restart application**
   ```bash
   mvn spring-boot:run
   ```

---

### 9. "SSL Connection Error" / "SSL Handshake Failed"

**Error Message:**
```
FATAL: no pg_hba.conf entry for host "XX.XX.XX.XX", user "postgres", database "postgres", SSL off
```

**Solution:**
1. **Add SSL requirement to connection URL**
   ```properties
   spring.datasource.url=jdbc:postgresql://db.YOUR_ID.supabase.co:5432/postgres?sslmode=require
   ```

2. **Full JDBC URL example**
   ```properties
   spring.datasource.url=jdbc:postgresql://db.abc123xyz.supabase.co:5432/postgres?sslmode=require&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory
   ```

---

### 10. "Cannot deserialize JSON" in API responses

**Error Message:**
```
com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field
```

**Causes:**
- DTO is missing getters
- Field names don't match JSON keys

**Solution:**
1. **Ensure all DTO fields have getters**
   ```java
   public class DailySalesDTO {
       private LocalDate date;
       private double totalSales;
       
       // REQUIRED: getters for JSON serialization
       public LocalDate getDate() { return date; }
       public double getTotalSales() { return totalSales; }
   }
   ```

2. **Verify field naming matches**
   - Private field: `totalSales`
   - Getter: `getTotalSales()`
   - JSON key: `totalSales` (auto-generated from getter name)

---

### 11. "org.postgresql.util.PSQLException: ERROR: relation does not exist"

**Error Message:**
```
PSQLException: ERROR: relation "order_items" does not exist
```

**Causes:**
- Table name case sensitivity issue
- Table not yet created
- Hibernated used different table name

**Solution:**
1. **Check table names in Supabase SQL Editor**
   ```sql
   SELECT table_name FROM information_schema.tables 
   WHERE table_schema = 'public';
   ```

2. **Verify JPA entity table names**
   ```java
   @Entity
   @Table(name = "orders")  // Lowercase, Supabase uses lowercase
   public class Order {
   ```

3. **If tables have different names, manually fix**
   ```sql
   ALTER TABLE "Order" RENAME TO orders;
   ALTER TABLE "OrderItem" RENAME TO order_items;
   ```

---

### 12. "No suitable driver found for jdbc:postgresql"

**Error Message:**
```
java.sql.SQLException: No suitable driver found for jdbc:postgresql://...
```

**Causes:**
- PostgreSQL JDBC driver not in classpath
- pom.xml missing dependency

**Solution:**
1. **Verify pom.xml has PostgreSQL driver**
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```

2. **Rebuild project**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Clear Maven cache if needed**
   ```bash
   rm -rf ~/.m2/repository/org/postgresql
   mvn clean install -DskipTests
   ```

---

## Verification Checklist

Before running your app, verify:

- [ ] Supabase project created at supabase.com
- [ ] Project status is "Active"
- [ ] Project ID copied correctly
- [ ] Database password retrieved
- [ ] application.properties updated with Project ID
- [ ] application.properties updated with password
- [ ] pom.xml has PostgreSQL driver dependency
- [ ] pom.xml has spring-boot-starter-data-jpa dependency
- [ ] Entity classes are in `model/` package
- [ ] Repository interfaces are in `repository/` package
- [ ] All classes have correct annotations (@Entity, @Repository, etc.)
- [ ] All DTOs have getters for JSON serialization

---

## Quick Debug Commands

```bash
# Check Maven dependencies
mvn dependency:tree | grep postgresql

# Test database connection (requires psql installed)
psql -h db.YOUR_ID.supabase.co -U postgres -d postgres -W

# View Spring Boot logs with more detail
mvn spring-boot:run -Ddebug=true

# Run tests to verify setup
mvn test

# Clean rebuild
mvn clean install -DskipTests
```

---

## When All Else Fails

1. **Check the logs carefully**
   - First error in logs is usually the root cause
   - Not the last one

2. **Restart fresh**
   ```bash
   mvn clean
   rm -rf target/
   mvn install -DskipTests
   ```

3. **Review all config**
   - Double-check every character in application.properties
   - Typos are the most common issue

4. **Check Supabase status**
   - Visit status.supabase.com
   - Ensure no ongoing incidents

5. **Contact support**
   - Supabase Discord: https://discord.supabase.io
   - Spring Boot docs: https://spring.io/projects/spring-boot

---

## Need More Help?

Refer to:
- `SETUP_COMPLETE.md` - Setup instructions
- `SUPABASE_SETUP.md` - Supabase-specific setup
- `PROJECT_SUMMARY.md` - Project overview
- `QUICK_REFERENCE.md` - Quick commands
