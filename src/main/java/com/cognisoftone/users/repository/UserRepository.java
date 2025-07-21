package com.cognisoftone.users.repository;

import com.cognisoftone.users.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
}
