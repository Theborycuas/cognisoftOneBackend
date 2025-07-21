package com.cognisoftone.auth.service;

import com.cognisoftone.auth.interfaz.AuthService;
import com.cognisoftone.auth.interfaz.TokenRepository;
import com.cognisoftone.auth.model.TokenModel;
import com.cognisoftone.auth.model.TokenType;
import com.cognisoftone.users.repository.RoleRepository;
import com.cognisoftone.users.repository.UserRepository;
import com.cognisoftone.auth.request.LoginRequest;
import com.cognisoftone.auth.request.RefreshTokenRequest;
import com.cognisoftone.auth.request.RegisterRequest;
import com.cognisoftone.auth.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import com.cognisoftone.users.model.RoleModel;
import com.cognisoftone.users.model.UserModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    public AuthResponse register(RegisterRequest request) {
        Set<RoleModel> roleModels = new HashSet<>();
        for (String roleName : request.getRoles()) {
            RoleModel roleModel = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roleModels.add(roleModel);
        }

        UserModel user = new UserModel();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setIdentification(request.getIdentification());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConsent(request.getConsent());
        user.setRoleModels(roleModels);

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        TokenModel tokenModel = new TokenModel();
        tokenModel.setToken(refreshToken);
        tokenModel.setUser(user);
        tokenModel.setTokenType(TokenType.BEARER);
        tokenModel.setRevoked(false);
        tokenModel.setExpired(false);
        tokenRepository.save(tokenModel);

        return new AuthResponse(
                jwtToken,
                refreshToken,
                user.getEmail(),
                roleModels.stream().map(RoleModel::getName).collect(Collectors.toSet())
        );
    }


    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        TokenModel tokenModel = new TokenModel();
        tokenModel.setToken(refreshToken);
        tokenModel.setUser(user);
        tokenModel.setTokenType(TokenType.BEARER);
        tokenModel.setRevoked(false);
        tokenModel.setExpired(false);
        tokenRepository.save(tokenModel);

        return new AuthResponse(
                jwtToken,
                refreshToken,
                user.getEmail(),
                user.getRoleModels().stream().map(RoleModel::getName).collect(Collectors.toSet())
        );
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        UserModel user = (UserModel) userDetailsService.loadUserByUsername(username);

        boolean isValid = jwtService.isTokenValid(refreshToken, user);
        if (!isValid) {
            throw new RuntimeException("Invalid refresh token");
        }

        tokenRepository.findByToken(refreshToken).ifPresent(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            tokenRepository.save(token);
        });

        String newToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        TokenModel tokenModel = new TokenModel();
        tokenModel.setToken(newRefreshToken);
        tokenModel.setUser(user);
        tokenModel.setTokenType(TokenType.BEARER);
        tokenModel.setRevoked(false);
        tokenModel.setExpired(false);
        tokenRepository.save(tokenModel);

        return new AuthResponse(
                newToken,
                newRefreshToken,
                user.getEmail(),
                user.getRoleModels().stream()
                        .map(RoleModel::getName)
                        .collect(Collectors.toSet())
        );
    }

}

