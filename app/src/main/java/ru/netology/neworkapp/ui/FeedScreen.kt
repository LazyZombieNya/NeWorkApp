package ru.netology.neworkapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import ru.netology.neworkapp.data.AttachmentType
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.viewmodel.FeedViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    sharedViewModel: SharedViewModel,
    feedViewModel: FeedViewModel = hiltViewModel(),
    onCreatePost: () -> Unit,
    onProfileClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        feedViewModel.loadPosts()
    }

    val posts by feedViewModel.posts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feed") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePost) {
                Icon(Icons.Default.Add, contentDescription = "Create Post")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(posts) { post ->
                    PostItem(post = post)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    )
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.author, style = MaterialTheme.typography.titleMedium)
            Text(text = post.published, style = MaterialTheme.typography.bodySmall)
            Text(text = post.content, style = MaterialTheme.typography.bodyMedium)
            post.attachment?.let { attachment ->
                when (attachment.type) {
                    AttachmentType.IMAGE -> {
                        Image(
                            painter = rememberImagePainter(attachment.url),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    AttachmentType.AUDIO -> TODO()
                    AttachmentType.VIDEO -> TODO()
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Likes: ${post.likeOwnerIds.size}")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Comments: ${post.mentionIds.size}")
            }
        }
    }
}