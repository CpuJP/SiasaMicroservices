package com.siasa.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class GatewayConfig {

    @Bean
    @Profile("dev")
    public RouteLocator configGatewayDev(RouteLocatorBuilder builder) {
        return builder.routes()
                //SIASA-PRINCIPAL
                .route(predicateSpec -> predicateSpec.path("/codigou/**")
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/rfid/**")
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/salacomputo/**")
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/biblioteca/**")
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/campus/**")
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/laboratorio/**")
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/actuator/**")
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/principal/**")
                        .uri("lb://siasa-principal-dev"))

                //SIASA-PRESTAMOS
                .route(predicateSpec -> predicateSpec.path("/materialdeportivo/**")
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/prestamoaudiovisual/**")
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/audiovisual/**")
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/prestamomaterialdeportivo/**")
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/actuator/**")
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/prestamos/**")
                        .uri("lb://siasa-prestamos-dev"))

                //SIASA-REPORTES
                .route(predicateSpec -> predicateSpec.path("/report/**")
                        .uri("lb://siasa-reportes-dev"))
                .route(predicateSpec -> predicateSpec.path("/actuator/**")
                        .uri("lb://siasa-reportes-dev"))
                .route(predicateSpec -> predicateSpec.path("/reportes/**")
                        .uri("lb://siasa-reportes-dev"))
                .build();
    }


    @Bean
    @Profile("prod")
    public RouteLocator configGatewayProd(RouteLocatorBuilder builder) {
        return builder.routes()
                //SIASA-PRINCIPAL
                .route(predicateSpec -> predicateSpec.path("/codigou/**")
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/rfid/**")
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/salacomputo/**")
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/biblioteca/**")
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/campus/**")
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/laboratorio/**")
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/actuator/**")
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/principal/**")
                        .uri("lb://siasa-principal-prod"))

                //SIASA-PRESTAMOS
                .route(predicateSpec -> predicateSpec.path("/materialdeportivo/**")
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/prestamoaudiovisual/**")
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/audiovisual/**")
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/prestamomaterialdeportivo/**")
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/actuator/**")
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/prestamos/**")
                        .uri("lb://siasa-prestamos-prod"))

                //SIASA-REPORTES
                .route(predicateSpec -> predicateSpec.path("/report/**")
                        .uri("lb://siasa-reportes-prod"))
                .route(predicateSpec -> predicateSpec.path("/actuator/**")
                        .uri("lb://siasa-reportes-prod"))
                .route(predicateSpec -> predicateSpec.path("/reportes/**")
                        .uri("lb://siasa-reportes-prod"))
                .build();
    }
}
