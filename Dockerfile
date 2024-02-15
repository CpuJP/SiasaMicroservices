FROM amazoncorretto:17-alpine
COPY target/siasa-eureka-1.0-BETA.jar java-app.jar
ENTRYPOINT ["java", "-jar", "java-app.jar"]