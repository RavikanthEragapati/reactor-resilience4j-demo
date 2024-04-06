package com.eragapati.resilience4j.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelloService {

    private final ReactiveCircuitBreaker r2dbcCircuitBreaker;

    public Mono<String> buildHelloMessage(String test) {
        return r2dbcCircuitBreaker.run(
                Mono.defer(() -> this.methodA(test)),
                throwable -> methodAFallback(test, throwable)
        );
    }

    private Mono<String> methodA(String test) {
        log.info("inside methodA");
        if ("fail".equalsIgnoreCase(test))
            return Mono.error(new RuntimeException("MethodA failed"));
        else
            return Mono.just("methodA success");
    }

    private Mono<String> methodAFallback(String test, Throwable throwable) {
        log.info("inside methodAFallback");
        return Mono.just("Fallback response: " + throwable.getMessage());
    }

}
