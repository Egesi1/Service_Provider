package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.example.models.Product;
import org.example.models.Seller;
import org.example.services.ProductServices;
import org.example.services.SellerServices;

import java.util.List;

public class ProductController {
    private final ProductServices productServices;
    private final SellerServices sellerServices;

    public ProductController(ProductServices productServices, SellerServices sellerServices){
        this.productServices = productServices;
        this.sellerServices = sellerServices;
    }

    // GET /product - get all products
    public void getAllProducts(Context ctx){
        List<Product> products = this.productServices.getAllProducts();
        ctx.json(products);
    }

    // GET /product - get single product
    public void getProduct(Context ctx){
        String productId = ctx.pathParam("id");

        Product product = productServices.getProduct(productId);
        if (product == null) {
            ctx.json(404).json("Product not found.");
        return;
        }

        ctx.json(product);

    }

    // POST /product - create a new product
    public void createProduct(Context ctx){
        // if user make a post request without sending a body
        if (ctx.body().isEmpty()) {
            ctx.status(400).json("Request Missing body");
            return;
        }

        String rawBody = ctx.body();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(rawBody);

            // Check for existence of product id
            String productId = jsonNode.has("productId") ? jsonNode.get("productId").asText():null;
            if (productId == null) {
                ctx.status(400).json("Product id is required");
                return;
            }

            //check if the product already exists
            boolean isProductAlreadyExists = productServices.isProductAlreadyExists(productId);

            if (isProductAlreadyExists) {
                ctx.status(400).json("Product already exists");
                return;
            }
            // Check for existence of productName
            String productName = jsonNode.has("productName") ? jsonNode.get("productName").asText():null;
            if (productName == null) {
                ctx.status(400).json("Product name is required");
                return;
            }
            // Check for existence of productPrice
            Double productPrice = jsonNode.has("productPrice") ? jsonNode.get("productPrice").asDouble():null;
            if (productPrice == null) {
                ctx.status(400).json("Product price is required");
                return;
            }

            if (productPrice <=0){
                ctx.status(400).json("Product price must be greater than 0");
                return;
            }

            // Check for existence of sellerName
            String sellerName = jsonNode.has("sellerName") ? jsonNode.get("sellerName").asText():null;
            if (sellerName == null) {
                ctx.status(400).json("Seller name is required");
                return;
            }

            // checking if the seller exists in DB
            Seller seller =sellerServices.getSeller(sellerName);
            System.out.println(sellerServices.getAllSellers());

            if (seller == null){
                ctx.status(400).json("Seller does not exist");
                return;
            }

           //creating product
            Product product = new Product(productId,productName,seller,productPrice);
            productServices.createProduct(product);
            ctx.json(product);
        }catch (IllegalArgumentException e){
            ctx.status(400).json(e.getMessage());
        } catch (JsonProcessingException e) {
            ctx.status(400).json("Cannot process data.");
        }


    }

    // PUT /product - update a product
    public void updateProduct(Context ctx){
        String productId = ctx.pathParam("id");

        // get a product
        Product product = productServices.getProductById(productId);
        // if the product with given id does not exist
        if (product == null) {
            ctx.status(404).json("Product not found");
            return;
        }

        String rawText = ctx.body();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            JsonNode jsonNode = objectMapper.readTree(rawText);

            // check if user provide seller. then is must exist already
            if (jsonNode.has("sellerName")){
                Seller seller = sellerServices.getSeller(jsonNode.get("sellerName").asText());
                if (seller == null){
                    ctx.status(404).json("Seller does not exists");
                    return;
                }
            }

            // check if the price is greater than 0
            if (jsonNode.has("productPrice")){
                if (jsonNode.get("productPrice").asDouble()<= 0){
                    ctx.status(400).json("Product price must be greater than 0");
                    return;
                }
            }

            // make a new object with previous data
            Product updatedProduct = new Product(product.getId(), product.getName(),product.getSeller(),product.getProductPrice());

            // if product name needs to be updated
            if (jsonNode.has("productName")){
                String productName = jsonNode.get("productName").asText();
                updatedProduct.setName(productName);
            }


            // if product price needs to be updated
            if (jsonNode.has("productPrice")){
                double productPrice = jsonNode.get("productPrice").asDouble();
                updatedProduct.setProductPrice(productPrice);
            }

            // if seller needs to be changed
            if (jsonNode.has("sellerName")){
                updatedProduct.setSeller(sellerServices.getSeller(jsonNode.get("sellerName").asText()));
            }

            // store the updated product
            productServices.updateProduct(productId, updatedProduct);
            ctx.json(updatedProduct);
        }catch (JsonProcessingException e){
            ctx.status(400).json("Cannot process send data");
        }
    }

    // DELETE - /product/{id} - delete product
    public void deleteProduct(Context ctx){
        String productId = ctx.pathParam("id");
        productServices.deleteProduct(productId);
        ctx.status(200).json("Deleted product successfully");
    }
}
