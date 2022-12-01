package com.example.wafhfbackend.services

import com.example.wafhfbackend.entities.User
import com.example.wafhfbackend.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsProvider(
        @Autowired private val userRepository: UserRepository
):UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if(username == null) throw UsernameNotFoundException("")
        val currentUser: User = userRepository.findByUsername(username)
        return org.springframework.security.core.userdetails.User(username,currentUser.password,
                true,true,true,true, AuthorityUtils.createAuthorityList(currentUser.role))


    }


}