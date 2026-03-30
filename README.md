 Airline Ticketing System API
 
Overview
This project is a microservice-based airline backend system built using Spring Boot and Spring Cloud Gateway. It provides APIs for flight management, ticket purchasing, check-in, and passenger listing.

 Architecture
Client → API Gateway → Backend → Database

Technologies
•	Java 17
•	Spring Boot
•	Spring Cloud Gateway
•	MySQL (AWS RDS)
•	Maven
•	Swagger
•	AWS EC2
•	k6 (Load Testing)

Live System
Gateway
http://13.53.166.181:8081/api/v1/flights
Swagger
http://13.53.166.181:8080/swagger-ui/index.html

 Features
•	Flight search and management
•	Ticket purchase system
•	Passenger check-in
•	API Gateway routing
•	Rate limiting (HTTP 429)
•	Load tested with k6

Run Locally
Build
mvn clean package -DskipTests
Run Backend
java -jar airline-api.jar
Run Gateway
java -jar airline-gateway.jar

 Notes
•	Rate limiting is implemented at gateway level
•	System is deployed on AWS EC2
•	Database is hosted on AWS RDS
 Project Status
The system is fully deployed and operational on AWS.

Watch Demo Video:
https://youtu.be/Iok0GcYcltg

