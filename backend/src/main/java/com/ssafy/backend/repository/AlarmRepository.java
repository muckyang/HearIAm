package com.ssafy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.ssafy.backend.model.Alarm;
@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{
    public Optional<Alarm> findByRoom(String room);
	public void deleteByRoom(String room);

}
