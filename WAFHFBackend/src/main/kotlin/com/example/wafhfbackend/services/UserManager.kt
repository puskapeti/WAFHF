package com.example.wafhfbackend.services

import com.example.wafhfbackend.entities.User
import com.example.wafhfbackend.form.CreateUserForm
import com.example.wafhfbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserManager(@Autowired val userRepository: UserRepository, @Autowired val encoder:PasswordEncoder) {

    fun addUser(userForm: CreateUserForm):User?{
        if(userForm.username != null &&
                userForm.email!=null &&
                userForm.password!=null) {
            return userRepository.save(
                User(
                    username = userForm.username!!,
                    email = userForm.email!!,
                    password = encoder.encode(userForm.password!!),
                    role = "ROLE_USER",
                )
            )
        }
        return null
    }

    fun findUserByUsername(name: String): User {
        return userRepository.findByUsername(name)
    }

}