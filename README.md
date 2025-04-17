# MoviesAPI

MoviesAPI is a RESTful API for managing movies and their reviews. It allows users to perform CRUD operations on movies and reviews, as well as retrieve movie ratings and reviews by various criteria.

## Features

- **Movies Management**:
  - Add, update, delete, and retrieve movies.
  - Retrieve movies by name, genre, or duration.

- **Reviews Management**:
  - Add, update, delete, and retrieve reviews for movies.
  - Retrieve reviews by email or specific review ID.
  - Get the average rating for a movie.

## Technologies Used

- **Java**: Backend programming language.
- **Spring Boot**: Framework for building the REST API.
- **MySQL**: Database for storing movies and reviews.
- **Docker**: Containerization of the application and database.
- **Maven**: Build and dependency management tool.

## Prerequisites

- **Java 17** or higher
- **Docker** and **Docker Compose**
- **Maven** (optional, if using Maven Wrapper)

## API endpoints
Movies
- GET /movies: Retrieve all movies.
- GET /movies/{movie_id}: Retrieve a movie by ID.
- POST /movies: Add a new movie.
- PUT /movies/{movie_id}: Update a movie.
- DELETE /movies/{movie_id}: Delete a movie.

Reviews
- GET /movies/{movie_id}/reviews: Retrieve reviews for a movie.
- POST /movies/{movie_id}/reviews: Add a review to a movie.
- PUT /movies/{movie_id}/reviews/{review_id}: Update a review.
- DELETE /movies/{movie_id}/reviews/{review_id}: Delete a review.

## Project structure

MoviesAPI/
├── src/
│   ├── main/
│   │   ├── java/com/Movies/
│   │   │   ├── controller/    # REST controllers
│   │   │   ├── model/         # Entity classes
│   │   │   ├── repository/    # JPA repositories
│   │   │   ├── service/       # Business logic
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/        # Static files
│   │       └── templates/     # Templates (if any)
│   └── test/                  # Unit and integration tests
├── Dockerfile                 # Dockerfile for the application
├── [docker-compose.yml](http://_vscodecontentref_/1)         # Docker Compose configuration
├── .env                       # Environment variables
├── .gitignore                 # Git ignore rules
├── mvnw, [mvnw.cmd](http://_vscodecontentref_/2)             # Maven Wrapper scripts
└── [README.md](http://_vscodecontentref_/3)                  # Project documentation