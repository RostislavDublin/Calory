# REST operations
## REST Authentication operations
### POST Authenticate as Portal Admin user
```curl browser:secret@localhost:8080/auth/oauth/token -dgrant_type=password -dscope=ui -dusername=Admin -dpassword=password -dclient_id=browser```

Copy "access_token" string from the resulting JSON and save it to the environment variable $ACCESS_TOKEN eg:

```export ACCESS_TOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6Ik...```

## REST User Accounts CRUD operations
### GET all Users
### GET certain User by ID



## REST User Settings CRUD operations
## REST Meal CRUD operations
### GET all Meals
```curl localhost:8080/calories/meals/ -H "Authorization: Bearer $ACCESS_TOKEN"```
