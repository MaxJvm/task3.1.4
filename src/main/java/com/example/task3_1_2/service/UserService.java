package com.example.task3_1_2.service;

import com.example.task3_1_2.dao.UserRepository;
import com.example.task3_1_2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getRoles());
    }

    public Iterable<User> allUsers() {
        return userRepository.findAll();
    }

    public User findUserById(long id) {
        return userRepository.findById(id).get();
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
