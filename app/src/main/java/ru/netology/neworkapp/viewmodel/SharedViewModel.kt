package ru.netology.neworkapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    //Чтобы скрывать topBar и bottomBar при переходе к другому
    private val _showTopBar = MutableStateFlow(true)
    val showTopBar: StateFlow<Boolean> = _showTopBar.asStateFlow()

    private val _showBottomBar = MutableStateFlow(true)
    val showBottomBar: StateFlow<Boolean> = _showBottomBar.asStateFlow()

    fun setShowTopBar(show: Boolean) {
        _showTopBar.value = show
    }

    fun setShowBottomBar(show: Boolean) {
        _showBottomBar.value = show
    }
}