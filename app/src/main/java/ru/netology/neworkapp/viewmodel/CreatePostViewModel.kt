package ru.netology.neworkapp.viewmodel

import android.util.Log
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

    fun createPost(token: String, content: String) {
        Log.d("CreatePostViewModel", "Creating post with token: $token and content: $content")
        viewModelScope.launch {
            _createPostState.value = CreatePostState.Loading
            val post = Post(
                author = "", // Здесь вы можете вставить реального автора
                content = content,
                published = "2024-07-02T21:20:38.221223539Z" // Или используйте текущую дату и время
            )
            val response = postRepository.createPost(token, post)
            if (response.isSuccessful) {
                _createPostState.value = CreatePostState.Success
                Log.d("CreatePostViewModel", "Post created successfully")
            } else {
                _createPostState.value = CreatePostState.Error
                Log.e("CreatePostViewModel", "Error creating post: ${response.errorBody()?.string()}")
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