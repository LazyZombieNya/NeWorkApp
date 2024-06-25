package ru.netology.neworkapp.data

data class Post(
    val id: Int,
    val userId: Int,
    val content: String,
    val timestamp: String,
    val likes: Int,
    val comments: Int
)