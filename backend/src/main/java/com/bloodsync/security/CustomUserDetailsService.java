package com.bloodsync.security;

import com.bloodsync.entity.User;
import com.bloodsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Unauthorized access attempt for username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        if (!user.isActive()) {
            log.warn("Inactive user login attempt: {}", username);
            throw new UsernameNotFoundException("User account is inactive: " + username);
        }

        if (!user.isAccountNonLocked()) {
            log.warn("Locked account login attempt: {}", username);
            throw new UsernameNotFoundException("User account is locked: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                user.isAccountNonLocked(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    public void incrementFailedAttempts(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            
            if (user.getFailedAttempts() >= 5) {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
                log.warn("Account locked for user: {} after {} failed attempts", username, user.getFailedAttempts());
            }
            
            userRepository.save(user);
        });
    }

    public void resetFailedAttempts(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setFailedAttempts(0);
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            userRepository.save(user);
            log.info("Reset failed attempts for user: {}", username);
        });
    }

    public void updateRefreshToken(String username, String refreshToken, LocalDateTime expiry) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpiry(expiry);
            userRepository.save(user);
        });
    }
} 