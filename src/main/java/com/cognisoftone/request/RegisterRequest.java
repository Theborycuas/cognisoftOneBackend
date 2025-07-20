package com.cognisoftone.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String identification;
    private String phone;
    private String address;
    private String email;
    private String password;
    private Boolean consent;
    private Set<String> roles;
}
