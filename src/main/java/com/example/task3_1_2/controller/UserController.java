package com.example.task3_1_2.controller;

import com.example.task3_1_2.model.Role;
import com.example.task3_1_2.model.User;
import com.example.task3_1_2.service.RoleService;
import com.example.task3_1_2.service.UserService;
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
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public String getAll(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "list";
    }

    @GetMapping("/user/{name}")
    public String getUser(Model model, @PathVariable String name) {
        User user = userService.findByUsername(name);
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
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/save/{id}")
    public String saveUser(@PathVariable long id, @RequestParam String name, @RequestParam String surname,
                           @RequestParam String username, @RequestParam String password, @RequestParam String roles) {
        User user = new User(username, password, name, surname);
        String[] names = roles.split("\\s");
        List<Role> roleList = new ArrayList<>();
        for (String n : names) {
            Role role = new Role(n);
            if (roleService.existsByName(n)) {
                role = roleService.findByName(n);
            } else {
                roleService.save(role);
            }
            roleList.add(role);
        }
        user.setRoles(roleList);
        if (id != 0) {
            userService.deleteById(id);
            user.setId(id);
        }
        userService.save(user);
        return "redirect:/admin";
    }

}