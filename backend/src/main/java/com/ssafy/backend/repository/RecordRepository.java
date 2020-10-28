package com.ssafy.backend.repository;

import java.util.List;
import com.ssafy.backend.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecordRepository extends JpaRepository<Record,String>{
    List<Record> findAll();
}
