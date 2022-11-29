package com.example.wafhfbackend.entities

import javax.persistence.*

@Entity
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recipeId: Long? = null,
    var name: String,
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    val author: User?,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_ingredients", joinColumns = [JoinColumn(name = "recipeId")])
    var ingredients: MutableSet<String>,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_steps", joinColumns = [JoinColumn(name = "recipeId")])
    var steps: MutableSet<String>,
) {
    override fun toString(): String {
        return "Recipe[$recipeId]: $name, ingredients: $ingredients, steps: $steps"
    }
}