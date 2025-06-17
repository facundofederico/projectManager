# syntax=docker/dockerfile:1

################################################################################
# Stage 1: Resolve and download dependencies
FROM eclipse-temurin:21-jdk-jammy AS deps

WORKDIR /build

# Copy Maven wrapper and settings
COPY mvnw ./
COPY .mvn/ .mvn/

# Copy root pom and all modules
COPY pom.xml ./
COPY cli-adapter/ cli-adapter/
COPY domain/ domain/
COPY infrastructure/ infrastructure/

# Download dependencies (cache Maven local repo)
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

################################################################################
# Stage 2: Build full project
FROM deps AS package

WORKDIR /build

# Build all modules
RUN --mount=type=cache,target=/root/.m2 ./mvnw package -DskipTests

# Move the jar built by cli-adapter module to a consistent location
RUN mkdir -p target && \
    mv cli-adapter/target/$(./mvnw -f cli-adapter/pom.xml help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw -f cli-adapter/pom.xml help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################
# Stage 3: Final minimal runtime image
FROM eclipse-temurin:21-jre-jammy AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

USER appuser

WORKDIR /app

# Copy jar from package stage
COPY --from=package /build/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
