FROM openjdk:8-jdk-alpine
LABEL maintainer="alexandre.em@pm.me"

COPY . .
RUN apk --no-cache add curl
EXPOSE 8080
