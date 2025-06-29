# Multi-Currency Ledger

This is a sample Spring Boot application for managing multi-currency client accounts and transactions. It supports currency conversion, account lookups, transaction histories with pagination, and transfer operations.

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

The application does not serve any HTML UI at the root URL (`http://localhost:8080`). Instead, use the Swagger UI to explore and interact with the available REST endpoints.

Once the application is running, open your browser at:

```
http://localhost:8080/swagger-ui/index.html
```

This UI allows you to interact with all available REST endpoints, such as:
- List client accounts
- Get transaction history
- Transfer funds between accounts

## 🔍 Using the H2 Console

You can explore the in-memory H2 database with a browser UI:

1. Navigate to:
   ```
   http://localhost:8080/h2-console
   ```
2. Enter the following connection details:
   - **JDBC URL:** `jdbc:h2:mem:ledgerdb`
   - **User Name:** `sa`
   - **Password:** *(leave blank)*
3. Click **Connect**.

You can now run SQL queries such as:
```sql
SELECT * FROM client;
SELECT * FROM account;
```

## 📦 Features
- Account and client management
- Transaction history with pagination
- Currency conversion (external API via frankfurter.app)
- H2 in-memory database with preloaded demo data (Alice, Bob, Eve)
- Resilience with retry handling for external services
- Swagger UI for API testing and documentation

## 🛠 Technologies Used
- Java 21, Spring Boot 3
- Maven
- H2 (in-memory database)
- Spring Data JPA
- Spring Retry
- SpringDoc OpenAPI (Swagger)

## 📂 Seeded Demo Data
At startup, the application creates 3 clients:
- Alice (EUR, USD, CHF accounts)
- Bob (EUR, USD, CHF accounts)
- Eve (EUR, USD, CHF accounts)

Each account has a non-negative balance and can be used for testing transfers.

---