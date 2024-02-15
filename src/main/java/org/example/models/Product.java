package org.example.models;

public class Product {

    private String id, name;
    private double price;

    private Seller seller;

    public Product(String productId, String productName, Seller seller, double proudctPrice){
        this.id = productId;
        this.name = productName;
        this.seller = seller;
        this.price = proudctPrice;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProductPrice() {
        return price;
    }

    public void setProductPrice(double price) {
        this.price = price;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
