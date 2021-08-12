package com.univer.universerver.source.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.univer.universerver.source.model.MatchRoom;
import com.univer.universerver.source.model.Matching;
import com.univer.universerver.source.model.User;

@Repository
public interface MatchingRepository extends JpaRepository<Matching,Long>{

	boolean existsByUser(User user);

	Matching findByMatchRoom(long matchRoomId);

	long countByMatchRoom(MatchRoom matchRoom);

}