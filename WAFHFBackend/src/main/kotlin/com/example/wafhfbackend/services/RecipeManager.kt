package com.example.wafhfbackend.services

import com.example.wafhfbackend.entities.Recipe
import com.example.wafhfbackend.entities.User
import com.example.wafhfbackend.repositories.RecipeRepository
import com.example.wafhfbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RecipeManager(@Autowired val recipeRepository: RecipeRepository, @Autowired val userRepository: UserRepository) {

    fun getAllRecipes(): Iterable<Recipe> {
        return recipeRepository.findAll()
    }

    fun getRecipesOfUser(username: String): Iterable<Recipe> {
        val user = userRepository.findByUsername(username)
        return recipeRepository.findAllByAuthor(user)
    }

    fun getRecipeById(id: Long): Recipe? {
        val recipe = recipeRepository.findById(id)
        return if (recipe.isPresent) recipe.get() else null
    }

    fun saveRecipe(recipe: Recipe): Recipe {
        return recipeRepository.save(recipe)
    }

    fun updateRecipe(recipe: Recipe, recipeId: Long, user: User): Recipe {
        val recipeFromDb = recipeRepository.findById(recipeId).get()

        if (user != recipeFromDb.author) return recipeFromDb

        println("Recipe to be updated: $recipeFromDb")

        if (recipe.name != recipeFromDb.name) {
            println("updating name")
            recipeFromDb.name = recipe.name
        }

        if (recipe.ingredients != recipeFromDb.ingredients) {
            println("updating ingredients")
            recipeFromDb.ingredients = recipe.ingredients
        }

        if (recipe.steps != recipeFromDb.steps) {
            println("updating steps")
            recipeFromDb.steps = recipe.steps
        }

        println("Updated recipe: $recipeFromDb")

        return recipeRepository.save(recipeFromDb)
    }

    fun deleteRecipe(id: Long) {
        recipeRepository.deleteById(id)
    }

    fun deleteRecipe(recipe: Recipe) {
        recipeRepository.delete(recipe)
    }
}