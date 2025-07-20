package com.cognisoftone.interfaces.auth;

import com.cognisoftone.model.TokenModel;
import com.cognisoftone.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenModel, Long> {
    List<TokenModel> findAllByUser(UserModel user);
    Optional<TokenModel> findByToken(String token);
}
