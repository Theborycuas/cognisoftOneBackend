package com.cognisoftone.auth.interfaces;

import com.cognisoftone.auth.request.LoginRequest;
import com.cognisoftone.auth.request.RefreshTokenRequest;
import com.cognisoftone.auth.request.RegisterRequest;
import com.cognisoftone.auth.response.AuthRegisterResponse;
import com.cognisoftone.auth.response.AuthResponse;

public interface AuthService {
    AuthRegisterResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}
