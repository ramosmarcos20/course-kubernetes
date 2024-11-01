FROM openjdk:22 as builder
LABEL authors="Marcos"

WORKDIR /app/course-service
COPY ./pom.xml /app
COPY ./course-service/.mvn ./.mvn
COPY ./course-service/mvnw .
COPY ./course-service/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
COPY ./course-service/src ./src/

RUN ./mvnw clean package -DskipTests

FROM openjdk:22
WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/course-service/target/course-service-0.0.1-SNAPSHOT.jar .
EXPOSE 8002
CMD ["java", "-jar", "course-service-0.0.1-SNAPSHOT.jar"]