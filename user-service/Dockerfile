FROM openjdk:22 as builder
LABEL authors="Marcos"

WORKDIR /app/user-service
COPY ./pom.xml /app
COPY ./user-service/.mvn ./.mvn
COPY ./user-service/mvnw .
COPY ./user-service/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
COPY ./user-service/src ./src/

RUN ./mvnw clean package -DskipTests

FROM openjdk:22

WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/user-service/target/user-service-0.0.1-SNAPSHOT.jar .
ENV PORT 8001
EXPOSE $PORT
CMD ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar"]