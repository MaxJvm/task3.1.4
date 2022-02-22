package com.example.task3_1_1.controller;

import com.example.task3_1_1.dao.UserRepository;
import com.example.task3_1_1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getAll(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "list";
    }

    @GetMapping("/user/{id}")
    public String getUser(Model model, @PathVariable long id) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "update";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/save/{id}")
    public String saveUser(@PathVariable long id, @RequestParam String name, @RequestParam String surname) {
        if (id == 0) {
            userRepository.save(new User(name, surname));
        } else {
            userRepository.deleteById(id);
            User user = new User(name, surname);
            user.setId(id);
            userRepository.save(user);
        }
        return "redirect:/";
    }

}