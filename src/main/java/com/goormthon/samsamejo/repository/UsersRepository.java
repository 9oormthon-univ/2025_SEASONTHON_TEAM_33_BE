package com.goormthon.samsamejo.repository;

import com.goormthon.samsamejo.domain.Users;
import com.goormthon.samsamejo.domain.type.EProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByOauthIdAndOauthProvider(String oauthId, EProvider oauthProvider);

    @Modifying
    @Query("UPDATE Users u SET u.refreshToken = :refreshToken WHERE u.id = :id")
    void updateRefreshTokenByUserId(String refreshToken, Long id);
}
