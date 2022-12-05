package com.example.wafhfbackend.controllers

import com.example.wafhfbackend.entities.User
import com.example.wafhfbackend.form.CreateRecipeForm
import com.example.wafhfbackend.form.CreateUserForm
import com.example.wafhfbackend.services.RecipeManager
import com.example.wafhfbackend.services.UserManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest

@Controller
class BaseController(
    @Autowired private val userManager: UserManager,
    @Autowired private val recipeManager: RecipeManager,
) {

    private fun getUser(httpServletRequest: HttpServletRequest): User? {
        val username = httpServletRequest.remoteUser ?: return null

        return userManager.findUserByUsername(username)
    }

    @GetMapping("/")
    fun showIndex(model: Model, httpServletRequest: HttpServletRequest): String {
        val recipes = recipeManager.getAllRecipes()
        println(recipes)
        val user = getUser(httpServletRequest)
        println(user)
        model.addAttribute("user", user)
        model.addAttribute("recipes", recipes)
        return "main"
    }

    @GetMapping("/admin")
    fun showAdmin(model: Model): String {
        return "admin"
    }

    @GetMapping("/login")
    fun loginUser(model: Model): String {
        return "login"
    }

    @GetMapping("/recipes/{id}")
    fun getRecipeDetail(@PathVariable id: String, model: Model, httpServletRequest: HttpServletRequest): String {
        val recipe = recipeManager.getRecipeById(id.toLong())
        model.addAttribute("recipe", recipe)

        val user = getUser(httpServletRequest)
        model.addAttribute("user", user)

        return "recipeDetail"
    }

    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("createUserForm", CreateUserForm())
        return "register"
    }

    @PostMapping("/register")
    fun registerUser(createUserForm: CreateUserForm, model: Model,ra : RedirectAttributes): String {
        createUserForm.password?.let {
            if(null == createUserForm.passwordagain || createUserForm.passwordagain != it || it ==""){
                ra.addAttribute("errorId",1)
                return "redirect:/register"
            }
        }
        createUserForm.email?.let {
            if(it == ""){
                ra.addAttribute("errorId",2)
                return "redirect:/register"
            }
        }
        createUserForm.username?.let {
            if(userManager.checkUserByUsername(it)){
                ra.addAttribute("errorId",3)
                return "redirect:/register"
            }
        }
        val user = userManager.addUser(createUserForm)
        println("Registered user: $user")
        println(createUserForm.toString())
        return "login"
    }

    @GetMapping("/myRecipes")
    fun getMyRecipes(model: Model, httpServletRequest: HttpServletRequest): String {
        val recipes = recipeManager.getRecipesOfUser(httpServletRequest.remoteUser)
        model.addAttribute("recipes", recipes)

        val user = getUser(httpServletRequest)
        model.addAttribute("user", user)

        return "myRecipes"
    }

    @PostMapping("/recipes/delete/{id}")
    fun deleteRecipe(@PathVariable id: String, httpServletRequest: HttpServletRequest): String {
        val username = httpServletRequest.remoteUser
        val user = userManager.findUserByUsername(username)!!

        val referer = httpServletRequest.getHeader("Referer")

        val recipe = recipeManager.getRecipeById(id.toLong()) ?: return "redirect:$referer"

        if (recipe.author != user) return "redirect:$referer"

        println("Deleting recipe: $recipe, redirecting to: $referer")
        recipeManager.deleteRecipe(recipe)

        return "redirect:$referer"
    }

    @GetMapping("recipes/edit/{id}")
    fun getUpdateRecipeForm(@PathVariable id: String, model: Model, httpServletRequest: HttpServletRequest): String {

        val user = getUser(httpServletRequest)
        val recipe = recipeManager.getRecipeById(id.toLong()) ?: return "main"
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
        @RequestParam(name = "addIngredient", required = false) addIngredient: String? = null,
        @RequestParam(name = "removeIngredient", required = false) removeIngredient: String? = null,
        @RequestParam(name = "addStep", required = false) addStep: String? = null,
        @RequestParam(name = "removeStep", required = false) removeStep: String? = null,
        updateRecipeForm: CreateRecipeForm,
        httpServletRequest: HttpServletRequest,
        model: Model
    ): String {

        val user = userManager.findUserByUsername(httpServletRequest.remoteUser)!!

        addIngredient?.let {
            updateRecipeForm.ingredients.add("")
            model.addAttribute("updateRecipeForm", updateRecipeForm)
            return "editRecipe"
        }

        removeIngredient?.let {
            updateRecipeForm.ingredients.removeAt(it.toInt())
            model.addAttribute("updateRecipeForm", updateRecipeForm)
            return "editRecipe"
        }

        addStep?.let {
            updateRecipeForm.steps.add("")
            model.addAttribute("updateRecipeForm", updateRecipeForm)
            return "editRecipe"
        }

        removeStep?.let {
            updateRecipeForm.steps.removeAt(it.toInt())
            model.addAttribute("updateRecipeForm", updateRecipeForm)
            return "editRecipe"
        }

        updateRecipeForm.user = user
        val recipe = updateRecipeForm.toRecipe()

        recipeManager.updateRecipe(recipe, id.toLong(), user)

        return "redirect:/myRecipes"
    }

    @GetMapping("/recipes/create")
    fun createRecipe(model: Model, httpServletRequest: HttpServletRequest): String {
        val user = getUser(httpServletRequest)

        model.addAttribute("user", user)
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
        val user = userManager.findUserByUsername(username)!!

        createRecipeForm.user = user

        recipeManager.saveRecipe(createRecipeForm.toRecipe())

        return "redirect:/myRecipes"
    }
}