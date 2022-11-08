package com.example.wafhfbackend.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class BaseController {
    @GetMapping("/")
    fun index(): String {
        return "login"
    }
}