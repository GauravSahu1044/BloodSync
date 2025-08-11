package com.bloodsync.controller;

import com.bloodsync.dto.DonorDto;
import com.bloodsync.service.DonorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/donor")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DonorController {
    
    private final DonorService donorService;
    
    @GetMapping
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<List<DonorDto>> getAllDonors() {
        log.info("GET /api/donor - Fetching all donors");
        List<DonorDto> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<DonorDto> getDonorById(@PathVariable Long id) {
        log.info("GET /api/donor/{} - Fetching donor by ID", id);
        return donorService.getDonorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/blood-group/{bloodGroup}")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<List<DonorDto>> getDonorsByBloodGroup(@PathVariable String bloodGroup) {
        log.info("GET /api/donor/blood-group/{} - Fetching donors by blood group", bloodGroup);
        List<DonorDto> donors = donorService.getDonorsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(donors);
    }
    
    @GetMapping("/eligible")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<List<DonorDto>> getEligibleDonors() {
        log.info("GET /api/donor/eligible - Fetching eligible donors");
        List<DonorDto> donors = donorService.getEligibleDonors();
        return ResponseEntity.ok(donors);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<DonorDto> createDonor(@Valid @RequestBody DonorDto donorDto) {
        log.info("POST /api/donor - Creating new donor");
        try {
            DonorDto createdDonor = donorService.createDonor(donorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDonor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<DonorDto> updateDonor(@PathVariable Long id, @Valid @RequestBody DonorDto donorDto) {
        log.info("PUT /api/donor/{} - Updating donor", id);
        try {
            DonorDto updatedDonor = donorService.updateDonor(id, donorDto);
            return ResponseEntity.ok(updatedDonor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<Void> deleteDonor(@PathVariable Long id) {
        log.info("DELETE /api/donor/{} - Deleting donor", id);
        try {
            donorService.deleteDonor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 