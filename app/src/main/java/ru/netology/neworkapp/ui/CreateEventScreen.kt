package ru.netology.neworkapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.netology.neworkapp.viewmodel.CreateEventViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CreateEventScreen(
    sharedViewModel: SharedViewModel,
    createEventViewModel: CreateEventViewModel = hiltViewModel(),
    onEventCreated: () -> Unit,
    onLocationClick: () -> Unit,
    onSpeakersClick: () -> Unit
) {
    val token by sharedViewModel.token.collectAsState()
    var content by remember { mutableStateOf("") }
    var datetime by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("online") }
    val createEventState by createEventViewModel.createEventState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("MM/dd/yyyy HH:mm", java.util.Locale.getDefault())
    val context = LocalContext.current

    LaunchedEffect(createEventState) {
        if (createEventState is CreateEventViewModel.CreateEventState.Success) {
            onEventCreated()
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<String?>(null) }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                showTimePicker = true
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                datetime = dateFormatter.format(calendar.time)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New event") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back action */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        token?.let {
                            createEventViewModel.createEvent(it, content, datetime, type)
                        } ?: run {
                            Log.d("CreateEventScreen", "Token is null")
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                if (imageUri != null) {
                    Image(
                        painter = rememberImagePainter(imageUri),
                        contentDescription = "Event Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Button(onClick = { imageUri = null }) {
                        Text("Remove")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { /* Take Photo */ }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Take Photo")
                    }
                    IconButton(onClick = { /* Pick Image */ }) {
                        Icon(Icons.Default.Image, contentDescription = "Pick Image")
                    }
                    IconButton(onClick = onSpeakersClick) {
                        Icon(Icons.Default.Group, contentDescription = "Pick Speakers")
                    }
                    IconButton(onClick = onLocationClick) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Pick Location")
                    }
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Attachment")
                    }
                }
            }
        }
    )

    if (showBottomSheet) {
        BottomSheetDialog(onDismissRequest = { showBottomSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Date")
                TextField(
                    value = datetime,
                    onValueChange = { datetime = it },
                    label = { Text("MM/dd/yyyy HH:mm") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Type")
                Row {
                    RadioButton(
                        selected = type == "online",
                        onClick = { type = "online" }
                    )
                    Text("Online", modifier = Modifier.padding(start = 8.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = type == "offline",
                        onClick = { type = "offline" }
                    )
                    Text("Offline", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { showBottomSheet = false }) {
                    Text("Save")
                }
            }
        }
    }
}
@Composable
fun BottomSheetDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}