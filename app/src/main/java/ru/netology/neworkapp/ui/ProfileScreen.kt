package ru.netology.neworkapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.netology.neworkapp.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {
    val user by profileViewModel.user.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Login: ${user?.login ?: "Loading..."}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Username: ${user?.name ?: "Loading..."}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Реализация редактирования профиля */ }) {
                    Text("Edit Profile")
                }
            }
        }
    )
}
