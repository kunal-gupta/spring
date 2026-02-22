# Runtime-only Docker image for Spring Boot app.
# Concept:
# - Build jar outside Docker (mvn clean package -DskipTests)
# - Keep container small by only copying runnable jar
# Common mistake:
# - Trying to run source code directly in runtime image without packaging jar first.

FROM eclipse-temurin:21-jre

WORKDIR /app

# App uses file DB at ./data/visitor-db, so keep a dedicated folder.
RUN mkdir -p /app/data

# Copy the packaged jar from Maven target folder.
# Ensure you build first so this file exists:
# target/spring-boot-visitor-app-0.0.1-SNAPSHOT.jar
COPY target/spring-boot-visitor-app-0.0.1-SNAPSHOT.jar app.jar

# Default Spring Boot port.
EXPOSE 8080

# Start the application.
ENTRYPOINT ["java", "-jar", "app.jar"]
