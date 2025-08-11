package com.bloodsync.repository;

import com.bloodsync.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    
    Optional<Donor> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Donor> findByBloodGroup(String bloodGroup);
    
    List<Donor> findByIsEligibleTrue();
    
    List<Donor> findByCityAndState(String city, String state);
    
    // Public access methods
    List<Donor> findByIsActiveTrue();
    
    List<Donor> findByBloodGroupAndIsActiveTrue(String bloodGroup);
    
    List<Donor> findByCityAndIsActiveTrue(String city);
} 