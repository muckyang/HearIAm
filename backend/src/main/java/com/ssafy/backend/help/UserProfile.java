package com.ssafy.backend.help;

public class UserProfile {

	private Long num;
	private String id;
	private String name;
	private String role;
	private int qualification;

	public UserProfile() {
		super();
	}

	public UserProfile(Long num, String id, String name, String role, int qualification) {
		super();
		this.num = num;
		this.id = id;
		this.name = name;
		this.role = role;
		this.qualification = qualification;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getQualification() {
		return qualification;
	}

	public void setQualification(int qualification) {
		this.qualification = qualification;
	}

	@Override
	public String toString() {
		return "UserProfile [num=" + num + ", id=" + id + ", name=" + name + ", role=" + role + ", qualification="
				+ qualification + "]";
	}

}
