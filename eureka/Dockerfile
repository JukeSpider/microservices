FROM openjdk:17-jdk-slim

COPY ./eureka/build/libs/eureka.jar /app.jar

EXPOSE 8080

CMD ["java", "-Xmx2048m", "-jar", "/app.jar", "--server.port=8761"]