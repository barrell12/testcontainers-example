# PET SERVICE 😎🐈🐕

## What
Example Spring application containing an example of a Testcontainers IT test (PetControllerIT).

Stores the names and ages of pets.

## How to run application standalone
> Requires a local MySQL server, hence the docker command

1. `docker run -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 -d mysql:8.0.31`

Wait for it to start, and then:

2. `mvn spring-boot:run`

