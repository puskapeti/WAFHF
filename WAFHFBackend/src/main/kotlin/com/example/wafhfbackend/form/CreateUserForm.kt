package com.example.wafhfbackend.form

class CreateUserForm {
    var username:String? = null
    var email: String? = null
    var password: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var passwordagain: String? = null
    override fun toString():String{
        return "username: $username, email: $email"
    }
}