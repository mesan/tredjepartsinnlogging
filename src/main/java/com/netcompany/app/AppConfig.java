package com.netcompany.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spark.Spark;

import java.util.List;

@SpringBootApplication
public class AppConfig {
    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final List<SparkService> services) {
        Spark.staticFileLocation("/public");

        return args -> services.forEach(SparkService::init);
    }
}
