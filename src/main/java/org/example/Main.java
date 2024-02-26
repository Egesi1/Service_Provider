package org.example;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import org.example.controllers.ProductController;
import org.example.controllers.SellerController;
import org.example.database.DBConnection;
import org.example.routes.ProductRoutes;
import org.example.routes.SellerRoutes;
import org.example.services.ProductServices;
import org.example.services.SellerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        // Load configuration from .env file
        Dotenv dotenv = Dotenv.load();

        //getting port from .env file
        int port = Integer.parseInt(dotenv.get("PORT","5500"));

        // creating javalin server
        var app = Javalin.create();

        // run before every request
        app.before(ctx-> LOG.info("Request: {} {} {}", ctx.method(), ctx.path(), ctx.url()));


        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.createTables();
        }catch (SQLException error){
            System.out.println(error);
        }

        // initializing services
        SellerServices sellerServices = new SellerServices(dbConnection);
        ProductServices productServices = new ProductServices(dbConnection, sellerServices);

        // initializing controllers
        SellerController sellerController = new SellerController(sellerServices);
        ProductController productController = new ProductController(productServices, sellerServices);

        // initializing routes
        SellerRoutes sellerRoutes = new SellerRoutes(sellerController);
        ProductRoutes productRoutes = new ProductRoutes(productController);
        // starting app
        sellerRoutes.register(app);
        productRoutes.register(app);

//        // error
//        app.error(404, (ctx) -> {
//            ctx.status(404);
//            // Optionally, send a custom 404 response body
//            ctx.json("Requested path does not exists");
//        });
        app.start(port);
    }
}