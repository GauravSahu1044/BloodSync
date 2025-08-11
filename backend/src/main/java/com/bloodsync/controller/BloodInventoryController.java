package com.bloodsync.controller;

import com.bloodsync.dto.BloodInventoryDto;
import com.bloodsync.entity.BloodInventory;
import com.bloodsync.service.BloodInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/blood-inventory")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BloodInventoryController {
    
    private final BloodInventoryService bloodInventoryService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodInventoryDto>> getAllBloodInventory() {
        log.info("GET /api/blood-inventory - Fetching all blood inventory");
        List<BloodInventoryDto> bloodInventory = bloodInventoryService.getAllBloodInventory();
        return ResponseEntity.ok(bloodInventory);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<BloodInventoryDto> getBloodInventoryById(@PathVariable Long id) {
        log.info("GET /api/blood-inventory/{} - Fetching blood inventory by ID", id);
        return bloodInventoryService.getBloodInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodInventoryDto>> getBloodInventoryByHospitalId(@PathVariable Long hospitalId) {
        log.info("GET /api/blood-inventory/hospital/{} - Fetching blood inventory by hospital ID", hospitalId);
        List<BloodInventoryDto> bloodInventory = bloodInventoryService.getBloodInventoryByHospitalId(hospitalId);
        return ResponseEntity.ok(bloodInventory);
    }
    
    @GetMapping("/blood-group/{bloodGroup}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodInventoryDto>> getBloodInventoryByBloodGroup(@PathVariable String bloodGroup) {
        log.info("GET /api/blood-inventory/blood-group/{} - Fetching blood inventory by blood group", bloodGroup);
        List<BloodInventoryDto> bloodInventory = bloodInventoryService.getBloodInventoryByBloodGroup(bloodGroup);
        return ResponseEntity.ok(bloodInventory);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodInventoryDto>> getBloodInventoryByStatus(@PathVariable BloodInventory.InventoryStatus status) {
        log.info("GET /api/blood-inventory/status/{} - Fetching blood inventory by status", status);
        List<BloodInventoryDto> bloodInventory = bloodInventoryService.getBloodInventoryByStatus(status);
        return ResponseEntity.ok(bloodInventory);
    }
    
    @GetMapping("/hospital/{hospitalId}/blood-group/{bloodGroup}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodInventoryDto>> getBloodInventoryByHospitalAndBloodGroup(
            @PathVariable Long hospitalId,
            @PathVariable String bloodGroup) {
        log.info("GET /api/blood-inventory/hospital/{}/blood-group/{} - Fetching blood inventory by hospital and blood group", hospitalId, bloodGroup);
        List<BloodInventoryDto> bloodInventory = bloodInventoryService.getBloodInventoryByHospitalAndBloodGroup(hospitalId, bloodGroup);
        return ResponseEntity.ok(bloodInventory);
    }
    
    @GetMapping("/expired")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodInventoryDto>> getExpiredBloodInventory() {
        log.info("GET /api/blood-inventory/expired - Fetching expired blood inventory");
        List<BloodInventoryDto> bloodInventory = bloodInventoryService.getExpiredBloodInventory();
        return ResponseEntity.ok(bloodInventory);
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<List<BloodInventoryDto>> getLowStockBloodInventory(
            @RequestParam(defaultValue = "1000") Integer threshold) {
        log.info("GET /api/blood-inventory/low-stock - Fetching low stock blood inventory with threshold: {}", threshold);
        List<BloodInventoryDto> bloodInventory = bloodInventoryService.getLowStockBloodInventory(threshold);
        return ResponseEntity.ok(bloodInventory);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<BloodInventoryDto> createBloodInventory(@Valid @RequestBody BloodInventoryDto bloodInventoryDto) {
        log.info("POST /api/blood-inventory - Creating new blood inventory");
        try {
            BloodInventoryDto createdInventory = bloodInventoryService.createBloodInventory(bloodInventoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
        } catch (RuntimeException e) {
            log.error("Error creating blood inventory: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<BloodInventoryDto> updateBloodInventory(
            @PathVariable Long id,
            @Valid @RequestBody BloodInventoryDto bloodInventoryDto) {
        log.info("PUT /api/blood-inventory/{} - Updating blood inventory", id);
        try {
            BloodInventoryDto updatedInventory = bloodInventoryService.updateBloodInventory(id, bloodInventoryDto);
            return ResponseEntity.ok(updatedInventory);
        } catch (RuntimeException e) {
            log.error("Error updating blood inventory: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBloodInventory(@PathVariable Long id) {
        log.info("DELETE /api/blood-inventory/{} - Deleting blood inventory", id);
        try {
            bloodInventoryService.deleteBloodInventory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting blood inventory: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
} 