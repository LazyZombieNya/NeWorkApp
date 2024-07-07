package ru.netology.neworkapp.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import ru.netology.neworkapp.BuildConfig
import ru.netology.neworkapp.data.AuthResponse
import ru.netology.neworkapp.data.Event
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.data.User

interface ApiService {

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

//    @GET("api/posts")
//    suspend fun getPosts(
//        @Header("Api-Key") apiKey: String = BuildConfig.API_KEY
//    ): Response<List<Post>>


    @POST("api/posts")
    suspend fun createPost(
        @Header("Api-Key") apiKey: String = BuildConfig.API_KEY,
        @Header("Authorization") token: String,
        @Body post: Post
    ): Response<Post>

    @PUT("api/posts/{id}")
    suspend fun updatePost(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Path("id") postId: Int,
        @Body post: Post
    ): Post

    @GET("api/posts")
    suspend fun getPosts(): List<Post>

    @GET("api/posts/{id}")
    suspend fun getPostById(@Path("id") postId: Int): Post

    @POST("api/posts/{id}/likes")
    suspend fun likePost(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    )

    @DELETE("api/posts/{id}")
    suspend fun deletePost(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Path("id") postId: Int
    )

    //Event

    @POST("events")
    suspend fun createEvent(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Body event: Event
    ): Response<Event>

    @PUT("events/{id}")
    suspend fun updateEvent(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Path("id") eventId: Long,
        @Body event: Event
    ): Response<Event>

    @POST("events/{id}/likes")
    suspend fun likeEvent(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Path("id") eventId: Int
    )

    @DELETE("events/{id}")
    suspend fun deleteEvent(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Path("id") eventId: Int
    )

    @GET("events")
    suspend fun getEvents(
        @Header("Api-Key") apiKey: String
    ): List<Event>

    @GET("events/{id}")
    suspend fun getEventById(
        @Header("Api-Key") apiKey: String,
        @Path("id") eventId: Int
    ): Event


    //User
    @GET("api/users")
    suspend fun getUsers(
        @Header("Api-Key") apiKey: String
    ): Response<List<User>>

    @GET("api/users/{id}")
    suspend fun getUserDetail(
        @Header("Api-Key") apiKey: String,
        @Path("id") userId: String
    ): Response<User>

    @POST("api/users/ids")
    suspend fun getUsersByIds(
        @Header("Api-Key") apiKey: String,
        @Header("Authorization") token: String,
        @Body ids: List<Int>
    ): List<User>

    @GET("api/profile")
    suspend fun getProfile(
        @Header("Api-Key") apiKey: String
    ): Response<User>

    @PUT("api/profile")
    suspend fun updateProfile(
        @Header("Api-Key") apiKey: String,
        @Body user: User
    ): Response<User>
}