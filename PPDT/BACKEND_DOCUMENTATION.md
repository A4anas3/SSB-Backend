# 🍃 SSB Prep AI - Backend Documentation (Spring Boot)

**Service Name:** PPDT Backend  
**Framework:** Spring Boot 3.3.5 (Java 21)  
**Port:** 9000

---

## 🏗️ Architecture Style
The web service follows a strictly **Layered Architecture**:

```mermaid
graph TD
    Client[React Client] -->|REST API| Controller[Controller Layer]
    Controller -->|DTOs| Service[Service Layer]
    Service -->|Entities| Repo[Repository Layer (JPA)]
    Repo -->|SQL| DB[(PostgreSQL)]
```

### Key Principles
1.  **Stateless Security:** JWT-based authentication (Resource Server).
2.  **DTO Pattern:** Entities are never exposed directly to the API. `DTOs` (Data Transfer Objects) are used for all Input/Output.
3.  **Lombok:** Used extensively to reduce boilerplate (`@Data`, `@Builder`).

---

## 🛠️ Tech Stack Details

| Technology | Usage |
|------------|-------|
| **Spring Web** | REST API endpoints. |
| **Spring Security** | OAuth 2.0 Resource Server (JWT). |
| **Spring Data JPA** | ORM for PostgreSQL. |
| **PostgreSQL** | Primary datastore (Port 5433). |
| **Keycloak** | Identity Provider (Port 18080). |
| **FastAPI** | AI & News aggregation services (Ports 8000/8001). |

---

## 📦 Project Structure (`com.example.ssb`)

```
b:\SSB\PPDT\src\main\java\com\example\ssb
├── 🔐 security/                # Security Configuration
│   ├── SecurityConfig.java     # JWT & CORS setup
│   ├── SecurityHeadersConfig.java # CSP Headers
│   └── CorsConfig.java         # CORS Policy
│
├── 🧠 oir/                     # OIR (Officer Intelligence Rating) Module
│   ├── controller/             # OirController, OirAdminController
│   ├── service/                # Business Logic
│   ├── model/                  # Entities (OirTest, OirQuestion)
│   ├── dto/                    # Request/Response objects
│   └── repo/                   # JPA Repositories
│
└── 📝 ppdt/                    # PPDT (Picture Perception) Module
    ├── controller/             # PPDTController, AnalysisController
    ├── Service/                # Image & Analysis Service
    ├── Entity/                 # PPDTImage, PPDTAttempt
    └── Repo/                   # DB Access
```

---

## 🔐 Security Implementation (`SecurityConfig.java`)

The backend acts as an **OAuth 2.0 Resource Server**. It does not handle login; it validates tokens issued by Keycloak.

### 1. Authorization Rules
| Path | Access Level | Purpose |
|------|--------------|---------|
| `/api/oir/tests/**` | `permitAll()` | Publicly accessible tests. |
| `/actuator/**` | `permitAll()` | Health checks. |
| `/admin/**` | `hasRole('ADMIN')` | Admin-only actions. |
| `/**` | `authenticated()` | All other endpoints require a valid JWT. |

### 2. Role Conversion
Keycloak stores roles in `realm_access.roles`. We use a custom `JwtAuthenticationConverter` to map these to Spring Security authorities:
- Keycloak: `admin` → Spring: `ROLE_admin`

---

## 🗄️ Database Entities (Schema)

### 1. OIR Module (`oir_test`, `oir_question`)
- **OirTest:** Represents a test set (e.g., "OIR Set 1").
    - `id`, `testName`, `totalQuestions`
- **OirQuestion:** Individual questions linked to a test.
    - `questionText`, `options (A-D)`, `correctOption`, `test_id` (FK)

### 2. PPDT Module (`ppdt_images`, `ppdt_attempts`)
- **PPDTImage:** Images for story writing.
    - `imageUrl`, `imageContext`, `guide`, `isSample`
- **PPDTAttempt:** User's written story.
    - `userStory`, `attemptedAt`, `imageId` (FK)

---

## 🔌 API Endpoints Reference

### 🧠 OIR Module (`/api/oir`)

| Method | Endpoint | Role | Description |
|:------:|----------|------|-------------|
| `GET` | `/tests` | Public | List all active tests. |
| `GET` | `/tests/{id}` | Public | Get full test with questions. |
| `POST` | `/submit` | User | Submit answers for grading. |

**Admin (`/api/admin/oir`)**
| Method | Endpoint | Role | Description |
|:------:|----------|------|-------------|
| `POST` | `/tests` | ADMIN | Create a new empty test. |
| `PATCH`| `/tests/{id}/full` | ADMIN | Bulk upload questions to a test. |
| `DELETE`| `/tests/{id}` | ADMIN | Delete a test and its questions. |

---

### 📝 PPDT Module (`/api/ppdt`)

| Method | Endpoint | Role | Description |
|:------:|----------|------|-------------|
| `GET` | `/images` | User | Get random/all practice images. |
| `POST` | `/analyze` | User | Submit story for AI analysis (Calls FastAPI). |
| `GET` | `/history` | User | Get user's past attempts. |

---

## 🤖 External Service Integration

### AI Analysis Flow
When a user submits a PPDT story:
1.  **Controller** receives the text.
2.  **Service** constructs a JSON payload.
3.  **WebClient** calls the Python FastAPI service (`http://localhost:8001/analyze/ppdt`).
4.  **Result** (OLQ Score, Feedback) is returned to the frontend.

---

## 🚀 How to Run (Local Dev)

1.  **Ensure Docker is running** (for DB and Keycloak).
2.  **Navigate to directory:**
    ```bash
    cd PPDT
    ```
3.  **Run with Maven Wrapper:**
    ```bash
    ./mvnw spring-boot:run
    ```
    *Application will start on Port 9000.*

---

## ✅ Best Practices Used
- **Dependency Injection:** Constructor-based injection (`@RequiredArgsConstructor`).
- **Separation of Concerns:** Controllers handle generic HTTP, Services handle business logic.
- **Global Exception Handling:** `@ControllerAdvice` (if implemented) standardizes errors.
- **Configuration Properties:** URLs and secrets are managed in `application.yml`.
