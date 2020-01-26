# Application for the input of calories

## Purpose
Provide a solution to allow people to track their personal meal calories consumption, 
get daily consumption details and statistics and be noticed about the consumption expectations exceed.

- For detailed requirements, see REQUIREMENTS.md document.  

## Functionality
- The detailed application requirements are stored in the REQUIREMENTS.md file
- The architecture description is in the README.ARCH.md file.

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
                
## REST resources
All the application's operations are also available through the REST. 
See the comprehensive manual in the README.REST.md file. 
                
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

# Build, Deploy and Configure, Run and Connect
## Build
- Pull the project from the Git repo
- Open the project in your IDE
- Run the following Gradle command to build the solution

```./gradlew clean build```
- Run the following Gradle command to copy modules' JARs from their own build dirs "build/libs/"
to the central "collector" dir "build/jars/" of the root module:

```./gradlew collectJars```
- be ready to use those resulting JARs for the solution microservices run:
    - Portal-Auth-0.1.0.jar - authentication service
    - Portal-Calories-0.1.0.jar - meals&calory service
    - Portal-Config-0.1.0.jar - config server service  
    - Portal-Gateway-0.1.0.jar - central gateway service
    - Portal-Registry-0.1.0.jar - registry/discovery service
    - Portal-UI-0.1.0.jar - web frontend service
    
## Deploy and Configure
All 6 services take their configs from the config service. 
On start, they know only the config endpoint URL of the config server 
written in their ```bootstrap.yml``` files: 

```
spring:
  cloud:
    config:
      uri: http://localhost:8888
```
The config server contains its own config in the ```application.yml``` file where it has settings 
for its own server port and the location of all its client services config files:
```
server:
  port: 8888 #the port where the config-server will be acessible
spring:
  cloud:
    config:
      server:
        native:
          search-locations: classpath:/shared #tells spring where to locate the config files
```  
And certainly, it contains a ```/shared``` folder where all the services' configs contain:
- auth-service.yml
- calories-service.yml
- frontend-service.yml
- gateway-service.yml
- registry-service.yml 

### Default configuration
To use embedded config defaults you simply run services' JARs as-is 
without any additional configs provided. It leads to all services use "localhost" URL
with the following ports of each service:
- config-service - 8888
- auth-service - 8082
- calories-service - 8084
- frontend-service - 8081
- gateway-service - 8080
- registry-service  - 8761

So, the application will be accessible on [http://localhost:8080](http://localhost:8080)

### Customized configuration
If you install services on any custom environment/location you need to set customized configs.
Each service is a Spring Boot application, so you can override any property by several means. 
Knowing the properties files which each service use (see this section above) we can override what we wish. 
Use any approach, see: [Spring Properties File Outside jar](https://www.baeldung.com/spring-properties-file-outside-jar)

### Deploy and Run
We have 6 services JARs in the ```build/jars/``` "collector" directory.
Grab them, put into the target directory/directories and run like this (all together or separately):
```shell script
java -jar Portal-Auth-0.1.0.jar /
java -jar Portal-Calories-0.1.0.jar /
java -jar Portal-Config-0.1.0.jar /  
java -jar Portal-Gateway-0.1.0.jar /
java -jar Portal-Registry-0.1.0.jar /
java -jar Portal-UI-0.1.0.jar
```
### Connect and Use
Open your browser and point to the gateway-service URL. 
This is http://localhost:8080 (on default)
  
 
