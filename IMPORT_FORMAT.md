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
| J | 9 | Date of Birth | No | Date of birth (formats: YYYY-MM-DD, MM/DD/YYYY, MM/DD/YY) |
| K | 10 | Gender | No | Gender (e.g., "Male", "Female", "Other") |

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

| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name | Date of Birth | Gender |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|---------------|--------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | PAIR | 10A | 2005-03-15 | Male |

This will automatically:
1. Create the student account (john.doe@example.com) with class assignment, DOB, and gender
2. Create the parent account (jane.smith@example.com) with default values
3. Link them together

**Note**: When using PAIR mode, the parent account will be created with:
- Email: from Parent Email column
- First Name: "Parent of" + Student's First Name
- Last Name: Student's Last Name
- Phone: empty (can be updated later)
- Role: Parent
- DOB: empty (can be updated later)
- Gender: empty (can be updated later)

### Method 2: Traditional Two-Row Approach
Create accounts separately and link them:

| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name | Date of Birth | Gender |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|---------------|--------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | | 10A | 2005-03-15 | Male |
| jane.smith@example.com | Jane | Smith | 0987654321 | Parent | | john.doe@example.com | | | 1980-07-22 | Female |

### Method 3: Manual Linking After Import
Import accounts without links and use the API:

| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name | Date of Birth | Gender |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|---------------|--------|
| john.doe@example.com | John | Doe | 1234567890 | Student | | | | 10A | 2005-03-15 | Male |
| jane.smith@example.com | Jane | Smith | 0987654321 | Parent | | | | | 1980-07-22 | Female |

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
| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name | Date of Birth | Gender |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|---------------|--------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | PAIR | 10A | 2005-03-15 | Male |
| mary.jones@example.com | Mary | Jones | 5551234567 | Student | robert.jones@example.com | | PAIR | 8B | 2007-11-08 | Male |
| nurse@school.com | Nurse | Staff | 5551234567 | Nurse | | | | | |

### Mixed Approach
| Email | First Name | Last Name | Phone | Role Name | Parent Email | Student Email | Link Type | Class Name | Date of Birth | Gender |
|-------|------------|-----------|-------|-----------|--------------|---------------|-----------|------------|---------------|--------|
| john.doe@example.com | John | Doe | 1234567890 | Student | jane.smith@example.com | | PAIR | 10A | 2005-03-15 | Male |
| existing.parent@example.com | Existing | Parent | 0987654321 | Parent | | new.student@example.com | | | 1980-07-22 | Female |
| new.student@example.com | New | Student | 1112223333 | Student | | | | 9C | 2006-09-14 | Male |

## Important Notes

1. **Header Row**: The first row (row 0) should contain headers and will be skipped during import
2. **Role Names**: Role names are case-insensitive but should match exactly one of the available roles
3. **Email Uniqueness**: Each email must be unique in the system
4. **Required Fields**: Email, First Name, Last Name, and Role Name are required fields
5. **Optional Fields**: Phone, Parent Email, Student Email, Link Type, Class Name, Date of Birth, and Gender are optional (nullable)
6. **PAIR Mode**: Automatically creates parent account with default values
7. **Class Assignment**: Only applies to Student accounts
8. **Multiple Children**: One parent can have multiple children
9. **Manual Linking**: Use `/api/v1/student-parents` API to manually link accounts after import
10. **Two-Phase Import**: The system uses a two-phase process to handle linking order issues:
    - **Phase 1**: Creates all accounts first
    - **Phase 2**: Handles all linking relationships
    - This ensures linking works regardless of row order in the import file
11. **Date Format**: Date of Birth supports multiple formats:
    - YYYY-MM-DD (e.g., "2005-03-15") - Recommended
    - MM/DD/YYYY (e.g., "3/15/2005") - Excel default format
    - MM/DD/YY (e.g., "3/15/05") - Short year format
12. **Gender**: Common values include "Male", "Female", "Other", or can be left blank

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

If you encounter issues with date of birth:

1. Supported formats: YYYY-MM-DD, MM/DD/YYYY, MM/DD/YY
2. Date of birth is optional and can be left blank
3. Excel's default date format (MM/DD/YYYY) is automatically supported
4. Excel date cells are automatically detected and converted
5. Invalid date formats will be ignored and the field will remain null

If you encounter issues with gender:

1. Gender is optional and can be left blank
2. Common values: "Male", "Female", "Other"
3. Case-insensitive: "male", "MALE", "Male" are all valid
4. Any text value is accepted

## API Endpoints

- `POST /api/accounts/import` - Import accounts from Excel file
- `GET /api/accounts/roles` - Get list of available roles
- `POST /api/accounts/initialize-roles` - Initialize roles in database
- `POST /api/v1/student-parents` - Manually link student and parent
- `DELETE /api/v1/student-parents` - Unlink student and parent
- `GET /api/v1/student-parents/children/{parentId}` - Get children for a parent 