package ru.netology.neworkapp.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Query
import ru.netology.neworkapp.BuildConfig
import ru.netology.neworkapp.data.AuthResponse
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.data.User

interface ApiService {
    @GET("user")
    suspend fun getUser(@Header("Authorization") apiKey: String = BuildConfig.API_KEY): Response<User>

    @FormUrlEncoded
    @POST("api/users/authentication")
    suspend fun loginUser(
        @Field("login") username: String,
        @Field("pass") password: String,
        @Header("Api-Key") apiKey: String = BuildConfig.API_KEY
    ): Response<AuthResponse>

    @Multipart
    @POST("api/users/registration")
    suspend fun registerUser(
        @Part("login") login: RequestBody,
        @Part("pass") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part file: MultipartBody.Part,
        @Header("Api-Key") apiKey: String = BuildConfig.API_KEY
    ): Response<AuthResponse>

    @GET("api/posts")
    suspend fun getPosts(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String
    ): Response<List<Post>>

    @POST("api/posts")
    suspend fun createPost(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Body post: Post
    ): Response<Post>
}