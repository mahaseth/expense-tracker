# Expense Tracker

Microservices-based expense tracking application with Google OAuth2 authentication.

---

## Step 1: 🛠 Generate Auth token from Google OAuth2.

- Run security-service locally to get auth token.
- add bellow in application.properties file of security-service
  - client-id : 
  - client-secret: 
- cd to security-service and run bellow command.
  ./mvnw spring-boot:run -DskipTests
- use in chrome browser - http://localhost:8093/oauth2/authorization/google

---

## Step 2: Build Backend

### Run Databases

```bash
cd backend/docker-compose
docker-compose -f ./db-docker-compose.yml up -d
```

### Build All Microservices

Navigate to each microservice and build:

```bash
cd backend/user-service
mvn clean package -DskipTests
# Or use Maven wrapper
./mvnw clean package -DskipTests
```

Repeat the same for all microservices (API-Gateway, Category-Service, Transaction-Service, Service-Discovery).

**If build fails with:**

```
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:3.5.8-SNAPSHOT:repackage
```

Run:

```powershell
taskkill /F /IM java.exe
```

### Run All Microservices in Docker

```bash
docker-compose up -d
# Or to build new images and run containers
docker-compose up --build -d
```

### Rebuild a Single Service

```bash
docker-compose stop category-service
docker-compose rm -f category-service
docker-compose build --no-cache category-service
docker-compose up -d category-service
```

---

Note: Eureka server is running on: http:localhost:8094

## Step 3: Performance Testing

1. First, disable and enable only the required tests in JMeter UI
2. Run the command:

```bash
jmeter -n -t ../backend/performance-testing.jmx -l performance-testing/result-combined.jtl -e -o performance-testing/result-combined
```

---

## Step 4: Monitoring (Prometheus & Grafana)

### Start Monitoring Containers

```bash
docker-compose -f ./docker-monitoring-compose.yml up -d
```

### Stop Monitoring Containers

```bash
docker-compose -f ./docker-monitoring-compose.yml down
```

### Access Monitoring Dashboards

- **Prometheus UI:** http://localhost:9090
- **Grafana UI:** http://localhost:3000
  - Add data source with connection: `http://prometheus:9090`
  - Save & test, then add dashboards

---

## Getting Auth Token

```
https://localhost:8095/oauth2/authorization/google
```
