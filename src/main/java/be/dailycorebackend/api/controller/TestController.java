package be.dailycorebackend.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {
    @GetMapping("/")
    public String home() { return "Back-End is running"; }
}
