package ru.netology.neworkapp.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.netology.neworkapp.BuildConfig
import ru.netology.neworkapp.data.AuthResponse
import ru.netology.neworkapp.network.RetrofitClient
import ru.netology.neworkapp.data.User
import ru.netology.neworkapp.network.ApiService
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
){
    suspend fun login(username: String, password: String): Response<AuthResponse> {
        return apiService.loginUser(username, password, BuildConfig.API_KEY)
    }

    suspend fun register(login: String, password: String, name: String, file: File): Response<AuthResponse> {
        val loginBody = RequestBody.create("text/plain".toMediaTypeOrNull(), login)
        val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val fileBody = file.asRequestBody("image/png".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)

        return apiService.registerUser(loginBody, passwordBody, nameBody, filePart, BuildConfig.API_KEY)
    }

    suspend fun getUser(): Response<User> {
        return RetrofitClient.instance.getUser()
    }
}