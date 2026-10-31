package com.bookworms.service;

import com.bookworms.exception.ApiException;
import com.bookworms.model.Role;
import com.bookworms.model.User;
import com.bookworms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int[] ACCENT_COLORS = {
            0xFFB5654A, 0xFF7C9473, 0xFF6E8AA3, 0xFFC9A227, 0xFF8B5E7A, 0xFF5D7A8C
    };

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String name, String email, String rawPassword) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw ApiException.conflict("An account with this email already exists.");
        }
        int color = ACCENT_COLORS[new Random().nextInt(ACCENT_COLORS.length)];
        User user = new User(name.trim(), email.trim().toLowerCase(), passwordEncoder.encode(rawPassword), color);
        return userRepository.save(user);
    }

    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> ApiException.unauthorized("Incorrect email or password."));
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw ApiException.unauthorized("Incorrect email or password.");
        }
        if (user.isBanned()) {
            throw ApiException.forbidden("This account has been suspended.");
        }
        return user;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> ApiException.notFound("User not found."));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User updateProfile(Long id, String name, String bio) {
        User user = findById(id);
        if (name != null && !name.isBlank()) user.setName(name.trim());
        if (bio != null) user.setBio(bio.trim());
        return userRepository.save(user);
    }

    public User setBanned(Long id, boolean banned) {
        User user = findById(id);
        user.setBanned(banned);
        return userRepository.save(user);
    }

    public User setRole(Long id, Role role) {
        User user = findById(id);
        user.setRole(role);
        return userRepository.save(user);
    }

    public long countAll() {
        return userRepository.count();
    }
}
