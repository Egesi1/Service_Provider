package org.example.routes;

import io.javalin.Javalin;

public interface Route {
       public void register(Javalin app);
}
