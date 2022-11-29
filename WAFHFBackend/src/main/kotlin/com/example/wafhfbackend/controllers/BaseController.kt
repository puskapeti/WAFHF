package com.example.wafhfbackend.controllers

import com.example.wafhfbackend.form.CreateRecipeForm
import com.example.wafhfbackend.form.CreateUserForm
import com.example.wafhfbackend.services.RecipeManager
import com.example.wafhfbackend.services.UserManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Controller
class BaseController(
    @Autowired private val userManager: UserManager,
    @Autowired private val recipeManager: RecipeManager,
) {
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

    @PostMapping("/register")
    fun registerUser(createUserForm: CreateUserForm, model: Model): String {
        val user = userManager.addUser(createUserForm)
        println("Registered user: $user")
        println(createUserForm.toString())
        return "registerSuccessful"
    }

    @GetMapping("/recipes")
    fun getRecipes(model: Model): String {
        val recipes = recipeManager.getAllRecipes()
        model.addAttribute("recipes", recipes)
        return "recipes"
    }

    @GetMapping("/myRecipes")
    fun getMyRecipes(model: Model, httpServletRequest: HttpServletRequest): String {
        val recipes = recipeManager.getRecipesOfUser(httpServletRequest.remoteUser)
        model.addAttribute("recipes", recipes)

        return "myRecipes"
    }

    @GetMapping("recipes/edit/{id}")
    fun getUpdateRecipeForm(@PathVariable id: String, model: Model, httpServletRequest: HttpServletRequest): String {

        val username = httpServletRequest.remoteUser
        val user = userManager.findUserByUsername(username)

        val recipe = recipeManager.getRecipeById(id.toLong()) ?: return "index"

        if (recipe.author != user) return "login"

        val updateRecipeForm = CreateRecipeForm().also { form ->
            form.id = recipe.recipeId
            form.recipeName = recipe.name
            form.ingredients = recipe.ingredients.toMutableList()
            form.steps = recipe.steps.toMutableList()
            form.user = user
        }

        model.addAttribute("updateRecipeForm", updateRecipeForm)
        model.addAttribute("user", user)

        return "editRecipe"
    }

    @PostMapping("recipes/edit/{id}")
    fun updateRecipe(
        @PathVariable id: String,
        updateRecipeForm: CreateRecipeForm,
        httpServletRequest: HttpServletRequest
    ): String {

        val user = userManager.findUserByUsername(httpServletRequest.remoteUser)

        updateRecipeForm.user = user
        val recipe = updateRecipeForm.toRecipe()

        recipeManager.updateRecipe(recipe, id.toLong(), user)

        return "redirect:/recipes"
    }

    @GetMapping("/recipes/create")
    fun createRecipe(model: Model): String {
        model.addAttribute("createRecipeForm", CreateRecipeForm())
        return "createRecipe"
    }

    @PostMapping("/recipes/create")
    fun createRecipe(
        model: Model,
        createRecipeForm: CreateRecipeForm? = null,
        @RequestParam(name = "addIngredient", required = false) addIngredient: String? = null,
        @RequestParam(name = "removeIngredient", required = false) removeIngredient: String? = null,
        @RequestParam(name = "addStep", required = false) addStep: String? = null,
        @RequestParam(name = "removeStep", required = false) removeStep: String? = null,
        httpServletRequest: HttpServletRequest
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

        // get user
        val username = httpServletRequest.remoteUser
        val user = userManager.findUserByUsername(username)

        createRecipeForm.user = user

        recipeManager.saveRecipe(createRecipeForm.toRecipe())

        return "redirect:/recipes"
    }

    @GetMapping("/recipes/delete/{id}")
    fun deleteRecipe(@PathVariable id: String): String {
        recipeManager.deleteRecipe(id.toLong())
        return "redirect:/recipes"
    }

}