package com.netcompany.app.dao;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecretDao {
    private final Map<String, String> secretStore = new ConcurrentHashMap<>();

    public void edit(final String username, final String secret) {
        secretStore.put(username, secret);
    }

    public String get(final String username) {
        return secretStore.get(username);
    }
}
