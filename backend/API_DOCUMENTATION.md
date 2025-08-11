# BloodSync API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication
The API uses JWT (JSON Web Token) authentication. Most endpoints require authentication except for public endpoints.

### Default Admin Credentials
- **Username**: `admin`
- **Password**: `admin`

## Public Endpoints (No Authentication Required)

### 1. Get All Public Donors
```http
GET /api/public/donors
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "bloodGroup": "A+",
    "city": "New York",
    "state": "NY",
    "eligible": true
  }
]
```

### 2. Get Donors by Blood Group
```http
GET /api/public/donors/blood-group/{bloodGroup}
```

**Example**: `GET /api/public/donors/blood-group/A+`

### 3. Get Donors by Location
```http
GET /api/public/donors/location/{city}
```

**Example**: `GET /api/public/donors/location/New York`

### 4. Get All Public Hospitals
```http
GET /api/public/hospitals
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "hospitalName": "City General Hospital",
    "email": "contact@cityhospital.com",
    "phoneNumber": "+1-555-0123",
    "address": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "licenseNumber": "HOSP001",
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

### 5. Get Hospitals by Location
```http
GET /api/public/hospitals/location/{city}
```

**Example**: `GET /api/public/hospitals/location/New York`

### 6. Get Blood Donation Statistics
```http
GET /api/public/blood-stats
```

### 7. Get Emergency Contacts
```http
GET /api/public/emergency-contacts
```

## Authentication Endpoints

### 1. User Login
```http
POST /api/auth/login
```

**Request Body**:
```json
{
  "username": "admin",
  "password": "admin"
}
```

**Response**: `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "refresh_token_here",
  "type": "Bearer",
  "expiresIn": 3600000
}
```

### 2. User Registration
```http
POST /api/auth/register
```

**Request Body**:
```json
{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1-555-0123",
  "address": "123 Main Street",
  "city": "New York",
  "state": "NY"
}
```

### 3. Refresh Token
```http
POST /api/auth/refresh
```

**Request Body**:
```json
{
  "refreshToken": "your_refresh_token_here"
}
```

## Protected Endpoints (Authentication Required)

### User Management

#### Get All Users (Admin Only)
```http
GET /api/users
Authorization: Bearer {token}
```

#### Get User by ID
```http
GET /api/users/{id}
Authorization: Bearer {token}
```

#### Create User (Admin Only)
```http
POST /api/users
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER"
}
```

#### Update User
```http
PUT /api/users/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com"
}
```

#### Delete User (Admin Only)
```http
DELETE /api/users/{id}
Authorization: Bearer {token}
```

### Donor Management

#### Get All Donors
```http
GET /api/donors
Authorization: Bearer {token}
```

#### Get Donor by ID
```http
GET /api/donors/{id}
Authorization: Bearer {token}
```

#### Create Donor
```http
POST /api/donors
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phoneNumber": "+1-555-0124",
  "dateOfBirth": "1990-01-01",
  "bloodGroup": "O+",
  "address": "456 Oak Street",
  "city": "Los Angeles",
  "state": "CA"
}
```

#### Update Donor
```http
PUT /api/donors/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Johnson",
  "bloodGroup": "O+"
}
```

#### Delete Donor
```http
DELETE /api/donors/{id}
Authorization: Bearer {token}
```

### Hospital Management

#### Get All Hospitals
```http
GET /api/hospitals
Authorization: Bearer {token}
```

#### Get Hospital by ID
```http
GET /api/hospitals/{id}
Authorization: Bearer {token}
```

#### Create Hospital
```http
POST /api/hospitals
Authorization: Bearer {token}
Content-Type: application/json

