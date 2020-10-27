package com.ssafy.backend.repository;

import java.util.List;

import com.ssafy.backend.model.RecordAssign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordAssignRepository extends JpaRepository<RecordAssign,String>{
    List<RecordAssign> findAll();
}

