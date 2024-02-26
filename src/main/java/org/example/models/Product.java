package org.example.models;

public class Product {

    private String productId,productName;
    private double proudctPrice;

    private Seller seller;

    public Product(String productId, String productName, Seller seller, double proudctPrice){
        this.productId = productId;
        this.productName = productName;
        this.seller = seller;
        this.proudctPrice = proudctPrice;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return proudctPrice;
    }

    public void setProductPrice(double price) {
        this.proudctPrice = price;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
