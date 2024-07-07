package ru.netology.neworkapp.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.repository.PostRepository
import ru.netology.neworkapp.util.ConvertDateTime.getFormattedCurrentDateTime
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private var token: String? = null
    private var currentUserId: Int = 0

    fun setTokenAndUserId(token: String?, userId: Int) {
        this.token = token
        this.currentUserId = userId
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun createPost(token: String, content: String) {
//        Log.d("CreatePostViewModel", "Creating post with token: $token and content: $content")
//        viewModelScope.launch {
//            _createPostState.value = CreatePostState.Loading
//            val post = Post(
//                author = "", // Здесь вы можете вставить реального автора
//                content = content,
//                published = getFormattedCurrentDateTime()
//            )
//            val response = postRepository.createPost(token, post)
//            if (response.isSuccessful) {
//                _createPostState.value = CreatePostState.Success
//                Log.d("CreatePostViewModel", "Post created successfully")
//            } else {
//                _createPostState.value = CreatePostState.Error
//                Log.e("CreatePostViewModel", "Error creating post: ${response.errorBody()?.string()}")
//            }
//        }
//    }
@RequiresApi(Build.VERSION_CODES.O)
fun createPost(content: String) {
    Log.e("CreatePostViewModel", "token:$token")
    if (token != null) {
        viewModelScope.launch {
            Log.e("CreatePostViewModel", "content: $content, token:$token, authorId$currentUserId")
            val newPost = Post(content = content, author = "Your Author", authorId = currentUserId, published = getFormattedCurrentDateTime())

            postRepository.createPost(token!!, newPost)
        }
    }
}

    @RequiresApi(Build.VERSION_CODES.O)
    fun updatePost(postId: Int, content: String) {
        if (token != null) {
            viewModelScope.launch {
                val updatedPost = Post(id = postId, content = content, author = "Your Author", authorId = currentUserId, published = getFormattedCurrentDateTime())
                postRepository.updatePost(token!!, updatedPost)
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