package ru.netology.neworkapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import ru.netology.neworkapp.viewmodel.PostDetailViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PostDetailScreen(
    navController: NavController,
    postId: Int,
    sharedViewModel: SharedViewModel,
    postDetailViewModel: PostDetailViewModel = hiltViewModel()
) {
    val post by postDetailViewModel.post.collectAsState()
    val likers by postDetailViewModel.likers.collectAsState()
    val mentionedUsers by postDetailViewModel.mentionedUsers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement share functionality */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberImagePainter(post?.authorAvatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(post?.author ?: "", fontWeight = FontWeight.Bold)
                    Text(post?.authorJob ?: "В поиске работы", style = MaterialTheme.typography.body2)
                    Text(post?.published ?: "", style = MaterialTheme.typography.body2)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (post?.attachment != null) {
                Image(
                    painter = rememberImagePainter(post!!.attachment!!.url),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(post?.content ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { postDetailViewModel.likePost(post?.id ?: 0) }) {
                    Icon(
                        imageVector = if (post?.likedByMe == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like"
                    )
                }
                Text(post?.likeOwnerIds?.size?.toString() ?: "0")
                Spacer(modifier = Modifier.width(8.dp))
                likers.take(5).forEach { liker ->
                    Image(
                        painter = rememberImagePainter(liker.avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                }
                if (likers.size > 5) {
                    IconButton(onClick = { /* TODO: Navigate to likers list */ }) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "More Likers")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Mentioned")
            mentionedUsers.take(5).forEach { user ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberImagePainter(user.avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(user.name)
                }
            }
            if (mentionedUsers.size > 5) {
                IconButton(onClick = { /* TODO: Navigate to mentioned users list */ }) {
                    Icon(Icons.Default.MoreHoriz, contentDescription = "More Mentioned Users")
                }
            }
            if (post?.coords != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Location")
                // TODO: Add map view with marker
            }
        }
    }
}