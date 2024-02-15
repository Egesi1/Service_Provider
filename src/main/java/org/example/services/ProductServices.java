package org.example.services;

import org.example.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductServices {
    private final List<Product> products = new ArrayList<>();

    // get all products
    public List<Product> getAllProducts(){
        return products;
    }

    //get single product
    public Product getProduct(String id){
        for(Product product: products){
            if (id.equals(product.getId())){
                return product;
            }
        }
        return null;
    }

    // create new product
    public void createProduct(Product product){
        products.add(product);
    }

    // update a product
    public void updateProduct(String productId, Product updatedProduct){
        for(int i=0;i< products.size();i++){
            Product product = products.get(i);
            if (product.getId().equals(productId)){
                // update this product
                products.set(i, updatedProduct);
            }
        }
    }

    // get product by id
    public Product getProductById(String id){
        for(Product product: products){
            if (id.equals(product.getId())){
                return product;
            }
        }

        return null;
    }

    //check if product exists
    public boolean isProductAlreadyExists(String id){
        for(Product product: products){
            if (id.equals(product.getId())){
                return true;
            }
        }
        return false;
    }

    // delete a product
    public boolean deleteProduct(String productId){
        for(Product product: products){
            if (product.getId().equals(productId)){
                products.remove(product);
                return true;
            }
        }
        return false;
    }

}
