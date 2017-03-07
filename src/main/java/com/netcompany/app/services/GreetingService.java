package com.netcompany.app.services;

import com.netcompany.app.SparkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static spark.Spark.get;

@Component
public class GreetingService implements SparkService {
    private final String greeting;

    public GreetingService(@Value("${greeting}") final String greeting) {
        this.greeting = greeting;
    }

    @Override
    public void init() {
        get("/greeting", (req, res) -> greeting);
    }
}
