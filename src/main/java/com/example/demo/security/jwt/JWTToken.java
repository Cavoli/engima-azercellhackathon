package com.example.demo.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken {
    private String idToken;

    private String expireIn;

    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn;
    }

    public JWTToken(String idToken) {
        this.idToken = idToken;
    }

    public JWTToken(String idToken, String expireIn) {
        this.idToken = idToken;
        this.expireIn = expireIn;
    }

    @JsonProperty("id_token")
    public String getIdToken() {
        return idToken;
    }

    @JsonProperty("expire_in")
    public String getExpireIn() {
        return expireIn;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
