# how to run: 

# Build and start all containers 

docker-compose up -d 

# Then go to http://localhost


# to run the tests: 

cd backend/MoliceiroMeals 

# and 

./mvnw test


# view cache

curl http://localhost:8080/api/weather/stats

curl -X POST http://localhost:8080/api/weather/cache/clear



# API Documentation: 

http://localhost:8080/swagger-ui.html

