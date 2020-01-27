# Write an application for the input of calories
## Requirements
### User Accounts and Roles
- User must be able to create an account and log in. (If a mobile application, this means that more users can use the app from the same phone).
- When logged in, a user can see a list of his meals, also he should be able to add, edit and delete meals. 
  - (user enters calories manually, no auto calculations!)
- Implement at least three roles with different permission levels: 
  - a regular user would only be able to CRUD on their owned records, 
  - a user manager would be able to CRUD users, 
  - and an admin would be able to CRUD all records and users.
### User Meal management  
- Each entry has: 
    - a date, 
    - time, 
    - text, 
    - and num of calories.
- Filter 
  - by: 
    - dates from-to, 
    - time from-to 
  - (e.g. how much calories have I had for lunch each day in the last month if lunch is between 12 and 15h).
  
- User setting – Expected number of calories per day.
- When meals are displayed, they go green if the total for that day is less than expected number of calories per day, otherwise they go red.

### REST API. 
- Make it possible to perform all user actions via the API, including authentication (If a mobile application and you don’t know how to create your own backend you can use Firebase.com or similar services to create the API).
- In any case, you should be able to explain how a REST API works and demonstrate that by creating functional tests that use the REST Layer directly. Please be prepared to use REST clients like Postman, cURL, etc. for this purpose.
- If it’s a web application, it must be a single-page application. 
    - All actions need to be done client side using AJAX, refreshing the page is not acceptable. (If a mobile application, disregard this).

### Design
- Functional UI/UX design is needed. 
- You are not required to create a unique design, however, do follow best practices to make the project as functional as possible.

## Bonus: 
- Unit and e2e tests.

## Repository
Please use this private repository to version-control your code:
- [Repository address](https://git.toptal.com/screening/rostislav-dublin)
- Username: javaisforever@gmail.com 

## Helpful take-home project guidelines:
- This project will be used to evaluate your skills, and should be fully functional without any obvious missing pieces. We will evaluate the project as if you were delivering it to a customer.
- The deadline to submit your completed project is 2 weeks from the date you received the project requirements.
- If you schedule your final interview after the 2-week deadline, make sure to submit your completed project and all code to the private repository before the deadline. Everything that is submitted after the deadline will not be taken into consideration.
- Please do not commit any code at least 12 hours before the meeting time so that it can be reviewed. Anything that is submitted after this time will not be taken into consideration.
- Please join the meeting room for this final interview on time. If you miss your interview without providing any prior notice, your application may be paused for six months.

- Please schedule an interview time that is most suitable for you. Bear in mind that you will need to show a finished project during this interview.

- Once you pick an appointment time, we’ll email you with additional meeting details and contact information of another senior developer from our team who will conduct your next interview. You may reach out to them if you have any questions.
