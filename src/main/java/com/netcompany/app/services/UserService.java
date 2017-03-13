package com.netcompany.app.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netcompany.app.SparkService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.stereotype.Component;
import spark.Session;

import java.lang.reflect.Type;
import java.util.Map;

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

        post("/api/verify", (req, res) -> {
            String tokenString = req.body();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + tokenString)
                    .get()
                    .build();
            okhttp3.Response tokenVerificationResponse = client.newCall(request).execute();

            if (200 == tokenVerificationResponse.code()) {
                Type type = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> jwtMap = new Gson().fromJson(tokenVerificationResponse.body().string(), type);
                final Session session = req.session(true);
                session.attribute("username", jwtMap.get("sub"));
                res.body(jwtMap.get("name"));
                res.status(Response.SC_NO_CONTENT);
            } else {
                res.status(Response.SC_FORBIDDEN);
            }
            return res;
        });
    }
}
