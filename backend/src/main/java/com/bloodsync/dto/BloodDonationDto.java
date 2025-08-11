package com.bloodsync.dto;

import com.bloodsync.entity.BloodDonation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodDonationDto {
    
    private Long id;
    
    @NotNull(message = "Donor ID is required")
    private Long donorId;
    
    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;
    
    @NotNull(message = "Donation date is required")
    private LocalDateTime donationDate;
    
    @NotBlank(message = "Blood group is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood group must be in format A+, A-, B+, B-, AB+, AB-, O+, O-")
    private String bloodGroup;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 200, message = "Minimum donation quantity is 200ml")
    @Max(value = 500, message = "Maximum donation quantity is 500ml")
    private Integer quantity; // in ml
    
    private BloodDonation.DonationStatus status = BloodDonation.DonationStatus.COMPLETED;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Additional fields for display
    private String donorName;
    private String hospitalName;
} 