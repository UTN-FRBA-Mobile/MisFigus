package com.misfigus

object UserRepository {
    private val users = mutableListOf<User>()

    fun register(user: User): Boolean {
        if (users.any { it.email == user.email }) return false // ya existe
        users.add(user)
        return true
    }

    fun login(email: String, password: String): Boolean {
        return users.any { it.email == email && it.password == password }
    }

    fun getUsers(): List<User> = users
}