{
  "hospitalName": "City Medical Center",
  "email": "contact@citymedical.com",
  "phoneNumber": "+1-555-0125",
  "address": "789 Pine Street",
  "city": "Chicago",
  "state": "IL",
  "licenseNumber": "HOSP002"
}
```

#### Update Hospital
```http
PUT /api/hospitals/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "hospitalName": "Updated Medical Center",
  "phoneNumber": "+1-555-0126"
}
```

#### Delete Hospital
```http
DELETE /api/hospitals/{id}
Authorization: Bearer {token}
```

### Blood Request Management

#### Get All Blood Requests
```http
GET /api/blood-requests
Authorization: Bearer {token}
```

#### Get Blood Request by ID
```http
GET /api/blood-requests/{id}
Authorization: Bearer {token}
```

#### Create Blood Request
```http
POST /api/blood-requests
Authorization: Bearer {token}
Content-Type: application/json

{
  "patientId": 1,
  "hospitalId": 1,
  "bloodGroup": "A+",
  "units": 2,
  "priority": "HIGH",
  "reason": "Emergency surgery",
  "requiredDate": "2024-01-15"
}
```

#### Update Blood Request
```http
PUT /api/blood-requests/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "FULFILLED",
  "units": 1
}
```

#### Delete Blood Request
```http
DELETE /api/blood-requests/{id}
Authorization: Bearer {token}
```

### Blood Donation Management

#### Get All Blood Donations
```http
GET /api/blood-donations
Authorization: Bearer {token}
```

#### Get Blood Donation by ID
```http
GET /api/blood-donations/{id}
Authorization: Bearer {token}
```

#### Get Blood Donations by Donor
```http
GET /api/blood-donations/donor/{donorId}
Authorization: Bearer {token}
```

#### Get Blood Donations by Hospital
```http
GET /api/blood-donations/hospital/{hospitalId}
Authorization: Bearer {token}
```

#### Get Blood Donations by Blood Group
```http
GET /api/blood-donations/blood-group/{bloodGroup}
Authorization: Bearer {token}
```

#### Get Blood Donations by Status
```http
GET /api/blood-donations/status/{status}
Authorization: Bearer {token}
```

**Available Status Values**: `SCHEDULED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`, `REJECTED`

#### Get Blood Donations by Date Range
```http
GET /api/blood-donations/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
Authorization: Bearer {token}
```

#### Create Blood Donation
```http
POST /api/blood-donations
Authorization: Bearer {token}
Content-Type: application/json

{
  "donorId": 1,
  "hospitalId": 1,
  "donationDate": "2024-01-15T10:30:00",
  "bloodGroup": "A+",
  "quantity": 450,
  "status": "COMPLETED",
  "notes": "Regular donation"
}
```

#### Update Blood Donation
```http
PUT /api/blood-donations/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "donationDate": "2024-01-15T10:30:00",
  "bloodGroup": "A+",
  "quantity": 450,
  "status": "COMPLETED",
  "notes": "Updated notes"
}
```

#### Delete Blood Donation
```http
DELETE /api/blood-donations/{id}
Authorization: Bearer {token}
```

### Blood Inventory Management

#### Get All Blood Inventory
```http
GET /api/blood-inventory
Authorization: Bearer {token}
```

#### Get Blood Inventory by ID
```http
GET /api/blood-inventory/{id}
Authorization: Bearer {token}
```

#### Get Blood Inventory by Hospital
```http
GET /api/blood-inventory/hospital/{hospitalId}
Authorization: Bearer {token}
```

#### Get Blood Inventory by Blood Group
```http
GET /api/blood-inventory/blood-group/{bloodGroup}
Authorization: Bearer {token}
```

#### Get Blood Inventory by Status
```http
GET /api/blood-inventory/status/{status}
Authorization: Bearer {token}
```

**Available Status Values**: `AVAILABLE`, `LOW_STOCK`, `OUT_OF_STOCK`, `EXPIRED`, `QUARANTINED`

#### Get Blood Inventory by Hospital and Blood Group
```http
GET /api/blood-inventory/hospital/{hospitalId}/blood-group/{bloodGroup}
Authorization: Bearer {token}
```

#### Get Expired Blood Inventory
```http
GET /api/blood-inventory/expired
Authorization: Bearer {token}
```

#### Get Low Stock Blood Inventory
```http
GET /api/blood-inventory/low-stock?threshold=1000
Authorization: Bearer {token}
```

#### Create Blood Inventory
```http
POST /api/blood-inventory
Authorization: Bearer {token}
Content-Type: application/json

