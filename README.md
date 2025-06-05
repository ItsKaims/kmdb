# Movies API

A Spring Boot application for managing movies, actors, and genres with RESTful API endpoints.

## Testing Server

The application is deployed on a testing server at:

```
http://188.166.163.195:8080/
```

You can test both the frontend and API functionality using this server.

## API Endpoints

### Movies

- `GET /api/movies` - Get all movies with optional filters (genre, year, actor)
- `GET /api/movies/{id}` - Get movie by ID
- `POST /api/movies` - Create new movie with associated genres and actors
- `PATCH /api/movies/{id}` - Update movie details
- `DELETE /api/movies/{id}` - Delete movie
- `GET /api/movies/search` - Search movies by title (case-insensitive, partial matches)
- `GET /api/movies/{id}/actors` - Get actors in a movie
- `PATCH /api/movies/{id}/actors` - Update movie's actors

### Actors

- `GET /api/actors` - Get all actors
- `GET /api/actors/{id}` - Get actor by ID
- `POST /api/actors` - Create new actor
- `PATCH /api/actors/{id}` - Update actor details
- `DELETE /api/actors/{id}` - Delete actor
- `GET /api/actors/{id}/movies` - Get movies of an actor

### Genres

- `GET /api/genres` - Get all genres
- `GET /api/genres/{id}` - Get genre by ID
- `POST /api/genres` - Create new genre
- `PATCH /api/genres/{id}` - Update genre details
- `DELETE /api/genres/{id}` - Delete genre

## Features

- RESTful API design
- Pagination support across all endpoints
- Case-insensitive and partial match search
- Input validation and error handling
- Proper HTTP status codes

## API Testing Tools

### Postman Collection
A Postman collection file `Movies_API.json` is included in the project root directory. This collection contains all the API endpoints with example requests and responses, making it easy to test the API endpoints.

### Swagger UI
The API documentation is available through Swagger UI at:
```
http://188.166.163.195:8080/swagger-ui/index.html
```
Replace `IP` with your server's IP address or localhost if running locally. This provides interactive API documentation with the ability to test endpoints directly from the browser.
- Transaction management
- Many-to-Many relationships between movies, actors, and genres
- API documentation via Springdoc OpenAPI

## Requirements

- Java 17+
- Maven
- SQLite database

## Running the Application

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. Access the API documentation at:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Testing

The application is deployed on a testing server at `http://188.166.163.195:8080/` where you can test both the frontend and API functionality.

## Error Handling

The API returns appropriate HTTP status codes:
- 200 OK - Successful request
- 201 Created - Resource created
- 400 Bad Request - Invalid input
- 404 Not Found - Resource not found
- 500 Internal Server Error - Server error

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
