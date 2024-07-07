package ru.netology.neworkapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.data.Event
import ru.netology.neworkapp.repository.EventRepository
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events.asStateFlow()

    private var token: String? = null
    private var currentUserId: Int = 0

    fun setTokenAndUserId(token: String?, userId: Int) {
        this.token = token
        this.currentUserId = userId
    }

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            try {
                _events.value = eventRepository.getEvents()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    fun likeEvent(eventId: Int) {
        if (token != null) {
            viewModelScope.launch {
                try {
                    eventRepository.likeEvent(token!!, eventId)
                    loadEvents()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun deleteEvent(eventId: Int) {
        if (token != null) {
            viewModelScope.launch {
                try {
                    eventRepository.deleteEvent(token!!, eventId)
                    loadEvents()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

}
