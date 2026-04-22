package com.example.apigatewayservice.filters;

import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class FilterConfig {

  @Bean
  RouterFunction<ServerResponse> firstServiceFilter() {
    return GatewayRouterFunctions.route("first-service")
             .GET("/first-service/**", HandlerFunctions.http())
//             .before(BeforeFilterFunctions.uri("http://localhost:8080/"))
             .filter(LoadBalancerFilterFunctions.lb("FIRST-API-SERVICE"))
             .before(CustomFilter.printPreRequestId()) // Custom Filter
             .before(BeforeFilterFunctions.addRequestHeader("f-request", 
                                                            "FirstTestApiServiceHeader"))
             .before(BeforeFilterFunctions.addRequestHeader("f-secret", 
                     										"FirstTestApiServiceHeader"))
             .before(BeforeFilterFunctions.addRequestHeader("first-request", 
                     										"FirstTestApiServiceHeader"))
             .after(CustomFilter.printPostResponseStatusCode()) // Custom Filter
             .after(AfterFilterFunctions.addResponseHeader("f-response", 
                                                           "FirstTestApiServiceResponseHeader"))
             .build();
  }

  @Bean
  RouterFunction<ServerResponse> secondServiceFilter() {
    return GatewayRouterFunctions.route("second-service")
             .GET("/second-service/**", HandlerFunctions.http())
//             .before(BeforeFilterFunctions.uri("http://localhost:8081/"))
             .filter(LoadBalancerFilterFunctions.lb("SECOND-API-SERVICE"))
             .before(CustomFilter.printPreRequestId()) // Custom Filter
             .before(BeforeFilterFunctions.addRequestHeader("second-request", 
                                                            "SecondTestApiServiceHeader"))
             .after(CustomFilter.printPostResponseStatusCode()) // Custom Filter
             .after(AfterFilterFunctions.addResponseHeader("s-response", 
                                                           "SecondTestApiServiceResponseHeader"))
             .build();
  }
}
