package com.bloodsync.service;

import com.bloodsync.dto.BloodDonationDto;
import com.bloodsync.entity.BloodDonation;
import com.bloodsync.entity.Donor;
import com.bloodsync.entity.Hospital;
import com.bloodsync.repository.BloodDonationRepository;
import com.bloodsync.repository.DonorRepository;
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
public class BloodDonationService {
    
    private final BloodDonationRepository bloodDonationRepository;
    private final DonorRepository donorRepository;
    private final HospitalRepository hospitalRepository;
    
    public List<BloodDonationDto> getAllBloodDonations() {
        log.info("Fetching all blood donations");
        return bloodDonationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<BloodDonationDto> getBloodDonationById(Long id) {
        log.info("Fetching blood donation with ID: {}", id);
        return bloodDonationRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<BloodDonationDto> getBloodDonationsByDonorId(Long donorId) {
        log.info("Fetching blood donations for donor ID: {}", donorId);
        return bloodDonationRepository.findByDonorId(donorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodDonationDto> getBloodDonationsByHospitalId(Long hospitalId) {
        log.info("Fetching blood donations for hospital ID: {}", hospitalId);
        return bloodDonationRepository.findByHospitalId(hospitalId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodDonationDto> getBloodDonationsByBloodGroup(String bloodGroup) {
        log.info("Fetching blood donations for blood group: {}", bloodGroup);
        return bloodDonationRepository.findByBloodGroup(bloodGroup).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodDonationDto> getBloodDonationsByStatus(BloodDonation.DonationStatus status) {
        log.info("Fetching blood donations with status: {}", status);
        return bloodDonationRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodDonationDto> getBloodDonationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching blood donations between {} and {}", startDate, endDate);
        return bloodDonationRepository.findByDonationDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BloodDonationDto createBloodDonation(BloodDonationDto bloodDonationDto) {
        log.info("Creating new blood donation for donor ID: {}", bloodDonationDto.getDonorId());
        
        // Validate donor exists
        Donor donor = donorRepository.findById(bloodDonationDto.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found with ID: " + bloodDonationDto.getDonorId()));
        
        // Validate hospital exists
        Hospital hospital = hospitalRepository.findById(bloodDonationDto.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital not found with ID: " + bloodDonationDto.getHospitalId()));
        
        // Validate donor is eligible
        if (!donor.isEligible()) {
            throw new RuntimeException("Donor is not eligible for donation");
        }
        
        BloodDonation bloodDonation = convertToEntity(bloodDonationDto);
        bloodDonation.setDonor(donor);
        bloodDonation.setHospital(hospital);
        
        // Set donation date if not provided
        if (bloodDonation.getDonationDate() == null) {
            bloodDonation.setDonationDate(LocalDateTime.now());
        }
        
        BloodDonation savedDonation = bloodDonationRepository.save(bloodDonation);
        
        // Update donor's last donation date
        donor.setLastDonationDate(bloodDonation.getDonationDate().toLocalDate());
        donorRepository.save(donor);
        
        log.info("Blood donation created successfully with ID: {}", savedDonation.getId());
        return convertToDto(savedDonation);
    }
    
    public BloodDonationDto updateBloodDonation(Long id, BloodDonationDto bloodDonationDto) {
        log.info("Updating blood donation with ID: {}", id);
        
        BloodDonation bloodDonation = bloodDonationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood donation not found with ID: " + id));
        
        // Update fields
        if (bloodDonationDto.getDonationDate() != null) {
            bloodDonation.setDonationDate(bloodDonationDto.getDonationDate());
        }
        if (bloodDonationDto.getBloodGroup() != null) {
            bloodDonation.setBloodGroup(bloodDonationDto.getBloodGroup());
        }
        if (bloodDonationDto.getQuantity() != null) {
            bloodDonation.setQuantity(bloodDonationDto.getQuantity());
        }
        if (bloodDonationDto.getStatus() != null) {
            bloodDonation.setStatus(bloodDonationDto.getStatus());
        }
        if (bloodDonationDto.getNotes() != null) {
            bloodDonation.setNotes(bloodDonationDto.getNotes());
        }
        
        BloodDonation updatedDonation = bloodDonationRepository.save(bloodDonation);
        log.info("Blood donation updated successfully with ID: {}", updatedDonation.getId());
        return convertToDto(updatedDonation);
    }
    
    public void deleteBloodDonation(Long id) {
        log.info("Deleting blood donation with ID: {}", id);
        if (!bloodDonationRepository.existsById(id)) {
            throw new RuntimeException("Blood donation not found with ID: " + id);
        }
        bloodDonationRepository.deleteById(id);
        log.info("Blood donation deleted successfully with ID: {}", id);
    }
    
    private BloodDonationDto convertToDto(BloodDonation bloodDonation) {
        BloodDonationDto dto = new BloodDonationDto();
        dto.setId(bloodDonation.getId());
        dto.setDonorId(bloodDonation.getDonor().getId());
        dto.setHospitalId(bloodDonation.getHospital().getId());
        dto.setDonationDate(bloodDonation.getDonationDate());
        dto.setBloodGroup(bloodDonation.getBloodGroup());
        dto.setQuantity(bloodDonation.getQuantity());
        dto.setStatus(bloodDonation.getStatus());
        dto.setNotes(bloodDonation.getNotes());
        dto.setCreatedAt(bloodDonation.getCreatedAt());
        dto.setUpdatedAt(bloodDonation.getUpdatedAt());
        
        // Set display names
        dto.setDonorName(bloodDonation.getDonor().getFirstName() + " " + bloodDonation.getDonor().getLastName());
        dto.setHospitalName(bloodDonation.getHospital().getHospitalName());
        
        return dto;
    }
    
    private BloodDonation convertToEntity(BloodDonationDto dto) {
        BloodDonation bloodDonation = new BloodDonation();
        bloodDonation.setId(dto.getId());
        bloodDonation.setDonationDate(dto.getDonationDate());
        bloodDonation.setBloodGroup(dto.getBloodGroup());
        bloodDonation.setQuantity(dto.getQuantity());
        bloodDonation.setStatus(dto.getStatus());
        bloodDonation.setNotes(dto.getNotes());
        return bloodDonation;
    }
} 