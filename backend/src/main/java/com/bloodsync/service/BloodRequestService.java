package com.bloodsync.service;

import com.bloodsync.dto.BloodRequestDto;
import com.bloodsync.entity.BloodRequest;
import com.bloodsync.entity.Hospital;
import com.bloodsync.entity.Patient;
import com.bloodsync.repository.BloodRequestRepository;
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
public class BloodRequestService {
    
    private final BloodRequestRepository bloodRequestRepository;
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;
    
    public List<BloodRequestDto> getAllBloodRequests() {
        return bloodRequestRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<BloodRequestDto> getBloodRequestById(Long id) {
        return bloodRequestRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<BloodRequestDto> getBloodRequestsByHospitalId(Long hospitalId) {
        return bloodRequestRepository.findByHospitalId(hospitalId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodRequestDto> getBloodRequestsByPatientId(Long patientId) {
        return bloodRequestRepository.findByPatientId(patientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodRequestDto> getBloodRequestsByStatus(String status) {
        return bloodRequestRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BloodRequestDto> getBloodRequestsByBloodGroup(String bloodGroup) {
        return bloodRequestRepository.findByBloodGroup(bloodGroup).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BloodRequestDto createBloodRequest(BloodRequestDto bloodRequestDto) {
        // Verify patient exists
        Patient patient = patientRepository.findById(bloodRequestDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        // Verify hospital exists
        Hospital hospital = hospitalRepository.findById(bloodRequestDto.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
        
        BloodRequest bloodRequest = convertToEntity(bloodRequestDto);
        bloodRequest.setPatient(patient);
        bloodRequest.setHospital(hospital);
        
        BloodRequest savedBloodRequest = bloodRequestRepository.save(bloodRequest);
        return convertToDto(savedBloodRequest);
    }
    
    public BloodRequestDto updateBloodRequest(Long id, BloodRequestDto bloodRequestDto) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blood request not found"));
        
        // Verify patient exists if being changed
        if (!bloodRequest.getPatient().getId().equals(bloodRequestDto.getPatientId())) {
            Patient patient = patientRepository.findById(bloodRequestDto.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            bloodRequest.setPatient(patient);
        }
        
        // Verify hospital exists if being changed
        if (!bloodRequest.getHospital().getId().equals(bloodRequestDto.getHospitalId())) {
            Hospital hospital = hospitalRepository.findById(bloodRequestDto.getHospitalId())
                    .orElseThrow(() -> new RuntimeException("Hospital not found"));
            bloodRequest.setHospital(hospital);
        }
        
        bloodRequest.setBloodGroup(bloodRequestDto.getBloodGroup());
        bloodRequest.setRequiredQuantity(bloodRequestDto.getRequiredQuantity());
        bloodRequest.setRequestDate(bloodRequestDto.getRequestDate());
        bloodRequest.setRequiredDate(bloodRequestDto.getRequiredDate());
        bloodRequest.setStatus(BloodRequest.RequestStatus.valueOf(bloodRequestDto.getStatus()));
        bloodRequest.setPriority(BloodRequest.PriorityLevel.valueOf(bloodRequestDto.getPriority()));
        bloodRequest.setReason(bloodRequestDto.getReason());
        bloodRequest.setNotes(bloodRequestDto.getNotes());
        
        BloodRequest updatedBloodRequest = bloodRequestRepository.save(bloodRequest);
        return convertToDto(updatedBloodRequest);
    }
    
    public void deleteBloodRequest(Long id) {
        if (!bloodRequestRepository.existsById(id)) {
            throw new RuntimeException("Blood request not found");
        }
        bloodRequestRepository.deleteById(id);
    }
    
    private BloodRequestDto convertToDto(BloodRequest bloodRequest) {
        BloodRequestDto dto = new BloodRequestDto();
        dto.setId(bloodRequest.getId());
        dto.setPatientId(bloodRequest.getPatient().getId());
        dto.setHospitalId(bloodRequest.getHospital().getId());
        dto.setBloodGroup(bloodRequest.getBloodGroup());
        dto.setRequiredQuantity(bloodRequest.getRequiredQuantity());
        dto.setRequestDate(bloodRequest.getRequestDate());
        dto.setRequiredDate(bloodRequest.getRequiredDate());
        dto.setStatus(bloodRequest.getStatus().name());
        dto.setPriority(bloodRequest.getPriority().name());
        dto.setReason(bloodRequest.getReason());
        dto.setNotes(bloodRequest.getNotes());
        dto.setCreatedAt(bloodRequest.getCreatedAt());
        dto.setUpdatedAt(bloodRequest.getUpdatedAt());
        return dto;
    }
    
    private BloodRequest convertToEntity(BloodRequestDto dto) {
        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setId(dto.getId());
        bloodRequest.setBloodGroup(dto.getBloodGroup());
        bloodRequest.setRequiredQuantity(dto.getRequiredQuantity());
        bloodRequest.setRequestDate(dto.getRequestDate());
        bloodRequest.setRequiredDate(dto.getRequiredDate());
        bloodRequest.setStatus(BloodRequest.RequestStatus.valueOf(dto.getStatus()));
        bloodRequest.setPriority(BloodRequest.PriorityLevel.valueOf(dto.getPriority()));
        bloodRequest.setReason(dto.getReason());
        bloodRequest.setNotes(dto.getNotes());
        return bloodRequest;
    }
} 