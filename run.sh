#!/bin/sh

./mvnw clean package -DskipTests
java -jar ./target/cv-0.0.1-SNAPSHOT.jar
