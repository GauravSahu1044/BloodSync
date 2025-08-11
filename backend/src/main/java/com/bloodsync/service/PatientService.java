package com.bloodsync.service;

import com.bloodsync.dto.PatientDto;
import com.bloodsync.entity.Hospital;
import com.bloodsync.entity.Patient;
import com.bloodsync.repository.HospitalRepository;
import com.bloodsync.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {
    
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;
    
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<PatientDto> getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<PatientDto> getPatientsByHospitalId(Long hospitalId) {
        return patientRepository.findByHospitalId(hospitalId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<PatientDto> getPatientsByBloodGroup(String bloodGroup) {
        return patientRepository.findByBloodGroup(bloodGroup).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public PatientDto createPatient(PatientDto patientDto) {
        if (patientRepository.existsByEmail(patientDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Verify hospital exists
        Hospital hospital = hospitalRepository.findById(patientDto.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
        
        Patient patient = convertToEntity(patientDto);
        patient.setHospital(hospital);
        
        Patient savedPatient = patientRepository.save(patient);
        return convertToDto(savedPatient);
    }
    
    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        // Check if email is being changed and if it already exists
        if (!patient.getEmail().equals(patientDto.getEmail()) && 
            patientRepository.existsByEmail(patientDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Verify hospital exists if being changed
        if (!patient.getHospital().getId().equals(patientDto.getHospitalId())) {
            Hospital hospital = hospitalRepository.findById(patientDto.getHospitalId())
                    .orElseThrow(() -> new RuntimeException("Hospital not found"));
            patient.setHospital(hospital);
        }
        
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhoneNumber(patientDto.getPhoneNumber());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setBloodGroup(patientDto.getBloodGroup());
        patient.setAddress(patientDto.getAddress());
        patient.setCity(patientDto.getCity());
        patient.setState(patientDto.getState());
        patient.setMedicalHistory(patientDto.getMedicalHistory());
        patient.setActive(patientDto.isActive());
        
        Patient updatedPatient = patientRepository.save(patient);
        return convertToDto(updatedPatient);
    }
    
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found");
        }
        patientRepository.deleteById(id);
    }
    
    private PatientDto convertToDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setAddress(patient.getAddress());
        dto.setCity(patient.getCity());
        dto.setState(patient.getState());
        dto.setHospitalId(patient.getHospital().getId());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setActive(patient.isActive());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());
        return dto;
    }
    
    private Patient convertToEntity(PatientDto dto) {
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setAddress(dto.getAddress());
        patient.setCity(dto.getCity());
        patient.setState(dto.getState());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setActive(dto.isActive());
        return patient;
    }
} 