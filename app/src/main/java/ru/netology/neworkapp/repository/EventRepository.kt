package ru.netology.neworkapp.repository

import retrofit2.Response
import ru.netology.neworkapp.BuildConfig
import ru.netology.neworkapp.data.Event
import ru.netology.neworkapp.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class EventRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getEvents(): Response<List<Event>> {
        return apiService.getEvents(apiKey = BuildConfig.API_KEY)
    }

    suspend fun createEvent(token: String, event: Event): Response<Event> {
        return apiService.createEvent(apiKey = BuildConfig.API_KEY, token = "$token", event = event)
    }
    // TODO Методы для редактирования и удаления событий
}