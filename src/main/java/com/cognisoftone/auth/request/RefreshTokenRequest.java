package com.cognisoftone.auth.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
