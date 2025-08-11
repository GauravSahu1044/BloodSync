package com.bloodsync.controller;

import com.bloodsync.entity.Hospital;
import com.bloodsync.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hospital")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HospitalController {
    
    private final HospitalService hospitalService;
    
    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        log.info("GET /api/hospital - Fetching all hospitals");
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable Long id) {
        log.info("GET /api/hospital/{} - Fetching hospital by ID", id);
        return hospitalService.getHospitalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<Hospital> getHospitalByEmail(@PathVariable String email) {
        log.info("GET /api/hospital/email/{} - Fetching hospital by email", email);
        return hospitalService.getHospitalByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/license/{licenseNumber}")
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<Hospital> getHospitalByLicenseNumber(@PathVariable String licenseNumber) {
        log.info("GET /api/hospital/license/{} - Fetching hospital by license number", licenseNumber);
        return hospitalService.getHospitalByLicenseNumber(licenseNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<Hospital> createHospital(@Valid @RequestBody Hospital hospital) {
        log.info("POST /api/hospital - Creating new hospital: {}", hospital.getHospitalName());
        Hospital createdHospital = hospitalService.createHospital(hospital);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHospital);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<Hospital> updateHospital(@PathVariable Long id, @Valid @RequestBody Hospital hospital) {
        log.info("PUT /api/hospital/{} - Updating hospital", id);
        Hospital updatedHospital = hospitalService.updateHospital(id, hospital);
        return ResponseEntity.ok(updatedHospital);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOSPITAL')")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        log.info("DELETE /api/hospital/{} - Deleting hospital", id);
        hospitalService.deleteHospital(id);
        return ResponseEntity.noContent().build();
    }
} 