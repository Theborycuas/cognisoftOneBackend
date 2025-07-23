package com.cognisoftone.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String displayName;
    private String email;
    private Set<String> roles;
    private String idToken;
    private String refreshToken;
    private long expiresIn;
    private boolean registered;
    private String kind;
    private String localId;
    private String avatarUrl;
}