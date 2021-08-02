package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken,Long> {
}
