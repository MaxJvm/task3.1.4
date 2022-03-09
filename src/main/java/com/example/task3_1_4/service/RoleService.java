package com.example.task3_1_4.service;

import com.example.task3_1_4.dao.RoleRepository;
import com.example.task3_1_4.model.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Role findByName(String n) {
        return roleRepository.findByName(n);
    }
}
