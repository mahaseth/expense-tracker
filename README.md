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

for building all microservice at once use bellow.

> cd /backend
> mvnd -DskipTests clean package

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

## Getting Auth Token from security-service

- build the jar file for security-service
- run "./mvnw clean package -DskipTests" for building jar file
- run "./mvnw spring-boot:run -DskipTests" for runnign application.
- hit bellow url in browser.

```
https://localhost:8093/oauth2/authorization/google
```

## Setting up Kubernetes locally

This project includes Kubernetes manifests in `backend/kubernates-configs`. The high-level steps below walk you through running the services locally with Minikube.

Prerequisites:

- Install: `minikube`, `kubectl`, and `docker` (or Docker Desktop).

Start Minikube and basic checks:

```bash
minikube start --driver=docker
minikube status
minikube dashboard
```

Databases (Postgres & MongoDB):

- Apply Postgres manifest:

```bash
kubectl apply -f backend/kubernates-configs/k8s-postgres.yml
```

- Apply Mongo manifest:

```bash
kubectl apply -f backend/kubernates-configs/k8s-mongo.yml
```

- View resources and pods:

```bash
kubectl get all
kubectl get pods
```

- Follow logs for a pod:

```bash
kubectl logs -f <pod-name>
```

Building and loading service images into Minikube

You can either use Minikube's Docker daemon or build images directly into the Minikube image cache.

- Option A — build image into Minikube (recommended):

```bash
cd backend/service-discovery
mvnw clean package -DskipTests   # or use mvn
minikube image build --tag=discovery-service:latest .
```

- Option B — point your shell to Minikube's Docker daemon (Windows PowerShell example):

```powershell
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
# then docker build -t discovery-service:latest .
```

Verify images inside Minikube:

```bash
minikube image ls
```

Deploying services

- Service discovery (Eureka):

```bash
kubectl apply -f backend/kubernates-configs/service-discovery.yml
kubectl port-forward svc/discovery-server 8094:8094
```

For each microservice, follow the service-specific steps below.

### Service-Discovery

1. Build the JAR in the `Service-Discovery` folder:

```bash
cd backend/Service-Discovery
./mvnw clean package -DskipTests
```

2. Build the image into Minikube:

```bash
minikube image build --tag=discovery-service:latest .
```

3. Apply the manifest and port-forward:

```bash
kubectl apply -f backend/kubernates-configs/service-discovery.yml
kubectl port-forward svc/discovery-server 8094:8094
```

4. Verify:

```bash
kubectl get all
kubectl logs -f <discovery-pod-name>
```

### Category-Service

1. Build the JAR:

```bash
cd backend/Category-Service
./mvnw clean package -DskipTests
```

2. Build the image into Minikube:

```bash
minikube image build --tag=category-service:latest .
```

3. Apply the manifest:

```bash
kubectl apply -f backend/kubernates-configs/category-service.yml
```

4. Verify:

```bash
kubectl get all
kubectl logs -f <category-pod-name>
```

### User-Service

1. Build the JAR:

```bash
cd backend/User-Service
./mvnw clean package -DskipTests
```

2. Build the image into Minikube:

```bash
minikube image build --tag=user-service:latest .
```

3. Apply the manifest:

```bash
kubectl apply -f backend/kubernates-configs/user-service.yml
```

4. Verify:

```bash
kubectl get all
kubectl logs -f <user-pod-name>
```

### Transaction-Service

1. Build the JAR:

```bash
cd backend/Transaction-Service
./mvnw clean package -DskipTests
```

2. Build the image into Minikube:

```bash
minikube image build --tag=transaction-service:latest .
```

3. Apply the manifest:

```bash
kubectl apply -f backend/kubernates-configs/transaction-service.yml
```

4. Verify:

```bash
kubectl get all
kubectl logs -f <transaction-pod-name>
```

### Ingress and routing

- Enable Minikube ingress and apply the ingress manifest:

```bash
minikube addons enable ingress
kubectl apply -f backend/kubernates-configs/ingress.yml
minikube tunnel
```

- Add a host entry on your machine (edit `C:\Windows\System32\drivers\etc\hosts` as administrator):

```
127.0.0.1 myapp.local
```

- Use `http://myapp.local/<service-path>` to access services via ingress.

Eureka / application properties

Ensure each service sets these properties so they register correctly with Eureka:

```properties
eureka.instance.hostname=${spring.application.name}
eureka.instance.prefer-ip-address=true
```

Troubleshooting tips

- If a pod is CrashLoopBackOff or failing, check logs: `kubectl logs -f <pod-name>`.
- If the image is not found, confirm you built with `minikube image build` or used the Minikube Docker daemon.
- Use `kubectl describe pod <pod-name>` for more diagnostic details.
