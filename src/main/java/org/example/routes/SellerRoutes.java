package org.example.routes;

import io.javalin.Javalin;
import org.example.controllers.SellerController;

public class SellerRoutes implements Route{
    private final SellerController sellerController;

    public SellerRoutes(SellerController sellerController){
        this.sellerController = sellerController;

    }

    @Override
    public void register(Javalin app) {
        app.get("/seller", sellerController::getAllSellers);
        app.post("/seller", sellerController::createSeller);
    }
}
