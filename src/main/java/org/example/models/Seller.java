package org.example.models;

public class Seller {
    private String sellerName;

    public Seller(String sellerName){
        this.sellerName = sellerName.trim();
    }

    //getter and setter
    public String getSellerName() {
        return sellerName;
    }
    public void setSellerName(String sellerName){
        this.sellerName = sellerName;
    }
}
