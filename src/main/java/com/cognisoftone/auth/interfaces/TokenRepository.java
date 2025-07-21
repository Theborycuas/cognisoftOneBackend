package com.cognisoftone.auth.interfaces;

import com.cognisoftone.auth.model.TokenModel;
import com.cognisoftone.users.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenModel, Long> {
    List<TokenModel> findAllByUser(UserModel user);
    Optional<TokenModel> findByToken(String token);
}
