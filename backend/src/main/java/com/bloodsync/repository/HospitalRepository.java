package com.bloodsync.repository;

import com.bloodsync.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    
    Optional<Hospital> findByEmail(String email);
    
    Optional<Hospital> findByLicenseNumber(String licenseNumber);
    
    boolean existsByEmail(String email);
    
    boolean existsByLicenseNumber(String licenseNumber);
    
    // Public access methods
    List<Hospital> findByIsActiveTrue();
    
    List<Hospital> findByCityAndIsActiveTrue(String city);
} 