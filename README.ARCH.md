# Solution Architecture
This document is dedicated to document chosen solution architecture

## Framework
Spring Boot 2 
  
## Microservices
- The solution is implemented based on the microservices architecture principles. 
- It consists of 6 microservices

### 1. Portal-Config microservice
- Based on: [Spring Cloud Config Server](https://spring.io/projects/spring-cloud-config)
- Central storage for all microservices configs
- When a service starts, it requests its config from this one.

### 2. Portal-Registry microservice
- Based on: [Eureka Service Discovery Server](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-eureka-server.html)
- On start, all microservices register themselves on Eureka  

### 3. Portal-Gateway microservice
- Based on [ZUUL Router and Filter service](https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html)
- Provides single URL to the solution and maps all microservices endpoints to its URL paths
- Uses Registry microservice for services discovery

### 4. Portal-Auth microservice
- Based on: 
    - [Spring Boot Data JPA](https://spring.io/projects/spring-data-jpa)
    - [Spring Security](https://spring.io/projects/spring-security)
- Privides:
    - User Accounts management - to store user accounts details
    - OAuth2 authorization server - to authenticate users and other services and give them JWT access tokens.
    - OAuth2 resource server - to serve other services requests for user accounts details
- Storage:
    - "calory_portal_auth" MySql5 database    
    
### 5. Portal-Calories microservice
- Based on: 
    - [Spring Boot Data JPA](https://spring.io/projects/spring-data-jpa)
    - [Spring Security](https://spring.io/projects/spring-security)
- Privides:
    - User Meals and Calories management - to store and calculate user calories consumtion
    - Meal consumption related UserSettings - "daily calories expectations in particular"
    - OAuth2 resource server - to serve other services requests for user meals details and statistics
- Storage:
    - "calory_portal_calories" MySql5 database 

### 6. Portal-UI
- Based on:
    - Angular 8
- Privides:
    - Application Web interface
- Consumes and stores
    - User data from Portal-Auth service
    - Meal and UserSettings data from the Portal-Calories service    
           
    
        


