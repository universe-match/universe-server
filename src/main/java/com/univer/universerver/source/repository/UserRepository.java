package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByNickname(String nickname);
    boolean existsByUserid(String email);
    Optional<User> findByUserid(String username);
    Optional<User> findByNickname(String username);
    Optional<User> findByEmail(String email);


    List<User> findByNicknameLike(String string);
    boolean existsByEmail(String email);
    boolean existsByFcmToken(String fcmToken);
    Optional<User> findByFcmToken(String fcmToken);
}
