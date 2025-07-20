package com.cognisoftone.controller.auth;

import com.cognisoftone.model.RoleModel;
import com.cognisoftone.model.UserModel;
import com.cognisoftone.request.auth.LoginRequest;
import com.cognisoftone.request.auth.RefreshTokenRequest;
import com.cognisoftone.request.auth.RegisterRequest;
import com.cognisoftone.interfaces.auth.AuthService;
import com.cognisoftone.response.auth.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final Logger log = Logger.getLogger(AuthController.class.getName());

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("START REGISTER");
        var response = authService.register(request);
        log.info("END REGISTER");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("START LOGIN");
        var response = authService.login(request);
        log.info("END LOGIN");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/refresh-token",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        var response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}

