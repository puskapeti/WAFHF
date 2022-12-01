package com.example.wafhfbackend.form

import com.example.wafhfbackend.entities.Recipe
import com.example.wafhfbackend.entities.User

class CreateRecipeForm {
    var id: Long? = null
    var recipeName: String = ""
    var ingredients: MutableList<String> = mutableListOf()
    var steps: MutableList<String> = mutableListOf()
    var user: User? = null

    override fun toString(): String {
        return "Recipe: $recipeName, ingredients: $ingredients, steps: $steps"
    }

    fun toRecipe(): Recipe {
        return Recipe(recipeId = id, name = recipeName, ingredients = ingredients.toMutableSet(), steps = steps.toMutableSet(), author = user!!)
    }
}