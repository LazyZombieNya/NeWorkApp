package ru.netology.neworkapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.repository.PostRepository
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _postState = MutableStateFlow(PostState.IDLE)
    val postState: StateFlow<PostState> = _postState

    fun createPost(content: String) {
        viewModelScope.launch {
            _postState.value = PostState.LOADING
            val response = postRepository.createPost(content)
            if (response.isSuccessful) {
                _postState.value = PostState.SUCCESS
            } else {
                _postState.value = PostState.ERROR
            }
        }
    }

    enum class PostState {
        IDLE, LOADING, SUCCESS, ERROR
    }
}