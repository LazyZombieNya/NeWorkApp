package ru.netology.neworkapp.repository

import android.util.Log
import retrofit2.Response
import ru.netology.neworkapp.BuildConfig
import ru.netology.neworkapp.network.RetrofitClient
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPosts(): Response<List<Post>> {
        return apiService.getPosts()
    }

//    suspend fun getPosts(): Response<List<Post>> {
//        Log.d("PostRepository", "Sending request with API key: ${BuildConfig.API_KEY}")
//        return apiService.getPosts(BuildConfig.API_KEY)
//    }

    suspend fun createPost(token: String, post: Post): Response<Post> {
        Log.d("PostRepository", "Sending request with token: Bearer $token and API key: ${BuildConfig.API_KEY}")
        return apiService.createPost(apiKey = BuildConfig.API_KEY, token = "$token", post = post)
    }
}