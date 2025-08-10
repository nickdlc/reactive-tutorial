package com.nicc.ReactiveTutorial;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Controller
public class MainController {
    @GetMapping("/")
    public Mono<String> handleMain() {
        return Mono.just("home");
    }
}
