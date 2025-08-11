package com.bloodsync.controller;

import com.bloodsync.dto.DonorDto;
import com.bloodsync.entity.Hospital;
import com.bloodsync.service.BloodDonationService;
import com.bloodsync.service.BloodInventoryService;
import com.bloodsync.service.DonorService;
import com.bloodsync.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PublicController {
    
    private final DonorService donorService;
    private final HospitalService hospitalService;
    private final BloodDonationService bloodDonationService;
    private final BloodInventoryService bloodInventoryService;
    
    @GetMapping("/donors")
    public ResponseEntity<List<DonorDto>> getPublicDonors() {
        log.info("GET /api/public/donors - Fetching public donor information");
        List<DonorDto> donors = donorService.getPublicDonors();
        return ResponseEntity.ok(donors);
    }
    
    @GetMapping("/donors/blood-group/{bloodGroup}")
    public ResponseEntity<List<DonorDto>> getPublicDonorsByBloodGroup(@PathVariable String bloodGroup) {
        log.info("GET /api/public/donors/blood-group/{} - Fetching public donors by blood group", bloodGroup);
        List<DonorDto> donors = donorService.getPublicDonorsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(donors);
    }
    
    @GetMapping("/donors/location/{city}")
    public ResponseEntity<List<DonorDto>> getPublicDonorsByLocation(@PathVariable String city) {
        log.info("GET /api/public/donors/location/{} - Fetching public donors by location", city);
        List<DonorDto> donors = donorService.getPublicDonorsByLocation(city);
        return ResponseEntity.ok(donors);
    }
    
    @GetMapping("/hospitals")
    public ResponseEntity<List<Hospital>> getPublicHospitals() {
        log.info("GET /api/public/hospitals - Fetching public hospital information");
        List<Hospital> hospitals = hospitalService.getPublicHospitals();
        return ResponseEntity.ok(hospitals);
    }
    
    @GetMapping("/hospitals/location/{city}")
    public ResponseEntity<List<Hospital>> getPublicHospitalsByLocation(@PathVariable String city) {
        log.info("GET /api/public/hospitals/location/{} - Fetching public hospitals by location", city);
        List<Hospital> hospitals = hospitalService.getPublicHospitalsByLocation(city);
        return ResponseEntity.ok(hospitals);
    }
    
    @GetMapping("/blood-stats")
    public ResponseEntity<Map<String, Object>> getBloodDonationStats() {
        log.info("GET /api/public/blood-stats - Fetching blood donation statistics");
        
        Map<String, Object> stats = new HashMap<>();
        
        // Get total donors
        List<DonorDto> allDonors = donorService.getPublicDonors();
        stats.put("totalDonors", allDonors.size());
        
        // Get total hospitals
        List<Hospital> allHospitals = hospitalService.getPublicHospitals();
        stats.put("totalHospitals", allHospitals.size());
        
        // Get total blood donations
        List<com.bloodsync.dto.BloodDonationDto> allDonations = bloodDonationService.getAllBloodDonations();
        stats.put("totalDonations", allDonations.size());
        
        // Get total blood inventory
        List<com.bloodsync.dto.BloodInventoryDto> allInventory = bloodInventoryService.getAllBloodInventory();
        stats.put("totalInventory", allInventory.size());
        
        // Calculate total blood volume in inventory
        int totalBloodVolume = allInventory.stream()
                .mapToInt(inv -> inv.getAvailableQuantity() != null ? inv.getAvailableQuantity() : 0)
                .sum();
        stats.put("totalBloodVolume", totalBloodVolume);
        
        // Blood group distribution
        Map<String, Long> bloodGroupDistribution = allDonors.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    DonorDto::getBloodGroup,
                    java.util.stream.Collectors.counting()
                ));
        stats.put("bloodGroupDistribution", bloodGroupDistribution);
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/emergency-contacts")
    public ResponseEntity<List<Hospital>> getEmergencyContacts() {
        log.info("GET /api/public/emergency-contacts - Fetching emergency contact information");
        List<Hospital> emergencyContacts = hospitalService.getEmergencyContacts();
        return ResponseEntity.ok(emergencyContacts);
    }
} 