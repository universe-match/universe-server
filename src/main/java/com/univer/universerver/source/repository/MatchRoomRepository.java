package com.univer.universerver.source.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.univer.universerver.source.model.MatchRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRoomRepository extends JpaRepository<MatchRoom,Long>{

	boolean existsByUserId(Long id);

//	Page<MatchRoom> findAllByMatchRoomsChatRoomUsersUserUserid(Pageable pageable, String name);

	@Query(value="select * from matchroom m order by rand() limit 5",nativeQuery = true)
	List<MatchRoom> findAllRandomList();

    Optional<MatchRoom> findByUserId(Long id);
	@Query(value="select * from matchroom m where title like %:title order by rand() limit 5\n",nativeQuery = true)
	List<MatchRoom> findAllRandomListByTitleLike(@Param("title") String title);
}
