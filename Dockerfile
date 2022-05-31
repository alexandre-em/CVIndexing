FROM maven:3.5-jdk-8 as maven

WORKDIR /api

# copy the Project Object Model file
COPY ./pom.xml ./pom.xml

# fetch all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for release
RUN mvn clean package -DskipTests && cp target/cv*.jar app.jar


# smaller, final base image
FROM openjdk:8u171-jre-alpine
RUN apk --no-cache add curl

# Copy dependencies so the thin jar won't need to re-download them
COPY --from=maven /root/.m2 /root/.m2

# set deployment directory
WORKDIR /api

# copy over the built artifact from the maven image
COPY --from=maven /api/app.jar ./app.jar

EXPOSE 8080
# set the startup command to run your binary
CMD ["java", "-jar", "/api/app.jar"]

