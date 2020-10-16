package com.ssafy.backend.help;

import java.util.Date;

/*
 UserProfile
{id:"sdfs",username:"sdfsdfsdf", name:"ddssffsdfsffsdfs"}
 */
public class UserProfile {

	private Long num;
	private String userId;
	private String name;
	private String nickName;
	private Date birth;

	public UserProfile(Long num, String userId, String name, String nickName, Date birth) {
		super();
		this.num = num;
		this.userId = userId;
		this.name = name;
		this.nickName = nickName;
		this.birth = birth;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

}
