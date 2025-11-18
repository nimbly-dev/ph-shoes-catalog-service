# ph-shoes-catalog-service

Repository for the Shoe Catalog Microservice inside the PH Shoes Project.

## Module layout

- **ph-shoes-catalog-service-core**: business logic, JPA repositories, entities, and the custom Snowflake dialect.
- **ph-shoes-catalog-service-web**: REST API that implements the OpenAPI contract, exposes Swagger UI, and depends on the core jar.
- **docs/openapi**: OpenAPI definition plus reusable component schemas that drive the generated API interfaces.
- **DockerfileDev / Dockerfile**: development and production container recipes matching the legacy backend patterns.
- **docker-compose.yml**: dev compose file that launches the web module on port 8083 with Maven live reload.

## Local development

1. Build everything once (required before running the dev container):
   ```bash
   mvn -pl ph-shoes-catalog-service-web -am clean verify
   ```
2. Create an `.env` file (you can copy `.env.example`) and fill in the Snowflake credentials required by the JPA datasource.
3. Start the dev stack with detached mode:
   ```bash
   docker compose up --build -d
   ```
4. The service will be available on [http://localhost:8083/api/v1](http://localhost:8083/api/v1) and exposes Swagger UI at `http://localhost:8083/api/v1/swagger-ui/index.html#/`.

Use `docker compose down` to stop the service when finished.
