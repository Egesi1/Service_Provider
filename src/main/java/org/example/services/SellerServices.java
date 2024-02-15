package org.example.services;

import org.example.models.Seller;

import java.util.ArrayList;
import java.util.List;

public class SellerServices {
    private final List<Seller> sellers = new ArrayList<>();

    public List<Seller> getAllSellers(){
        return sellers;
    }

    public Seller createSeller(Seller seller){
        // Check if a seller with the same name already exists
        if (sellers.stream().anyMatch(existingSeller -> existingSeller.getName().equals(seller.getName()))) {
            throw new IllegalArgumentException("Seller with the same name already exists");
        }

        // If not, add the new seller
        sellers.add(seller);
        return seller;
    }

    //check if the seller exists
    public Seller getSeller(String sellerName){
        for(Seller seller: sellers){
            if (sellerName.equals(seller.getName())){
                return seller;
            }
        }
        return null;
    }
}
