package com.ssafy.backend.help;

import java.util.Date;

import javax.validation.constraints.*;


public class SignUpRequest {
    private String id;
    private String name;
    private String password;
    private String role;
    private int qualification;
	public SignUpRequest(String id, String name, String password, String role, int qualification) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.role = role;
		this.qualification = qualification;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
		return "SignUpRequest [id=" + id + ", name=" + name + ", password=" + password + ", role=" + role
				+ ", qualification=" + qualification + "]";
	}
    
}
