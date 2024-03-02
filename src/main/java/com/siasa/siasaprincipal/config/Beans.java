package com.siasa.siasaprincipal.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Beans {

    //Se crea un Bean para inyeccion en cualquier clase del modelo Mapper
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}