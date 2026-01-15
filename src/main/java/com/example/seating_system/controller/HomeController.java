package com.example.seating_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping(value = {
            "/dashboard",
            "/seats",
            "/employees"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
