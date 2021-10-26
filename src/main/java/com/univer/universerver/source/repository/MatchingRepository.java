package com.univer.universerver.source.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	boolean existsByUserAndMatchRoomId(User user, long mid);

    void deleteByMatchRoomIdAndUserId(long matchRoomId, Long id);

    @Query(value = "select id from (select (id) from matching where match_room_id =:matchRoomId order by id asc limit 1,1) as id",nativeQuery = true)
    Long selectMinUserId(@Param("matchRoomId") long matchRoomId);

	Matching findByUserId(long userId);

	Matching findByMatchRoomIdAndUserId(long matchRoomId, long minUserId);
}
