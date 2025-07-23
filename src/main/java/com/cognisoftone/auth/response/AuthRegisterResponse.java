package com.cognisoftone.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterResponse {
    private String idToken;
    private String refreshToken;
}