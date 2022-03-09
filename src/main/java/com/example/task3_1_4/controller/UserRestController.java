package com.example.task3_1_4.controller;

import com.example.task3_1_4.model.User;
import com.example.task3_1_4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    public User getUserById(@PathVariable long id){
        return userService.findUserById(id);
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody User user){
        userService.save(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user){
        userService.save(user);
        return user;
    }

    @DeleteMapping("users/{id}")
    public String deleteUserById(@PathVariable long id){
        userService.deleteById(id);
        return "User with id = " + id + " was deleted";
    }
    @GetMapping("users/0")
    public User getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        return user;
    }

}
