> #  Transaction Management Service
>
> 
>
> Transaction management service is a bank system, providing Create/Delete/Modify/View transaction operations with high performance.
>
> ## Project Overview
>
> 
>
> ### Project Features
>
> - Create transaction
> - Query transaction by ID
> - Query Paginated transaction list
> - Update transaction information
> - Delete transaction
> - Transaction request validation 
> - Exception handling
> - In-memory Data storage
> - Caching for high performance
>
> 
>
> ### Technology
>
> - Java 21
> - Spring Boot 3.2.1
> - Maven
> - Caffeine Cache
> - JUnit 5 + Mockito
> - Docker
>
> 
>
> ### Running in Local
>
> #### 1. Compile the project
>
> ```
> mvn clean compile
> ```
>
> #### 2. Run tests
>
> ```
> mvn test
> ```
>
> #### 3. Start the application
>
> ```
> mvn spring-boot:run
> ```
>
> 
>
> Once Application is started, API can be visited via the swagger URL:
>
>  http://localhost:8080/swagger-ui.html
>
> 
>
> Application Acuator health check URL:
>
>  http://localhost:8080/actuator/health
>
> 
>
> ### Docker Deployment
>
> #### 1. Build the JAR file
>
> ```
> mvn clean package
> ```
>
> #### 2. Build the image
>
> ```
> docker build -t transaction-management-service .
> ```
>
> #### 3. Run the container
>
> ```
> docker run -p 8080:8080 transaction-management-service
> ```
>
> 
>
> ## API Interface
>
> ### Basic URL
>
> - **Base URL**: `http://localhost:8080/bank/transactions`
> - **Content-Type**: `application/json`
>
> 
>
> #### 1. Create Transaction
>
> ```
> POST /bank/transactions
> Content-Type: application/json
> 
> {
>    "amount": 500.00,
>    "currency": "USD",
>    "transactionType": "DEPOSIT",
>    "transactionReference": "REF1"
> }
> ```
>
> **Response Example**:
>
> ```
> {
>    "id": "751e8400-d29b-41d4-a816-446655440000",
>    "amount": 500.00,
>    "currency": "USD",
>    "transactionType": "DEPOSIT",
>    "transactionReference": "REF1",
>    "timestamp": "2025-05-15T11:30:00"
> }
> ```
>
> 
>
> #### 2. Query Transaction By ID
>
> ```
> GET /bank/transactions/{id}
> ```
>
> 
>
> #### 3. Query Paginated Transaction List
>
> ```
> GET /bank/transactions?page=0&size=10
> ```
>
> **Response Example**:
>
> ```
> {
>    "content": [
>         {
>            "id": "751e8400-d29b-41d4-a816-446655440000",
>            "amount": 500.00,
>            "currency": "USD",
>            "transactionType": "DEPOSIT",
>            "transactionReference": "REF1",
>            "timestamp": "2025-05-15T11:30:00"
>         }
>    ],
>    "page": 0,
>    "size": 10,
>    "totalElements": 1,
>    "totalPages": 1,
>    "first": true,
>    "last": true
> }
> ```
>
> 
>
> #### 4. Update Transaction Information
>
> ```
> PUT /bank/transactions/{id}
> Content-Type: application/json
> 
> {
>    "amount": 300.00,
>    "currency": "EUR",
>    "transactionType": "WITHDRAWAL",
>    "transactionReference": "REF2"
> }
> ```
>
> 
>
> #### 5. Delete Transaction
>
> ```
> DELETE /bank/transactions/{id}
> ```
>
> 
>
> ### Supported Parameters
>
> #### Currency Type
>
> - `USD` 
> - `EUR` 
> - `GBP` 
> - `JPY` 
> - `CNY` 
> - `AUD` 
> - `CAD` 
>
> #### Transaction Type
>
> - `DEPOSIT` 
> - `WITHDRAWAL` 
> - `TRANSFER` 
>