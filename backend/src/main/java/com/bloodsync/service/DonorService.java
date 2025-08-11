package com.bloodsync.service;

import com.bloodsync.dto.DonorDto;
import com.bloodsync.entity.Donor;
import com.bloodsync.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DonorService {
    
    private final DonorRepository donorRepository;
    
    public List<DonorDto> getAllDonors() {
        return donorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<DonorDto> getDonorById(Long id) {
        return donorRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<DonorDto> getDonorsByBloodGroup(String bloodGroup) {
        return donorRepository.findByBloodGroup(bloodGroup).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<DonorDto> getEligibleDonors() {
        return donorRepository.findByIsEligibleTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public DonorDto createDonor(DonorDto donorDto) {
        if (donorRepository.existsByEmail(donorDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        Donor donor = convertToEntity(donorDto);
        Donor savedDonor = donorRepository.save(donor);
        return convertToDto(savedDonor);
    }
    
    public DonorDto updateDonor(Long id, DonorDto donorDto) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found"));
        
        // Check if email is being changed and if it already exists
        if (!donor.getEmail().equals(donorDto.getEmail()) && 
            donorRepository.existsByEmail(donorDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        donor.setFirstName(donorDto.getFirstName());
        donor.setLastName(donorDto.getLastName());
        donor.setEmail(donorDto.getEmail());
        donor.setPhoneNumber(donorDto.getPhoneNumber());
        donor.setDateOfBirth(donorDto.getDateOfBirth());
        donor.setBloodGroup(donorDto.getBloodGroup());
        donor.setAddress(donorDto.getAddress());
        donor.setCity(donorDto.getCity());
        donor.setState(donorDto.getState());
        donor.setEligible(donorDto.isEligible());
        donor.setLastDonationDate(donorDto.getLastDonationDate());
        donor.setActive(donorDto.isActive());
        
        Donor updatedDonor = donorRepository.save(donor);
        return convertToDto(updatedDonor);
    }
    
    public void deleteDonor(Long id) {
        if (!donorRepository.existsById(id)) {
            throw new RuntimeException("Donor not found");
        }
        donorRepository.deleteById(id);
    }
    
    private DonorDto convertToDto(Donor donor) {
        DonorDto dto = new DonorDto();
        dto.setId(donor.getId());
        dto.setFirstName(donor.getFirstName());
        dto.setLastName(donor.getLastName());
        dto.setEmail(donor.getEmail());
        dto.setPhoneNumber(donor.getPhoneNumber());
        dto.setDateOfBirth(donor.getDateOfBirth());
        dto.setBloodGroup(donor.getBloodGroup());
        dto.setAddress(donor.getAddress());
        dto.setCity(donor.getCity());
        dto.setState(donor.getState());
        dto.setEligible(donor.isEligible());
        dto.setLastDonationDate(donor.getLastDonationDate());
        dto.setActive(donor.isActive());
        dto.setCreatedAt(donor.getCreatedAt());
        dto.setUpdatedAt(donor.getUpdatedAt());
        return dto;
    }
    
    private Donor convertToEntity(DonorDto dto) {
        Donor donor = new Donor();
        donor.setId(dto.getId());
        donor.setFirstName(dto.getFirstName());
        donor.setLastName(dto.getLastName());
        donor.setEmail(dto.getEmail());
        donor.setPhoneNumber(dto.getPhoneNumber());
        donor.setDateOfBirth(dto.getDateOfBirth());
        donor.setBloodGroup(dto.getBloodGroup());
        donor.setAddress(dto.getAddress());
        donor.setCity(dto.getCity());
        donor.setState(dto.getState());
        donor.setEligible(dto.isEligible());
        donor.setLastDonationDate(dto.getLastDonationDate());
        donor.setActive(dto.isActive());
        return donor;
    }
    
    // Public methods for unauthenticated access
    public List<DonorDto> getPublicDonors() {
        // Return only basic information for public access
        return donorRepository.findByIsActiveTrue().stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }
    
    public List<DonorDto> getPublicDonorsByBloodGroup(String bloodGroup) {
        return donorRepository.findByBloodGroupAndIsActiveTrue(bloodGroup).stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }
    
    public List<DonorDto> getPublicDonorsByLocation(String city) {
        return donorRepository.findByCityAndIsActiveTrue(city).stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }
    
    public Object getBloodDonationStats() {
        // Return basic statistics about blood donations
        return new Object(); // Placeholder implementation
    }
    
    private DonorDto convertToPublicDto(Donor donor) {
        DonorDto dto = new DonorDto();
        dto.setId(donor.getId());
        dto.setFirstName(donor.getFirstName());
        dto.setLastName(donor.getLastName());
        dto.setBloodGroup(donor.getBloodGroup());
        dto.setCity(donor.getCity());
        dto.setState(donor.getState());
        dto.setEligible(donor.isEligible());
        // Don't include sensitive information like email, phone, address
        return dto;
    }
} 