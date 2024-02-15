package org.example.routes;

import io.javalin.Javalin;
import org.example.controllers.ProductController;

public class ProductRoutes implements Route {
    private final ProductController productController;

    public ProductRoutes(ProductController productController){
        this.productController =  productController;
    }

    @Override
    public void register(Javalin app){
        app.get("/product", productController::getAllProducts);
        app.get("/product/{id}", productController::getProduct);
        app.post("/product",productController::createProduct);
        app.put("/product/{id}", productController::updateProduct);
        app.delete("/product/{id}", productController::deleteProduct);
    }

}
