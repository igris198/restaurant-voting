## **Restaurant voting system.**

Technical requirement:
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote for a restaurant they want to have lunch at today
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we assume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.

### Credentials

| Email | Password | Roles       |
|-------|----------|-------------|
|user@yandex.ru | password | USER        |
|admin@gmail.com       | admin    | USER, ADMIN |
|guest@gmail.com       | guest    |             |

### Api description links

[Swagger UI](http://localhost:8081/swagger-ui/index.html)

[Open API](http://localhost:8081/v3/api-docs)

Use JWT authorization:

curl -X POST "http://localhost:8081/api/auth/login" -H "Content-Type: application/json" --data "{\"email\": \"admin@gmail.com\", \"password\": \"admin\"}" 