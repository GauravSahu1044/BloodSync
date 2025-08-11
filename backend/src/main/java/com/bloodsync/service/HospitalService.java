package com.bloodsync.service;

import com.bloodsync.entity.Hospital;
import com.bloodsync.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HospitalService {
    
    private final HospitalRepository hospitalRepository;
    
    public List<Hospital> getAllHospitals() {
        log.info("Fetching all hospitals");
        return hospitalRepository.findAll();
    }
    
    public Optional<Hospital> getHospitalById(Long id) {
        log.info("Fetching hospital with ID: {}", id);
        return hospitalRepository.findById(id);
    }
    
    public Optional<Hospital> getHospitalByEmail(String email) {
        log.info("Fetching hospital with email: {}", email);
        return hospitalRepository.findByEmail(email);
    }
    
    public Optional<Hospital> getHospitalByLicenseNumber(String licenseNumber) {
        log.info("Fetching hospital with license number: {}", licenseNumber);
        return hospitalRepository.findByLicenseNumber(licenseNumber);
    }
    
    public Hospital createHospital(Hospital hospital) {
        log.info("Creating new hospital: {}", hospital.getHospitalName());
        
        // Check if hospital with same email already exists
        if (hospitalRepository.existsByEmail(hospital.getEmail())) {
            throw new RuntimeException("Hospital with email " + hospital.getEmail() + " already exists");
        }
        
        // Check if hospital with same license number already exists
        if (hospitalRepository.existsByLicenseNumber(hospital.getLicenseNumber())) {
            throw new RuntimeException("Hospital with license number " + hospital.getLicenseNumber() + " already exists");
        }
        
        Hospital savedHospital = hospitalRepository.save(hospital);
        log.info("Hospital created successfully with ID: {}", savedHospital.getId());
        return savedHospital;
    }
    
    public Hospital updateHospital(Long id, Hospital hospitalDetails) {
        log.info("Updating hospital with ID: {}", id);
        
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + id));
        
        // Check if email is being changed and if it already exists
        if (!hospital.getEmail().equals(hospitalDetails.getEmail()) && 
            hospitalRepository.existsByEmail(hospitalDetails.getEmail())) {
            throw new RuntimeException("Hospital with email " + hospitalDetails.getEmail() + " already exists");
        }
        
        // Check if license number is being changed and if it already exists
        if (!hospital.getLicenseNumber().equals(hospitalDetails.getLicenseNumber()) && 
            hospitalRepository.existsByLicenseNumber(hospitalDetails.getLicenseNumber())) {
            throw new RuntimeException("Hospital with license number " + hospitalDetails.getLicenseNumber() + " already exists");
        }
        
        hospital.setHospitalName(hospitalDetails.getHospitalName());
        hospital.setEmail(hospitalDetails.getEmail());
        hospital.setPhoneNumber(hospitalDetails.getPhoneNumber());
        hospital.setAddress(hospitalDetails.getAddress());
        hospital.setCity(hospitalDetails.getCity());
        hospital.setState(hospitalDetails.getState());
        hospital.setLicenseNumber(hospitalDetails.getLicenseNumber());
        hospital.setActive(hospitalDetails.isActive());
        
        Hospital updatedHospital = hospitalRepository.save(hospital);
        log.info("Hospital updated successfully with ID: {}", updatedHospital.getId());
        return updatedHospital;
    }
    
    public void deleteHospital(Long id) {
        log.info("Deleting hospital with ID: {}", id);
        if (!hospitalRepository.existsById(id)) {
            throw new RuntimeException("Hospital not found with id: " + id);
        }
        hospitalRepository.deleteById(id);
        log.info("Hospital deleted successfully with ID: {}", id);
    }
    
    // Public methods for unauthenticated access
    public List<Hospital> getPublicHospitals() {
        log.info("Fetching public hospital information");
        return hospitalRepository.findByIsActiveTrue();
    }
    
    public List<Hospital> getPublicHospitalsByLocation(String city) {
        log.info("Fetching public hospitals by location: {}", city);
        return hospitalRepository.findByCityAndIsActiveTrue(city);
    }
    
    public List<Hospital> getEmergencyContacts() {
        log.info("Fetching emergency contact information");
        return hospitalRepository.findByIsActiveTrue(); // All active hospitals can be emergency contacts
    }
} 