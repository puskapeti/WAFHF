package com.example.wafhfbackend.repositories

import com.example.wafhfbackend.entities.Recipe
import org.springframework.data.repository.CrudRepository

interface RecipeRepository: CrudRepository<Recipe, Long>