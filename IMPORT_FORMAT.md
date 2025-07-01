# Account Import Format

## Excel File Structure

The Excel file should have the following columns in order:

| Column | Index | Field Name | Required | Description |
|--------|-------|------------|----------|-------------|
| A | 0 | Email | Yes | User's email address (must be unique) |
| B | 1 | First Name | Yes | User's first name |
| C | 2 | Last Name | Yes | User's last name |
| D | 3 | Phone | No | User's phone number |
| E | 4 | Role Name | Yes | User's role (must match one of the available roles) |

## Available Roles

The following roles are supported:
- Student
- Parent
- Nurse
- Manager
- Admin

## Example Excel Format

| Email | First Name | Last Name | Phone | Role Name |
|-------|------------|-----------|-------|-----------|
| john.doe@example.com | John | Doe | 1234567890 | Student |
| jane.smith@example.com | Jane | Smith | 0987654321 | Parent |

## Important Notes

1. **Header Row**: The first row (row 0) should contain headers and will be skipped during import
2. **Role Names**: Role names are case-insensitive but should match exactly one of the available roles
3. **Email Uniqueness**: Each email must be unique in the system
4. **Required Fields**: Email, First Name, Last Name, and Role Name are required fields
5. **Phone**: Phone number is optional

## Troubleshooting

If you encounter issues with role matching:

1. Check that the role name in your Excel file exactly matches one of the available roles
2. Use the `/api/accounts/roles` endpoint to see all available roles in the database
3. Use the `/api/accounts/initialize-roles` endpoint to ensure all roles are created in the database
4. Check the application logs for detailed error messages during import

## API Endpoints

- `POST /api/accounts/import` - Import accounts from Excel file
- `GET /api/accounts/roles` - Get list of available roles
- `POST /api/accounts/initialize-roles` - Initialize roles in database 