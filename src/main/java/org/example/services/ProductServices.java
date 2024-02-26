package org.example.services;

import org.example.database.DBConnection;
import org.example.models.Product;
import org.example.models.Seller;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductServices {
    private final DBConnection dbConnection;
    private final SellerServices sellerServices;

    public ProductServices(DBConnection dbConnection, SellerServices sellerServices) {
        this.dbConnection = dbConnection;
        this.sellerServices = sellerServices;
    }

    // get all products
    public List<Product> getAllProducts() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Product> products = new ArrayList<>();
        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Products");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String productId = resultSet.getString("id");
                String productName = resultSet.getString("name");
                double productPrice = resultSet.getDouble("price");
                int sellerId = resultSet.getInt("seller_id");

                Seller seller = sellerServices.getSellerById(sellerId);

                Product product = new Product(productId, productName, seller, productPrice);
                products.add(product);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            dbConnection.closeConnection(connection);
        }
        return products;
    }

//    // create new product
    public void createProduct(Product product) throws  SQLException{
        Connection connection = null;
        PreparedStatement statement;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(
                    "INSERT INTO Products (id,name, seller_id, price) VALUES (?, ?, ?, ?)");
            statement.setString(1, product.getProductId());
            statement.setString(2,product.getProductName());
            statement.setInt(3,sellerServices.getSellerId(product.getSeller().getSellerName()));
            statement.setDouble(4, product.getProductPrice());
            statement.executeUpdate();
            connection.close();
        }finally {
            dbConnection.closeConnection(connection);
        }
    }

//
//    // update a product
    public void updateProduct(Product product) throws SQLException{
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement(
                    "UPDATE Products SET name = ?, seller_id = ?, price = ? WHERE id = ?");
            statement.setString(1, product.getProductName());
            statement.setInt(2, sellerServices.getSellerId(product.getSeller().getSellerName()));
            statement.setDouble(3, product.getProductPrice());
            statement.setString(4, product.getProductId());
            statement.executeUpdate();
        }  finally {
            dbConnection.closeConnection(connection);
        }
    }
// get product by Id
    public Product getProductById(String productId) throws SQLException{
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Products WHERE id = ?");
            statement.setString(1, productId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String productName = resultSet.getString("name");
                double productPrice = resultSet.getDouble("price");
                int sellerId = resultSet.getInt("seller_id");
                Seller seller = sellerServices.getSellerById(sellerId);
                return new Product(productId, productName, seller, productPrice);
            } else {
                return null; // Product not found
            }
        } finally {
            dbConnection.closeConnection(connection);
        }
    }


//    // get product
    public Product getProduct(String productId) throws  SQLException{
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Products WHERE id = ?");
            statement.setString(1, productId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Create a Product object with retrieved data
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                Seller seller = sellerServices.getSellerById(resultSet.getInt("seller_id"));
                double price =  resultSet.getDouble("price");

                return new Product(id,name,seller,price);
            } else {
                return null; // Product not found
            }
        } finally {
            dbConnection.closeConnection(connection);
        }
    }
//
    //check if product exists
    public boolean isProductAlreadyExists(String id) throws SQLException{
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Products WHERE id = ?");
            statement.setString(1,id);
            resultSet = statement.executeQuery();
            return resultSet.next();
        }
        finally {
            dbConnection.closeConnection(connection);
        }
    }

    // delete a product
    public void deleteProduct(String productId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement;

        try {
            connection = dbConnection.getConnection();
            statement = connection.prepareStatement("DELETE FROM Products WHERE id = ?");
            statement.setString(1, productId);
            statement.executeUpdate();

        } finally {
            dbConnection.closeConnection(connection);
        }
    }

}
