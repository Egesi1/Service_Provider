package org.example.models;

public class Seller {
    private String name;

    public Seller(String sellerName){
        this.name = sellerName.trim();
    }

    //getter and setter
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}
