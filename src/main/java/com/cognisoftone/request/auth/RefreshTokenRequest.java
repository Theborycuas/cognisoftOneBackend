package com.cognisoftone.request.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
