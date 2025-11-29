import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class create_Table {
    private static final String jdbcURL = "jdbc:postgresql://db.kdvnogkvnpvbnedsmoui.supabase.co:5432/postgres";
    private static final String jdbcUser = "postgres";
    private static final String jdbcPass = "thisisnotai123";

    public static void main(String[] args) {
        
        // Step 1: Drop existing tables that need to be recreated
        String dropSQL = 
        "DROP TABLE IF EXISTS payments CASCADE;" +
        "DROP TABLE IF EXISTS order_items CASCADE;" +
        "DROP TABLE IF EXISTS orders CASCADE;";
        
        // Step 2: Create all tables with correct schema
        String createSQL = 
        // Products table
        "CREATE TABLE IF NOT EXISTS products (" +
        "    id SERIAL PRIMARY KEY," +
        "    name VARCHAR(255) NOT NULL," +
        "    description TEXT," +
        "    price NUMERIC(10,2) NOT NULL," +
        "    stock INT NOT NULL," +
        "    image_url TEXT" +
        ");" +
        
        // Users table
        "CREATE TABLE IF NOT EXISTS users (" +
        "    id BIGSERIAL PRIMARY KEY," +
        "    email VARCHAR(255) UNIQUE NOT NULL," +
        "    enabled BOOLEAN NOT NULL DEFAULT TRUE," +
        "    full_name VARCHAR(255)," +
        "    password VARCHAR(255) NOT NULL," +
        "    username VARCHAR(255) UNIQUE NOT NULL" +
        ");" +
        
        // User roles table
        "CREATE TABLE IF NOT EXISTS user_roles (" +
        "    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE," +
        "    role VARCHAR(100) NOT NULL," +
        "    PRIMARY KEY (user_id, role)" +
        ");" +
        
        // Orders table - WITH order_number and status
        "CREATE TABLE orders (" +
        "    id BIGSERIAL PRIMARY KEY," +
        "    order_number VARCHAR(50) UNIQUE NOT NULL," +
        "    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL," +
        "    customer_name TEXT NOT NULL," +
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
        "    product_name VARCHAR(255) NOT NULL," +
        "    quantity INT NOT NULL," +
        "    supplier_cost_at_time DOUBLE PRECISION NOT NULL," +
        "    unit_price_at_time_of_order DOUBLE PRECISION NOT NULL," +
        "    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE" +
        ");" +
        
        // Payments table
        "CREATE TABLE payments (" +
        "    payment_id SERIAL PRIMARY KEY," +
        "    order_id VARCHAR(50) NOT NULL," +
        "    user_id BIGINT," +
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
        
        // Create indexes
        "CREATE INDEX idx_vnp_txn_ref ON payments(vnp_txn_ref);" +
        "CREATE INDEX idx_payment_order_id ON payments(order_id);" +
        "CREATE INDEX idx_payment_status ON payments(payment_status);" +
        "CREATE INDEX idx_payment_user_id ON payments(user_id);" +
        "CREATE INDEX idx_order_number ON orders(order_number);" +
        "CREATE INDEX idx_order_status ON orders(status);" +
        "CREATE INDEX idx_order_user_id ON orders(user_id);";
        
        // Step 3: Insert sample data
        String insertSQL = 
        "INSERT INTO orders (order_number, user_id, customer_name, total_amount, status, description) " +
        "VALUES " +
        "('ORD20251129001', 1, 'Nguyen Van A', 150000, 'PENDING', 'Order for Product A and B')," +
        "('ORD20251129002', 1, 'Nguyen Van A', 250000, 'PENDING', 'Order for Product C')," +
        "('ORD20251129003', 1, 'Nguyen Van A', 100000, 'PENDING', 'Service Package')," +
        "('ORD20251129004', 1, 'Nguyen Van A', 500000, 'PENDING', 'Premium Product Bundle')," +
        "('ORD20251129005', 1, 'Nguyen Van A', 75000, 'PENDING', 'Basic Service');";

        try (Connection conn = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connecting to database...");
            
            // Drop old tables
            System.out.println("Dropping old tables...");
            stmt.execute(dropSQL);
            System.out.println("✅ Old tables dropped!");
            
            // Create new tables
            System.out.println("Creating new tables...");
            stmt.execute(createSQL);
            System.out.println("✅ All tables created successfully!");
            
            // Insert sample data
            System.out.println("Inserting sample orders...");
            stmt.execute(insertSQL);
            System.out.println("✅ Sample orders inserted!");
            
            System.out.println("\n🎉 Database setup complete!");
            System.out.println("You can now test your payment integration!");
            
        } catch (Exception e) {
            System.err.println("❌ Error:");
            e.printStackTrace();
        }
    }
}
