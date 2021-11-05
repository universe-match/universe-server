package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.UserInteresting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userInterestingRepository extends JpaRepository<UserInteresting,Long> {
}
