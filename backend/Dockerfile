FROM maven:3-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-noble
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY checks ./checks
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
