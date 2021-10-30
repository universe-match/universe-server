package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityRepository extends JpaRepository<University,Long> {

    List<University> findByNameLike(String s);
}
