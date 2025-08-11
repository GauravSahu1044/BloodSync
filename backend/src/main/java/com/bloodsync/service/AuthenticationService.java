package com.bloodsync.service;

import com.bloodsync.dto.AuthResponse;
import com.bloodsync.dto.LoginRequest;
import com.bloodsync.dto.RefreshTokenRequest;
import com.bloodsync.dto.RegisterRequest;
import com.bloodsync.entity.User;
import com.bloodsync.enums.UserRole;
import com.bloodsync.repository.UserRepository;
import com.bloodsync.security.CustomUserDetailsService;
import com.bloodsync.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            
            // Reset failed attempts on successful login
            userDetailsService.resetFailedAttempts(loginRequest.getUsername());
            
            // Update refresh token in database
            LocalDateTime refreshExpiry = LocalDateTime.now().plusHours(24);
            userDetailsService.updateRefreshToken(loginRequest.getUsername(), refreshToken, refreshExpiry);
            
            User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
            String role = user != null ? user.getRole().name() : "USER";
            
            log.info("User {} logged in successfully", loginRequest.getUsername());
            return new AuthResponse(accessToken, refreshToken, loginRequest.getUsername(), role);
            
        } catch (BadCredentialsException e) {
            userDetailsService.incrementFailedAttempts(loginRequest.getUsername());
            log.warn("Failed login attempt for user: {}", loginRequest.getUsername());
            return new AuthResponse("Invalid username or password");
            
        } catch (LockedException e) {
            log.warn("Login attempt for locked account: {}", loginRequest.getUsername());
            return new AuthResponse("Account is locked due to multiple failed attempts");
            
        } catch (Exception e) {
            log.error("Login error for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return new AuthResponse("Login failed: " + e.getMessage());
        }
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        try {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return new AuthResponse("Username already exists");
            }
            
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return new AuthResponse("Email already exists");
            }
            
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setFullName(registerRequest.getFullName());
            user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : UserRole.USER);
            user.setActive(true);
            user.setAccountNonLocked(true);
            user.setFailedAttempts(0);
            
            User savedUser = userRepository.save(user);
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            
            // Update refresh token in database
            LocalDateTime refreshExpiry = LocalDateTime.now().plusHours(24);
            userDetailsService.updateRefreshToken(savedUser.getUsername(), refreshToken, refreshExpiry);
            
            log.info("User {} registered successfully", savedUser.getUsername());
            return new AuthResponse(accessToken, refreshToken, savedUser.getUsername(), savedUser.getRole().name());
            
        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());
            return new AuthResponse("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            String username = jwtTokenUtil.extractUsername(refreshTokenRequest.getRefreshToken());
            
            if (username == null) {
                return new AuthResponse("Invalid refresh token");
            }
            
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null || !user.getRefreshToken().equals(refreshTokenRequest.getRefreshToken())) {
                return new AuthResponse("Invalid refresh token");
            }
            
            if (user.getRefreshTokenExpiry() != null && user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
                return new AuthResponse("Refresh token has expired");
            }
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtTokenUtil.generateToken(userDetails);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            
            // Update refresh token in database
            LocalDateTime refreshExpiry = LocalDateTime.now().plusHours(24);
            userDetailsService.updateRefreshToken(username, newRefreshToken, refreshExpiry);
            
            log.info("Token refreshed for user: {}", username);
            return new AuthResponse(newAccessToken, newRefreshToken, username, user.getRole().name());
            
        } catch (Exception e) {
            log.error("Token refresh error: {}", e.getMessage());
            return new AuthResponse("Token refresh failed: " + e.getMessage());
        }
    }
} 