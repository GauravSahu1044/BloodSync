package com.bloodsync.repository;

import com.bloodsync.entity.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {
    
    List<BloodInventory> findByHospitalId(Long hospitalId);
    
    List<BloodInventory> findByBloodGroup(String bloodGroup);
    
    List<BloodInventory> findByStatus(BloodInventory.InventoryStatus status);
    
    List<BloodInventory> findByHospitalIdAndBloodGroup(Long hospitalId, String bloodGroup);
    
    List<BloodInventory> findByHospitalIdAndStatus(Long hospitalId, BloodInventory.InventoryStatus status);
    
    List<BloodInventory> findByBloodGroupAndStatus(String bloodGroup, BloodInventory.InventoryStatus status);
    
    List<BloodInventory> findByExpiryDateBefore(LocalDateTime date);
    
    List<BloodInventory> findByAvailableQuantityLessThan(Integer quantity);
    
    Optional<BloodInventory> findByHospitalIdAndBloodGroupAndStatus(Long hospitalId, String bloodGroup, BloodInventory.InventoryStatus status);
} 