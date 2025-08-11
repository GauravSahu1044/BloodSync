package com.bloodsync.repository;

import com.bloodsync.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    Optional<Patient> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Patient> findByHospitalId(Long hospitalId);
    
    List<Patient> findByBloodGroup(String bloodGroup);
    
    List<Patient> findByCityAndState(String city, String state);
} 