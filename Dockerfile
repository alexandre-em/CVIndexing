FROM openjdk:8-jdk-alpine
COPY . .
RUN ./mvnw clean package
EXPOSE 8080
CMD ["java", "-jar", "./target/cv-0.0.1-SNAPSHOT.jar"]