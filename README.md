# REST operations
## REST Authentication operations
### POST Authenticate as Portal Admin user
```curl browser:secret@localhost:8080/auth/oauth/token -dgrant_type=password -dscope=ui -dusername=Admin -dpassword=password -dclient_id=browser```

This is for the application clients to Authenticate at the Auth microservice OAuth2 authorization server and get JWT
 access token to be able to operate at other application's microservices.  

Copy "access_token" string from the resulting JSON and save it to the environment variable, eg.  $ACCESS_TOKEN:

```export ACCESS_TOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6Ik...```

## REST User Accounts CRUD operations
### GET all Users
```curl localhost:8080/auth/users/ -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have USER_ALL_CRUD_PRIVILEGE authority.

### GET certain User by ID 
```curl localhost:8080/auth/users/{userId} -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have USER_ALL_CRUD_PRIVILEGE authority, or USER_OWN_CRUD_PRIVILEGE (to request by own userId).

### GET current Authorized User 
```curl localhost:8080/auth/users/current -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to be Authorized. Returns extended Userdetails. Used as OAUth2 user-info-uri endpoint.

### POST create User
```curl localhost:8080/auth/users -d'{"name": "TestUser2", "email": "testuser2@mail.ru", "dob": "2001-01-01", "gender": "Male", "password": "12345678"}'  -H 'Content-Type: application/json'```

Doesn't require Authorization. Returns created User. Can be used by new user to self register. 

### PUT update User by ID 
```curl -X PUT localhost:8080/auth/users/13 -d'{"name": "TestUser2", "email": "testuser2@mail.ru", "dob": "2001-01-01", "gender": "Male", "password": "12345678"}'  -H 'Content-Type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have USER_ALL_CRUD_PRIVILEGE authority, or USER_OWN_CRUD_PRIVILEGE (to request by own userId).

### DELETE User by ID 
```curl -X DELETE localhost:8080/auth/users/13 -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have USER_ALL_CRUD_PRIVILEGE authority, or USER_OWN_CRUD_PRIVILEGE (to request by own userId).

## REST User Settings CRUD operations

## REST Meal CRUD operations

### GET Meal by ID
```curl localhost:8080/calories/meals/4 -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to request Meal with own userId).

### GET all Meals of all Users
```curl localhost:8080/calories/meals/ -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority.

### GET all Meals of User by Id
```curl localhost:8080/calories/meals?userId=10 -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to request by own userId).

### GET filtered Meals of all Users or certain User by Id

All filter parameters are optional. Certain parameter has effect only if it is set to non empty value.
- Filter parameters are:
    - dateFrom - period start date (in yyyy-MM-dd format) 
    - dateTo - period end date (in yyyy-MM-dd format) 
 
    - timeFrom - period start time (in HH:MM format)
    - timeTo - period end time (in HH:MM format)
    
- Examples:
    - all Meals of all Users in January 2020 at time between 10:00 and 16:30
    
    ```curl "localhost:8080/calories/meals?dateFrom=2020-01-01&dateTo=2020-01-31&timeFrom=10:00&timeTo=16:30" -H "Authorization: Bearer $ACCESS_TOKEN"```
  
    - all Meals of User with ID 10 before 10 January 2020 at time not earlier 13:00
    
    ```curl "localhost:8080/calories/meals?userId=10&dateTo=2020-01-10&timeFrom=13:00" -H "Authorization: Bearer $ACCESS_TOKEN"```
  
### POST create Meal

```curl localhost:8080/calories/meals -d'{"userId": 10, "mealDate": "2020-01-15", "mealTime": "11:30", "meal": "cake", "calories": 125}' -H 'Content-Type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to create Meal with own userId).

### PUT update Meal by ID 
```curl -X PUT localhost:8080/calories/meals/3 -d'{"userId": 8, "mealDate": "2020-01-03", "mealTime": "08:30", "meal": "cheesecake", "calories": 321}'  -H 'Content-Type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to update Meal with own userId and without option to change userId).

### DELETE Meal by ID 
```curl -X DELETE localhost:8080/calories/meals/3 -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to delete Meal with own userId).

## REST UserSetting CRUD operations

### GET all UserSetting of all Users
```curl localhost:8080/calories/userSettings/ -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority.

### GET UserSetting by User ID
```curl localhost:8080/calories/userSettings/10 -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to request UserSetting with own userId).
  
### POST create UserSetting

```curl localhost:8080/calories/userSettings -d'{"userId": 10, "caloriesExpected": 3000}' -H 'Content-Type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to create Meal with own userId).

### PUT update UserSetting by User ID 
```curl -X PUT localhost:8080/calories/userSettings/ -d'{"userId": 10, "caloriesExpected": 5000}'  -H 'Content-Type: application/json' -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to update Meal with own userId and without option to change userId).

### DELETE UserSetting by UserId 
```curl -X DELETE localhost:8080/calories/userSettings/10 -H "Authorization: Bearer $ACCESS_TOKEN"```

Requires user to have MEAL_ALL_CRUD_PRIVILEGE authority or MEAL_OWN_CRUD_PRIVILEGE (to delete Meal with own userId).
