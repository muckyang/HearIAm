package com.ssafy.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
public class UserRoles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_num")
	private Long userNum;
	@Column(name = "role_num")
	private Long roleNum;

	public UserRoles() {
		super();
	}

	public UserRoles(Long userNum, Long roleNum) {
		super();
		this.userNum = userNum;
		this.roleNum = roleNum;
	}

	public Long getUserNum() {
		return userNum;
	}

	public void setUserNum(Long userNum) {
		this.userNum = userNum;
	}

	public Long getRoleNum() {
		return roleNum;
	}

	public void setRoleNum(Long roleNum) {
		this.roleNum = roleNum;
	}

	@Override
	public String toString() {
		return "UserRoles [userNum=" + userNum + ", roleNum=" + roleNum + "]";
	}

}
