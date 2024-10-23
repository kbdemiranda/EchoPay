FROM maven:3.9.5-amazoncorretto-21 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.5
WORKDIR /app
COPY --from=build /app/target/echopay-0.0.1-SNAPSHOT.jar /app/echopay.jar

EXPOSE 8080

LABEL authors="kbdemiranda"

ENTRYPOINT ["java", "-jar", "/app/echopay.jar"]
