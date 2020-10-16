package com.ssafy.backend.help;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public String toString() {
		return "LoginRequest [userId=" + userId + ", password=" + password + "]";
	}
    
}
