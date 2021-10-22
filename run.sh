#!/bin/sh

./mvnw clean package
java -jar ./target/cv-0.0.1-SNAPSHOT.jar
