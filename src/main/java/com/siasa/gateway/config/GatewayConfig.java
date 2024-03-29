package com.siasa.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class GatewayConfig {

    private final AuthFilter authFilter;

    public GatewayConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    @Profile("dev")
    public RouteLocator configGatewayDev(RouteLocatorBuilder builder ) {
        return builder.routes()
                //SIASA-PRINCIPAL

                //AUTH
                .route(predicateSpec -> predicateSpec.path("/auth/**")
                        .uri("lb://siasa-auth-dev"))
                .route(predicateSpec -> predicateSpec.path("/admin/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-auth-dev"))

                //CODIGOU
                .route(predicateSpec -> predicateSpec.path("/codigou")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover")
                                            .setRouteId("dbFailoverCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/codigou/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUPageFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/page")
                                            .setRouteId("dbFailoverCodigoUPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/codigou/{id}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUIdFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/%7Bid%7D")
                                            .setRouteId("dbFailoverCodigoUId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/codigou/rfid/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUIdRfidFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/rfid/%7BidRfid%7D")
                                            .setRouteId("dbFailoverCodigoUIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/codigou/update/{id}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUUpdateIdFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/update/%7Bid%7D")
                                            .setRouteId("dbFailoverCodigoUUpdateId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                //RFID
                .route(predicateSpec -> predicateSpec.path("/rfid")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover")
                                            .setRouteId("dbFailoverRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/rfid/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidPageFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover/page")
                                            .setRouteId("dbFailoverRfidPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/rfid/without")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidWithOutFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover/without")
                                            .setRouteId("dbFailoverRfidWithOut"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/rfid/{id}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidIdFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover/%7Bid%7D")
                                            .setRouteId("dbFailoverRfidId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                //SALACOMPUTO
                .route(predicateSpec -> predicateSpec.path("/salacomputo")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover")
                                            .setRouteId("dbFailoverSalaComputo"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoPageFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/page")
                                            .setRouteId("dbFailoverSalaComputoPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoCodigoUIdFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoverSalaComputoCodigoUId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoExistsCodigoUIdFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoverSalaComputoExistsCodigoUId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoIdRfidFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoverSalaComputoIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/out/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoOutIdRfidFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/out/%7BidRfid%7D")
                                            .setRouteId("dbFailoverSalaComputoOutIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/fechaingreso")
                                            .setRouteId("dbFailoverSalaComputoFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/fechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/fechasalida")
                                            .setRouteId("dbFailoverSalaComputoFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/idcodigouandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/idcodigouandfechaingreso")
                                            .setRouteId("dbFailoverSalaComputoIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/idcodigouandfechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoIdCodigoUAndFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/idcodigouandfechasalida")
                                            .setRouteId("dbFailoverSalaComputoIdCodigoUAndFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                //BIBLIOTECA
                .route(predicateSpec -> predicateSpec.path("/biblioteca")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover")
                                            .setRouteId("dbFailoveBiblioteca"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaPageFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/page")
                                            .setRouteId("dbFailoveBibliotecaPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveBibliotecaIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaExistsIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveBibliotecaExistsIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaIdRfidFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoveBibliotecaIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/fechaingreso")
                                            .setRouteId("dbFailoveBibliotecaFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/idcodigouandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/idcodigouandfechaingreso")
                                            .setRouteId("dbFailoveBibliotecaIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                //CAMPUS
                .route(predicateSpec -> predicateSpec.path("/campus")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusFailoverCB")
                                            .setFallbackUri("forward:/campus-failover")
                                            .setRouteId("dbFailoveCampus"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/campus/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusPageFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/page")
                                            .setRouteId("dbFailoveCampusPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/campus/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveCampusIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/campus/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusExistsIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveCampusExistsIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/campus/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusIdRfidFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoveCampusIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/campus/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/fechaingreso")
                                            .setRouteId("dbFailoveCampusFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/campus/idcodigouandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/idcodigouandfechaingreso")
                                            .setRouteId("dbFailoveCampusIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                //LABORATORIO
                .route(predicateSpec -> predicateSpec.path("/laboratorio")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover")
                                            .setRouteId("dbFailoveLaboratorio"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioPageFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/page")
                                            .setRouteId("dbFailoveLaboratorioPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveLaboratorioIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioExistsIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveLaboratorioExistsIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdRfidFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoveLaboratorioIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/out/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioOutIdRfidFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/out/%7BidRfid%7D")
                                            .setRouteId("dbFailoveLaboratorioOutIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/fechaingreso")
                                            .setRouteId("dbFailoveLaboratorioFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/fechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/fechasalida")
                                            .setRouteId("dbFailoveLaboratorioFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/idcodigoandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/idcodigoandfechaingreso")
                                            .setRouteId("dbFailoveLaboratorioIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/idcodigouandfechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdCodigoUAndFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/idcodigouandfechasalida")
                                            .setRouteId("dbFailoveLaboratorioIdCodigoUAndFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                //SWAGGER
                .route(predicateSpec -> predicateSpec.path("/principal/**")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("principalFailoverCB")
                                            .setFallbackUri("forward:/principal-failover/swagger-ui/index.html")
                                            .setRouteId("dbFailovePrincipal"));
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))
                .route(predicateSpec -> predicateSpec.path("/principal/v3")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("principalV3FailoverCB")
                                            .setFallbackUri("forward:/principal-failover/v3")
                                            .setRouteId("dbFailoverPrincipalV3"));
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-dev"))

                //SIASA-PRINCIPAL-FAILOVEWR
                .route(predicateSpec -> predicateSpec.path("/codigou-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-dev"))
                .route(predicateSpec -> predicateSpec.path("/rfid-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-dev"))
                .route(predicateSpec -> predicateSpec.path("/salacomputo-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-dev"))
                .route(predicateSpec -> predicateSpec.path("/biblioteca-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-dev"))
                .route(predicateSpec -> predicateSpec.path("/campus-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-dev"))
                .route(predicateSpec -> predicateSpec.path("/laboratorio-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-dev"))
                .route(predicateSpec -> predicateSpec.path("/principal-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-dev"))


                //SIASA-PRESTAMOS
                .route(predicateSpec -> predicateSpec.path("/materialdeportivo/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/prestamoaudiovisual/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/audiovisual/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/prestamomaterialdeportivo/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-dev"))
                .route(predicateSpec -> predicateSpec.path("/prestamos/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-dev"))

                //SIASA-REPORTES
                .route(predicateSpec -> predicateSpec.path("/report/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-reportes-dev"))
                .route(predicateSpec -> predicateSpec.path("/reportes/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-reportes-dev"))
                .build();
    }


    @Bean
    @Profile("prod")
    public RouteLocator configGatewayProd(RouteLocatorBuilder builder) {
        return builder.routes()
                //SIASA-PRINCIPAL

                //AUTH
                .route(predicateSpec -> predicateSpec.path("/auth/**")
                        .uri("lb://siasa-auth-prod"))
                .route(predicateSpec -> predicateSpec.path("/admin/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-auth-prod"))

                //CODIGOU
                .route(predicateSpec -> predicateSpec.path("/codigou")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover")
                                            .setRouteId("dbFailoverCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/codigou/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUPageFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/page")
                                            .setRouteId("dbFailoverCodigoUPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/codigou/{id}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUIdFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/%7Bid%7D")
                                            .setRouteId("dbFailoverCodigoUId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/codigou/rfid/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUIdRfidFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/rfid/%7BidRfid%7D")
                                            .setRouteId("dbFailoverCodigoUIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/codigou/update/{id}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config
                                            .setName("codigoUUpdateIdFailoverCB")
                                            .setFallbackUri("forward:/codigou-failover/update/%7Bid%7D")
                                            .setRouteId("dbFailoverCodigoUUpdateId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                //RFID
                .route(predicateSpec -> predicateSpec.path("/rfid")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover")
                                            .setRouteId("dbFailoverRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/rfid/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidPageFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover/page")
                                            .setRouteId("dbFailoverRfidPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/rfid/without")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidWithOutFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover/without")
                                            .setRouteId("dbFailoverRfidWithOut"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/rfid/{id}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("rfidIdFailoverCB")
                                            .setFallbackUri("forward:/rfid-failover/%7Bid%7D")
                                            .setRouteId("dbFailoverRfidId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                //SALACOMPUTO
                .route(predicateSpec -> predicateSpec.path("/salacomputo")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover")
                                            .setRouteId("dbFailoverSalaComputo"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoPageFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/page")
                                            .setRouteId("dbFailoverSalaComputoPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoCodigoUIdFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoverSalaComputoCodigoUId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoExistsCodigoUIdFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoverSalaComputoExistsCodigoUId"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoIdRfidFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoverSalaComputoIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/out/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoOutIdRfidFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/out/%7BidRfid%7D")
                                            .setRouteId("dbFailoverSalaComputoOutIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/fechaingreso")
                                            .setRouteId("dbFailoverSalaComputoFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/fechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/fechasalida")
                                            .setRouteId("dbFailoverSalaComputoFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/idcodigouandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/idcodigouandfechaingreso")
                                            .setRouteId("dbFailoverSalaComputoIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/salacomputo/idcodigouandfechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("salacomputoIdCodigoUAndFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/salacomputo-failover/idcodigouandfechasalida")
                                            .setRouteId("dbFailoverSalaComputoIdCodigoUAndFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                //BIBLIOTECA
                .route(predicateSpec -> predicateSpec.path("/biblioteca")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover")
                                            .setRouteId("dbFailoveBiblioteca"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaPageFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/page")
                                            .setRouteId("dbFailoveBibliotecaPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveBibliotecaIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaExistsIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveBibliotecaExistsIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaIdRfidFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoveBibliotecaIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/fechaingreso")
                                            .setRouteId("dbFailoveBibliotecaFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/biblioteca/idcodigouandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("bibliotecaIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/biblioteca-failover/idcodigouandfechaingreso")
                                            .setRouteId("dbFailoveBibliotecaIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                //CAMPUS
                .route(predicateSpec -> predicateSpec.path("/campus")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusFailoverCB")
                                            .setFallbackUri("forward:/campus-failover")
                                            .setRouteId("dbFailoveCampus"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/campus/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusPageFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/page")
                                            .setRouteId("dbFailoveCampusPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/campus/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveCampusIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/campus/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusExistsIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveCampusExistsIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/campus/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusIdRfidFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoveCampusIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/campus/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/fechaingreso")
                                            .setRouteId("dbFailoveCampusFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/campus/idcodigouandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("campusIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/campus-failover/idcodigouandfechaingreso")
                                            .setRouteId("dbFailoveCampusIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                //LABORATORIO
                .route(predicateSpec -> predicateSpec.path("/laboratorio")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover")
                                            .setRouteId("dbFailoveLaboratorio"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/page")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioPageFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/page")
                                            .setRouteId("dbFailoveLaboratorioPage"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/codigou/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/codigou/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveLaboratorioIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/exists/{idCodigoU}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioExistsIdCodigoUFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/exists/%7BidCodigoU%7D")
                                            .setRouteId("dbFailoveLaboratorioExistsIdCodigoU"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdRfidFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/%7BidRfid%7D")
                                            .setRouteId("dbFailoveLaboratorioIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/out/{idRfid}")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioOutIdRfidFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/out/%7BidRfid%7D")
                                            .setRouteId("dbFailoveLaboratorioOutIdRfid"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/fechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/fechaingreso")
                                            .setRouteId("dbFailoveLaboratorioFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/fechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/fechasalida")
                                            .setRouteId("dbFailoveLaboratorioFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/idcodigoandfechaingreso")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdCodigoUAndFechaIngresoFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/idcodigoandfechaingreso")
                                            .setRouteId("dbFailoveLaboratorioIdCodigoUAndFechaIngreso"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                .route(predicateSpec -> predicateSpec.path("/laboratorio/idcodigouandfechasalida")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("laboratorioIdCodigoUAndFechaSalidaFailoverCB")
                                            .setFallbackUri("forward:/laboratorio-failover/idcodigouandfechasalida")
                                            .setRouteId("dbFailoveLaboratorioIdCodigoUAndFechaSalida"));
                            gatewayFilterSpec.filters(authFilter);
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                //SWAGGER
                .route(predicateSpec -> predicateSpec.path("/principal/**")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("principalFailoverCB")
                                            .setFallbackUri("forward:/principal-failover/swagger-ui/index.html")
                                            .setRouteId("dbFailovePrincipal"));
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))
                .route(predicateSpec -> predicateSpec.path("/principal/v3")
                        .filters(gatewayFilterSpec -> {
                            gatewayFilterSpec.circuitBreaker
                                    (config -> config.setName("principalV3FailoverCB")
                                            .setFallbackUri("forward:/principal-failover/v3")
                                            .setRouteId("dbFailoverPrincipalV3"));
                            return gatewayFilterSpec;
                        })
                        .uri("lb://siasa-principal-prod"))

                //SIASA-PRINCIPAL-FAILOVEWR
                .route(predicateSpec -> predicateSpec.path("/codigou-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-prod"))
                .route(predicateSpec -> predicateSpec.path("/rfid-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-prod"))
                .route(predicateSpec -> predicateSpec.path("/salacomputo-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-prod"))
                .route(predicateSpec -> predicateSpec.path("/biblioteca-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-prod"))
                .route(predicateSpec -> predicateSpec.path("/campus-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-prod"))
                .route(predicateSpec -> predicateSpec.path("/laboratorio-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-prod"))
                .route(predicateSpec -> predicateSpec.path("/principal-failover/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-principal-failover-prod"))


                //SIASA-PRESTAMOS
                .route(predicateSpec -> predicateSpec.path("/prestamosdoc/**")
                        .uri("lb://siasa-prestamos-prod")
                )
                .route(predicateSpec -> predicateSpec.path("/materialdeportivo/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/prestamoaudiovisual/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/audiovisual/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/prestamomaterialdeportivo/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-prod"))
                .route(predicateSpec -> predicateSpec.path("/prestamos/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-prestamos-prod"))

                //SIASA-REPORTES
                .route(predicateSpec -> predicateSpec.path("/reportesdoc/**")
                        .uri("lb://siasa-reportes-prod")
                )
                .route(predicateSpec -> predicateSpec.path("/report/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-reportes-prod"))
                .route(predicateSpec -> predicateSpec.path("/reportes/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filters(authFilter))
                        .uri("lb://siasa-reportes-prod"))
                .build();
    }
}
