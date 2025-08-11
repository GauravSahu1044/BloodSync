# Blood Bank Management System - Entity Relationship Diagram (ERD)

## Overview
This ERD represents the Blood Bank Management System with 8 entities and unidirectional relationships. No entity is independent, and all relationships flow in one direction to avoid circular dependencies.

## Entity Definitions

### 1. User (Authentication Entity)
- **Purpose**: Base authentication entity for all system users
- **Primary Key**: `id`
- **Unique Fields**: `username`, `email`
- **Relationships**: None (base entity for authentication)

### 2. Admin (Authentication Entity)
- **Purpose**: System administrators
- **Primary Key**: `id`
- **Unique Fields**: `username`, `email`
- **Relationships**: None (authentication/authorization only)

### 3. Hospital
- **Purpose**: Medical facilities that manage blood inventory
- **Primary Key**: `id`
- **Unique Fields**: `name`, `licenseNumber`
- **Relationships**: 
  - One-to-Many with `BloodInventory`
  - One-to-Many with `Patient`

### 4. Donor
- **Purpose**: Blood donors
- **Primary Key**: `id`
- **Unique Fields**: `email`, `phoneNumber`
- **Relationships**: 
  - One-to-Many with `BloodDonation`

### 5. BloodDonation
- **Purpose**: Records of blood donations
- **Primary Key**: `id`
- **Foreign Keys**: 
  - `donor_id` → Donor
  - `hospital_id` → Hospital
- **Relationships**: 
  - Many-to-One with `Donor`
  - Many-to-One with `Hospital`

### 6. BloodInventory
- **Purpose**: Blood stock management at hospitals
- **Primary Key**: `id`
- **Foreign Keys**: 
  - `hospital_id` → Hospital
- **Relationships**: 
  - Many-to-One with `Hospital`

### 7. Patient
- **Purpose**: Patients who need blood
- **Primary Key**: `id`
- **Foreign Keys**: 
  - `hospital_id` → Hospital
- **Relationships**: 
  - Many-to-One with `Hospital`
  - One-to-Many with `BloodRequest`

### 8. BloodRequest
- **Purpose**: Blood requests from patients
- **Primary Key**: `id`
- **Foreign Keys**: 
  - `patient_id` → Patient
  - `hospital_id` → Hospital
- **Relationships**: 
  - Many-to-One with `Patient`
  - Many-to-One with `Hospital`

## ERD Diagram

```
┌─────────────────┐    ┌─────────────────┐
│       User      │    │      Admin      │
├─────────────────┤    ├─────────────────┤
│ id (PK)         │    │ id (PK)         │
│ username (UK)   │    │ username (UK)   │
│ email (UK)      │    │ email (UK)      │
│ password        │    │ password        │
│ role            │    │ fullName        │
│ isActive        │    │ phoneNumber     │
│ createdAt       │    │ isActive        │
│ updatedAt       │    │ createdAt       │
└─────────────────┘    │ updatedAt       │
                       └─────────────────┘
                                │
                                │
                       ┌─────────────────┐
                       │     Hospital    │
                       ├─────────────────┤
                       │ id (PK)         │
                       │ name (UK)       │
                       │ licenseNumber   │
                       │ address         │
                       │ phoneNumber     │
                       │ email           │
                       │ isActive        │
                       │ createdAt       │
                       │ updatedAt       │
                       └─────────────────┘
                                │
                                │ 1
                                │
                                ▼
                       ┌─────────────────┐
                       │  BloodInventory │
                       ├─────────────────┤
                       │ id (PK)         │
                       │ hospital_id(FK) │
                       │ bloodGroup      │
                       │ quantity        │
                       │ lastUpdated     │
                       │ createdAt       │
                       │ updatedAt       │
                       └─────────────────┘

┌─────────────────┐
│      Donor      │
├─────────────────┤
│ id (PK)         │
│ name            │
│ email (UK)      │
│ phoneNumber (UK)│
│ bloodGroup      │
│ age             │
│ address         │
│ isActive        │
│ createdAt       │
│ updatedAt       │
└─────────────────┘
         │
         │ 1
         │
         ▼
┌─────────────────┐
│  BloodDonation  │
├─────────────────┤
│ id (PK)         │
│ donor_id (FK)   │
│ hospital_id (FK)│
│ donationDate    │
│ bloodGroup      │
│ quantity        │
│ status          │
│ createdAt       │
│ updatedAt       │
└─────────────────┘
         │
         │ N
         │
         ▼
┌─────────────────┐    ┌─────────────────┐
│     Hospital    │◄───│     Patient     │
└─────────────────┘    ├─────────────────┤
         │              │ id (PK)         │
         │              │ hospital_id(FK) │
         │              │ name            │
         │              │ email           │
         │              │ phoneNumber     │
         │              │ bloodGroup      │
         │              │ age             │
         │              │ address         │
         │              │ isActive        │
         │              │ createdAt       │
         │              │ updatedAt       │
         │              └─────────────────┘
         │                       │
         │                       │ 1
         │                       │
         │                       ▼
         │              ┌─────────────────┐
         │              │  BloodRequest   │
         │              ├─────────────────┤
         │              │ id (PK)         │
         │              │ patient_id (FK) │
         │              │ hospital_id (FK)│
         │              │ bloodGroup      │
         │              │ quantity        │
         │              │ priority        │
         │              │ status          │
         │              │ requestDate     │
         │              │ createdAt       │
         │              │ updatedAt       │
         │              └─────────────────┘
         │                       │
         │                       │ N
         │                       │
         └───────────────────────┘
```

## Relationship Details

### Unidirectional Relationships

1. **Donor → BloodDonation** (1:N)
   - One donor can have multiple blood donations
   - Each donation belongs to exactly one donor

2. **BloodDonation → Hospital** (N:1)
   - Multiple donations can be made to one hospital
   - Each donation is associated with exactly one hospital

3. **Hospital → BloodInventory** (1:N)
   - One hospital can have multiple blood inventory records
   - Each inventory record belongs to exactly one hospital

4. **Hospital → Patient** (1:N)
   - One hospital can have multiple patients
   - Each patient is registered at exactly one hospital

5. **Patient → BloodRequest** (1:N)
   - One patient can make multiple blood requests
   - Each request belongs to exactly one patient

6. **BloodRequest → Hospital** (N:1)
   - Multiple requests can be made to one hospital
   - Each request is associated with exactly one hospital

### Authentication Entities

- **User**: Base authentication entity (no relationships)
- **Admin**: System administrators (no relationships)

## Key Features

1. **No Circular Dependencies**: All relationships are unidirectional
2. **No Independent Entities**: Every entity (except User/Admin) is connected through relationships
3. **Clear Data Flow**: 
   - Donors donate blood to hospitals
   - Hospitals manage inventory and patients
   - Patients request blood from hospitals
4. **Audit Trail**: All entities include `createdAt` and `updatedAt` timestamps
5. **Soft Deletes**: `isActive` flag for logical deletion
6. **Validation**: Unique constraints on critical fields

## Database Schema Benefits

1. **Referential Integrity**: Foreign key constraints ensure data consistency
2. **Scalability**: Unidirectional relationships allow for easy scaling
3. **Maintainability**: Clear separation of concerns
4. **Performance**: Optimized queries without circular joins
5. **Flexibility**: Easy to extend with additional entities

This ERD provides a solid foundation for the Blood Bank Management System with clear, maintainable relationships and no circular dependencies. 