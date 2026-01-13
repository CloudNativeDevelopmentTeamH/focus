# Focus

A Quarkus-based application for focus session tracking, built following **Clean Architecture** principles.

## Architecture

The project follows Clean Architecture (Hexagonal / Ports & Adapters):

```
src/main/java/de/thi/focus/
├── entities/               # Enterprise Business Rules (Innermost Ring)
│   ├── Category.java       # Category Entity
│   ├── FocusSession.java   # Focus Session Entity
│   ├── ids/                # Value Objects for IDs
│   ├── valueobjects/       # Value Objects (Note, TimeRange, Color, CategoryName)
│   ├── errors/             # Domain-specific Exceptions
│   └── events/             # Domain Events
│
├── usecases/               # Application Business Rules
│   ├── ports/
│   │   ├── inbound/        # Input Ports (Use Case Interfaces)
│   │   └── outbound/       # Output Ports (Repository & System Interfaces)
│   ├── interactor/         # Use Case Implementations
│   ├── policies/           # Business Policies (e.g., RunningSessionPolicy)
│   ├── factories/          # Value Object Factories
│   ├── dtos/               # Use Case DTOs (Input/Output)
│   └── errors/             # Use Case Errors
│
├── interfaceadapters/      # Interface Adapters
│   ├── web/                # REST Controllers
│   │   ├── SessionController.java
│   │   ├── CategoryController.java
│   │   ├── dto/            # HTTP Request/Response DTOs
│   │   ├── presenter/      # Output Presenters
│   │   └── exception/      # Exception Mappers
│   ├── health/             # Health Endpoints
│   │   ├── HealthResource.java   # GET /healthz
│   │   └── ReadinessResource.java # GET /readyz (incl. DB check)
│   └── grpc/               # gRPC Services
│
├── frameworksdrivers/      # Frameworks & Drivers (Outermost Ring)
│   ├── persistence/        # JPA Repositories & Mappers
│   ├── events/             # Event Publisher Implementations
│   └── time/               # Clock Implementations
│
├── config/                 # Configuration
│   ├── ApplicationWiring.java     # CDI Producer (Dependency Injection)
│   ├── FocusConstraintsConfig.java
│   └── FocusDefaultsConfig.java
│
└── support/                # Test Support (e.g., FixedClock)
```

### Layer Model

| Layer               | Responsibility                         | Examples                                         |
|---------------------|----------------------------------------|--------------------------------------------------|
| **Entities**        | Core business logic, domain invariants | `FocusSession`, `Category`, Value Objects        |
| **Use Cases**       | Application use cases, orchestration   | `StartSessionInteractor`, `StopSessionInteractor`|
| **Interface Adapters**| Conversion, presentation              | REST Controllers, Presenters, Mappers            |
| **Frameworks/Drivers**| Technical infrastructure              | JPA Repositories, Event Publishers               |

### Use Cases (Inbound Ports)

**Sessions:**
- `StartSessionInputPort` - Start a session
- `StopSessionInputPort` - Stop a session
- `ResumeSessionInputPort` - Resume a session
- `GetRunningSessionInputPort` - Get active session
- `UpdateSessionInputPort` - Update a session

**Categories:**
- `CreateCategoryInputPort` - Create a category
- `RenameCategoryInputPort` - Rename a category
- `ChangeCategoryColorInputPort` - Change color
- `ArchiveCategoryInputPort` / `UnarchiveCategoryInputPort` - Archive/Unarchive
- `DeleteCategoryInputPort` - Delete a category
- `ListCategoriesInputPort` - List categories

### Policies

- `RunningSessionPolicy` - Checks if a session is already running
- `UniqueCategoryNamePolicy` - Ensures unique category names

## Tech Stack

- **Runtime:** Java 21, Quarkus 3.30
- **Persistence:** Hibernate ORM, PostgreSQL, Flyway
- **API:** REST (Jackson), gRPC
- **Build:** Maven

## Development

### Prerequisites

- Java 21+
- Docker (for PostgreSQL)

### Start Database

```shell
docker compose up -d
```

### Run Application in Dev Mode

```shell
./mvnw quarkus:dev
```

The application runs on port **8088** (configured in `application.yaml`).

### Packaging

```shell
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

### Native Build

```shell
./mvnw package -Dnative
./target/focus-analysis-1.0-SNAPSHOT-runner
```

## API Endpoints

| Method | Path                  | Description               |
|--------|-----------------------|---------------------------|
| GET    | `/healthz`            | Liveness Check            |
| GET    | `/readyz`             | Readiness Check (incl. DB)|
| POST   | `/sessions/start`     | Start a session           |
| POST   | `/sessions/stop`      | Stop a session            |
| POST   | `/sessions/resume`    | Resume a session          |
| GET    | `/sessions/running`   | Get active session        |
| POST   | `/categories`         | Create a category         |
| GET    | `/categories`         | List categories           |

## Configuration

See `src/main/resources/application.yaml`:

```yaml
focus:
  constraints:
    note:
      max-length: 1000
    category:
      name:
        max-length: 50
  defaults:
    category-color: "#FFFFFF"
```
