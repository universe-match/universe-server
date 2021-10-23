package com.univer.universerver.source.repository;

import com.univer.universerver.source.model.ChatRoomUser;
import com.univer.universerver.source.model.Complain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplainRepository extends JpaRepository<Complain,Long> {
}
