package com.example.wafhfbackend.entities

import javax.persistence.*

@Entity
class User(username:String,email:String,password:String,role:String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    val userId:Long?=null

    @Column(nullable = false, unique = true)
    val username:String

    @Column(nullable = false, unique = true)
    val email: String

    @Column(nullable = false)
    val password: String

    @Column(nullable = false)
    val role:String
    init {
        this.username=username
        this.email = email
        this.password = password
        this.role=role

    }
    //private val firstName: String,
    //private val secondName: String
    override fun toString(): String {
        return /*"firstname: $firstName, second name: $secondName,*/" email: $email, password: $password"
    }
}