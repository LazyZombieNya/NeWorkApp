package ru.netology.neworkapp.ui

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
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.viewmodel.FeedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    feedViewModel: FeedViewModel = viewModel(),
    onCreatePost: () -> Unit,
    onProfileClick: () -> Unit
) {
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
            Text(text = post.content, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Likes: ${post.likes}")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Comments: ${post.comments}")
            }
        }
    }
}
