package com.cognisoftone.users.init;

import com.cognisoftone.users.model.RoleModel;
import com.cognisoftone.users.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        List<String> defaultRoles = List.of("SUPER_ADMIN", "ADMIN", "PSYCHOLOGIST", "PATIENT");

        for (int i = 0; i < defaultRoles.size(); i++) {
            String roleName = defaultRoles.get(i);
            if (roleRepository.findByName(roleName).isEmpty()) {
                RoleModel role = new RoleModel();
                role.setName(roleName);
                role.setDescription("Default role: " + roleName);
                role.setActive(true);
                role.setLevel(i + 1);
                role.setCreatedAt(LocalDateTime.now());

                roleRepository.save(role);
            }
        }
    }
}

