package com.cognisoftone.serviceImpl;

import com.cognisoftone.interfaces.AuthService;
import com.cognisoftone.repository.RoleRepository;
import com.cognisoftone.repository.UserRepository;
import com.cognisoftone.request.LoginRequest;
import com.cognisoftone.request.RegisterRequest;
import com.cognisoftone.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import com.cognisoftone.model.RoleModel;
import com.cognisoftone.model.UserModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

        return new AuthResponse(
                jwtToken,
                refreshToken,
                user.getEmail(),
                user.getRoleModels().stream().map(RoleModel::getName).collect(Collectors.toSet())
        );
    }
}

