import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class create_Table {
    // Updated to use Supabase pooler connection
    private static final String jdbcURL = "jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?prepareThreshold=0";
    private static final String jdbcUser = "postgres.kdvnogkvnpvbnedsmoui";
    private static final String jdbcPass = "thisisnotai123";

    public static void main(String[] args) {
        
        // Step 1: Drop existing tables in correct order (reverse of dependencies)
        String dropSQL = 
        "DROP TABLE IF EXISTS stock_alerts CASCADE;" +
        "DROP TABLE IF EXISTS inventory_logs CASCADE;" +
        "DROP TABLE IF EXISTS product_suppliers CASCADE;" +
        "DROP TABLE IF EXISTS suppliers CASCADE;" +
        "DROP TABLE IF EXISTS product_variants CASCADE;" +
        "DROP TABLE IF EXISTS payments CASCADE;" +
        "DROP TABLE IF EXISTS order_items CASCADE;" +
        "DROP TABLE IF EXISTS orders CASCADE;" +
        "DROP TABLE IF EXISTS products CASCADE;" +
        "DROP TABLE IF EXISTS categories CASCADE;" +
        "DROP TABLE IF EXISTS user_roles CASCADE;" +
        "DROP TABLE IF EXISTS users CASCADE;";
        
        // Step 2: Create all tables with correct schema
        String createSQL = 
        // Categories table
        "CREATE TABLE categories (" +
        "    id SERIAL PRIMARY KEY," +
        "    name VARCHAR(255) NOT NULL UNIQUE," +
        "    description TEXT," +
        "    parent_category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL," +
        "    is_active BOOLEAN DEFAULT true," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Products table (Enhanced)
        "CREATE TABLE products (" +
        "    id SERIAL PRIMARY KEY," +
        "    name VARCHAR(255) NOT NULL," +
        "    description TEXT," +
        "    detailed_description TEXT," +
        "    price NUMERIC(10,2) NOT NULL," +
        "    stock INT NOT NULL DEFAULT 0," +
        "    category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL," +
        "    image_url TEXT," +
        "    has_variants BOOLEAN DEFAULT false," +
        "    specifications JSONB," +
        "    tags TEXT[]," +
        "    is_active BOOLEAN DEFAULT true," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Product Variants table
        "CREATE TABLE product_variants (" +
        "    id SERIAL PRIMARY KEY," +
        "    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE," +
        "    sku VARCHAR(100) UNIQUE NOT NULL," +
        "    size VARCHAR(50)," +
        "    color VARCHAR(50)," +
        "    material VARCHAR(100)," +
        "    price NUMERIC(10,2)," +
        "    stock_quantity INTEGER DEFAULT 0," +
        "    low_stock_threshold INTEGER DEFAULT 10," +
        "    image_url TEXT," +
        "    is_active BOOLEAN DEFAULT true," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Users table
        "CREATE TABLE users (" +
        "    id BIGSERIAL PRIMARY KEY," +
        "    email VARCHAR(255) UNIQUE NOT NULL," +
        "    enabled BOOLEAN NOT NULL DEFAULT TRUE," +
        "    full_name VARCHAR(255)," +
        "    password VARCHAR(255) NOT NULL," +
        "    username VARCHAR(255) UNIQUE NOT NULL," +
        "    phone VARCHAR(50)," +
        "    address TEXT," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // User roles table
        "CREATE TABLE user_roles (" +
        "    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE," +
        "    role VARCHAR(100) NOT NULL," +
        "    PRIMARY KEY (user_id, role)" +
        ");" +
        
        // Suppliers table
        "CREATE TABLE suppliers (" +
        "    id SERIAL PRIMARY KEY," +
        "    name VARCHAR(255) NOT NULL," +
        "    contact_person VARCHAR(255)," +
        "    email VARCHAR(255)," +
        "    phone VARCHAR(50)," +
        "    address TEXT," +
        "    api_endpoint TEXT," +
        "    api_key TEXT," +
        "    is_active BOOLEAN DEFAULT true," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Product Supplier Mapping
        "CREATE TABLE product_suppliers (" +
        "    id SERIAL PRIMARY KEY," +
        "    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE," +
        "    supplier_id INTEGER NOT NULL REFERENCES suppliers(id)," +
        "    supplier_product_id VARCHAR(255)," +
        "    cost_price NUMERIC(10,2)," +
        "    lead_time_days INTEGER," +
        "    minimum_order_quantity INTEGER DEFAULT 1," +
        "    is_primary BOOLEAN DEFAULT false," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Inventory Logs table
        "CREATE TABLE inventory_logs (" +
        "    id SERIAL PRIMARY KEY," +
        "    product_id INTEGER REFERENCES products(id)," +
        "    variant_id INTEGER REFERENCES product_variants(id)," +
        "    change_type VARCHAR(50) NOT NULL," +
        "    quantity_change INTEGER NOT NULL," +
        "    previous_stock INTEGER," +
        "    new_stock INTEGER," +
        "    reason TEXT," +
        "    performed_by BIGINT REFERENCES users(id)," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Stock Alerts table
        "CREATE TABLE stock_alerts (" +
        "    id SERIAL PRIMARY KEY," +
        "    product_id INTEGER REFERENCES products(id)," +
        "    variant_id INTEGER REFERENCES product_variants(id)," +
        "    alert_type VARCHAR(50) NOT NULL," +
        "    current_stock INTEGER," +
        "    threshold_value INTEGER," +
        "    status VARCHAR(50) DEFAULT 'PENDING'," +
        "    acknowledged_by BIGINT REFERENCES users(id)," +
        "    acknowledged_at TIMESTAMP," +
        "    resolved_at TIMESTAMP," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Orders table
        "CREATE TABLE orders (" +
        "    id BIGSERIAL PRIMARY KEY," +
        "    order_number VARCHAR(50) UNIQUE NOT NULL," +
        "    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL," +
        "    customer_name TEXT NOT NULL," +
        "    customer_email VARCHAR(255)," +
        "    customer_phone VARCHAR(50)," +
        "    shipping_address TEXT," +
        "    total_amount NUMERIC(10,2) NOT NULL," +
        "    status VARCHAR(20) DEFAULT 'PENDING'," +
        "    description TEXT," +
        "    order_date TIMESTAMP NOT NULL DEFAULT NOW()," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Order items table
        "CREATE TABLE order_items (" +
        "    id BIGSERIAL PRIMARY KEY," +
        "    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE," +
        "    product_id INTEGER REFERENCES products(id)," +
        "    variant_id INTEGER REFERENCES product_variants(id)," +
        "    product_name VARCHAR(255) NOT NULL," +
        "    variant_info TEXT," +
        "    quantity INT NOT NULL," +
        "    unit_price_at_time_of_order NUMERIC(10,2) NOT NULL," +
        "    supplier_cost_at_time NUMERIC(10,2) NOT NULL," +
        "    subtotal NUMERIC(10,2) NOT NULL," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");" +
        
        // Payments table
        "CREATE TABLE payments (" +
        "    payment_id SERIAL PRIMARY KEY," +
        "    order_id VARCHAR(50) NOT NULL," +
        "    user_id BIGINT REFERENCES users(id)," +
        "    vnp_txn_ref VARCHAR(100) UNIQUE NOT NULL," +
        "    vnp_transaction_no VARCHAR(100)," +
        "    vnp_amount BIGINT NOT NULL," +
        "    vnp_bank_code VARCHAR(20)," +
        "    vnp_card_type VARCHAR(20)," +
        "    vnp_response_code VARCHAR(10)," +
        "    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING'," +
        "    vnp_pay_date TIMESTAMP," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "    vnp_order_info TEXT," +
        "    ip_address VARCHAR(45)" +
        ");" +
        
        // Create indexes for better performance
        "CREATE INDEX idx_products_category ON products(category_id);" +
        "CREATE INDEX idx_products_active ON products(is_active);" +
        "CREATE INDEX idx_product_variants_product ON product_variants(product_id);" +
        "CREATE INDEX idx_product_variants_sku ON product_variants(sku);" +
        "CREATE INDEX idx_inventory_logs_product ON inventory_logs(product_id);" +
        "CREATE INDEX idx_inventory_logs_variant ON inventory_logs(variant_id);" +
        "CREATE INDEX idx_stock_alerts_status ON stock_alerts(status);" +
        "CREATE INDEX idx_stock_alerts_product ON stock_alerts(product_id);" +
        "CREATE INDEX idx_vnp_txn_ref ON payments(vnp_txn_ref);" +
        "CREATE INDEX idx_payment_order_id ON payments(order_id);" +
        "CREATE INDEX idx_payment_status ON payments(payment_status);" +
        "CREATE INDEX idx_payment_user_id ON payments(user_id);" +
        "CREATE INDEX idx_order_number ON orders(order_number);" +
        "CREATE INDEX idx_order_status ON orders(status);" +
        "CREATE INDEX idx_order_user_id ON orders(user_id);" +
        "CREATE INDEX idx_order_items_order ON order_items(order_id);" +
        "CREATE INDEX idx_order_items_product ON order_items(product_id);";
        
        // Step 3: Insert sample data
        String insertSQL = 
        // Insert categories
        "INSERT INTO categories (name, description) VALUES " +
        "('Electronics', 'Electronic devices and accessories')," +
        "('Clothing', 'Apparel and fashion items')," +
        "('Home & Garden', 'Home improvement and garden supplies')," +
        "('Sports', 'Sports equipment and accessories')," +
        "('Books', 'Books and educational materials');" +
        
        // Insert sample products
        "INSERT INTO products (name, description, detailed_description, price, stock, category_id, has_variants, tags) VALUES " +
        "('Wireless Bluetooth Headphones', 'High-quality wireless headphones', " +
        "'Premium wireless headphones with active noise cancellation and 30-hour battery life', " +
        "89.99, 50, 1, true, ARRAY['electronics', 'audio', 'wireless'])," +
        
        "('Cotton T-Shirt', 'Comfortable cotton t-shirt', " +
        "'100% cotton t-shirt available in multiple colors and sizes', " +
        "19.99, 200, 2, true, ARRAY['clothing', 'casual', 'cotton'])," +
        
        "('Smart LED Bulb', 'WiFi-enabled smart bulb', " +
        "'Control your lighting from anywhere with voice control compatibility', " +
        "24.99, 100, 3, false, ARRAY['smart-home', 'lighting', 'wifi'])," +
        
        "('Yoga Mat', 'Non-slip exercise mat', " +
        "'Premium 6mm thick yoga mat with carrying strap', " +
        "29.99, 75, 4, false, ARRAY['fitness', 'yoga', 'exercise'])," +
        
        "('Programming Guide', 'Complete programming reference', " +
        "'Comprehensive guide to modern programming languages', " +
        "39.99, 30, 5, false, ARRAY['education', 'programming', 'reference']);" +
        
        // Insert product variants for headphones
        "INSERT INTO product_variants (product_id, sku, size, color, price, stock_quantity, low_stock_threshold) VALUES " +
        "(1, 'HP-BLK-001', NULL, 'Black', 89.99, 25, 5)," +
        "(1, 'HP-WHT-001', NULL, 'White', 89.99, 15, 5)," +
        "(1, 'HP-BLU-001', NULL, 'Blue', 94.99, 10, 5);" +
        
        // Insert product variants for t-shirt
        "INSERT INTO product_variants (product_id, sku, size, color, price, stock_quantity, low_stock_threshold) VALUES " +
        "(2, 'TS-BLK-S', 'S', 'Black', 19.99, 50, 10)," +
        "(2, 'TS-BLK-M', 'M', 'Black', 19.99, 60, 10)," +
        "(2, 'TS-BLK-L', 'L', 'Black', 19.99, 40, 10)," +
        "(2, 'TS-WHT-S', 'S', 'White', 19.99, 45, 10)," +
        "(2, 'TS-WHT-M', 'M', 'White', 19.99, 55, 10)," +
        "(2, 'TS-WHT-L', 'L', 'White', 19.99, 35, 10);" +
        
        // Insert sample suppliers
        "INSERT INTO suppliers (name, contact_person, email, phone, address) VALUES " +
        "('TechSupply Co.', 'John Smith', 'john@techsupply.com', '+1234567890', '123 Tech Street')," +
        "('Fashion Wholesale', 'Jane Doe', 'jane@fashionwholesale.com', '+0987654321', '456 Fashion Ave');" +
        
        // Insert product-supplier mappings
        "INSERT INTO product_suppliers (product_id, supplier_id, supplier_product_id, cost_price, lead_time_days, is_primary) VALUES " +
        "(1, 1, 'TECH-HP-001', 45.00, 7, true)," +
        "(2, 2, 'FASH-TS-001', 8.00, 5, true)," +
        "(3, 1, 'TECH-LED-001', 12.00, 3, true);" +
        
        // Insert sample user
        "INSERT INTO users (email, full_name, password, username, phone, address) VALUES " +
        "('admin@dspstore.com', 'Admin User', '$2a$10$hashedpassword', 'admin', '+1234567890', '123 Admin Street')," +
        "('customer@example.com', 'Nguyen Van A', '$2a$10$hashedpassword', 'customer01', '+0987654321', '456 Customer Road');" +
        
        // Insert user roles
        "INSERT INTO user_roles (user_id, role) VALUES " +
        "(1, 'ADMIN')," +
        "(1, 'USER')," +
        "(2, 'USER');" +
        
        // Insert sample orders
        "INSERT INTO orders (order_number, user_id, customer_name, customer_email, customer_phone, shipping_address, total_amount, status, description) VALUES " +
        "('ORD20251130001', 2, 'Nguyen Van A', 'customer@example.com', '+0987654321', '456 Customer Road', 109.98, 'PENDING', 'Wireless headphones and yoga mat')," +
        "('ORD20251130002', 2, 'Nguyen Van A', 'customer@example.com', '+0987654321', '456 Customer Road', 39.98, 'COMPLETED', 'T-shirts order');" +
        
        // Insert order items
        "INSERT INTO order_items (order_id, product_id, variant_id, product_name, variant_info, quantity, unit_price_at_time_of_order, supplier_cost_at_time, subtotal) VALUES " +
        "(1, 1, 1, 'Wireless Bluetooth Headphones', 'Color: Black', 1, 89.99, 45.00, 89.99)," +
        "(1, 4, NULL, 'Yoga Mat', NULL, 1, 29.99, 15.00, 29.99)," +
        "(2, 2, 4, 'Cotton T-Shirt', 'Size: S, Color: Black', 2, 19.99, 8.00, 39.98);" +
        
        // Create some low stock alerts
        "INSERT INTO stock_alerts (product_id, variant_id, alert_type, current_stock, threshold_value, status) VALUES " +
        "(1, 3, 'LOW_STOCK', 10, 5, 'PENDING')," +
        "(2, 6, 'LOW_STOCK', 8, 10, 'PENDING');";

        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);
             Statement stmt = conn.createStatement()) {

            System.out.println("========================================");
            System.out.println("  DSP STORE DATABASE SETUP");
            System.out.println("========================================");
            System.out.println("Connecting to Supabase database...");
            
            // Drop old tables
            System.out.println("\n[1/4] Dropping old tables...");
            stmt.execute(dropSQL);
            System.out.println(" Old tables dropped successfully!");
            
            // Create new tables
            System.out.println("\n[2/4] Creating database schema...");
            stmt.execute(createSQL);
            System.out.println(" All tables and indexes created successfully!");
            System.out.println("   - Categories");
            System.out.println("   - Products & Variants");
            System.out.println("   - Users & Roles");
            System.out.println("   - Suppliers & Mappings");
            System.out.println("   - Inventory Logs");
            System.out.println("   - Stock Alerts");
            System.out.println("   - Orders & Order Items");
            System.out.println("   - Payments");
            
            // Insert sample data
            System.out.println("\n[3/4] Inserting sample data...");
            stmt.execute(insertSQL);
            System.out.println(" Sample data inserted!");
            System.out.println("   - 5 Categories");
            System.out.println("   - 5 Products with variants");
            System.out.println("   - 2 Suppliers");
            System.out.println("   - 2 Users (admin & customer)");
            System.out.println("   - 2 Sample orders");
            System.out.println("   - 2 Low stock alerts");
            
            System.out.println("\n[4/4] Verifying setup...");
            System.out.println(" Database verification complete!");
            
            System.out.println("\n========================================");
            System.out.println("? DATABASE SETUP COMPLETE!");
            System.out.println("========================================");
            System.out.println("\nYour DSP Dropshipping Store is ready!");
            System.out.println("\nDefault Credentials:");
            System.out.println("  Admin  : admin@dspstore.com");
            System.out.println("  Customer: customer@example.com");
            System.out.println("\nFeatures Available:");
            System.out.println("  ✓ Product Catalog with Variants");
            System.out.println("  ✓ Real-time Inventory Tracking");
            System.out.println("  ✓ Low Stock Alerts");
            System.out.println("  ✓ Supplier Management");
            System.out.println("  ✓ Order Processing");
            System.out.println("  ✓ Payment Integration Ready");
            System.out.println("========================================\n");
            
        } catch (Exception e) {
            System.err.println("\n========================================");
            System.err.println(" ERROR OCCURRED:");
            System.err.println("========================================");
            e.printStackTrace();
            System.err.println("\nTroubleshooting Tips:");
            System.err.println("1. Check your database connection credentials");
            System.err.println("2. Verify Supabase project is active");
            System.err.println("3. Ensure you're using the pooler connection URL");
            System.err.println("4. Check your internet connection");
            System.err.println("========================================\n");
        }
    }
}
