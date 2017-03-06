package com.netcompany.app.services;

import com.netcompany.app.SparkService;
import org.springframework.stereotype.Component;

import static spark.Spark.get;

@Component
public class HelloWorld2Service implements SparkService {
    @Override
    public void init() {
        get("/hello-world2", (req, res) -> "Hello World 2");
    }
}
