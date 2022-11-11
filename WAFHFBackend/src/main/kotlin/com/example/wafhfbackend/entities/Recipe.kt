package com.example.wafhfbackend.entities

import javax.persistence.*

@Entity
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recipeId: Long? = null,
    val name: String,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_ingredients", joinColumns = [JoinColumn(name = "recipeId")])
    val ingredients: Set<String>,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_steps", joinColumns = [JoinColumn(name = "recipeId")])
    val steps: Set<String>,
) {
    override fun toString(): String {
        return "Recipe[$recipeId]: $name, ingredients: $ingredients, steps: $steps"
    }
}