FROM maven:3.9.8-eclipse-temurin-22 AS builder
ENV APP_DIR=/opt/app
WORKDIR ${APP_DIR}
COPY pom.xml .
RUN mvn verify --fail-never
COPY src/ ./src
RUN mvn package -DskipTests=true

FROM bellsoft/liberica-openjdk-alpine AS prod
ENV APP_DIR=/opt/app
LABEL author="osho"
WORKDIR ${APP_DIR}
COPY --from=builder /opt/app/target/*.jar /opt/app/manager.jar
EXPOSE 8080
ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java", "-jar", "manager.jar"]