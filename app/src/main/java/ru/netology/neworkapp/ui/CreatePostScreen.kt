package ru.netology.neworkapp.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.netology.neworkapp.viewmodel.CreatePostViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    sharedViewModel: SharedViewModel,
    createPostViewModel: CreatePostViewModel = hiltViewModel(),
    onPostCreated: () -> Unit
) {
    val token by sharedViewModel.token.collectAsState()
    var content by remember { mutableStateOf("") }
    val createPostState by createPostViewModel.createPostState.collectAsState()

    LaunchedEffect(createPostState) {
        if (createPostState is CreatePostViewModel.CreatePostState.Success) {
            onPostCreated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Log.d("CreatePostScreen", "Initializing CreatePostScreen")
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            token?.let {
                Log.d("CreatePostScreen", "Creating post with token: $it and content: $content")
                createPostViewModel.createPost(it, content)
            } ?: run {
                Log.d("CreatePostScreen", "Token is null")
            }
        }) {
            Text("Create Post")
        }
        if (createPostState is CreatePostViewModel.CreatePostState.Loading) {
            CircularProgressIndicator()
        }
        if (createPostState is CreatePostViewModel.CreatePostState.Error) {
            Text("Failed to create post. Please try again.", color = MaterialTheme.colorScheme.error)
        }
    }
}