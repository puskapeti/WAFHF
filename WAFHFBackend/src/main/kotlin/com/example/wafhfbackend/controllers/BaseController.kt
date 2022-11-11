package com.example.wafhfbackend.controllers

import com.example.wafhfbackend.form.CreateRecipeForm
import com.example.wafhfbackend.form.CreateUserForm
import com.example.wafhfbackend.repositories.RecipeRepository
import com.example.wafhfbackend.services.UserManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Controller
class BaseController(@Autowired private val userManager: UserManager, @Autowired private val recipeRepository: RecipeRepository) {
    @GetMapping("/")
    fun showIndex(model: Model): String {
        return "index"
    }

    @GetMapping("/home")
    fun showHome(model: Model, httpServletRequest: HttpServletRequest): String {
        println(httpServletRequest.remoteUser)
        return "index"
    }

    @GetMapping("/admin")
    fun showAdmin(model: Model): String {
        return "adminpage"
    }

    @GetMapping("/login")
    fun loginUser(model: Model): String {
        return "login"
    }

    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("createUserForm", CreateUserForm())
        return "register"
    }

    @PostMapping("/registerUser")
    fun registerUser(createUserForm: CreateUserForm, model: Model): String {
        userManager.addUser(createUserForm)
        println(createUserForm.toString())
        return "registerSuccessful"
    }

    @GetMapping("/recipes")
    fun getRecipes(model: Model): String {
        println(recipeRepository.findAll())
        model.addAttribute("recipes", recipeRepository.findAll())
        return "recipes"
    }

    @GetMapping("/recipes/create")
    fun createRecipe(model: Model): String {
        model.addAttribute("createRecipeForm", CreateRecipeForm())
        return "createRecipe"
    }

    @PostMapping(
        "/recipes/create",

    )
    fun createRecipe(
        model: Model,
        createRecipeForm: CreateRecipeForm? = null,
        @RequestParam(name = "addIngredient", required = false) addIngredient: String? = null,
        @RequestParam(name = "removeIngredient", required = false) removeIngredient: String? = null,
        @RequestParam(name = "addStep", required = false) addStep: String? = null,
        @RequestParam(name = "removeStep", required = false) removeStep: String? = null,
    ): String {
        if (createRecipeForm == null) {
            model.addAttribute("createRecipeForm", CreateRecipeForm())
            return "createRecipe"
        }

        addIngredient?.let {
            createRecipeForm.ingredients.add("")
            return "createRecipe"
        }

        removeIngredient?.let {
            createRecipeForm.ingredients.removeAt(it.toInt())
            return "createRecipe"
        }

        addStep?.let {
            createRecipeForm.steps.add("")
            return "createRecipe"
        }

        removeStep?.let {
            createRecipeForm.steps.removeAt(it.toInt())
            return "createRecipe"
        }

        recipeRepository.save(createRecipeForm.toRecipe())

        return "redirect:/recipes"
    }

    @GetMapping("/recipes/delete/{id}")
    fun deleteRecipe(@PathVariable id: String): String {
        recipeRepository.deleteById(id.toLong())
        return "redirect:/recipes"
    }

}