package com.netcompany.app.services;

import com.netcompany.app.SparkService;
import org.springframework.stereotype.Component;

import static spark.Spark.get;

@Component
public class HelloWorldService implements SparkService{
    @Override
    public void init() {
        get("/hello-world", (req, res) -> "Hello World");
    }
}
