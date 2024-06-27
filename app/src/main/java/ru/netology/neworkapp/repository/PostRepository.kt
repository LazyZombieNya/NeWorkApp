package ru.netology.neworkapp.repository

import retrofit2.Response
import ru.netology.neworkapp.network.RetrofitClient
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.network.ApiService
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPosts(token: String): Response<List<Post>> {
        return apiService.getPosts("Bearer $token")
    }

    suspend fun createPost(content: String): Response<Post> {
        val post = Post(0, userId = 1, content = content, timestamp = "", likes = 0, comments = 0)
        return RetrofitClient.instance.createPost(post)
    }
}