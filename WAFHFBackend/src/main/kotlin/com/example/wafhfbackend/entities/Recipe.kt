package com.example.wafhfbackend.entities

import javax.persistence.*

@Entity
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val recipeId: Long? = null,
    private val name: String,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_ingredients", joinColumns = [JoinColumn(name = "recipeId")])
    val ingredients: Set<String>
) {
    override fun toString(): String {
        return "Recipe[$recipeId]: $name, ingredients: $ingredients"
    }
}