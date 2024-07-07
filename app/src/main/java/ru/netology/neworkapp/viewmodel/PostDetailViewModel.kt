package ru.netology.neworkapp.viewmodel

import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.data.User
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import ru.netology.neworkapp.repository.PostRepository
import ru.netology.neworkapp.repository.UserRepository
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> get() = _post.asStateFlow()

    private val _likers = MutableStateFlow<List<User>>(emptyList())
    val likers: StateFlow<List<User>> get() = _likers.asStateFlow()

    private val _mentionedUsers = MutableStateFlow<List<User>>(emptyList())
    val mentionedUsers: StateFlow<List<User>> get() = _mentionedUsers.asStateFlow()

    private var token: String? = null

    fun setToken(token: String?) {
        this.token = token
    }

    fun loadPost(postId: Int) {
        viewModelScope.launch {
            try {
                val loadedPost = postRepository.getPostById(postId)
                _post.value = loadedPost

                if (token != null) {
                    // Load likers and mentioned users
                    _likers.value = userRepository.getUsersByIds(token!!, loadedPost.likeOwnerIds)
                    _mentionedUsers.value = userRepository.getUsersByIds(token!!, loadedPost.mentionIds)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun likePost(postId: Int) {
        if (token != null) {
            viewModelScope.launch {
                try {
                    postRepository.likePost(token!!, postId)
                    loadPost(postId) // Reload post to get updated likes
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}