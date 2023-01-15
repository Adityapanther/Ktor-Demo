package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Signup(val name: String, val email: String, val password: String)
@Serializable
data class Login(val email: String, val password: String)
@Serializable
data class User(val id: String, val name: String, val email: String, val passwordHash: String, val issueDate: String, val modifiedDate: String)

val userStorage = mutableListOf<User>()