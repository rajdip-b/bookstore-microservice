FROM openjdk:18-jdk-alpine
RUN apk --no-cache add curl

ENV BMS_PROFILE=${BMS_PROFILE}
ENV CONFIG_REPO_PATH=${CONFIG_REPO_PATH}
ENV EUREKA_SERVER_URL=${EUREKA_SERVER_URL}
ENV CONFIG_SERVER_URL=${CONFIG_SERVER_URL}

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B
COPY src src
RUN ./mvnw package -DskipTests
EXPOSE 8765

ENTRYPOINT ["java","-jar","target/api-gateway.jar"]