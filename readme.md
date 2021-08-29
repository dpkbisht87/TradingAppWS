<h1 align="center">
<br>
  Trading Application
  <br>
</h1>

## Table of Contents ##
1. [Application](#Application)
2. [Database Schema](#Database-Schema)
3. [Technology](#Technology)
4. [Application Structure](#Application-Structure)
5. [Run Locally](#Running-the-server-locally)
6. [API Documentation](#API-Documentation)
7. [User Interface](#User-Interface)
8. [Contributor](#Contributor)
9. [License](#License)

## Application ##
This application focuses on the main use case for online trading with the following operations.
1. List all Stocks
2. Create a new Stock
3. Get Single Stock
4. Update the price of existing stock
5. Delete the stock

_Update_ and _Delete_ operation can not be performed on a locked stock.
A stock will go on a _locked_ state if it is newly created or its price has been updated recently.
The stock will be automatically unlocked after 5 minutes

## Database Schema ##
This application supports 2 databases
1. In-memory database - Reads a static json file(mockdata.json) during bootstrap of the application.
2. MongoDB - With minimal changes in Service and Repository we can use the mongoDB(local / remote).


## Technology ##
Following libraries were used during the development of this application :

- **Spring Boot** - Server side framework
- **MongoDB** - NoSQL database
- **Swagger** - API documentation

## Application Structure ##

**_Entity & DTOs_**
Entity and dto are kept in different package. DTO let us share only the data we want to share with customer.
The mapping of models to the DTOs can be handled using ModelMapper utility.

**_DAOs_**
The data access objects (DAOs) allow us to persist and retrieve data from the database.
They are all extensions of the MongoRepository and MockData Interface.

**_Controllers_**
Last, but the most important part is the controller layer. It binds everything together right from the moment a request is intercepted till the response is prepared and sent back. The controller layer is present in the controller package, the best practices suggest that we keep this layer versioned to support multiple versions of the application and the same practice is applied here. 

**_Request_**
The incoming request are handle by a separate request object in controller.v1.request package. These objects are used to validate the incoming request as well as holds only required data.

## Running the server locally ##
To be able to run this Spring Boot app you will need to first build it. To build and package a Spring Boot app into a single executable Jar file with a Maven, use the below command. You will need to run it from the project folder which contains the pom.xml file.

```
mvn package
```
or you can also use

```
mvn install
```

You can also use Maven plugin to run the app. Use the below example to run your Spring Boot app with Maven plugin :

```
mvn spring-boot:run
```
Once the server is setup you should be able to access the following endpoints
- http://localhost:8080/api/v1/stocks (HTTP:GET)
- http://localhost:8080/api/v1/stocks/{id} (HTTP:GET)
- http://localhost:8080/api/v1/stocks (HTTP:GET)
- http://localhost:8080/api/v1/stocks/{id} (HTTP:PUT)
- http://localhost:8080/api/v1/stocks/{id} (HTTP:DELETE)

## API Documentation ##
Its as important to document(as is the development) and make your APIs available in a readable manner for frontend teams or external consumers. The tool for API documentation used in this starter kit is Swagger3, you can open the same inside a browser at the following url -

http://localhost:8080/swagger-ui/index.html

## User Interface ##
We have developed a separate app for user interface.It can be dowloaded from here.
git@github.com:dpkbisht87/TradingApplicationUI.git

## Contributors ##
Deepak Bisht 

## License ##
None