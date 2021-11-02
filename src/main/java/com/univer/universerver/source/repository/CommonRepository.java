package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.Common;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonRepository extends JpaRepository<Common,Long> {
    List<Common> findByComCd(String cd01);
}
