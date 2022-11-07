package com.example.wafhfbackend

import com.example.wafhfbackend.entities.CookPlan
import com.example.wafhfbackend.entities.Recipe
import com.example.wafhfbackend.repositories.CookPlanRepository
import com.example.wafhfbackend.repositories.RecipeRepository
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.event.annotation.BeforeTestExecution

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class BackendApplicationTests {

    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @Autowired
    lateinit var cookPlanRepository: CookPlanRepository

    var recipe = Recipe(
        name = "A Very Nice Recipe",
        ingredients = setOf("first ingredient", "second ingredient", "third ingredient")
    )

    @BeforeTestExecution
    fun initDB() {
        cookPlanRepository.deleteAll()
        recipeRepository.deleteAll()
    }

    @Test
    @Order(1)
    fun testRecipeCRUD() {

        recipeRepository.save(recipe)

        println(recipeRepository.findAll())
    }

    @Test
    @Order(2)
    fun testCookPlanCRUD() {

        val cookPlan = CookPlan(recipe = recipe, steps = setOf("first step", "second step", "final step"))

        cookPlanRepository.save(cookPlan)

        println(cookPlanRepository.findAll())
    }


}
