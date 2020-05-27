FROM maven:3.6.3-openjdk-11-slim as build
WORKDIR /myapp
COPY pom.xml pom.xml
COPY src src
COPY conf conf
RUN mvn package

FROM azul/zulu-openjdk:11
WORKDIR /myapp
COPY --from=build /myapp/target/myapp-1.0.0.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
