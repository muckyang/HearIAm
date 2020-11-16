package com.ssafy.backend.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

import com.ssafy.backend.exception.ResourceNotFoundException;
import com.ssafy.backend.help.UserIdentityAvailability;
import com.ssafy.backend.help.UserProfile;
import com.ssafy.backend.model.User;
import com.ssafy.backend.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;


  @GetMapping("/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
    Boolean isAvailable = !userRepository.existsById(username);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/get/{num}")
  public User getUserProfileByNum(@PathVariable(value = "num") Long num) {
    User user = userRepository.findByNum(num).orElseThrow(() -> new ResourceNotFoundException("User", "num", num));

    return user;
  }

  @GetMapping("/userName")
  public List<User> getUserName() {
    List<User> list = userRepository.findAll();
    return list;
  }

  @GetMapping("/users/{userId}")
  public UserProfile getUserProfile(@PathVariable(value = "userId") String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "UserId", userId));
    UserProfile userProfile = new UserProfile(user.getNum(), user.getId(), user.getName(), user.getRole(),
        user.getQualification());
    return userProfile;
  }

  @PutMapping("/{num}")
  public ResponseEntity<String> modifyUserProfile(@RequestBody User memberUser, @PathVariable(value = "num") Long num) {
    User user = null;

    String SUCCESS = "success";
    try {
      user = userRepository.findByNum(num).orElseThrow(() -> new ResourceNotFoundException("User", "num", num));
      user.setName(memberUser.getName());
      user.setPassword(passwordEncoder.encode(memberUser.getPassword()));
    } catch (Exception e) {
      return null;
    }
    userRepository.save(user);

    return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{num}")
  public Object deleteUser(@PathVariable("num") Long num) {
    try {
      User user = userRepository.findByNum(num).orElseThrow(() -> new ResourceNotFoundException("User", "num", num));
      userRepository.delete(user);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("checkId/{id}")
  public Object checkId(@PathVariable String id) throws SQLException, IOException {
    try {
      return new ResponseEntity<>(userRepository.findById(id), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/userDId/{userId}/{deviceId}")
  public ResponseEntity<String> updateDeviceId(@PathVariable(value = "userId") String id, @PathVariable(value = "deviceId") String deviceId) {
    User user = null;
    System.out.println(id+" "+deviceId);
    try {
      user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));
      user.setDeviceId(deviceId);
    } catch (Exception e) {
      return null;
    }
    userRepository.save(user);

    return new ResponseEntity<String>(deviceId, HttpStatus.OK);
  }

  @GetMapping("/menteeName/{num}")
  @ApiOperation("멘티 닉네임 가져오기")
  public String getName(@PathVariable Long num) throws IOException, SQLException {
    Optional<User> user = userRepository.findByNum(num);
    return user.get().getName();
  }

}
