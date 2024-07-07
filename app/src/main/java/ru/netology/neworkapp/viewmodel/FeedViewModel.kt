package ru.netology.neworkapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.repository.PostRepository
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts.asStateFlow()

    private var token: String? = null
    private var currentUserId: Int = 0

    fun setTokenAndUserId(token: String?, userId: Int) {
        this.token = token
        this.currentUserId = userId
    }

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            try {
                val response = postRepository.getPosts()
                _posts.value = response
                Log.d("FeedViewModel", "Posts loaded successfully: ${response.size}")
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Error loading posts", e)
            }
        }
    }

    fun likePost(postId: Int) {
        if (token != null) {
            viewModelScope.launch {
                try {
                    postRepository.likePost(token!!, postId)
                    loadPosts()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun deletePost(postId: Int) {
        if (token != null) {
            viewModelScope.launch {
                try {
                    postRepository.deletePost(token!!, postId)
                    loadPosts()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}