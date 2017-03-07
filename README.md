# README
Simple Spring Boot app using Spark Framework to create RESTful services.

## Installation

1. Install nodejs and maven
2. `mvn clean package`
3. `java -jar target/app.jar`

## Frontend development
Follow the steps below to run the webpack dev server. The webapp will refresh automatically when changes are made to the source code. All requests to /api will be routed to http://localhost:4567

1. `cd frontend`
2. `npm install`
3. `node_modules/.bin/webpack-dev-server --progress --colors --port 5000`
4. Open http://localhost:5000/webpack-dev-server/
