package com.gymapp.ms_rutinas.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced // Permite que RestTemplate entienda los nombres de Eureka (ej: http://ms-miembros)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

