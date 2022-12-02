package com.example.wafhfbackend.repositories

import com.example.wafhfbackend.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository:CrudRepository<User,Long> {

    fun findByUsername(username:String): User
    fun existsByUsername(username: String):Boolean

}