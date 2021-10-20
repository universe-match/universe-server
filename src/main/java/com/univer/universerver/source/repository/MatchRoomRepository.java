package com.univer.universerver.source.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.univer.universerver.source.model.MatchRoom;

import java.util.List;

@Repository
public interface MatchRoomRepository extends JpaRepository<MatchRoom,Long>{

	boolean existsByUserId(Long id);

//	Page<MatchRoom> findAllByMatchRoomsChatRoomUsersUserUserid(Pageable pageable, String name);

	@Query(value="select * from matchroom m order by rand() limit 5",nativeQuery = true)
	List<MatchRoom> findAllRandomList();
}
