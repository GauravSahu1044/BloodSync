package com.bloodsync.controller;

import com.bloodsync.dto.BloodRequestDto;
import com.bloodsync.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/request")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BloodRequestController {
    
    private final BloodRequestService bloodRequestService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<BloodRequestDto>> getAllBloodRequests() {
        log.info("GET /api/request - Fetching all blood requests");
        List<BloodRequestDto> bloodRequests = bloodRequestService.getAllBloodRequests();
        return ResponseEntity.ok(bloodRequests);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<BloodRequestDto> getBloodRequestById(@PathVariable Long id) {
        log.info("GET /api/request/{} - Fetching blood request by ID", id);
        return bloodRequestService.getBloodRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<BloodRequestDto>> getBloodRequestsByHospitalId(@PathVariable Long hospitalId) {
        log.info("GET /api/request/hospital/{} - Fetching blood requests by hospital ID", hospitalId);
        List<BloodRequestDto> bloodRequests = bloodRequestService.getBloodRequestsByHospitalId(hospitalId);
        return ResponseEntity.ok(bloodRequests);
    }
    
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<BloodRequestDto>> getBloodRequestsByPatientId(@PathVariable Long patientId) {
        log.info("GET /api/request/patient/{} - Fetching blood requests by patient ID", patientId);
        List<BloodRequestDto> bloodRequests = bloodRequestService.getBloodRequestsByPatientId(patientId);
        return ResponseEntity.ok(bloodRequests);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<BloodRequestDto>> getBloodRequestsByStatus(@PathVariable String status) {
        log.info("GET /api/request/status/{} - Fetching blood requests by status", status);
        List<BloodRequestDto> bloodRequests = bloodRequestService.getBloodRequestsByStatus(status);
        return ResponseEntity.ok(bloodRequests);
    }
    
    @GetMapping("/blood-group/{bloodGroup}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<BloodRequestDto>> getBloodRequestsByBloodGroup(@PathVariable String bloodGroup) {
        log.info("GET /api/request/blood-group/{} - Fetching blood requests by blood group", bloodGroup);
        List<BloodRequestDto> bloodRequests = bloodRequestService.getBloodRequestsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(bloodRequests);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<BloodRequestDto> createBloodRequest(@Valid @RequestBody BloodRequestDto bloodRequestDto) {
        log.info("POST /api/request - Creating new blood request");
        try {
            BloodRequestDto createdBloodRequest = bloodRequestService.createBloodRequest(bloodRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBloodRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<BloodRequestDto> updateBloodRequest(@PathVariable Long id, @Valid @RequestBody BloodRequestDto bloodRequestDto) {
        log.info("PUT /api/request/{} - Updating blood request", id);
        try {
            BloodRequestDto updatedBloodRequest = bloodRequestService.updateBloodRequest(id, bloodRequestDto);
            return ResponseEntity.ok(updatedBloodRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<Void> deleteBloodRequest(@PathVariable Long id) {
        log.info("DELETE /api/request/{} - Deleting blood request", id);
        try {
            bloodRequestService.deleteBloodRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 