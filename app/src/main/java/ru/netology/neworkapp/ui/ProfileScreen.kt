package ru.netology.neworkapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import ru.netology.neworkapp.data.User
import ru.netology.neworkapp.viewmodel.ProfileViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profile by profileViewModel.profile.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        },
        content = {
            profile?.let { user ->
                if (isEditing) {
                    EditProfileScreen(user = user, onSave = {
                        profileViewModel.updateProfile(it)
                        isEditing = false
                    })
                } else {
                    DisplayProfileScreen(user = user)
                }
            } ?: run {
                Text("No profile data available.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}

@Composable
fun DisplayProfileScreen(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user.avatar != null) {
            Image(
                painter = rememberImagePainter(user.avatar),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        } else {
            Icon(Icons.Default.Person, contentDescription = "Avatar", modifier = Modifier.size(100.dp))
        }
        Text(text = user.name, style = MaterialTheme.typography.titleMedium)
        Text(text = user.login, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun EditProfileScreen(user: User, onSave: (User) -> Unit) {
    var name by remember { mutableStateOf(user.name) }
    var login by remember { mutableStateOf(user.login) }
    var avatar by remember { mutableStateOf(user.avatar ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        BasicTextField(
            value = login,
            onValueChange = { login = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        BasicTextField(
            value = avatar,
            onValueChange = { avatar = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onSave(User(user.id, login, name, if (avatar.isEmpty()) null else avatar))
        }) {
            Text("Save")
        }
    }
}