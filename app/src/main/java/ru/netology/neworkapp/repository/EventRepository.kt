package ru.netology.neworkapp.repository

import android.util.Log
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
    suspend fun getEvents(): List<Event> {
        return apiService.getEvents(apiKey = BuildConfig.API_KEY)
    }
//    suspend fun getEvents(): List<Event> {
//        return apiService.getEvents()
//    }

    suspend fun updateEvent(token: String, event: Event): Response<Event> {
        return apiService.updateEvent(apiKey = BuildConfig.API_KEY, token = "$token", eventId = event.id, event = event)
    }

    suspend fun likeEvent(token: String, eventId: Int) {
        apiService.likeEvent(apiKey = BuildConfig.API_KEY, token = "$token", eventId = eventId)
    }

    suspend fun deleteEvent(token: String, eventId: Int) {
        apiService.deleteEvent(apiKey = BuildConfig.API_KEY, token = "$token", eventId = eventId)
    }

    suspend fun getEventById(eventId: Int): Event {
        return apiService.getEventById(apiKey = BuildConfig.API_KEY, eventId = eventId)
    }
    suspend fun createEvent(token: String, event: Event): Response<Event> {
        Log.d("EvenRepository", "Sending request with token:  $token and API key: ${BuildConfig.API_KEY}")
        return apiService.createEvent(apiKey = BuildConfig.API_KEY, token = "$token", event = event)
    }
}