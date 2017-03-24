package com.netcompany.app;

import com.netcompany.app.auth.DiscoveryDocument;
import com.netcompany.app.auth.OpenIdConnectAuth;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    @Qualifier("google")
    public OpenIdConnectAuth googleAuth(
            @Value("${open-id-connect.google.client-id}") final String clientId,
            @Value("${open-id-connect.google.secret}") final String secret,
            @Value("${open-id-connect.google.redirect-uri}") final String redirectUri,
            @Qualifier("google") final DiscoveryDocument discoveryDocument) {
        return new OpenIdConnectAuth(clientId, secret, redirectUri, discoveryDocument);
    }

    @Bean
    @Qualifier("google")
    public DiscoveryDocument googleDiscoveryDocument(
            @Value("${open-id-connect.google.discovery-document-url}") final String discoveryDocumentUrl) {
        return new DiscoveryDocument(discoveryDocumentUrl);
    }
}
