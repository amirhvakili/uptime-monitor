# Stage 1: compile the Scala code into one runnable jar
FROM virtuslab/scala-cli:latest AS build
WORKDIR /build
COPY src ./src
RUN scala-cli --power package src --assembly --main-class Main -o monitor.jar

# Stage 2: small image that only runs the jar
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/monitor.jar .
CMD ["java", "-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8", "-jar", "monitor.jar"]