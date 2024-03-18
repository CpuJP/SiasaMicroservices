FROM amazoncorretto:17-alpine
COPY target/siasa-gateway-2.0-STABLE.jar java-app.jar
ENTRYPOINT ["java", "-jar", "java-app.jar"]