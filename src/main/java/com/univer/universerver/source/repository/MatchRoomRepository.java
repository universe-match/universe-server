package com.univer.universerver.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univer.universerver.source.model.MatchRoom;

@Repository
public interface MatchRoomRepository extends JpaRepository<MatchRoom,Long>{

	boolean existsByUserId(Long id);

}
