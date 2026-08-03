# ===========================
# Build Stage
# ===========================
FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# ===========================
# Runtime Stage
# ===========================
FROM eclipse-temurin:17-jre

LABEL maintainer="Raj Kumar Dutta"

RUN groupadd spring && \
    useradd -r -g spring spring

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN chown -R spring:spring /app

USER spring

EXPOSE 8083

ENV JAVA_OPTS="-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75 \
-XX:+UseG1GC"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]