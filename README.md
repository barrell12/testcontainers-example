# PET SERVICE 😎🐈🐕

## What
Example Spring application containing an example of a Testcontainers IT test (PetControllerIT).

Stores the names and ages of pets.

## How to run application standalone
> Requires a local redis server, hence the docker command

docker run --name some-redis -p 6379:6379 -d redis && mvn spring-boot:run

