package ru.netology.neworkapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.data.Event
import ru.netology.neworkapp.repository.EventRepository
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _createEventState = MutableStateFlow<CreateEventState>(CreateEventState.Idle)
    val createEventState: StateFlow<CreateEventState> = _createEventState

    fun createEvent(token: String, content: String, datetime: String, type: String) {
        Log.d("CreateEventViewModel", "Creating event with token: $token, content: $content, datetime: $datetime, type: $type")
        viewModelScope.launch {
            _createEventState.value = CreateEventState.Loading
            val event = Event(
                authorId = 0, // Замените на реальный ID автора
                author = "123", // Замените на реального автора
                content = content,
                published = "2024-07-02T21:20:38.221223539Z", // Замените на текущую дату и время
                datetime = datetime,
                type = type
            )
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