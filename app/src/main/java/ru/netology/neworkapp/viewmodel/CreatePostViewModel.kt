package ru.netology.neworkapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.repository.PostRepository
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _createPostState = MutableStateFlow<CreatePostState>(CreatePostState.Idle)
    val createPostState: StateFlow<CreatePostState> = _createPostState

    fun createPost(content: String) {
        viewModelScope.launch {
            _createPostState.value = CreatePostState.Loading
            val post = Post(content = content)
            val response = postRepository.createPost(post)
            if (response.isSuccessful) {
                _createPostState.value = CreatePostState.Success
            } else {
                _createPostState.value = CreatePostState.Error
            }
        }
    }

    sealed class CreatePostState {
        object Idle : CreatePostState()
        object Loading : CreatePostState()
        object Success : CreatePostState()
        object Error : CreatePostState()
    }
}