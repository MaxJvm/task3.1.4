package com.example.task3_1_4.dao;

import com.example.task3_1_4.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    boolean existsByName(String name);

    Role findByName(String n);
}
