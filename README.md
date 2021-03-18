Transactions Statistics
----
##### About
System used to calculate realtime statistics for the last 60 seconds of transactions.
Endpoints:
 - POST /transactions – called every time a transaction is made.
 - GET /statistics – returns the statistic based of the transactions of the last 60
seconds.
 - DELETE /transactions – deletes all transactions.


#### Assumptions
- API will be used for the interval for 60 seconds only.
- There will be api gateway for the throttling of request.
- Authentication/Authorization can also be implemented in the future.
- We can use lombok to get rid of getter/setters/hashcode/equals etc methods.

#### Technology Used
- Java
- SpringBoot
- Maven

#### Approach
We are having the in-memory array of Statistics[60] known as `store`. It will consist of data of statistics within last 60 seconds.
- As we are using array, the traversal will be O(1).
- While adding the transaction, we use the timestamp to determine the index in the array.It will be O(1) as we are dealing with fixed size array elements.
- Calculation of Statistics is O(1) as well, because we are iterating over fixed size array

#### Steps to Run
- Build Steps
    - `mvn clean install`  for build
    - `mvn clean integration-test` for integration-test
    - `mvn clean test` for unit test case
- Run Steps
   - `mvn spring-boot:run` for running the application
- Entry Point
    - `com.n26.Application` is the entry point of the application
    - Swagger Endpoint `http://localhost:8080/swagger-ui.html`
