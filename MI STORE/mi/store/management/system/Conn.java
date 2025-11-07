package mi.store.management.system;

import java.sql.*;

public class Conn {
    Connection c;
    Statement s;

    public Conn() {
        try {
            // Debug message to indicate the beginning of the connection process
            System.out.println("Initializing database connection...");

            // Load the JDBC driver
            System.out.println("Loading MySQL JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Set the connection URL
            String url = "jdbc:mysql://localhost:3306/mistoremanagement"; // Database URL
            String username = "root";  // MySQL username
            String password = "root";  // MySQL password

            // Debugging message to indicate the connection URL, username, and password being used
            System.out.println("Connecting to database with URL: " + url);
            System.out.println("Using username: " + username);

            // Establish the connection
            c = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully!");

            // Create the statement object for executing queries
            s = c.createStatement();
            System.out.println("Statement object created successfully.");

        } catch (ClassNotFoundException e) {
            // This will catch errors if the JDBC driver is not found
            System.err.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            // This will catch SQL exceptions such as invalid URL, credentials, or issues with the connection
            System.err.println("SQL Exception occurred while trying to connect to the database.");
            e.printStackTrace();
        } catch (Exception e) {
            // This will catch any other exceptions
            System.err.println("An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Create an instance of the Conn class to test the connection
        new Conn();
    }
}
