package ru.netology.neworkapp.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.data.Event
import ru.netology.neworkapp.repository.EventRepository
import ru.netology.neworkapp.util.ConvertDateTime.getFormattedCurrentDateTime
import ru.netology.neworkapp.util.ConvertDateTime.getFormattedISODateTime
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private var token: String? = null
    private var currentUserId: Int = 0

    fun setTokenAndUserId(token: String?, userId: Int) {
        this.token = token
        this.currentUserId = userId
    }

    private val _createEventState = MutableStateFlow<CreateEventState>(CreateEventState.Idle)
    val createEventState: StateFlow<CreateEventState> = _createEventState

    @RequiresApi(Build.VERSION_CODES.O)
    fun createEvent(token: String, content: String, datetime: String, type: String) {
        Log.d("CreateEventViewModel", "Creating event with token: $token, content: $content, datetime: $datetime, type: $type")
        viewModelScope.launch {
            _createEventState.value = CreateEventState.Loading
            val event = Event(
                authorId = 0, // Замените на реальный ID автора
                author = "", // Замените на реального автора
                content = content,
                published = getFormattedCurrentDateTime(), // Текущая дата и время
                datetime = getFormattedISODateTime(datetime),
                type = type
            )
            Log.d("CreateEventViewModel", "Event:$event")
            val response = eventRepository.createEvent(token, event)
            if (response.isSuccessful) {
                _createEventState.value = CreateEventState.Success
                Log.d("CreateEventViewModel", "Event created successfully")
            } else {
                _createEventState.value = CreateEventState.Error
                Log.e("CreateEventViewModel", "Error creating event: ${response.errorBody()?.string()}")
            }
        }
    }

    sealed class CreateEventState {
        object Idle : CreateEventState()
        object Loading : CreateEventState()
        object Success : CreateEventState()
        object Error : CreateEventState()
    }
}