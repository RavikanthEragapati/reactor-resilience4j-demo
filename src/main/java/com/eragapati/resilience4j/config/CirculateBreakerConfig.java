package com.eragapati.resilience4j.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CirculateBreakerConfig {

    private CircuitBreaker.State previousState = CircuitBreaker.State.CLOSED;

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.addCircuitBreakerCustomizer(circuitBreaker -> circuitBreaker
                .getEventPublisher()
                .onStateTransition(event -> {
                    CircuitBreaker.State currentState = event.getStateTransition().getToState();
                    if (currentState != previousState) {
                        log.info("CircuitBreaker state changed to {}", currentState);
                        previousState = currentState;
                    }
                }), "my-circuit-breaker");
    }

    @Bean
    public ReactiveCircuitBreaker r2dbcCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory) {
        return circuitBreakerFactory.create("my-circuit-breaker");
    }
}
