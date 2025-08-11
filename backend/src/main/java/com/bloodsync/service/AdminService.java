package com.bloodsync.service;

import com.bloodsync.dto.AdminDto;
import com.bloodsync.entity.Admin;
import com.bloodsync.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    
    private final AdminRepository adminRepository;
    
    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<AdminDto> getAdminById(Long id) {
        return adminRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public Optional<AdminDto> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(this::convertToDto);
    }
    
    public AdminDto createAdmin(AdminDto adminDto) {
        if (adminRepository.existsByUsername(adminDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        Admin admin = convertToEntity(adminDto);
        Admin savedAdmin = adminRepository.save(admin);
        return convertToDto(savedAdmin);
    }
    
    public AdminDto updateAdmin(Long id, AdminDto adminDto) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // Check if username is being changed and if it already exists
        if (!admin.getUsername().equals(adminDto.getUsername()) && 
            adminRepository.existsByUsername(adminDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email is being changed and if it already exists
        if (!admin.getEmail().equals(adminDto.getEmail()) && 
            adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        admin.setUsername(adminDto.getUsername());
        admin.setEmail(adminDto.getEmail());
        admin.setPassword(adminDto.getPassword());
        admin.setFullName(adminDto.getFullName());
        admin.setPhoneNumber(adminDto.getPhoneNumber());
        admin.setActive(adminDto.isActive());
        
        Admin updatedAdmin = adminRepository.save(admin);
        return convertToDto(updatedAdmin);
    }
    
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(id);
    }
    
    private AdminDto convertToDto(Admin admin) {
        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setPassword(admin.getPassword());
        dto.setFullName(admin.getFullName());
        dto.setPhoneNumber(admin.getPhoneNumber());
        dto.setActive(admin.isActive());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());
        return dto;
    }
    
    private Admin convertToEntity(AdminDto dto) {
        Admin admin = new Admin();
        admin.setId(dto.getId());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setFullName(dto.getFullName());
        admin.setPhoneNumber(dto.getPhoneNumber());
        admin.setActive(dto.isActive());
        return admin;
    }
} 