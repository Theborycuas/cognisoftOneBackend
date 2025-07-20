package com.cognisoftone.interfaces;

import com.cognisoftone.request.LoginRequest;
import com.cognisoftone.request.RegisterRequest;
import com.cognisoftone.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
