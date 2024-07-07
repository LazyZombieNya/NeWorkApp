package ru.netology.neworkapp.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.netology.neworkapp.viewmodel.RegisterViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: (String) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val registerState by registerViewModel.registerState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFile = uri
    }

    LaunchedEffect(registerState) {
        if (registerState is RegisterViewModel.RegisterState.Success) {
            val token = (registerState as RegisterViewModel.RegisterState.Success).token
            onRegisterSuccess(token)
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
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            selectedFile?.let { uri ->
                val file = uriToFile(uri, context)
                registerViewModel.register(login, password, name, file)
            }
        }) {
            Text("Register")
        }
        if (registerState is RegisterViewModel.RegisterState.Loading) {
            CircularProgressIndicator()
        }
        if (registerState is RegisterViewModel.RegisterState.Error) {
            Text("Registration failed. Please try again.", color = MaterialTheme.colorScheme.error)
        }
    }
}

fun uriToFile(uri: Uri, context: Context): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "temp_image.png")
    val outputStream: OutputStream = FileOutputStream(file)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
        outputStream.write(buffer, 0, length)
    }
    outputStream.close()
    inputStream?.close()
    return file
}