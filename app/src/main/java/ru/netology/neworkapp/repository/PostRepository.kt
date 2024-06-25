package ru.netology.neworkapp.repository

import retrofit2.Response
import ru.netology.neworkapp.network.RetrofitClient
import ru.netology.neworkapp.data.Post

class PostRepository {
    suspend fun getPosts() = RetrofitClient.instance.getPosts()

    suspend fun createPost(content: String): Response<Post> {
        val post = Post(0, userId = 1, content = content, timestamp = "", likes = 0, comments = 0)
        return RetrofitClient.instance.createPost(post)
    }
}