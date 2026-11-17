# ParcelPulse 

A production-ready, full-stack e-commerce fulfillment system engineered to handle concurrent high-volume traffic, stateless authentication, and distributed caching. 

Built with a **Kotlin/Spring Boot** backend and a **React/Vite** frontend, this platform demonstrates enterprise software architecture patterns including Domain-Driven Design (DDD), optimistic locking, and automated background processing.

##  Key Features

* **Race-Condition Resilience:** Implemented JPA Optimistic Locking to mathematically prevent inventory overselling during concurrent purchase requests. Backed by automated integration tests simulating simultaneous users.
* **Stateless Security:** Secured REST API endpoints using JSON Web Token (JWT) authentication, Spring Security filter chains, and strict CORS preflight configurations.
* **High-Performance Caching:** Integrated an in-memory Redis cache to intercept product catalog database queries, serving read-heavy endpoints instantly and reducing database load.
* **Automated Data Migrations:** Managed PostgreSQL relational schema evolution and sample data seeding using Flyway.
* **Asynchronous Workers:** Engineered background order fulfillment processes utilizing Spring's `@Scheduled` annotation to process transactions independently of the main request thread.
* **Modern Web Client:** Built a fast, responsive UI using React and Vite, featuring an Axios HTTP client with automated JWT request interceptors.

##  Tech Stack

**Backend (REST API)**
* **Language:** Kotlin (JDK 21)
* **Framework:** Spring Boot 3.3.5, Spring Security, Spring Data JPA
* **Database & Caching:** PostgreSQL, Redis
* **Migrations:** Flyway
* **Testing:** JUnit 5, Spring Boot Test

**Frontend (Storefront)**
* **Framework:** React, Vite
* **HTTP Client:** Axios

**Infrastructure**
* **Containerization:** Docker & Docker Compose (for Postgres and Redis)
* **Build Tool:** Gradle

## ⚙️ Local Development Setup

### Prerequisites
* JDK 21
* Node.js (v18+)
* Docker Desktop (for running PostgreSQL and Redis)

### 1. Boot up Infrastructure
Start the database and cache containers from the root directory:

docker-compose up -d

1. Boot up Infrastructure
Start the database and cache containers from the root directory:

docker-compose up -d

(Flyway will automatically run the SQL migrations on startup to build the tables and insert the sample products).

2. Start the Spring Boot Backend
Navigate to the backend directory and start the API on localhost:8080:

cd backend
./gradlew bootRun

3. Start the React Frontend
Open a new terminal, navigate to the frontend directory, install dependencies, and start the Vite server on localhost:5173:

cd frontend
npm install
npm run dev

 Testing:
The backend includes an automated integration test suite verifying concurrency control. To run the race-condition simulation that proves the system's resilience to inventory overselling:

cd backend
./gradlew test
