FROM openjdk:17-jdk-slim

COPY ./notification/build/libs/notification.jar /app.jar

EXPOSE 8080

CMD ["java", "-Xmx2048m", "-jar", "/app.jar", "--server.port=8081"]