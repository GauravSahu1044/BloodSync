# Role-Based Security Implementation Guide

## Overview
This document describes the implementation of role-based access control (RBAC) for the Blood Bank Management System API endpoints.

## User Roles
The system supports the following user roles:
- **ADMIN**: System administrators with full access
- **HOSPITAL**: Hospital staff and administrators
- **DONOR**: Blood donors
- **RECIPIENT**: Blood recipients
- **PATIENT**: Patients requiring blood
- **USER**: General users (legacy role)

## API Endpoint Security

### Public Endpoints (No Authentication Required)
These endpoints are accessible without authentication:
```
POST /api/auth/login          - User login
POST /api/auth/register       - User registration
POST /api/auth/refresh        - Token refresh
GET  /api/health/**          - Health check endpoints
```

### Role-Based Protected Endpoints

#### ADMIN Role - `/api/admin/**`
Only users with ADMIN role can access these endpoints:
```
GET    /api/admin             - Get all admins
GET    /api/admin/{id}        - Get admin by ID
POST   /api/admin             - Create new admin
PUT    /api/admin/{id}        - Update admin
DELETE /api/admin/{id}        - Delete admin
```

#### HOSPITAL Role - `/api/hospital/**`
Only users with HOSPITAL role can access these endpoints:
```
GET    /api/hospital                    - Get all hospitals
GET    /api/hospital/{id}               - Get hospital by ID
GET    /api/hospital/email/{email}      - Get hospital by email
GET    /api/hospital/license/{license}  - Get hospital by license number
POST   /api/hospital                    - Create new hospital
PUT    /api/hospital/{id}               - Update hospital
DELETE /api/hospital/{id}               - Delete hospital
```

#### DONOR Role - `/api/donor/**`
Only users with DONOR role can access these endpoints:
```
GET    /api/donor                    - Get all donors
GET    /api/donor/{id}               - Get donor by ID
GET    /api/donor/blood-group/{bg}   - Get donors by blood group
GET    /api/donor/eligible           - Get eligible donors
POST   /api/donor                    - Create new donor
PUT    /api/donor/{id}               - Update donor
DELETE /api/donor/{id}               - Delete donor
```

#### RECIPIENT/PATIENT Role - `/api/request/**`
Users with either RECIPIENT or PATIENT role can access these endpoints:

**Blood Requests:**
```
GET    /api/request                    - Get all blood requests
GET    /api/request/{id}               - Get blood request by ID
GET    /api/request/hospital/{id}      - Get requests by hospital
GET    /api/request/patient/{id}       - Get requests by patient
GET    /api/request/status/{status}    - Get requests by status
GET    /api/request/blood-group/{bg}   - Get requests by blood group
POST   /api/request                    - Create new blood request
PUT    /api/request/{id}               - Update blood request
DELETE /api/request/{id}               - Delete blood request
```

**Patient Management:**
```
GET    /api/request/patient                    - Get all patients
GET    /api/request/patient/{id}               - Get patient by ID
GET    /api/request/patient/hospital/{id}      - Get patients by hospital
GET    /api/request/patient/blood-group/{bg}   - Get patients by blood group
POST   /api/request/patient                    - Create new patient
PUT    /api/request/patient/{id}               - Update patient
DELETE /api/request/patient/{id}               - Delete patient
```

### General User Management - `/api/users/**`
These endpoints require authentication and have specific access rules:
```
GET    /api/users                    - Get all users (ADMIN only)
GET    /api/users/{id}               - Get user by ID (ADMIN or own user)
GET    /api/users/username/{name}    - Get user by username (ADMIN or own user)
POST   /api/users                    - Create user (ADMIN only)
PUT    /api/users/{id}               - Update user (ADMIN or own user)
DELETE /api/users/{id}               - Delete user (ADMIN only)
POST   /api/users/{id}/change-password - Change password (ADMIN or own user)
POST   /api/users/{username}/unlock  - Unlock account (ADMIN only)
GET    /api/users/role/{role}        - Get users by role (ADMIN only)
```

## Security Configuration

### Spring Security Configuration
The security is configured in `SecurityConfig.java` with the following key features:

1. **JWT Authentication**: Stateless authentication using JWT tokens
2. **CORS Support**: Cross-origin resource sharing enabled
3. **CSRF Disabled**: CSRF protection disabled for API endpoints
4. **Role-Based Authorization**: URL pattern-based role restrictions

### Method-Level Security
Controllers use `@PreAuthorize` annotations for fine-grained access control:
- `@PreAuthorize("hasRole('ADMIN')")` - ADMIN role only
- `@PreAuthorize("hasRole('HOSPITAL')")` - HOSPITAL role only
- `@PreAuthorize("hasRole('DONOR')")` - DONOR role only
- `@PreAuthorize("hasAnyRole('RECIPIENT', 'PATIENT')")` - Either RECIPIENT or PATIENT role

### Authentication Flow
1. User registers/logs in via `/api/auth/register` or `/api/auth/login`
2. System returns JWT access token and refresh token
3. Client includes JWT token in Authorization header: `Bearer <token>`
4. Spring Security validates token and extracts user roles
5. Access is granted/denied based on user roles and endpoint requirements

## Error Responses

### Authentication Errors
- **401 Unauthorized**: Invalid or missing JWT token
- **403 Forbidden**: Valid token but insufficient privileges
- **423 Locked**: Account locked due to failed login attempts

### Authorization Errors
When a user tries to access an endpoint they don't have permission for:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 403,
  "error": "Access Denied",
  "message": "You don't have permission to access this resource",
  "details": null
}
```

## Best Practices

### 1. Token Management
- Store JWT tokens securely (not in localStorage for production)
- Implement token refresh before expiration
- Logout by clearing tokens on client side

### 2. Role Assignment
- Assign minimal required roles to users
- Use principle of least privilege
- Regularly audit user roles and permissions

### 3. API Usage
- Always include Authorization header with valid JWT token
- Handle 401/403 responses appropriately
- Implement proper error handling for authentication failures

### 4. Security Headers
The application includes security headers:
- CORS configuration for cross-origin requests
- Content-Type validation
- Proper HTTP status codes for different scenarios

## Testing Role-Based Access

### Test Scenarios
1. **Unauthenticated Access**: Try accessing protected endpoints without token
2. **Wrong Role Access**: Use token with insufficient privileges
3. **Valid Access**: Use token with correct role for endpoint
4. **Token Expiration**: Test with expired tokens
5. **Account Locking**: Test failed login attempts

### Example Test Commands
```bash
# Test public endpoint (should work)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password"}'

# Test protected endpoint without token (should fail)
curl -X GET http://localhost:8080/api/admin

# Test protected endpoint with token (should work if ADMIN role)
curl -X GET http://localhost:8080/api/admin \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Implementation Notes

### URL Pattern Changes
The following URL patterns were updated to match security requirements:
- `/api/hospitals/**` → `/api/hospital/**`
- `/api/donors/**` → `/api/donor/**`
- `/api/blood-requests/**` → `/api/request/**`
- `/api/patients/**` → `/api/request/patient/**`
- `/api/admins/**` → `/api/admin/**`

### Role Enum Updates
Added new roles to `UserRole` enum:
- `HOSPITAL`
- `PATIENT`

### Controller Updates
All controllers were updated with:
- Correct URL mappings
- `@PreAuthorize` annotations
- Proper logging
- CORS configuration

This implementation provides a secure, role-based access control system that ensures users can only access endpoints appropriate to their role in the blood bank management system. 