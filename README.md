# SWP SMMS - JWT Authentication API

This project implements a comprehensive JWT-based authentication system with access tokens and refresh tokens, following a simplified and clean architecture.

## Features

- JWT-based authentication with access tokens and refresh tokens
- Email-based login (instead of username)
- Secure password hashing with BCrypt
- Role-based authorization
- Token refresh mechanism
- Logout functionality with token revocation
- Password reset functionality
- Account locking support
- Scheduled token cleanup

## Architecture

### Simplified Structure
- **AuthenticationService** - Main authentication logic
- **Security Components** - All JWT and security handling in `security` package
- **Clean Separation** - No redundant services, single responsibility principle

### Security Components
- `JwtTokenProvider` - JWT token generation and validation
- `JwtAuthenticationFilter` - Request filtering and token validation
- `CustomUserDetailService` - User details loading from database
- `CustomLogoutHandler` - Logout token revocation
- `JwtAuthenticationEntryPoint` - Unauthorized access handling

## API Endpoints

### Authentication Endpoints

#### 1. Login
- **URL**: `POST /api/v1/auth/login`
- **Description**: Authenticate user with email and receive access token and refresh token
- **Request Body**:
```json
{
    "email": "user@example.com",
    "password": "password123"
}
```
- **Response**:
```json
{
    "http_status": 200,
    "time_stamp": "10/29/2024 11:20:03",
    "message": "Successfully Sign in",
    "data": {
        "access_token": "xxx.yyy.zzz",
        "refresh_token": "refresh_token_uuid"
    }
}
```

#### 2. Refresh Token
- **URL**: `POST /api/v1/auth/refresh-token`
- **Description**: Get a new access token using refresh token
- **Headers**: `Authorization: Bearer <refresh_token>`
- **Response**:
```json
{
    "http_status": 200,
    "time_stamp": "10/29/2024 11:20:03",
    "message": "Generate new Refresh Token and Access Token successfully",
    "data": {
        "access_token": "new_access_token",
        "refresh_token": "new_refresh_token"
    }
}
```

#### 3. Logout
- **URL**: `POST /api/v1/auth/logout`
- **Description**: Logout and revoke tokens
- **Headers**: `Authorization: Bearer <access_token>`
- **Response**: `"Logged out successfully"`

#### 4. Forgot Password
- **URL**: `POST /api/v1/auth/forgot-password`
- **Description**: Request password reset email
- **Parameters**: `email=user@example.com`
- **Response**: `"Please check your email for password reset link"`

#### 5. Reset Password
- **URL**: `POST /api/v1/auth/reset-password`
- **Description**: Reset password using token
- **Parameters**: `password=newPassword&token=reset_token`
- **Response**: `"Password reset successfully"`

## Usage

### 1. Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@example.com", "password": "password123"}'
```

### 2. Use Access Token
```bash
curl -X GET http://localhost:8080/api/protected-endpoint \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 3. Refresh Token
```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh-token \
  -H "Authorization: Bearer YOUR_REFRESH_TOKEN"
```

### 4. Logout
```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 5. Forgot Password
```bash
curl -X POST "http://localhost:8080/api/v1/auth/forgot-password?email=user@example.com"
```

### 6. Reset Password
```bash
curl -X POST "http://localhost:8080/api/v1/auth/reset-password?password=newPassword&token=reset_token"
```

## Configuration

The JWT configuration is set in `application.properties`:

```properties
# JWT Configuration
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000        # 24 hours in milliseconds
jwt.refresh-expiration=604800000 # 7 days in milliseconds

# Email Configuration (for password reset)
application.email.url=http://localhost:3000/reset-password
application.email.secure.characters=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789
```

## Security

- Access tokens expire after 24 hours
- Refresh tokens expire after 7 days
- Passwords are hashed using BCrypt
- All tokens are stored securely in the database
- JWT tokens are signed with HMAC-SHA256
- Account locking support for security
- Automatic token cleanup via scheduled tasks

## Database Schema

### Account Entity (UUID)
- Primary key: `account_id` (UUID)
- Email-based authentication
- Contains user credentials and profile information
- Support for account locking

### AccessToken Entity (Integer ID)
- Primary key: `id` (Integer)
- Foreign key: `account_id` (UUID) references Account
- Foreign key: `refresh_token_id` (Long) references RefreshToken
- Stores access tokens with expiration and revocation status

### RefreshToken Entity (Long ID)
- Primary key: `id` (Long)
- Stores refresh tokens with expiration and revocation status
- One-to-many relationship with AccessToken

### PasswordResetToken Entity (Integer ID)
- Primary key: `passwordResetTokenId` (Integer)
- Foreign key: `account_id` (UUID) references Account
- Stores password reset tokens with expiration

## Dependencies

- Spring Boot 3.5.0
- Spring Security 6
- JWT (jjwt) 0.12.3
- JPA/Hibernate
- SQL Server
- Lombok
- Swagger/OpenAPI

## Key Improvements

✅ **Simplified Architecture** - Removed redundant services  
✅ **Email Authentication** - Login using email instead of username  
✅ **Security Package** - All security components in dedicated package  
✅ **Clean Separation** - Single responsibility principle  
✅ **Enhanced Security** - Custom logout handler and authentication entry point  
✅ **Scheduled Cleanup** - Automatic token cleanup  
✅ **Password Reset** - Complete password reset functionality  
✅ **Account Locking** - Support for locked accounts  