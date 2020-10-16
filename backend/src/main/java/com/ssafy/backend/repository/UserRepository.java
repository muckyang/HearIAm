package com.ssafy.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByNum(Long num);
    
    Optional<User> findById(String id);
    
    List<User> findByNumIn(List<Long> userIds);
    
    Boolean existsById(String id);
    
}
