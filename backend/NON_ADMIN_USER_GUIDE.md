# Non-Admin User Guide for BloodSync

This guide explains what different types of non-admin users can do in the BloodSync blood donation management system.

## User Roles Overview

The system supports the following non-admin user roles:

1. **USER** - Basic registered user
2. **DONOR** - Blood donors
3. **RECIPIENT** - Blood recipients
4. **HOSPITAL** - Hospital staff
5. **PATIENT** - Patients

## Public Access (No Authentication Required)

Anyone can access these endpoints without logging in:

### Blood Donor Information
- `GET /api/public/donors` - View all active blood donors (basic info only)
- `GET /api/public/donors/blood-group/{bloodGroup}` - Search donors by blood group
- `GET /api/public/donors/location/{city}` - Search donors by location

### Hospital Information
- `GET /api/public/hospitals` - View all active hospitals
- `GET /api/public/hospitals/location/{city}` - Search hospitals by location
- `GET /api/public/emergency-contacts` - Get emergency contact information

### Statistics
- `GET /api/public/blood-stats` - View blood donation statistics

## USER Role Capabilities

Basic registered users can:

### Profile Management
- `GET /api/users/profile` - View their own profile
- `PUT /api/users/profile` - Update their own profile
- `POST /api/users/profile/change-password` - Change their password

### Dashboard
- `GET /api/users/dashboard` - Access personalized dashboard

### Role Upgrades
- `POST /api/users/become-donor` - Request to become a blood donor
- `POST /api/users/become-recipient` - Request to become a blood recipient

### Authentication
- `POST /api/auth/login` - Login to the system
- `POST /api/auth/register` - Register new account
- `POST /api/auth/refresh` - Refresh authentication token

## DONOR Role Capabilities

Blood donors can:

### Donor Management
- `GET /api/donor` - View all donors
- `GET /api/donor/{id}` - View specific donor details
- `GET /api/donor/blood-group/{bloodGroup}` - Search donors by blood group
- `GET /api/donor/eligible` - View eligible donors

### Personal Donor Operations
- `POST /api/donor` - Create new donor record
- `PUT /api/donor/{id}` - Update donor information
- `DELETE /api/donor/{id}` - Delete donor record

## RECIPIENT/PATIENT Role Capabilities

Blood recipients and patients can:

### Blood Request Management
- `GET /api/request` - View all blood requests
- `GET /api/request/{id}` - View specific blood request
- `GET /api/request/hospital/{hospitalId}` - View requests by hospital
- `GET /api/request/patient/{patientId}` - View requests by patient
- `GET /api/request/status/{status}` - View requests by status
- `GET /api/request/blood-group/{bloodGroup}` - View requests by blood group

### Blood Request Operations
- `POST /api/request` - Create new blood request
- `PUT /api/request/{id}` - Update blood request
- `DELETE /api/request/{id}` - Delete blood request

### Patient Management
- `GET /api/request/patient` - View all patients
- `GET /api/request/patient/{id}` - View specific patient
- `GET /api/request/patient/hospital/{hospitalId}` - View patients by hospital
- `GET /api/request/patient/blood-group/{bloodGroup}` - View patients by blood group

### Patient Operations
- `POST /api/request/patient` - Create new patient record
- `PUT /api/request/patient/{id}` - Update patient information
- `DELETE /api/request/patient/{id}` - Delete patient record

## HOSPITAL Role Capabilities

Hospital staff can:

### Hospital Management
- `GET /api/hospital` - View all hospitals
- `GET /api/hospital/{id}` - View specific hospital
- `GET /api/hospital/email/{email}` - View hospital by email
- `GET /api/hospital/license/{licenseNumber}` - View hospital by license number

### Hospital Operations
- `POST /api/hospital` - Create new hospital record
- `PUT /api/hospital/{id}` - Update hospital information
- `DELETE /api/hospital/{id}` - Delete hospital record

## Security Features

### Role-Based Access Control
- Each endpoint is protected with `@PreAuthorize` annotations
- Users can only access endpoints appropriate for their role
- Some endpoints allow access to own data only

### Data Privacy
- Public endpoints only return basic, non-sensitive information
- Personal contact details are protected from public access
- Users can only view and modify their own profile data

## Getting Started

1. **Register**: Use `POST /api/auth/register` to create a new account
2. **Login**: Use `POST /api/auth/login` to authenticate
3. **Browse Public Info**: Access public endpoints to view donors and hospitals
4. **Upgrade Role**: Request to become a donor or recipient if needed
5. **Manage Data**: Use role-specific endpoints to manage your data

## API Response Format

All endpoints return JSON responses with appropriate HTTP status codes:
- `200 OK` - Successful operation
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid input data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Error Handling

The system includes comprehensive error handling:
- Validation errors for invalid input data
- Authentication errors for unauthorized access
- Authorization errors for insufficient permissions
- Resource not found errors for missing data

## Best Practices

1. **Always authenticate** before accessing protected endpoints
2. **Use appropriate roles** for different types of operations
3. **Respect data privacy** - only access data you're authorized to view
4. **Handle errors gracefully** in your client applications
5. **Keep tokens secure** and refresh them when needed 