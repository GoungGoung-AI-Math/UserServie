FROM openjdk:17-jdk-slim
WORKDIR /app
COPY Math.AI-0.0.1-SNAPSHOT.jar app.jar
COPY key.p12 /app/key.p12
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]
