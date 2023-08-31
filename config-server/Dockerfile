FROM openjdk:17-jdk-slim

COPY ./config-server/build/libs/config-server.jar /app.jar

EXPOSE 8888

CMD ["java", "-Xmx2048m", "-jar", "/app.jar", "--server.port=8888"]