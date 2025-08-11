package com.bloodsync.service;

import com.bloodsync.entity.User;
import com.bloodsync.enums.UserRole;
import com.bloodsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Encode password with BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User savedUser = userRepository.save(user);
        log.info("User created: {}", savedUser.getUsername());
        return savedUser;
    }
    
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if username is being changed and if it already exists
        if (!user.getUsername().equals(userDetails.getUsername()) && 
            userRepository.existsByUsername(userDetails.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(userDetails.getEmail()) && 
            userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setRole(userDetails.getRole());
        user.setActive(userDetails.isActive());
        
        // Only encode password if it's being changed
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        log.info("User updated: {}", updatedUser.getUsername());
        return updatedUser;
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        log.info("User deleted with ID: {}", id);
    }
    
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getUsername());
    }
    
    public void unlockAccount(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setAccountNonLocked(true);
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            log.info("Account unlocked for user: {}", username);
        });
    }
    
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(java.util.stream.Collectors.toList());
    }
    
    // USER role specific methods
    public Optional<User> getCurrentUserProfile() {
        // This would typically get the current user from SecurityContext
        // For now, we'll return a placeholder implementation
        // In a real implementation, you would get the current user from SecurityContextHolder
        return Optional.empty(); // Placeholder
    }
    
    public User updateCurrentUserProfile(User userDetails) {
        // This would update the current user's profile
        // For now, we'll return a placeholder implementation
        throw new RuntimeException("Method not implemented yet");
    }
    
    public void changeCurrentUserPassword(String newPassword) {
        // This would change the current user's password
        // For now, we'll return a placeholder implementation
        throw new RuntimeException("Method not implemented yet");
    }
    
    public Object getUserDashboard() {
        // This would return dashboard data for the current user
        // For now, we'll return a placeholder implementation
        return new Object(); // Placeholder
    }
    
    public void becomeDonor(String bloodGroup) {
        // This would change the current user's role to DONOR
        // For now, we'll return a placeholder implementation
        throw new RuntimeException("Method not implemented yet");
    }
    
    public void becomeRecipient() {
        // This would change the current user's role to RECIPIENT
        // For now, we'll return a placeholder implementation
        throw new RuntimeException("Method not implemented yet");
    }
} 