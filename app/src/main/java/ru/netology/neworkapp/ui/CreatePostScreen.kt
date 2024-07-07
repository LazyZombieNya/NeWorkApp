package ru.netology.neworkapp.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Photo
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.netology.neworkapp.viewmodel.CreatePostViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    onPostCreated: () -> Unit,
    createPostViewModel: CreatePostViewModel = hiltViewModel()
) {
    val editingPost by sharedViewModel.editingPost.collectAsState()
    var content by remember { mutableStateOf(TextFieldValue(editingPost?.content ?: "")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (editingPost != null) {
                            createPostViewModel.updatePost(editingPost!!.id, content.text)
                        } else {
                            createPostViewModel.createPost(content.text)
                        }
                        sharedViewModel.setEditingPost(null)
                        onPostCreated()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 10,
                textStyle = LocalTextStyle.current.copy(lineHeight = 20.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            BottomActionBar()
        }
    }
}

@Composable
fun BottomActionBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionButton(icon = Icons.Default.CameraAlt, contentDescription = "Camera")
        ActionButton(icon = Icons.Default.Photo, contentDescription = "Gallery")
        ActionButton(icon = Icons.Default.People, contentDescription = "Mention")
        ActionButton(icon = Icons.Default.LocationOn, contentDescription = "Location")
        ActionButton(icon = Icons.Default.Add, contentDescription = "Add")
    }
}

@Composable
fun ActionButton(icon: ImageVector, contentDescription: String, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = contentDescription)
    }
}