# expense-tracker

# covers code for expense tracker

## To build backend

> cd backend/docker-compose

## Run databases

> docker-compose -f ./db-docker-compose.yml up -d

## Build all microservices

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
> or for building new image and running container.
> docker-compose up --build -d

## For rebuilding one service

docker-compose stop category-service
docker-compose rm -f category-service
docker-compose build --no-cache category-service
docker-compose up -d category-service

## To run performance testing.

- First disable and enable only the required test in jmeter UI.
- Run bellow command

  > jmeter -n -t ../backend/performance-testing.jmx -l performance-testing/result-combined.jtl -e -o performance-testing/result-combined

  ## for running monitoring container: prometheus and grafana

  > docker-compose -f ./docker-monitoring-compose.yml up -d

  > docker-compose -f ./docker-monitoring-compose.yml down

  - Open localhost:9090 for prometheus UI
  - Open localhost:3000 for opening grafana UI -> add data source with connection as http://prometheus:9090 -> save & test, Add dashboard -> dashboard id -> save.
