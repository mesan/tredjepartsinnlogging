package com.netcompany.app.services;

import com.netcompany.app.SparkService;
import org.eclipse.jetty.server.Response;
import org.springframework.stereotype.Component;
import spark.Session;

import static spark.Spark.get;
import static spark.Spark.post;

@Component
public class UserService implements SparkService {
    @Override
    public void init() {
        post("/api/login", (req, res) -> {
            final Session session = req.session(true);
            session.attribute("username", req.body());
            res.status(Response.SC_NO_CONTENT);
            return res;
        });

        post("/api/logout", (req, res) -> {
            req.session().invalidate();
            res.status(Response.SC_NO_CONTENT);
            return res;
        });

        get("/api/user", (req, res) -> req.session().attribute("username"));
    }
}
