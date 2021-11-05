package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage,Long> {

    void deleteByUserId(long id);
}
