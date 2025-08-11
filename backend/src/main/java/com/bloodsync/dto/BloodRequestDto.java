package com.bloodsync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestDto {
    
    private Long id;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;
    
    @NotBlank(message = "Blood group is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood group must be in format A+, A-, B+, B-, AB+, AB-, O+, O-")
    private String bloodGroup;
    
    @NotNull(message = "Required quantity is required")
    @Min(value = 100, message = "Minimum required quantity is 100ml")
    @Max(value = 2000, message = "Maximum required quantity is 2000ml")
    private Integer requiredQuantity;
    
    @NotNull(message = "Request date is required")
    private LocalDateTime requestDate;
    
    @NotNull(message = "Required date is required")
    @Future(message = "Required date must be in the future")
    private LocalDateTime requiredDate;
    
    private String status;
    private String priority;
    
    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 