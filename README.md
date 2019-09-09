# MovieNight
MovieNight is a web application for browsing and buying movies. The application consists of a front-end and four different RESTful microservices: an IDM service, inventory service, billing service, and API gateway. All four of these microservices are built using Java, Gradle, Jackson, and Jersey. 

## How to run application
### Prerequisites
- Need SQL server running with four databases created (each DB can be on a different server or on the same server)
- Configure config.yaml in each of the microservices to point to the correct database with the correct credentials, ports, etc.
- Java 8 and Gradle 5.2+
- Docker installed (if building using Dockerfile)
### Run using Dockerfile
Clone this repository and cd into it:  
```
git clone https://github.com/joshlopez97/MovieNight.git
cd MovieNight
```
Build and run the docker image
```
docker build -t movienight .
docker run movienight
```
### Run without Docker
Clone this repository and cd into it:  
```
git clone https://github.com/joshlopez97/MovieNight.git
cd MovieNight
```
Build and run all four microservices using Gradle
```
gradle -p idm build
java -jar idm/build/lib/com.mn.service.idm-all.jar -c idm/config.yaml
gradle -p inventory build
java -jar inventory/build/lib/com.mn.service.inventory-all.jar -c inventory/config.yaml
gradle -p billing build
java -jar billing/build/lib/com.mn.service.billing-all.jar -c billing/config.yaml
gradle -p gateway build
java -jar gateway/build/lib/com.mn.service.gateway-all.jar -c gateway/config.yaml
```

## Service Architecture
### Gateway Service
This service is responsible for load-balancing all client requests across multiple worker threads which continuously schedule HTTPS requests to all the endpoints in the other 3 services (with the exception of session validation in the IDM service). Aside from that exception, the endpoints in the other services must be reached through the gateway by prepending /g/ to the path of the endpoint they intend to reach. Here's an example:
- Frontend makes call to `$GATEWAY_URL/g/movies/search?genre='action'` to reach the movie search endpoint
- The Gateway receives the request and inserts a new request in the gateway db, a 204 No Content response is returned to the client with a transaction ID in the header to follow-up with
- In the background, once a worker thread becomes available, the request will be popped from the database and executed to the appropriate service
- That service will return a response which will be stored in the gateway db along with the matching transaction ID
- The client will make a request to `/g/report` with the transaction ID in the header. This endpoint will return the response associated with that ID if found in the database, otherwise a 204 is again returned along with a time to wait before making another request
### IDM Service
This service is responsible for handling user information and authentication. There are endpoints for registering a new user, logging in, and user session management. Sessions are each given a unique ID that times out if a user is not active within a time window and expires automatically after a set period of time (all configured in IDM config.yaml). Session IDs are critical for authenticating requests in other servies.
### Inventory Service
This service is responsible for serving requests for movie, genre, and actor data. The search endpoints can include a multitude of query paremeters to use for fetching movies/stars from the mySQL database. There are also privileges admin endpoints for adding and removing stars/movies/genres from the database. All endpoints are required to have a valid Session ID and email in their request, which is validated by the IDM service.
### Billing Service
This service is responsible for handling shopping cart data, customer billing information, and checking out. Users can add movies with to their shopping carts, update item quantities, create or change billing info, and checkout, which uses PayPal's API to redirect to the PayPal Sandbox where a user would actually submit their payment (since this is a demo, there is no actual pyament processing). All endpoints are required to have a valid Session ID and email in their request, which is validated by the IDM service.


## Front-End
The front-end features some basic functionality to test the endpoints in each of the four microservices. The UI is also simple and minimalistic. Some UI features were required to be included from the Databases course that this project originated from.

### Browse By Title
The Browse By Title page allows users to select a letter (or non-alphabetic character signified by "#" icon) from the vertically aligned section on the left of the screen. This will filter so that only movies started with the specified character are shown. There is also some basic sorting and limiting features that are available on all search/browse pages, along with pagination for looking through lower ranked results.
![title](https://raw.githubusercontent.com/joshlopez97/MovieNight/master/images/browsetitle.png)

### Browse By Genre
The Browse By Genre allows users to use a simple drop-down of known genres in our database to filter movies. 
![genre](https://raw.githubusercontent.com/joshlopez97/MovieNight/master/images/browsegenre.png)

### Movie Details
The Movie Details popup is available on any of the search/browse pages by clicking on a movie result. In this popup, a user read further details about the selected movie, rate the movie 1-5 stars, add a movie to their shopping cart, or click one of the genre buttons to switch to the Browse by Genre page with that genre selected.
![details](https://raw.githubusercontent.com/joshlopez97/MovieNight/master/images/details.png)

### Shopping Cart
The Shopping Cart page allows users to see all the movies that they've added to their shopping cart and eventually checkout. Users can also remove items or update item quantities on this page.
![cart](https://raw.githubusercontent.com/joshlopez97/MovieNight/master/images/cart.png)

### Order History
The Order History page allows users to review all the past orders they've made on MovieNight. The price breakdown of each transaction is also shown here.
![history](https://raw.githubusercontent.com/joshlopez97/MovieNight/master/images/orderhistory.png)

### Basic Search
Basic searching allows user to find movies by title, partial matching of titles is supported.
![basic](https://raw.githubusercontent.com/joshlopez97/MovieNight/master/images/basicsearch.png)

### Advanced Search
Advanced searching of movies enables users to search for content based on title, directory, year released, and/or genre.
![advanced](https://raw.githubusercontent.com/joshlopez97/MovieNight/master/images/advancedsearch.png)

## Live Demo
The site is running under the domain https://movienight.dev on a shared server for demonstration purposes only.

