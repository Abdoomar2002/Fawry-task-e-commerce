package com.example.springwebapi.controller;

import com.example.springwebapi.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DemoController {

    private final DemoService demoService;

    @GetMapping("/run")
    public String runDemo() {
        demoService.runDemo();
        return "Demo completed. Check console for output.";
    }

    @PostMapping("/run")
    public String runDemoPost() {
        demoService.runDemo();
        return "Demo completed. Check console for output.";
    }

    @GetMapping("/test")
    public String runSpecificTest() {
        demoService.runSpecificTest();
        return "Specific test completed. Check console for output.";
    }
} 