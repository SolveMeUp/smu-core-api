package com.solvemeup.smucoreapi.domain.user.repository;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.enums.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauth2ProviderAndOauth2ProviderId(OAuth2Provider oauth2Provider, String oauth2ProviderId);

    Optional<User> findByNickname(String nickname);
}
