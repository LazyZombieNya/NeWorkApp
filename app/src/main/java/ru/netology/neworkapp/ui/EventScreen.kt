package ru.netology.neworkapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import ru.netology.neworkapp.data.AttachmentType
import ru.netology.neworkapp.data.Event
import ru.netology.neworkapp.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventScreen(
    eventViewModel: EventViewModel = hiltViewModel(),
    onCreateEvent: () -> Unit,
    onProfileClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        eventViewModel.loadEvents()
    }

    val events by eventViewModel.events.collectAsState()

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(onClick = onCreateEvent) {
                Icon(Icons.Default.Add, contentDescription = "Create Event")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(events) { event ->
                    EventItem(event = event)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    )
}

@Composable
fun EventItem(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.author, style = MaterialTheme.typography.titleMedium)
            Text(text = "Published: ${event.published}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Event Date: ${event.datetime}", style = MaterialTheme.typography.bodySmall)
            Text(text = event.content, style = MaterialTheme.typography.bodyMedium)
            event.attachment?.let { attachment ->
                when (attachment.type) {
                    AttachmentType.IMAGE -> {
                        Image(
                            painter = rememberImagePainter(attachment.url),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    // Добавьте обработку других типов вложений (видео, аудио)
                    AttachmentType.AUDIO -> TODO()
                    AttachmentType.VIDEO -> TODO()
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Likes: ${event.likeOwnerIds.size}")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Participants: ${event.participantsIds.size}")
            }
        }
    }
}