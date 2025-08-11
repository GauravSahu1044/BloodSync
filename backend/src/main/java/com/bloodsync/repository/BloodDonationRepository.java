package com.bloodsync.repository;

import com.bloodsync.entity.BloodDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloodDonationRepository extends JpaRepository<BloodDonation, Long> {
    
    List<BloodDonation> findByDonorId(Long donorId);
    
    List<BloodDonation> findByHospitalId(Long hospitalId);
    
    List<BloodDonation> findByBloodGroup(String bloodGroup);
    
    List<BloodDonation> findByStatus(BloodDonation.DonationStatus status);
    
    List<BloodDonation> findByDonationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<BloodDonation> findByDonorIdAndStatus(Long donorId, BloodDonation.DonationStatus status);
    
    List<BloodDonation> findByHospitalIdAndStatus(Long hospitalId, BloodDonation.DonationStatus status);
    
    List<BloodDonation> findByBloodGroupAndStatus(String bloodGroup, BloodDonation.DonationStatus status);
} 