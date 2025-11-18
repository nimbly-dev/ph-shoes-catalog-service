########################################
# Build stage
########################################
FROM maven:3-amazoncorretto-21 AS builder
WORKDIR /app

COPY pom.xml .
COPY ph-shoes-catalog-service-core/pom.xml ph-shoes-catalog-service-core/pom.xml
COPY ph-shoes-catalog-service-web/pom.xml ph-shoes-catalog-service-web/pom.xml
RUN mvn dependency:go-offline -B

COPY ph-shoes-catalog-service-core ph-shoes-catalog-service-core
COPY ph-shoes-catalog-service-web ph-shoes-catalog-service-web
COPY docs docs
RUN mvn -pl ph-shoes-catalog-service-web -am clean package -DskipTests

########################################
# Runtime stage
########################################
FROM amazoncorretto:21-alpine
WORKDIR /app

COPY --from=builder /app/ph-shoes-catalog-service-web/target/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="--add-opens=java.base/java.nio=ALL-UNNAMED -Dsnowflake.jdbc.enableArrow=false"
EXPOSE 8083
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar app.jar --spring.profiles.active=prod"]
