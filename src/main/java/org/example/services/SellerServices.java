package org.example.services;

import org.example.database.DBConnection;
import org.example.models.Product;
import org.example.models.Seller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SellerServices {
    private final DBConnection dbConnection;

    public SellerServices(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }


    public List<Seller> getAllSellers() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Seller> sellers = new ArrayList<>();
       try{
           connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Sellers");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
               String name = resultSet.getString("name");
               Seller seller = new Seller(name);
               sellers.add(seller);
           }
           resultSet.close();
           connection.close();
           return sellers;
       } finally {
           dbConnection.closeConnection(connection);
       }
    }

    public boolean isSellerAlreadyExists(String sellerName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Sellers WHERE name = ?");
            statement.setString(1, sellerName);
            resultSet = statement.executeQuery();

            boolean isSellerExists = false;
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                if (name != null) {
                    isSellerExists = true;
                }
            }
            resultSet.close();
            connection.close();
            return isSellerExists;
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    public Seller getSellerById(int sellerId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Sellers WHERE id = ?");
            statement.setInt(1, sellerId);
            resultSet = statement.executeQuery();

            Seller seller = null;
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                seller = new Seller(name);
            }
            resultSet.close();
            connection.close();
            return seller;
        }finally {
            dbConnection.closeConnection(connection);
        }
    }

    public Seller getSellerByName(String sellerName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Sellers WHERE name = ?");
            statement.setString(1, sellerName);
            resultSet = statement.executeQuery();

            Seller seller = null;
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                seller = new Seller(name);
            }
            resultSet.close();
            connection.close();
            return seller;
        }finally {
            dbConnection.closeConnection(connection);
        }
    }

    public int getSellerId(String sellerName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Sellers WHERE name = ?");
            statement.setString(1, sellerName);
            resultSet = statement.executeQuery();

            int sellerId =-1;
            if (resultSet.next()) {
                sellerId = resultSet.getInt("id");
            }
            resultSet.close();
            connection.close();
            return sellerId;
        }finally {
            dbConnection.closeConnection(connection);
        }
    }

    public void createSeller(String sellerName) throws SQLException{
        Connection connection = null;
        try{
            connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Sellers (name) VALUES (?)");
            statement.setString(1, sellerName);
            statement.executeUpdate();
            connection.close();
        }finally {
            dbConnection.closeConnection(connection);
            }
        }



}
