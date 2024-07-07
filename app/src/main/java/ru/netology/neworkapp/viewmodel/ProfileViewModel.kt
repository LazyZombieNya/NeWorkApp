package ru.netology.neworkapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.data.User
import ru.netology.neworkapp.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<User?>(null)
    val profile: StateFlow<User?> = _profile

    fun loadProfile() {
        viewModelScope.launch {
            val response = userRepository.getProfile()
            if (response.isSuccessful) {
                _profile.value = response.body()
            } else {
                Log.e("ProfileViewModel", "Error loading profile: ${response.errorBody()?.string()}")
            }
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            val response = userRepository.updateProfile(user)
            if (response.isSuccessful) {
                _profile.value = response.body()
            } else {
                Log.e("ProfileViewModel", "Error updating profile: ${response.errorBody()?.string()}")
            }
        }
    }
}