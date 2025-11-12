package io.heapdog.core.feature.hello;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/private")
    @PreAuthorize("hasRole('ADMIN')")
    String hello(Authentication authentication) {
        return "Hello, " + authentication.getName() + "!";
    }
}
