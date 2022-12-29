# Overview

This Project exposes 1 endpoint "/country/get" that accepts a list of country codes as input and returns a list of country codes that are in the same continent as the country code.

The API is rate limited based on user authentication using [Bucket4j](https://bucket4j.com/) - a Java rate-limiting library that is mainly based on the token-bucket algorithm. If the user is authenticated, rate limit is **20** requests per second. Otherwise, the rate limit is set to **5** requests per second

## Running the Application

### Run as Spring Boot
(pre-requisite: IDE is installed, preferably [Eclipse](https://spring.io/tools))
- Right-click the root folder
- click Run-as
- click Spring Boot App

### Deploy as Docker
(pre-requisite: [Docker](https://www.docker.com/products/docker-desktop/) is installed)
- Make sure that the Docker service is enabled
- open your favorite terminal and navigate to the root folder where the docker-compose.yml and Dockerfile are located
- enter this command:
```bash
docker compose up
```
(the docker multi-stage build is configured for this application)
- If you make changes to the file, you need to run this command
```bash
 docker-compose up -d --no-deps --build
```
(this will ensure that the application will be rebuilt so that the changes will take effect)

## API Definition
### Request
```
path:
  /country/get:
    get:
      summary: Finds Pets by tags
      description: Multiple country codes can be provided with comma separated strings. 
      parameters:
        - name: countries
          in: query
          description: Tags to filter by
          required: true
          schema:
            type: array
            items:
              type: string
```
### Response
```
responses:
        '200':
          description: successful operation
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/CountryResponseDto'          
        '400':
          description: request parameter is missing
        '401':
          description: bad user credentials
        '429':
          description: exceeded request limit
components:
  schemas:
    CountryResponseDto:
      type: object
      properties:
        continents:
          type: array
          items:
            $ref: '#/components/schemas/CountryItemResponseDto'  
    CountryItemResponseDto:
      type: object
      properties:
        countries: 
          type: array
          items:
            type: string
          example: ["US"]
        name:
          type: string
          example: "North America"
        otherCountries:
          type: array
          items:
            type: string
          example: ["CA", ...]
```

## Usage
### Postman
(pre-requisite: [Postman](https://www.postman.com/downloads/) is installed)
1. set Method to GET
2. set URL to <host>/country/get (e.g. http://localhost:8080/country/get)
3. add "countries" to Query Params
   - You can directly add to the URL (e.g http://localhost:8080/country/get?countries=US,CA,...)
   - Navigate to Params tab. add "country" as KEY and set country codes as "VALUES"
       * Note: Values are comma-delimited
4. Authentication Options for Rate Limiting:
   - Anonymous - Navigate to Authorization Tab and set type to "No Auth"
   - Authenticated - Navigate to Authorization Tab and set type to "Basic Auth" and use the following credentials:
      * Username: user
      * Password: user
5. Press Send

### Curl
1. Anonymous User
```bash
curl http://localhost:8080/country/get?countries=US
```
2. Authenticated User
```bash
curl -u 'user:user' http://localhost:8080/country/get?countries=US
```

## Rate Limit Option
You can modify the rate limit by modifying the rate limit properties in application.yml
```
rate-limit:
  authenticated: 20
  anonymous: 5
  duration: 1
```
- authenticated - request per duration for an authenticated user
- anonymous - request per duration for a non-authenticated user
- duration(in sec) - the period within tokens will be fully regenerated
