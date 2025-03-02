FROM openjdk:21-jdk-slim as builder
#Install Maven
RUN apt-get update && apt-get install -y maven

#Set the working directory
WORKDIR /app

#Copy the pom.xml
COPY pom.xml .

# Cache dependencies
RUN mvn dependency:go-offline

#Copy the entire source code
COPY src ./src

#Package the application
RUN mvn clean package -DskipTests


FROM openjdk:21-slim

#Set the working directory inside the container
WORKDIR /app

#Copy the built .jar file from the builder image
COPY --from=builder /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

#Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]