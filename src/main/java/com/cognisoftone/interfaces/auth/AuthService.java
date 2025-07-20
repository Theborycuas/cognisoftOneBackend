package com.cognisoftone.interfaces.auth;

import com.cognisoftone.request.auth.LoginRequest;
import com.cognisoftone.request.auth.RegisterRequest;
import com.cognisoftone.response.auth.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
