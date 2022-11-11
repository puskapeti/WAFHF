package com.example.wafhfbackend.controllers

import com.example.wafhfbackend.form.CreateUserForm
import com.example.wafhfbackend.services.UserManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.HttpServletRequest

@Controller
class BaseController(@Autowired private val userManager: UserManager) {
    @GetMapping("/")
    fun showIndex(model:Model): String {
        return "index"
    }

    @GetMapping("/home")
    fun showHome(model:Model, httpServletRequest: HttpServletRequest):String{
        println(httpServletRequest.remoteUser)
        return "index"
    }

    @GetMapping("/admin")
    fun showAdmin(model:Model):String{
        return "adminpage"
    }

    @GetMapping("/login")
    fun loginUser(model:Model):String{
        return "login"
    }
    @GetMapping("/register")
    fun registerPage(model:Model):String{
        model.addAttribute("createUserForm",CreateUserForm())
        return "register"
    }
    @PostMapping("/registerUser")
    fun registerUser(createUserForm: CreateUserForm,model:Model):String{
        userManager.addUser(createUserForm)
        println(createUserForm.toString())
        return "registerSuccessful"
    }

}