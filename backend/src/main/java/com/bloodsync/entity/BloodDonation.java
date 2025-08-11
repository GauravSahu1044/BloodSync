package com.bloodsync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodDonation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Donor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
    
    @NotNull(message = "Hospital is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
    
    @NotNull(message = "Donation date is required")
    @Column(name = "donation_date", nullable = false)
    private LocalDateTime donationDate;
    
    @NotNull(message = "Blood group is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Blood group must be in format A+, A-, B+, B-, AB+, AB-, O+, O-")
    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 200, message = "Minimum donation quantity is 200ml")
    @Max(value = 500, message = "Maximum donation quantity is 500ml")
    @Column(nullable = false)
    private Integer quantity; // in ml
    
    @Enumerated(EnumType.STRING)
    @Column(name = "donation_status", nullable = false)
    private DonationStatus status = DonationStatus.COMPLETED;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum DonationStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, REJECTED
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 