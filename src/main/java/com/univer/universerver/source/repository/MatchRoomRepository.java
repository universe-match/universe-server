package com.univer.universerver.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.univer.universerver.source.model.MatchRoom;

public interface MatchRoomRepository extends JpaRepository<MatchRoom,Long>{

	boolean existsByUserId(Long id);

}
