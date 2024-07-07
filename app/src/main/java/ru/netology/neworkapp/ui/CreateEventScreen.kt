package ru.netology.neworkapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import ru.netology.neworkapp.viewmodel.CreateEventViewModel
import ru.netology.neworkapp.viewmodel.SharedViewModel
import java.io.OutputStream
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CreateEventScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    createEventViewModel: CreateEventViewModel = hiltViewModel(),
    onEventCreated: () -> Unit,
    onLocationClick: () -> Unit,
    onSpeakersClick: () -> Unit
) {
    val token by sharedViewModel.token.collectAsState()
    var content by remember { mutableStateOf("") }
    var datetime by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("ONLINE") }
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }

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

    val saveImageToGallery: (Bitmap) -> Uri? = { bitmap ->
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "event_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/NeWorkApp")
        }
        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream!!)
            }
        }
        uri
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            val uri = saveImageToGallery(it)
            if (uri != null) {
                imageUri = uri
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New event") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

                actions = {
                    IconButton(onClick = {
                        if (token != null && content.isNotBlank() && datetime.isNotBlank()) {

                            createEventViewModel.createEvent(token!!, content, datetime,type)
                            //createPostViewModel.createPost(it, content)
                        } else {
                            Log.d("CreateEventScreen", "Token:$token or content:$content or datetime:$datetime is null/empty")
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
                    IconButton(onClick = { takePhotoLauncher.launch(null) }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Take Photo")
                    }
                    IconButton(onClick = { pickImageLauncher.launch("image/*") }) {
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
                Text("Select Date and Time")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = datetime,
                        onValueChange = { datetime = it },
                        label = { Text("MM/dd/yyyy HH:mm") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Type")
                Row {
                    RadioButton(
                        selected = type == "ONLINE",
                        onClick = { type = "ONLINE"}
                    )
                    Text("Online", modifier = Modifier.padding(start = 8.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = type == "OFFLINE",
                        onClick = { type = "OFFLINE" }
                    )
                    Text("Offline", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { showBottomSheet = false },modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("Ok")
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