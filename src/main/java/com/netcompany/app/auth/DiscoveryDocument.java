package com.netcompany.app.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DiscoveryDocument {
    private final String discoveryDocumentUrl;
    private static Map<String, Map.Entry<Instant, JsonNode>> cache = new ConcurrentHashMap<>();

    public DiscoveryDocument(final String discoveryDocumentUrl) {
        Objects.requireNonNull(discoveryDocumentUrl);
        this.discoveryDocumentUrl = discoveryDocumentUrl;
    }

    public String getAuthorizationEndpoint() {
        return getFromCache(discoveryDocumentUrl).get("authorization_endpoint").asText();
    }

    public String getTokenEndpoint() {
        return getFromCache(discoveryDocumentUrl).get("token_endpoint").asText();
    }

    private JsonNode getFromCache(final String url) {
        if (cache.containsKey(url) && cache.get(url).getKey().isAfter(Instant.now())) {
            return cache.get(url).getValue();
        }

        try {
            final Response response = new OkHttpClient().newCall(new Request.Builder()
                    .url(url)
                    .get()
                    .build())
                    .execute();

            if (response.code() == 200) {
                final JsonNode node = new ObjectMapper().readTree(response.body().bytes());

                cache.put(url, Maps.immutableEntry(
                        Instant.now().plus(response.cacheControl().maxAgeSeconds(), ChronoUnit.SECONDS),
                        node));

                return node;
            }
            throw new RuntimeException("Retrieving the discovery document failed with status code: " + response.code());
        } catch (final IOException e) {
            throw new RuntimeException("Could not get the discovery document from the following url: "
                    + discoveryDocumentUrl);
        }
    }
}
