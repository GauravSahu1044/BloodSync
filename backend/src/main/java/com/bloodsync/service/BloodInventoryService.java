package com.bloodsync.service;

import com.bloodsync.dto.BloodInventoryDto;
import com.bloodsync.entity.BloodInventory;
import com.bloodsync.entity.Hospital;
import com.bloodsync.repository.BloodInventoryRepository;
import com.bloodsync.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BloodInventoryService {
    
    private final BloodInventoryRepository bloodInventoryRepository;
    private final HospitalRepository hospitalRepository;
    
    public List<BloodInventoryDto> getAllBloodInventory() {
        log.info("Fetching all blood inventory");
        return bloodInventoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<BloodInventoryDto> getBloodInventoryById(Long id) {
        log.info("Fetching blood inventory with ID: {}", id);
        return bloodInventoryRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<BloodInventoryDto> getBloodInventoryByHospitalId(Long hospitalId) {
        log.info("Fetching blood inventory for hospital ID: {}", hospitalId);
        return bloodInventoryRepository.findByHospitalId(hospitalId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodInventoryDto> getBloodInventoryByBloodGroup(String bloodGroup) {
        log.info("Fetching blood inventory for blood group: {}", bloodGroup);
        return bloodInventoryRepository.findByBloodGroup(bloodGroup).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodInventoryDto> getBloodInventoryByStatus(BloodInventory.InventoryStatus status) {
        log.info("Fetching blood inventory with status: {}", status);
        return bloodInventoryRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodInventoryDto> getBloodInventoryByHospitalAndBloodGroup(Long hospitalId, String bloodGroup) {
        log.info("Fetching blood inventory for hospital ID: {} and blood group: {}", hospitalId, bloodGroup);
        return bloodInventoryRepository.findByHospitalIdAndBloodGroup(hospitalId, bloodGroup).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodInventoryDto> getExpiredBloodInventory() {
        log.info("Fetching expired blood inventory");
        return bloodInventoryRepository.findByExpiryDateBefore(LocalDateTime.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodInventoryDto> getLowStockBloodInventory(Integer threshold) {
        log.info("Fetching low stock blood inventory with threshold: {}", threshold);
        return bloodInventoryRepository.findByAvailableQuantityLessThan(threshold).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BloodInventoryDto createBloodInventory(BloodInventoryDto bloodInventoryDto) {
        log.info("Creating new blood inventory for hospital ID: {}", bloodInventoryDto.getHospitalId());
        
        // Validate hospital exists
        Hospital hospital = hospitalRepository.findById(bloodInventoryDto.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + bloodInventoryDto.getHospitalId()));
        
        // Check if inventory already exists for this hospital and blood group
        Optional<BloodInventory> existingInventory = bloodInventoryRepository
                .findByHospitalIdAndBloodGroupAndStatus(
                    bloodInventoryDto.getHospitalId(), 
                    bloodInventoryDto.getBloodGroup(), 
                    BloodInventory.InventoryStatus.AVAILABLE
                );
        
        if (existingInventory.isPresent()) {
            // Update existing inventory
            BloodInventory existing = existingInventory.get();
            existing.setAvailableQuantity(existing.getAvailableQuantity() + bloodInventoryDto.getAvailableQuantity());
            existing.setTotalQuantity(existing.getTotalQuantity() + bloodInventoryDto.getTotalQuantity());
            
            // Update status based on available quantity
            if (existing.getAvailableQuantity() <= 0) {
                existing.setStatus(BloodInventory.InventoryStatus.OUT_OF_STOCK);
            } else if (existing.getAvailableQuantity() < 1000) { // Less than 1L
                existing.setStatus(BloodInventory.InventoryStatus.LOW_STOCK);
            } else {
                existing.setStatus(BloodInventory.InventoryStatus.AVAILABLE);
            }
            
            BloodInventory updatedInventory = bloodInventoryRepository.save(existing);
            log.info("Blood inventory updated successfully with ID: {}", updatedInventory.getId());
            return convertToDto(updatedInventory);
        } else {
            // Create new inventory
            BloodInventory bloodInventory = convertToEntity(bloodInventoryDto);
            bloodInventory.setHospital(hospital);
            
            // Set status based on available quantity
            if (bloodInventory.getAvailableQuantity() <= 0) {
                bloodInventory.setStatus(BloodInventory.InventoryStatus.OUT_OF_STOCK);
            } else if (bloodInventory.getAvailableQuantity() < 1000) { // Less than 1L
                bloodInventory.setStatus(BloodInventory.InventoryStatus.LOW_STOCK);
            } else {
                bloodInventory.setStatus(BloodInventory.InventoryStatus.AVAILABLE);
            }
            
            BloodInventory savedInventory = bloodInventoryRepository.save(bloodInventory);
            log.info("Blood inventory created successfully with ID: {}", savedInventory.getId());
            return convertToDto(savedInventory);
        }
    }
    
    public BloodInventoryDto updateBloodInventory(Long id, BloodInventoryDto bloodInventoryDto) {
        log.info("Updating blood inventory with ID: {}", id);
        
        BloodInventory bloodInventory = bloodInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood inventory not found with ID: " + id));
        
        // Update fields
        if (bloodInventoryDto.getBloodGroup() != null) {
            bloodInventory.setBloodGroup(bloodInventoryDto.getBloodGroup());
        }
        if (bloodInventoryDto.getAvailableQuantity() != null) {
            bloodInventory.setAvailableQuantity(bloodInventoryDto.getAvailableQuantity());
        }
        if (bloodInventoryDto.getTotalQuantity() != null) {
            bloodInventory.setTotalQuantity(bloodInventoryDto.getTotalQuantity());
        }
        if (bloodInventoryDto.getExpiryDate() != null) {
            bloodInventory.setExpiryDate(bloodInventoryDto.getExpiryDate());
        }
        if (bloodInventoryDto.getStatus() != null) {
            bloodInventory.setStatus(bloodInventoryDto.getStatus());
        }
        if (bloodInventoryDto.getNotes() != null) {
            bloodInventory.setNotes(bloodInventoryDto.getNotes());
        }
        
        // Update status based on available quantity
        if (bloodInventory.getAvailableQuantity() <= 0) {
            bloodInventory.setStatus(BloodInventory.InventoryStatus.OUT_OF_STOCK);
        } else if (bloodInventory.getAvailableQuantity() < 1000) { // Less than 1L
            bloodInventory.setStatus(BloodInventory.InventoryStatus.LOW_STOCK);
        } else {
            bloodInventory.setStatus(BloodInventory.InventoryStatus.AVAILABLE);
        }
        
        BloodInventory updatedInventory = bloodInventoryRepository.save(bloodInventory);
        log.info("Blood inventory updated successfully with ID: {}", updatedInventory.getId());
        return convertToDto(updatedInventory);
    }
    
    public void deleteBloodInventory(Long id) {
        log.info("Deleting blood inventory with ID: {}", id);
        if (!bloodInventoryRepository.existsById(id)) {
            throw new RuntimeException("Blood inventory not found with ID: " + id);
        }
        bloodInventoryRepository.deleteById(id);
        log.info("Blood inventory deleted successfully with ID: {}", id);
    }
    
    private BloodInventoryDto convertToDto(BloodInventory bloodInventory) {
        BloodInventoryDto dto = new BloodInventoryDto();
        dto.setId(bloodInventory.getId());
        dto.setHospitalId(bloodInventory.getHospital().getId());
        dto.setBloodGroup(bloodInventory.getBloodGroup());
        dto.setAvailableQuantity(bloodInventory.getAvailableQuantity());
        dto.setTotalQuantity(bloodInventory.getTotalQuantity());
        dto.setExpiryDate(bloodInventory.getExpiryDate());
        dto.setStatus(bloodInventory.getStatus());
        dto.setNotes(bloodInventory.getNotes());
        dto.setCreatedAt(bloodInventory.getCreatedAt());
        dto.setUpdatedAt(bloodInventory.getUpdatedAt());
        
        // Set display name
        dto.setHospitalName(bloodInventory.getHospital().getHospitalName());
        
        return dto;
    }
    
    private BloodInventory convertToEntity(BloodInventoryDto dto) {
        BloodInventory bloodInventory = new BloodInventory();
        bloodInventory.setId(dto.getId());
        bloodInventory.setBloodGroup(dto.getBloodGroup());
        bloodInventory.setAvailableQuantity(dto.getAvailableQuantity());
        bloodInventory.setTotalQuantity(dto.getTotalQuantity());
        bloodInventory.setExpiryDate(dto.getExpiryDate());
        bloodInventory.setStatus(dto.getStatus());
        bloodInventory.setNotes(dto.getNotes());
        return bloodInventory;
    }
} 