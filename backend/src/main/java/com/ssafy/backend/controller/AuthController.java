package com.ssafy.backend.controller;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ssafy.backend.exception.AppException;
import com.ssafy.backend.help.ApiResult;
import com.ssafy.backend.help.JwtAuthenticationResult;
import com.ssafy.backend.help.LoginRequest;
import com.ssafy.backend.help.SignUpRequest;
import com.ssafy.backend.model.Role;
import com.ssafy.backend.model.RoleName;
import com.ssafy.backend.model.User;
import com.ssafy.backend.repository.RoleRepository;
import com.ssafy.backend.repository.UserRepository;
import com.ssafy.backend.security.JwtTokenProvider;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;
	public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/signin")
	// public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	public Object authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JwtAuthenticationResult(jwt));
	}

	@PostMapping("/signup")
	public Object registerUser(@RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsById(signUpRequest.getId())) {
			return new ResponseEntity(new ApiResult(false, "사용자 아이디가 이미 존재합니다!"), HttpStatus.BAD_REQUEST);
		}
		// Creating user's account
		int number = (int)(Math.random()*63);
		String [] color  = {"#dabbfe","#b6f9e1","#e1f7d5","#ece8dc" ,"#bae1ff","#c9c9ff","#ffbdbd","#ffcb85","#97ebdb"};
		String [] icon = {"bear.png","bird.png","chicken.png","dog.png","frog.png","koala.png","lion.png"};

		User user = new User(null, signUpRequest.getId(), signUpRequest.getName(),
				signUpRequest.getPassword(), signUpRequest.getRole(), signUpRequest.getQualification(), signUpRequest.getGender(), null);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("User Role not set."));
		user.setRoles(Collections.singleton(userRole));
		user.setIcon(icon[number % 9]);
		user.setColor(color[number % 7]);
		User result = userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).body(new ApiResult(true, "성공적으로 등록되었습니다."));
	}
}
