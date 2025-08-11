package com.bloodsync.controller;

import com.bloodsync.dto.PatientDto;
import com.bloodsync.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/request/patient")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PatientController {
    
    private final PatientService patientService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        log.info("GET /api/request/patient - Fetching all patients");
        List<PatientDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        log.info("GET /api/request/patient/{} - Fetching patient by ID", id);
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<PatientDto>> getPatientsByHospitalId(@PathVariable Long hospitalId) {
        log.info("GET /api/request/patient/hospital/{} - Fetching patients by hospital ID", hospitalId);
        List<PatientDto> patients = patientService.getPatientsByHospitalId(hospitalId);
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/blood-group/{bloodGroup}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<List<PatientDto>> getPatientsByBloodGroup(@PathVariable String bloodGroup) {
        log.info("GET /api/request/patient/blood-group/{} - Fetching patients by blood group", bloodGroup);
        List<PatientDto> patients = patientService.getPatientsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(patients);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto patientDto) {
        log.info("POST /api/request/patient - Creating new patient");
        try {
            PatientDto createdPatient = patientService.createPatient(patientDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDto patientDto) {
        log.info("PUT /api/request/patient/{} - Updating patient", id);
        try {
            PatientDto updatedPatient = patientService.updatePatient(id, patientDto);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.info("DELETE /api/request/patient/{} - Deleting patient", id);
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 