package com.bloodsync.repository;

import com.bloodsync.entity.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    
    List<BloodRequest> findByHospitalId(Long hospitalId);
    
    List<BloodRequest> findByPatientId(Long patientId);
    
    List<BloodRequest> findByStatus(String status);
    
    List<BloodRequest> findByBloodGroup(String bloodGroup);
    
    List<BloodRequest> findByPriority(String priority);
} 