
# GitHub Repository Searcher

## 1. Project Overview

GitHub Repository Searcher is a Spring Boot REST API that allows users to search GitHub repositories, retrieve repository details, and store the results in a PostgreSQL database.

The application integrates with the GitHub REST API to fetch repository information and provides additional filtering and sorting capabilities through its own REST endpoints.

## 2. Features

- Search GitHub repositories using a search query.
- Filter repositories by programming language.
- Sort repositories by star count.
- Store repository information in PostgreSQL.
- Retrieve previously stored repositories.
- Filter stored repositories by language and minimum stars.
- Update existing repository records when the same GitHub repository is fetched again.
- Validate incoming requests and query parameters.
- Handle API, database, and application errors.
- Interactive API documentation using Swagger UI.

## 3. Architecture

The application follows a layered architecture.

- **Controller:** Handles HTTP requests, validates inputs, and returns API responses.
- **Service:** Contains the business logic and coordinates repository searches and database operations.
- **GitHub API Client:** Communicates with the GitHub REST API using Spring RestClient.
- **Repository:** Handles database operations using Spring Data JPA.
- **Entity:** Represents repository records stored in PostgreSQL.
- **DTOs:** Separate request and response objects from internal entity and external API models.

## 4. How It Works

1. The client sends a repository search request to the application.
2. The controller validates the incoming request.
3. The service calls the GitHub REST API through the API client.
4. The returned repository data is mapped to application entities.
5. Repository records are saved to PostgreSQL.
6. The application returns the search results to the client.

The GET endpoint retrieves stored repositories and applies the requested filters and sorting.

## 5. Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **API Integration:** Spring RestClient
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA, Hibernate
- **Build Tool:** Maven
- **API Documentation:** Springdoc OpenAPI / Swagger UI
- **Testing:** JUnit 5, Mockito, MockMvc, H2, MockWebServer
- **Code Coverage:** JaCoCo

## 6. API Documentation

Swagger UI provides interactive documentation for the available endpoints.

**Local URL:**

`http://localhost:8080/swagger-ui/index.html`

## 7. API Endpoints

### Search GitHub Repositories

**POST** `/api/github/search`

Searches GitHub repositories using the provided query and optional language and sorting criteria.

**Request:**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

### Retrieve Stored Repositories

**GET** `/api/github/repositories`

Retrieves repositories stored in the database.

Supported query parameters:

| Parameter | Description |
|---|---|
| `language` | Filter by programming language |
| `minStars` | Filter by minimum star count |
| `sort` | Sort repositories by stars |

**Example:**

`GET /api/github/repositories?language=Java&minStars=100&sort=stars`

## 8. Error Handling

The application uses centralized exception handling through `GlobalExceptionHandler`.

It handles:

- Invalid request bodies and validation failures.
- Invalid query parameters.
- GitHub API errors and rate limits.
- GitHub API connectivity failures.
- Database integrity violations.
- Unexpected application errors.

Appropriate HTTP status codes and structured error responses are returned to the client.

## 9. Database Design

The application uses PostgreSQL to persist GitHub repository information.

Each repository is identified by its GitHub repository ID, which is used as the primary key.

When a repository with an existing GitHub ID is fetched again, its stored information is updated instead of creating a duplicate record.

## 10. Testing

The application includes unit, integration, and web-layer tests.

- **JUnit 5:** Test execution and assertions.
- **Mockito:** Mocking service dependencies.
- **MockMvc:** Controller endpoint testing.
- **H2:** Repository testing with an in-memory database.
- **MockWebServer:** Testing GitHub API responses and failure scenarios.
- **JaCoCo:** Code coverage measurement.

Run the test suite:

```bash
mvn clean verify
```

JaCoCo coverage report:

`target/site/jacoco/index.html`

## 11. Configuration

The application uses environment variables for database configuration.

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |

**Example JDBC URL:**

```text
jdbc:postgresql://<host>/<database>?sslmode=require
```

Sensitive credentials are kept outside the source code and should not be committed to version control.

## 12. Run Locally

### Prerequisites

- Java 21
- Maven
- PostgreSQL database

### Steps

1. Clone the repository.

```bash
git clone <repository-url>
```

2. Navigate to the project directory.

```bash
cd github-searcher
```

3. Configure the database environment variables.

4. Run the application.

```bash
mvn spring-boot:run
```

5. Open Swagger UI.

`http://localhost:8080/swagger-ui/index.html`

## 13. Deployment

Deployment details and the live application URL will be added after deployment.

## 14. Design Decisions and Trade-offs

- **Spring RestClient:** Used to communicate with the GitHub REST API.
- **Layered Architecture:** Separates HTTP handling, business logic, external API communication, and persistence.
- **DTOs:** Keeps external API models, internal entities, and public API responses separate.
- **PostgreSQL:** Provides persistent storage for repository information.
- **GitHub Repository ID:** Used as the primary key to avoid duplicate records when repositories are fetched repeatedly.
- **Environment Variables:** Keeps database credentials separate from application source code.
