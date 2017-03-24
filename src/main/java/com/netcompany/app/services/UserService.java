package com.netcompany.app.services;

import com.netcompany.app.SparkService;
import com.netcompany.app.auth.OpenIdConnectAuth;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;

import static spark.Spark.*;

@Component
public class UserService implements SparkService {
    private final OpenIdConnectAuth openIdConnectAuth;

    @Autowired
    public UserService(@Qualifier("google") final OpenIdConnectAuth openIdConnectAuth) {
        Objects.requireNonNull(openIdConnectAuth);
        this.openIdConnectAuth = openIdConnectAuth;
    }

    @Override
    public void init() {
        get("api/login", (req, res) -> {
            final String state = new BigInteger(130, new SecureRandom()).toString(32);
            req.session(true);
            req.session().attribute("state", state);
            res.redirect(openIdConnectAuth.createAuthenticationUrl(state));
            return res;
        });

        get("api/auth/code", (req, res) -> {
            if (!req.queryParams("state").equals(req.session().attribute("state"))) {
                req.session().invalidate();
                halt(401, "Invalid state parameter.");
            }

            final String email = openIdConnectAuth.exchangeCodeForToken(req.queryParams("code"));

            req.session().attribute("username", email);

            res.redirect("/");
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
