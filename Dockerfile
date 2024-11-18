FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/MyProducts-0.1.jar MyProducts-0.1.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "MyProducts-0.1.jar"]