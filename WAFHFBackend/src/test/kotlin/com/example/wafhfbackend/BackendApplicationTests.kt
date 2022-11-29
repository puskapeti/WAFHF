package com.example.wafhfbackend

import com.example.wafhfbackend.entities.Recipe
import com.example.wafhfbackend.entities.User
import com.example.wafhfbackend.repositories.RecipeRepository
import com.example.wafhfbackend.repositories.UserRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.annotation.AfterTestClass
import org.springframework.test.context.event.annotation.BeforeTestExecution

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class BackendApplicationTests {

    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    var user: User? = null
    var recipes: Iterable<Recipe>? = null

    @AfterTestClass
    fun cleanUp() {
        if (recipes != null) {
            recipeRepository.deleteAll(recipes!!)
        }
        if (user != null) {
            userRepository.delete(user!!)
        }
    }

    @Test
    fun testCRUD() {
        createUser()
        createRecipe()
        readUser()
        readRecipe()
        updateRecipes()
        deleteRecipe()
        deleteUser()
    }

    fun createUser() {
        user = userRepository.save(
            User(
                username = "example",
                email = "test@test.com",
                password = "testing",
                role = "testRole"
            )
        )
    }

    fun createRecipe() {
        recipeRepository.save(
            Recipe(
                name = "testRecipe",
                ingredients = mutableSetOf("testIng1", "testIng2"),
                steps = mutableSetOf("step1", "step2"),
                author = user!!
            )
        )
    }

    fun readUser() {
        user = userRepository.findByUsername(user!!.username)
        println(user)
    }

    fun readRecipe() {
        recipes = recipeRepository.findAllByAuthor(user!!)
        println(recipes)
    }

    fun updateRecipes() {
        check(recipes != null)

        recipes!!.forEach { recipe ->
            recipe.ingredients.add("additional step")
        }

        recipeRepository.saveAll(recipes!!)
    }

    fun deleteUser() {
        userRepository.delete(user!!)
    }

    fun deleteRecipe() {
        recipeRepository.deleteAll(recipes!!)
    }
}
