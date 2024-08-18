package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
// Valida
// denyAll por defecto niego todos
@PreAuthorize("denyAll()")
public class TestAuthController {

    // EndPoints
    @GetMapping("/hello")
    @PreAuthorize("permitAll()")
    public String hello(){
        return "Hello World";
    }


    @GetMapping("/hello-secured")
    @PreAuthorize("hasAnyAuthority('READ')")
    public String helloSecured(){
        return "Hello World Secured";
    }
}
