package ru.netology.neworkapp.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import ru.netology.neworkapp.data.AttachmentType
import ru.netology.neworkapp.data.Post
import ru.netology.neworkapp.viewmodel.FeedViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    feedViewModel: FeedViewModel = hiltViewModel(),
    onCreatePost: () -> Unit,
    onProfileClick: () -> Unit
) {
    val posts by feedViewModel.posts.collectAsState()
    val isAuthorized by sharedViewModel.isAuthorized.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (isAuthorized) {
                    onCreatePost()
                } else {
                    navController.navigate("login")
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Create Post")
            }
        }
    ) {
        LazyColumn {
            items(posts) { post ->
                PostCard(
                    post = post,
                    currentUserId = sharedViewModel.currentUserId.value,
                    onPostClick = { navController.navigate("postDetail/${post.id}") },
                    onLikeClick = { feedViewModel.likePost(post.id) },
                    onEditClick = {
                        sharedViewModel.setEditingPost(post)
                        navController.navigate("createPost")
                    },
                    onDeleteClick = { feedViewModel.deletePost(post.id) }
                )
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    currentUserId: Int,
    onPostClick: () -> Unit,
    onLikeClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onPostClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberImagePainter(post.authorAvatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(post.author, fontWeight = FontWeight.Bold)
                    Text(post.published, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* TODO: Implement share functionality */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
                if (post.authorId == currentUserId) {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (post.attachment != null) {
                Image(
                    painter = rememberImagePainter(post.attachment.url),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(post.content)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (post.likedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like"
                    )
                }
                Text(post.likeOwnerIds.size.toString())
            }
        }
    }
}