package ru.netology.neworkapp.repository

import retrofit2.Response
import ru.netology.neworkapp.BuildConfig
import ru.netology.neworkapp.network.RetrofitClient
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.network.ApiService
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPosts(): Response<List<Post>> {
        return apiService.getPosts()
    }

    suspend fun createPost(token: String, post: Post): Response<Post> {
        return apiService.createPost(apiKey = BuildConfig.API_KEY, token = "$token", post = post)
    }
}