# Stage 1 – Build
FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle installDist --no-daemon

# Stage 2 - Run
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=builder /home/gradle/project/build/install/cm-challenge/ ./
RUN chmod +x ./bin/cm-challenge
ENTRYPOINT ["./bin/cm-challenge"]
