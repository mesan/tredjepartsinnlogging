package com.netcompany.app.auth;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class OpenIdConnectAuth {
    private final String clientId;
    private final String secret;
    private final String redirectUri;
    private final DiscoveryDocument discoveryDocument;

    public OpenIdConnectAuth(final String clientId,
                             final String secret,
                             final String redirectUri,
                             final DiscoveryDocument discoveryDocument) {
        Objects.requireNonNull(clientId);
        this.clientId = clientId;
        Objects.requireNonNull(secret);
        this.secret = secret;
        Objects.requireNonNull(redirectUri);
        this.redirectUri = redirectUri;
        Objects.requireNonNull(discoveryDocument);
        this.discoveryDocument = discoveryDocument;
    }

    public String createAuthenticationUrl(final String state) {
        return discoveryDocument.getAuthorizationEndpoint() + "?" +
                "client_id=" + clientId + "&" +
                "response_type=code&" +
                "scope=openid email&" +
                "redirect_uri=" + redirectUri + "&" +
                "state=" + state;
    }

    public String exchangeCodeForToken(final String code) throws IOException {
        final FormBody form = new FormBody.Builder()
                .add("code", code)
                .add("client_id", clientId)
                .add("client_secret", secret)
                .add("redirect_uri", redirectUri)
                .add("grant_type", "authorization_code")
                .build();

        final Request request = new Request.Builder()
                .url(discoveryDocument.getTokenEndpoint())
                .post(form)
                .build();

        final Response response = new OkHttpClient()
                .newCall(request)
                .execute();

        final String token = new ObjectMapper().readTree(response.body().bytes()).get("id_token").asText();

        final DecodedJWT jwt = JWT.decode(token);

        return jwt.getClaim("email").asString();
    }
}
