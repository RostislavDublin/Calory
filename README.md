# Application for the input of calories

## Purpose
Provide a solution to allow people to track their personal meal calories consumption, 
get daily consumption details and statistics and be noticed about the consumption expectations exceed.

- For detailed requirements, see REQUIREMENTS.md document.  

## Functionality
### Central Web GUI 
- single-page design
- navigation bar links:
    - Users: 
        - user list (with filters) 
            - User profile CRUD dialog;
    - Meals: 
        - meals list (with filters and conditional data formatting)
            - Meal item CRUD dialog;
    - Login:
        - login form (username/password)
    - Register:
        - new user self registration page
    - LoggedIn Username:
        - logged-in user name informer
        - context menu:
            - Profile:
                - current user profile self edit dialog;
            - Logout
                - logout option
### Security, Authentication and Authorization         
#### User security privileges predefined
- USER_OWN_CRUD_PRIVILEGE - allows user to CRUD own user profile data
- MEAL_OWN_CRUD_PRIVILEGE - allows user to CRUD own meal records
- USER_ALL_CRUD_PRIVILEGE - allows user to CRUD all users' profiles data
- MEAL_ALL_CRUD_PRIVILEGE - allows user to CRUD all users' meal records

#### User roles (and their privileges) predefined
- ROLE_ADMIN - Admin with full access, has:
    - USER_ALL_CRUD_PRIVILEGE, 
    - MEAL_ALL_CRUD_PRIVILEGE 
- ROLE_USER_MANAGER - Manager with full access to all users' profiles
    - USER_ALL_CRUD_PRIVILEGE
- ROLE_REGULAR_USER - Regular User with access to only own user profile and meal data 
    - USER_OWN_CRUD_PRIVILEGE
    - MEAL_OWN_CRUD_PRIVILEGE
    
#### User accounts predefined
- Admin. Password: password. 
- Manager. Password: password. 
- TestUser. Password: password.      
