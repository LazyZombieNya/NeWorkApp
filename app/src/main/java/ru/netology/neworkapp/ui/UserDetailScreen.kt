package ru.netology.neworkapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import ru.netology.neworkapp.viewmodel.UserDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserDetailScreen(
    userId: String?,
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {
    val userDetail by userDetailViewModel.userDetail.collectAsState()

    LaunchedEffect(userId) {
        userId?.let { userDetailViewModel.loadUserDetail(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Detail") }
            )
        },
        content = {
            userDetail?.let { user ->
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
                    //Text(text = user.job ?: "In search of a job", style = MaterialTheme.typography.bodySmall)
                }
            } ?: run {
                Text("No user data available.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}
