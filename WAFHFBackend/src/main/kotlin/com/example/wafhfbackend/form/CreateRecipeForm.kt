package com.example.wafhfbackend.form

import com.example.wafhfbackend.entities.Recipe

class CreateRecipeForm {
    var recipeName: String = ""
    val ingredients: MutableList<String> = mutableListOf()
    val steps: MutableList<String> = mutableListOf()

    override fun toString(): String {
        return "Recipe: $recipeName, ingredients: $ingredients, steps: $steps"
    }

    fun toRecipe(): Recipe {
        return Recipe(name = recipeName, ingredients = ingredients.toSet(), steps = steps.toSet())
    }
}