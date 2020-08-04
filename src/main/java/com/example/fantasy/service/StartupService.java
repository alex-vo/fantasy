package com.example.fantasy.service;

import com.example.fantasy.entity.Role;
import com.example.fantasy.entity.User;
import com.example.fantasy.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StartupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public StartupService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          @Value("${admin.username}") String adminUsername,
                          @Value("${admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @EventListener
    public void appReady(ApplicationStartedEvent e) {
        if (userRepository.findNonBlockedByEmail(adminUsername).isPresent()) {
            return;
        }
        userRepository.save(prepareUser(adminUsername, adminPassword, Role.ROLE_ADMIN));
    }

    private User prepareUser(String email, String password, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setBlocked(false);
        user.setFailedLoginAttempts(0);
        user.setRole(role);
        return user;
    }
}
