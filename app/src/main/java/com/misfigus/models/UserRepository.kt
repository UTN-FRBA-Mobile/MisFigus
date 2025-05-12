package com.misfigus.models

object UserRepository {
    private val users = mutableListOf<User>()

    // Usuario actualmente logueado
    private var currentUser: User? = null

    fun register(user: User): Boolean {
        if (users.any { it.email == user.email }) return false // ya existe
        users.add(user)
        currentUser = user
        return true
    }

    fun login(email: String, password: String): Boolean {
        val user = users.find { it.email == email && it.password == password }
        currentUser = user
        return user != null
    }

    fun getCurrentUser(): User? = currentUser

    fun logout() {
        currentUser = null
    }

    fun updateNameAndUsername(newName: String, newUsername: String) {
        val updatedUser = currentUser?.copy(fullName = newName, username = newUsername) ?: return
        users.replaceAll { if (it.email == updatedUser.email) updatedUser else it }
        currentUser = updatedUser
    }

    fun getUsers(): List<User> = users
}