{
  "hospitalId": 1,
  "bloodGroup": "A+",
  "availableQuantity": 2000,
  "totalQuantity": 2500,
  "expiryDate": "2024-02-15T00:00:00",
  "notes": "Fresh stock"
}
```

#### Update Blood Inventory
```http
PUT /api/blood-inventory/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "availableQuantity": 1800,
  "totalQuantity": 2500,
  "expiryDate": "2024-02-15T00:00:00",
  "notes": "Updated stock"
}
```

#### Delete Blood Inventory
```http
DELETE /api/blood-inventory/{id}
Authorization: Bearer {token}
```

## Postman Testing Guide

### 1. Setup Postman Collection

1. Create a new collection called "BloodSync API"
2. Set up environment variables:
   - `base_url`: `http://localhost:8080`
   - `token`: (will be set after login)

### 2. Test Public Endpoints First

1. **Test Public Donors**:
   - Method: `GET`
   - URL: `{{base_url}}/api/public/donors`
   - Should return `200 OK`

2. **Test Public Hospitals**:
   - Method: `GET`
   - URL: `{{base_url}}/api/public/hospitals`
   - Should return `200 OK`

### 3. Test Authentication

1. **Login Request**:
   - Method: `POST`
   - URL: `{{base_url}}/api/auth/login`
   - Headers: `Content-Type: application/json`
   - Body (raw JSON):
   ```json
   {
     "username": "admin",
     "password": "admin"
   }
   ```

2. **Extract Token**:
   - In the Tests tab, add this script to automatically set the token:
   ```javascript
   if (pm.response.code === 200) {
       const response = pm.response.json();
       pm.environment.set("token", response.token);
   }
   ```

### 4. Test Protected Endpoints

1. **Get All Users**:
   - Method: `GET`
   - URL: `{{base_url}}/api/users`
   - Headers: `Authorization: Bearer {{token}}`

2. **Create a Donor**:
   - Method: `POST`
   - URL: `{{base_url}}/api/donors`
   - Headers: 
     - `Authorization: Bearer {{token}}`
     - `Content-Type: application/json`
   - Body (raw JSON):
   ```json
   {
     "firstName": "Test",
     "lastName": "Donor",
     "email": "test.donor@example.com",
     "phoneNumber": "+1-555-0127",
     "dateOfBirth": "1995-01-01",
     "bloodGroup": "B+",
     "address": "123 Test Street",
     "city": "Test City",
     "state": "TS"
   }
   ```

## Common HTTP Status Codes

- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required or invalid credentials
- `403 Forbidden`: Access denied (insufficient permissions)
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Error Response Format

```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users"
}
```

## Testing Checklist

- [ ] Public endpoints work without authentication
- [ ] Login endpoint returns valid JWT token
- [ ] Protected endpoints require valid token
- [ ] CRUD operations work for all entities
- [ ] Error handling works correctly
- [ ] Validation works for required fields
- [ ] Role-based access control works

## Troubleshooting

### Common Issues:

1. **401 Unauthorized**: Check if token is valid and not expired
2. **403 Forbidden**: Check if user has required role/permissions
3. **400 Bad Request**: Check request body format and required fields
4. **500 Internal Server Error**: Check server logs for detailed error

### If Application Won't Start:

1. Check if MySQL is running
2. Verify database credentials in `application.properties`
3. Check if port 8080 is available
4. Ensure Java 17+ is installed and JAVA_HOME is set

### Database Connection Issues:

1. Verify MySQL service is running
2. Check database credentials
3. Ensure database `bloodsync` exists
4. Check MySQL user permissions 