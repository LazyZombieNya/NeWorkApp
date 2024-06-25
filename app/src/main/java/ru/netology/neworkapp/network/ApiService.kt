package ru.netology.neworkapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Body
import ru.netology.neworkapp.BuildConfig
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.data.User

interface ApiService {
    @GET("posts")
    suspend fun getPosts(@Header("Authorization") apiKey: String = BuildConfig.API_KEY): Response<List<Post>>

    @GET("user")
    suspend fun getUser(@Header("Authorization") apiKey: String = BuildConfig.API_KEY): Response<User>

    @POST("users")
    suspend fun createUser(@Body user: User, @Header("Authorization") apiKey: String = BuildConfig.API_KEY): Response<User>

    @POST("posts")
    suspend fun createPost(@Body post: Post, @Header("Authorization") apiKey: String = BuildConfig.API_KEY): Response<Post>
}