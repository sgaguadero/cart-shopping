# Shopping-Cart application 

### Reference Documentation and Goals
The idea of this assessment is create CRUD API to manage products and orders
* Create a new product
* Get a list of all products
* Update a product

The API should also support:
* Placing an order
* Retrieving all orders within a given time period

## Tools
To implement this exercise we're going to use the following tools
* maven 3.6.x
* Java 11
* Spring-boot as main framework
* Spring-boot JPA
* Swagger for API Documentation
* Lombok as a tool to avoid boilerplate code
    
    
## Decisions during implementation
* For the API basic CRUD 
* DTO for the external model, we don't want to show our model externally, but for this case the difference is minimum
* Use Converters  fro Dto > Model and Model > Dto
* FOr the product findAll we don't use pagination but should be an improvement
* repository Layer doesn't make sense to test, is part of the spb framework
 


## Answer Questions
#### You do not need to add authentication to your web service, but propose a protocol / method and justify your choice
* Spring Boot Security with OAuth2 and provide authentication con Gmail or Github. 
* the reason is because it's simple and provide a confidence 3er party solution

### How can you make the service redundant? What considerations should you do?
Not clear the question, but from my perspective if the service is redundat means data can be manage from different
request, what dos it mean? It means manage the data from different queries and provide the redundant data. 
For example, to be redundant we can provide extra information of products when you get an order and also the products 
could be store all the orders related but it's not good practice.
In a service mesh with data distributed in different microservices, these services manage their own data and 
we have to try avoid coupling and store same data in different places.  

## To Start
folder docker/ and run $docker-compose up
http://localhost:8080/actuator/... to the status of the app
Swagger http://localhost:8080/swagger-ui.html


## Improvements 
* test for the other two converters
* Improve immutability with lombok and entities, jpa etc.


## Examples

```
## Create Product
curl --location --request POST 'http://localhost:8080/api/product' \
--header 'Content-Type: application/json' \
--data-raw '{
	"name": "orangeTshirt1",
	"price": 200
}'

## Find Product
curl --location --request GET 'http://localhost:8080/api/product/orangeTshirt1'

## Update Product
curl --location --request PUT 'http://localhost:8080/api/product' \
--header 'Content-Type: application/json' \
--data-raw '{
        "id": 1,
        "name": "orangeTshirt1",
        "price": 130.0
    }'

## Create an Order
curl --location --request POST 'http://localhost:8080/api/order' \
--header 'Content-Type: application/json' \
--data-raw '{
	"email": "a@a.com",
	"products": ["2","4"]
}'

## Retrieving all orders within a given time period 
curl --location --request GET 'http://localhost:8080/api/order?date=2019-01-01'
```
