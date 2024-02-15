package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.example.models.Seller;
import org.example.services.SellerServices;
import java.util.List;

public class SellerController {
    private final SellerServices sellerServices;

    public SellerController(SellerServices sellerServices){
        this.sellerServices = sellerServices;
    }

//    GET /seller - Get all sellers
    public void getAllSellers(Context ctx){
        List<Seller> sellers = sellerServices.getAllSellers();
        ctx.json(sellers);
    }

    // POST /seller - Create a new seller
    public void createSeller(Context ctx){
        // if user make a post request without sending a body
        if (ctx.body().isEmpty()) {
            ctx.status(400).json("Request Missing body");
            return;
        }

        String rawBody = ctx.body();
        // Parse JSON string to JsonNode
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            JsonNode jsonNode = objectMapper.readTree(rawBody);
            // Read values from JsonNode
            String sellerName = jsonNode.has("sellerName") ? jsonNode.get("sellerName").asText() : null;
            if (sellerName == null){
                ctx.status(400).json("Seller name is required");
                return;
            }

            //create a new seller
            Seller seller = new Seller(sellerName);
            sellerServices.createSeller(seller);
            ctx.json(seller);

        }catch (IllegalArgumentException e){
            ctx.status(400).json(e.getMessage());
        } catch (JsonProcessingException e) {
            ctx.status(400).json("Cannot process send data");
        }
    }
}


