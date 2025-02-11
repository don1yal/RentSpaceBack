package com.rentspace.gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route.path("/rentspace/users/**")
                        .filters(filter -> filter.rewritePath("/rentspace/users/(?<segment>.*)", "/${segment}"))
                        .uri("lb://USERSERVICE"))
                .build();
    }
}