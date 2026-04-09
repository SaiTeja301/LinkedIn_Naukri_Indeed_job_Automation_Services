package com.jobbot.controller;

import com.jobbot.dto.UserDto;
import com.jobbot.entity.UserEntity;
import com.jobbot.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auto-jobs")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/add-user-info")
  public UserDto addUserInfo(@RequestBody UserDto user) {
    return userService.saveUser(user);
  }

  @GetMapping("/get-user-info/{userId}")
  public UserDto getUserInfo(@PathVariable Long userId) {
    return userService.getUser(userId);
  }

  @DeleteMapping("/delete-user-info/{name}/{email}")
  public ResponseEntity<String> deleteUserInfo(@PathVariable String name, @PathVariable String email) {
    Optional<UserDto> dto = Optional.ofNullable(userService.getUserByNameAndEmail(name, email));
    if (dto.isEmpty()) {
      return ResponseEntity.status(404).body("User not found with name: " + name + " and email: " + email);
    }
    userService.deleteUserByNameAndEmail(name, email);
    return ResponseEntity.ok("User deleted successfully with name: " + name + " and email: " + email);
  }
}
