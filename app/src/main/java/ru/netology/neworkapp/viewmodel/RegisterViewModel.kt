package ru.netology.neworkapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.repository.UserRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(login: String, password: String, name: String, file: File) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val response = userRepository.register(login, password, name, file)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    _registerState.value = RegisterState.Success(authResponse.token)
                } else {
                    _registerState.value = RegisterState.Error
                }
            } else {
                _registerState.value = RegisterState.Error
            }
        }
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        data class Success(val token: String) : RegisterState()
        object Error : RegisterState()
    }
}
