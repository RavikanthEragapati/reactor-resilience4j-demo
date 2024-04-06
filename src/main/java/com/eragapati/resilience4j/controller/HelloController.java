package com.eragapati.resilience4j.controller;

import com.eragapati.resilience4j.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/api/test/{input}")
    public ResponseEntity<Mono<String>> sayHello(@PathVariable("input") String input) {
        return ResponseEntity.ok(helloService.buildHelloMessage(input));
    }
}
