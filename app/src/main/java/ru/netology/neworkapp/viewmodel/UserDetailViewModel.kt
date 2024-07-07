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
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userDetail = MutableStateFlow<User?>(null)
    val userDetail: StateFlow<User?> = _userDetail

    fun loadUserDetail(userId: String) {
        viewModelScope.launch {
            val response = userRepository.getUserDetail(userId)
            if (response.isSuccessful) {
                _userDetail.value = response.body()
            } else {
                Log.e("UserDetailViewModel", "Error loading user detail: ${response.errorBody()?.string()}")
            }
        }
    }
}
