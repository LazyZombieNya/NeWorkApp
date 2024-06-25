package ru.netology.neworkapp.repository

import ru.netology.neworkapp.network.RetrofitClient
import ru.netology.neworkapp.data.User

class UserRepository {
    suspend fun login(username: String, password: String) =
        RetrofitClient.instance.createUser(User(0, username, password, null))

    suspend fun getUser() = RetrofitClient.instance.getUser()
}