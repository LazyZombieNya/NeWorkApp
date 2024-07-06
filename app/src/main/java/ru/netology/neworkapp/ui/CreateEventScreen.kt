package ru.netology.neworkapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.netology.neworkapp.viewmodel.CreateEventViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel

@Composable
fun CreateEventScreen(
    sharedViewModel: SharedViewModel,
    createEventViewModel: CreateEventViewModel = hiltViewModel(),
    onEventCreated: () -> Unit
) {
    val token by sharedViewModel.token.collectAsState()
    var content by remember { mutableStateOf("") }
    var datetime by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("offline") } // Или "online"
    val createEventState by createEventViewModel.createEventState.collectAsState()

    LaunchedEffect(createEventState) {
        if (createEventState is CreateEventViewModel.CreateEventState.Success) {
            onEventCreated()
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
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = datetime,
            onValueChange = { datetime = it },
            label = { Text("Event Date (yyyy-MM-dd'T'HH:mm:ss.SSSZ)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Event Type (offline/online)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            token?.let {
                createEventViewModel.createEvent(it, content, datetime, type)
            } ?: run {
                Log.d("CreateEventScreen", "Token is null")
            }
        }) {
            Text("Create Event")
        }
        if (createEventState is CreateEventViewModel.CreateEventState.Loading) {
            CircularProgressIndicator()
        }
        if (createEventState is CreateEventViewModel.CreateEventState.Error) {
            Text("Failed to create event. Please try again.", color = MaterialTheme.colorScheme.error)
        }
    }
}