package ru.netology.neworkapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    init {
        Log.d("SharedViewModel", "SharedViewModel created")
    }

    fun setToken(newToken: String?) {
        _token.value = newToken
        Log.d("SharedViewModel", "Token set: $newToken")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SharedViewModel", "SharedViewModel cleared")
    }
}