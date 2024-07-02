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
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    fun loadPosts() {
        Log.d("FeedViewModel", "Loading posts")
        viewModelScope.launch {
            val response = postRepository.getPosts()
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("FeedViewModel", "Response body: $responseBody")
                _posts.value = responseBody ?: emptyList()
                Log.d("FeedViewModel", "Posts loaded: ${_posts.value.size}")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("FeedViewModel", "Error loading posts: $errorBody")
            }
        }
    }
}