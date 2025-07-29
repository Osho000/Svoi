//package com.example.Svoi.controllers;
//
//import com.example.Svoi.model.User;
//import com.example.Svoi.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
//
//
//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        return ResponseEntity.ok(userService.saveUser(user));
//    }
//
//    @GetMapping
//    public List<User> getUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/{id}/matches")
//    public ResponseEntity<List<User>> getMatches(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.findMatches(id));
//    }
//}
