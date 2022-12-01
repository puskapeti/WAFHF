package com.example.wafhfbackend.repositories

import com.example.wafhfbackend.entities.Recipe
import com.example.wafhfbackend.entities.User
import org.springframework.data.repository.CrudRepository

interface RecipeRepository: CrudRepository<Recipe, Long> {
    fun findAllByAuthor(author: User): Iterable<Recipe>
}