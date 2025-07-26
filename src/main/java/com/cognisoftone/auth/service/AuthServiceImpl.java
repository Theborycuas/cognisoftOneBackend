package com.cognisoftone.auth.service;

import com.cognisoftone.auth.interfaces.AuthService;
import com.cognisoftone.auth.interfaces.TokenRepository;
import com.cognisoftone.auth.model.TokenModel;
import com.cognisoftone.auth.model.TokenType;
import com.cognisoftone.auth.response.AuthRegisterResponse;
import com.cognisoftone.common.exception.DuplicateResourceException;
import com.cognisoftone.common.exception.InvalidTokenException;
import com.cognisoftone.users.repository.RoleRepository;
import com.cognisoftone.users.repository.UserRepository;
import com.cognisoftone.auth.request.LoginRequest;
import com.cognisoftone.auth.request.RefreshTokenRequest;
import com.cognisoftone.auth.request.RegisterRequest;
import com.cognisoftone.auth.response.AuthResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.cognisoftone.users.model.RoleModel;
import com.cognisoftone.users.model.UserModel;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public AuthRegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + request.getEmail());
        }

        if (userRepository.existsByIdentification(request.getIdentification())) {
            throw new DuplicateResourceException("Ya existe un usuario con la identificación: " + request.getIdentification());
        }

        Set<RoleModel> roleModels = new HashSet<>();
        for (String roleName : request.getRoles()) {
            RoleModel roleModel = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));
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

        return new AuthRegisterResponse(
                jwtToken,
                refreshToken
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
                user.getId(),
                user.getFirstName() + user.getLastName(),
                user.getEmail(),
                user.getRoleModels().stream().map(RoleModel::getName).collect(Collectors.toSet()),
                jwtToken,
                refreshToken,
                jwtExpiration,
                user.isEnabled(),
                "qmt6dRyipIad8UCc0QpMV2MENSy1",
                "identitytoolkit#VerifyPasswordResponse",
                user.getAvatarUrl()
        );
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        UserModel user = (UserModel) userDetailsService.loadUserByUsername(username);

        boolean isValid = jwtService.isTokenValid(refreshToken, user);
        if (!isValid) {
            throw new InvalidTokenException("Invalid refresh token");
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
                user.getId(),
                user.getFirstName() + user.getLastName(),
                user.getEmail(),
                user.getRoleModels().stream().map(RoleModel::getName).collect(Collectors.toSet()),
                newToken,
                refreshToken,
                jwtExpiration,
                user.isEnabled(),
                "qmt6dRyipIad8UCc0QpMV2MENSy1",
                "identitytoolkit#VerifyPasswordResponse",
                "qmt6dRyipIad8UCc0QpMV2MENSy1"
        );
    }

}

