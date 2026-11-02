package atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Database Connection Manager for the ATM Simulation.
 *
 * This class handles establishing and maintaining JDBC connections to MySQL.
 * 
 * NOTE FOR STUDENTS:
 * 1. Ensure MySQL Server is running on your machine (Default port: 3306).
 * 2. Create the database using the database.sql script.
 * 3. Replace the placeholder DB_PASSWORD below with your actual MySQL root password.
 */
public class DBConnection {

    // ========================================================================
    // DATABASE CONFIGURATION CONSTANTS
    // ========================================================================
    // JDBC connection URL: Points to local MySQL server and atm_db database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    
    // MySQL username (default is 'root')
    private static final String DB_USER = "root";

    // Configured for local MySQL root (empty password)
    private static final String DB_PASSWORD = "";

    // Static block to register MySQL JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found! Ensure mysql-connector-j is in pom.xml dependencies.");
            e.printStackTrace();
        }
    }

    // Private constructor to prevent instantiation of utility class
    private DBConnection() {}

    /**
     * Establishes and returns a new JDBC Connection to MySQL.
     *
     * @return a valid java.sql.Connection object
     * @throws SQLException if a database access error occurs or credentials are wrong
     */
    public static Connection getConnection() throws SQLException {
        if ("YOUR_MYSQL_PASSWORD_HERE".equals(DB_PASSWORD)) {
            System.err.println("\n=======================================================================");
            System.err.println("[WARNING] Default placeholder password is still in use in DBConnection.java!");
            System.err.println("If your MySQL requires a password, please edit DB_PASSWORD in DBConnection.java.");
            System.err.println("=======================================================================\n");
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Helper method to verify database connectivity.
     * Useful for diagnostics before starting the Swing UI.
     *
     * @return true if connection succeeds, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
