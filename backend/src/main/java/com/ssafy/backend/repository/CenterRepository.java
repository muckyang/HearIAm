package com.ssafy.backend.repository;

import com.ssafy.backend.model.Center;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long>{
    List<Center> findAll();
    List<Center> findByAddressLike(String word);
}
