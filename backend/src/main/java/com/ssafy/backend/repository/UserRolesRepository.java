package com.ssafy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.backend.model.UserRoles;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
	UserRoles findByUserNum(Long userNum);
}
