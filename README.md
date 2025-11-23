# expense-tracker

covers code for expense tracker

To build backend

> cd backend/docker-compose

Run databases

> docker-compose -f ./db-docker-compose.yml up -d

Build all microservices

> cd backend/user-service
> mvn clean package -DskipTests
> Or use maven wrapper
> ./mvnw clean package -DskipTests

Follow same for all microservice

if failed [ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:3.5.8-SNAPSHOT:repackage
use bellow command

> taskkill /F /IM java.exe

Running all microservices in docker

> docker-compose up -d
