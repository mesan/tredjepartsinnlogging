package com.netcompany.app.services;

import com.netcompany.app.SparkService;
import com.netcompany.app.dao.SecretDao;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.put;

@Component
public class SecretService implements SparkService {
    private final SecretDao secretDao;

    @Autowired
    public SecretService(final SecretDao secretDao) {
        Objects.requireNonNull(secretDao);
        this.secretDao = secretDao;
    }

    @Override
    public void init() {
        get("/api/secret", (req, res) -> {
            final String username = req.session().attribute("username");
            if (username == null)
                halt(403, "You are not welcome here.");
            return secretDao.get(username);
        });

        put("/api/secret", (req, res) -> {
            final String username = req.session().attribute("username");
            secretDao.edit(username, req.body());
            res.status(Response.SC_NO_CONTENT);
            return res;
        });
    }
}
