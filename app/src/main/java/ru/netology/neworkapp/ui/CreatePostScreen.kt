package ru.netology.neworkapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.netology.neworkapp.viewmodel.CreatePostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    createPostViewModel: CreatePostViewModel = viewModel(),
    onPostCreated: () -> Unit
) {
    var content by remember { mutableStateOf("") }
    val postState by createPostViewModel.postState.collectAsState()

    LaunchedEffect(postState) {
        if (postState == CreatePostViewModel.PostState.SUCCESS) {
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
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { createPostViewModel.createPost(content) }) {
            Text("Post")
        }
        if (postState == CreatePostViewModel.PostState.LOADING) {
            CircularProgressIndicator()
        }
    }
}