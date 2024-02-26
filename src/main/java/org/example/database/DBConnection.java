package org.example.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {

    private static final String DB_URL = "jdbc:h2:tcp://localhost/~/data";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private final DataSource dataSource;

    public DBConnection() {
        // Use HikariCP for connection pooling (optional)
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(DB_URL);
        ds.setUsername(DB_USER);
        ds.setPassword(DB_PASSWORD);
        // Configure additional pool settings as needed
        this.dataSource = ds;
    }

    public void createTables() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();


        // Create Sellers table
        statement.execute("CREATE TABLE IF NOT EXISTS Sellers (\n" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(255) NOT NULL\n" +
                ");");
        // Create Products table
        statement.execute("CREATE TABLE IF NOT EXISTS Products ("
                + "id VARCHAR(255) PRIMARY KEY NOT NULL,"
                + "name VARCHAR(255) NOT NULL,"
                + "price DOUBLE NOT NULL CHECK (price > 0),"
                + "seller_id INT NOT NULL,"
                + "FOREIGN KEY (seller_id) REFERENCES Sellers(id))");




        closeConnection(connection);
    }


    public void resetSellerTable(){
        try  {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            // Delete all data from Sellers table
            statement.executeUpdate("DELETE FROM Sellers");
            System.out.println("Sellers table reset successfully!");
            closeConnection(connection);

        } catch (SQLException e) {
            System.out.println("Error resetting database!");
            e.printStackTrace();
        }
    }
    public  void resetProductTable() {
        try  {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            // Delete all data from Products table
            statement.executeUpdate("DELETE FROM Products");
            System.out.println("Products table reset successfully!");

            closeConnection(connection);

        } catch (SQLException e) {
            System.out.println("Error resetting database!");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
