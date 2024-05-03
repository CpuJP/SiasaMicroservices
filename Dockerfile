FROM amazoncorretto:17-alpine
COPY target/siasa-gateway-2.2-STABLE.jar java-app.jar
ENTRYPOINT ["java", "-jar", "java-app.jar"]