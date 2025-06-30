# Multi-Currency Ledger

This is a sample Spring Boot application for managing multi-currency client accounts and transactions. It supports currency conversion, account lookups, transaction histories with pagination, and transfer operations.


This sample was built to fulfill the given functional and non-functional requirements for the Java Home Assignment challenge. It follows clean code practices and layered architecture.

## 🚀 How to Run the Application

### Prerequisites
- Java 21+
- Maven 3.8+

### Start the App
```bash
mvn clean spring-boot:run
```

This will start the server at: `http://localhost:8080`

### Optional: Build a Jar
```bash
mvn clean package
java -jar target/multi-currency-ledger-*.jar
```

## 📖 API Documentation (Swagger UI)

The application does not serve any HTML UI at the root URL. Instead, use the Swagger UI to explore and interact with the available REST endpoints.

Once the application is running, open your browser at:

```
http://localhost:8080/swagger-ui/index.html
```

### Available Endpoints
- `GET /api/clients/{id}` — Get client details
- `GET /api/accounts/client/{id}` — List accounts for a client
- `GET /api/accounts/{id}/transactions` — Get paginated transactions
- `POST /api/transfers` — Transfer funds between accounts

## 🔍 Using the H2 Console

Explore the in-memory database:

1. Go to: `http://localhost:8080/h2-console`
2. Set:
   - **JDBC URL:** `jdbc:h2:mem:ledgerdb`
   - **User Name:** `sa`
   - **Password:** (leave blank)
3. Click **Connect**.

Example queries:
```sql
SELECT * FROM client;
SELECT * FROM account;
SELECT * FROM transaction;
```

## 📦 Features
- Account and client management
- Transaction history with pagination
- Currency conversion via public API
- Validations for currency mismatches and insufficient funds
- Swagger UI for API testing
- In-memory H2 database with demo data (Alice, Bob, Eve)
- Retry mechanism for external currency service

## 🧪 Testing

### Unit & Integration Tests
Run all tests and generate a coverage report:
```bash
mvn clean verify
```

### JaCoCo Coverage Report
After running tests, open:
```
target/site/jacoco/index.html
```

### Coverage Goals
- **80%+ line coverage** (actual target met)
- Integration tests validate main business flows

## 🧱 Schema Versioning

This project uses **Liquibase** for database schema versioning.

Even with an in-memory H2 DB, Liquibase tracks changes via changelog files.
Make sure this is enabled in your `application.yml`:
```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml
```

## 📂 Demo Clients

Seeded at startup:
- **Alice**: Accounts in EUR, USD, CHF
- **Bob**: Accounts in EUR, USD, CHF
- **Eve**: Accounts in EUR, USD, CHF

---