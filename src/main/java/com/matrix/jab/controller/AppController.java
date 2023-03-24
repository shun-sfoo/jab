package com.matrix.jab.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/")
public class AppController {


    @RequestMapping
    public String index(Model model) {
        model.addAttribute("key", LocalDate.now());
        return "index";
    }
}
