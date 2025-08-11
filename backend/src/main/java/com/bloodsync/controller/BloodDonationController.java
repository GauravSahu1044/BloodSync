package com.bloodsync.controller;

import com.bloodsync.dto.BloodDonationDto;
import com.bloodsync.entity.BloodDonation;
import com.bloodsync.service.BloodDonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/blood-donations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BloodDonationController {
    
    private final BloodDonationService bloodDonationService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL', 'DONOR')")
    public ResponseEntity<List<BloodDonationDto>> getAllBloodDonations() {
        log.info("GET /api/blood-donations - Fetching all blood donations");
        List<BloodDonationDto> bloodDonations = bloodDonationService.getAllBloodDonations();
        return ResponseEntity.ok(bloodDonations);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL', 'DONOR')")
    public ResponseEntity<BloodDonationDto> getBloodDonationById(@PathVariable Long id) {
        log.info("GET /api/blood-donations/{} - Fetching blood donation by ID", id);
        return bloodDonationService.getBloodDonationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/donor/{donorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL', 'DONOR')")
    public ResponseEntity<List<BloodDonationDto>> getBloodDonationsByDonorId(@PathVariable Long donorId) {
        log.info("GET /api/blood-donations/donor/{} - Fetching blood donations by donor ID", donorId);
        List<BloodDonationDto> bloodDonations = bloodDonationService.getBloodDonationsByDonorId(donorId);
        return ResponseEntity.ok(bloodDonations);
    }
    
    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodDonationDto>> getBloodDonationsByHospitalId(@PathVariable Long hospitalId) {
        log.info("GET /api/blood-donations/hospital/{} - Fetching blood donations by hospital ID", hospitalId);
        List<BloodDonationDto> bloodDonations = bloodDonationService.getBloodDonationsByHospitalId(hospitalId);
        return ResponseEntity.ok(bloodDonations);
    }
    
    @GetMapping("/blood-group/{bloodGroup}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodDonationDto>> getBloodDonationsByBloodGroup(@PathVariable String bloodGroup) {
        log.info("GET /api/blood-donations/blood-group/{} - Fetching blood donations by blood group", bloodGroup);
        List<BloodDonationDto> bloodDonations = bloodDonationService.getBloodDonationsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(bloodDonations);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodDonationDto>> getBloodDonationsByStatus(@PathVariable BloodDonation.DonationStatus status) {
        log.info("GET /api/blood-donations/status/{} - Fetching blood donations by status", status);
        List<BloodDonationDto> bloodDonations = bloodDonationService.getBloodDonationsByStatus(status);
        return ResponseEntity.ok(bloodDonations);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodDonationDto>> getBloodDonationsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        log.info("GET /api/blood-donations/date-range - Fetching blood donations by date range");
        List<BloodDonationDto> bloodDonations = bloodDonationService.getBloodDonationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(bloodDonations);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<BloodDonationDto> createBloodDonation(@Valid @RequestBody BloodDonationDto bloodDonationDto) {
        log.info("POST /api/blood-donations - Creating new blood donation");
        try {
            BloodDonationDto createdDonation = bloodDonationService.createBloodDonation(bloodDonationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDonation);
        } catch (RuntimeException e) {
            log.error("Error creating blood donation: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<BloodDonationDto> updateBloodDonation(
            @PathVariable Long id,
            @Valid @RequestBody BloodDonationDto bloodDonationDto) {
        log.info("PUT /api/blood-donations/{} - Updating blood donation", id);
        try {
            BloodDonationDto updatedDonation = bloodDonationService.updateBloodDonation(id, bloodDonationDto);
            return ResponseEntity.ok(updatedDonation);
        } catch (RuntimeException e) {
            log.error("Error updating blood donation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBloodDonation(@PathVariable Long id) {
        log.info("DELETE /api/blood-donations/{} - Deleting blood donation", id);
        try {
            bloodDonationService.deleteBloodDonation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting blood donation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
} 