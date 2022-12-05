package com.example.wafhfbackend.entities

import javax.persistence.*

@Entity
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recipeId: Long? = null,

    var name: String,

    @Lob
    var description: String,

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    val author: User?,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_ingredients", joinColumns = [JoinColumn(name = "recipeId")])
    var ingredients: MutableSet<String>,

    @Suppress("JpaAttributeTypeInspection")
    @Lob
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_steps", joinColumns = [JoinColumn(name = "recipeId")])
    var steps: MutableSet<String>,
) {
    override fun toString(): String {
        return "Recipe[$recipeId]: $name, $description | ingredients: $ingredients, steps: $steps"
    }
}