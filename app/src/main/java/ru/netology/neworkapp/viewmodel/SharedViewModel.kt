package ru.netology.neworkapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.netology.neworkapp.data.Post
import javax.inject.Inject

class SharedViewModel : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token.asStateFlow()

    private val _currentUserId = MutableStateFlow(0)
    val currentUserId: StateFlow<Int> get() = _currentUserId.asStateFlow()

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> get() = _isAuthorized.asStateFlow()

    private val _showTopBar = MutableStateFlow(true)
    val showTopBar: StateFlow<Boolean> get() = _showTopBar.asStateFlow()

    private val _showBottomBar = MutableStateFlow(true)
    val showBottomBar: StateFlow<Boolean> get() = _showBottomBar.asStateFlow()

    private val _editingPost = MutableStateFlow<Post?>(null)
    val editingPost: StateFlow<Post?> get() = _editingPost.asStateFlow()

    fun setToken(token: String) {
        _token.value = token
        _isAuthorized.value = token.isNotEmpty()
    }

    fun setCurrentUserId(userId: Int) {
        _currentUserId.value = userId
    }

    fun setShowTopBar(show: Boolean) {
        _showTopBar.value = show
    }

    fun setShowBottomBar(show: Boolean) {
        _showBottomBar.value = show
    }

    fun setEditingPost(post: Post?) {
        _editingPost.value = post
    }
}