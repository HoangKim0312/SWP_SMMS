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
| F | 5 | Parent Email | No | Email of parent to link (for Student role only) |
| G | 6 | Student Email | No | Email of student to link (for Parent role only) |
| H | 7 | Link Type | No | Special linking mode: "PAIR" to create both accounts and link |
| I | 8 | Class Name | No | Class name for student (e.g., "10A", "Grade 11B") |

## Available Roles

The following roles are supported:
- Student
- Parent
- Nurse
- Manager
- Admin

## Parent-Student Linking

### Method 1: Single Row Pair Creation (Recommended)
Use column H (Link Type) with value "PAIR" to create both accounts and link them in one row:

| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | PAIR | 10A |

This will automatically:
1. Create the student account (john.doe@example.com) with class assignment
2. Create the parent account (jane.smith@example.com) with default values
3. Link them together

**Note**: When using PAIR mode, the parent account will be created with:
- Email: from Parent Email column
- First Name: "Parent of" + Student's First Name
- Last Name: Student's Last Name
- Phone: empty (can be updated later)
- Role: Parent

### Method 2: Traditional Two-Row Approach
Create accounts separately and link them:

| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | | 10A |
| jane.smith@example.com | Jane | Smith | 0987654321 | Parent | | john.doe@example.com | | |

### Method 3: Manual Linking After Import
Import accounts without links and use the API:

| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|
| john.doe@example.com | John | Doe | 1234567890 | Student | | | | 10A |
| jane.smith@example.com | Jane | Smith | 0987654321 | Parent | | | | |

Then use: `POST /api/v1/student-parents` to link them manually.

## One-to-Many Parent-Child Relationship

**Important**: One parent can have multiple children. The system supports this naturally:

### Example: One Parent, Multiple Children
```
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | PAIR | 10A |
| mary.doe@example.com | Mary | Doe | 1234567891 | Student | jane.smith@example.com | | PAIR | 8B |
```

This will:
1. Create John (student) and link to Jane (parent)
2. Create Mary (student) and link to the same Jane (parent)
3. Jane will now have two children: John and Mary

### Adding More Children to Existing Parent
```
| existing.parent@example.com | Existing | Parent | 0987654321 | Parent | | | | |
| new.child@example.com | New | Child | 1112223333 | Student | existing.parent@example.com | | | 9C |
```

This will:
1. Link new child to existing parent
2. Parent will now have the new child in addition to any existing children

## Linking Rules

- **PAIR Mode**: Creates both accounts and links them automatically
- **Traditional Mode**: Links existing accounts (both must exist)
- **Parent Email (column F)**: Only processed for "Student" role or "PAIR" mode
- **Student Email (column G)**: Only processed for "Parent" role
- **Link Type (column H)**: Use "PAIR" for automatic pair creation
- **Class Name (column I)**: Only processed for "Student" role
- All linking fields are optional (nullable) - accounts can be imported without links
- One parent can have multiple children
- One child can have multiple parents (if needed)

## Example Excel Format

### Single Row Pair Creation (Recommended)
| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | PAIR | 10A |
| mary.jones@example.com | Mary | Jones | 5551234567 | Student | robert.jones@example.com | | PAIR | 8B |
| nurse@school.com | Nurse | Staff | 5551234567 | Nurse | | | | |

### Mixed Approach
| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | PAIR | 10A |
| existing.parent@example.com | Existing | Parent | 0987654321 | Parent | | new.student@example.com | | |
| new.student@example.com | New | Student | 1112223333 | Student | | | | 9C |

## Important Notes

1. **Header Row**: The first row (row 0) should contain headers and will be skipped during import
2. **Role Names**: Role names are case-insensitive but should match exactly one of the available roles
3. **Email Uniqueness**: Each email must be unique in the system
4. **Required Fields**: Email, First Name, Last Name, and Role Name are required fields
5. **Optional Fields**: Phone, Parent Email, Student Email, Link Type, and Class Name are optional (nullable)
6. **PAIR Mode**: Automatically creates parent account with default values
7. **Class Assignment**: Only applies to Student accounts
8. **Multiple Children**: One parent can have multiple children
9. **Manual Linking**: Use `/api/v1/student-parents` API to manually link accounts after import
10. **Two-Phase Import**: The system uses a two-phase process to handle linking order issues:
    - **Phase 1**: Creates all accounts first
    - **Phase 2**: Handles all linking relationships
    - This ensures linking works regardless of row order in the import file

## Troubleshooting

If you encounter issues with role matching:

1. Check that the role name in your Excel file exactly matches one of the available roles
2. Use the `/api/accounts/roles` endpoint to see all available roles in the database
3. Use the `/api/accounts/initialize-roles` endpoint to ensure all roles are created in the database
4. Check the application logs for detailed error messages during import

If you encounter issues with parent-student linking:

1. Ensure the referenced email exists in the system (except in PAIR mode)
2. Check that the referenced account has the correct role (Parent for Parent Email, Student for Student Email)
3. In PAIR mode, the parent account will be created automatically
4. Use the `/api/v1/student-parents` API to manually link accounts after import

If you encounter issues with class assignment:

1. The class name will be created automatically if it doesn't exist
2. Class assignment only applies to Student accounts
3. If class name is empty or null, no class will be assigned

## API Endpoints

- `POST /api/accounts/import` - Import accounts from Excel file
- `GET /api/accounts/roles` - Get list of available roles
- `POST /api/accounts/initialize-roles` - Initialize roles in database
- `POST /api/v1/student-parents` - Manually link student and parent
- `DELETE /api/v1/student-parents` - Unlink student and parent
- `GET /api/v1/student-parents/children/{parentId}` - Get children for a parent 