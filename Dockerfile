FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./target/bibliotecaob-1.0.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]
