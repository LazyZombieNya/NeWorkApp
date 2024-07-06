package ru.netology.neworkapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.netology.neworkapp.viewmodel.SharedViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Feed : Screen("feed")
    object CreatePost : Screen("createPost")
    object Profile : Screen("profile")
    object Register : Screen("register")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: SharedViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val showTopBar by sharedViewModel.showTopBar.collectAsState()
    val showBottomBar by sharedViewModel.showBottomBar.collectAsState()

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text("NeWork App") },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("login")
                        }) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Posts") },
                        label = { Text("Posts") },
                        selected = navController.currentDestination?.route == "posts",
                        onClick = {
                            coroutineScope.launch {
                                navController.navigate("posts") {
                                    popUpTo("posts") { inclusive = true }
                                }
                            }
                        }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Event, contentDescription = "Events") },
                        label = { Text("Events") },
                        selected = navController.currentDestination?.route == "events",
                        onClick = {
                            coroutineScope.launch {
                                navController.navigate("events") {
                                    popUpTo("events") { inclusive = true }
                                }
                            }
                        }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Group, contentDescription = "Users") },
                        label = { Text("Users") },
                        selected = navController.currentDestination?.route == "users",
                        onClick = {
                            coroutineScope.launch {
                                navController.navigate("users") {
                                    popUpTo("users") { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "posts",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("posts") {
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                FeedScreen(
                    sharedViewModel = sharedViewModel,
                    onCreatePost = { navController.navigate("createPost") },
                    onProfileClick = { navController.navigate("profile") }
                )
            }
            composable("events") {
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                EventScreen(
                    onCreateEvent = { navController.navigate("createEvent") },
                    onProfileClick = { navController.navigate("profile") }
                )
            }
            composable("users") {
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                UserScreen(onUserClick = { userId -> navController.navigate("userDetail/$userId") })
            }
            composable("createPost") {
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                CreatePostScreen(
                    sharedViewModel = sharedViewModel,
                    onPostCreated = { navController.navigate("posts") }
                )
            }
            composable("createEvent") {
                sharedViewModel.setShowTopBar(false)
                sharedViewModel.setShowBottomBar(false)
                CreateEventScreen(
                    sharedViewModel = sharedViewModel,
                    onEventCreated = {
                        sharedViewModel.setShowTopBar(true)
                        sharedViewModel.setShowBottomBar(true)
                        navController.navigate("events")
                    },
                    onLocationClick = { /* Handle location click */ },
                    onSpeakersClick = { /* Handle speakers click */ }
                )
            }
            composable("profile") {
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                ProfileScreen()
            }
            composable("login") {
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                LoginScreen(
                    onLoginSuccess = { token ->
                        sharedViewModel.setToken(token)
                        navController.navigate("posts") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate("register") }
                )
            }
            composable("register") {
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                RegisterScreen(
                    onRegisterSuccess = { token ->
                        sharedViewModel.setToken(token)
                        navController.navigate("posts") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }
            composable("userDetail/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                sharedViewModel.setShowTopBar(true)
                sharedViewModel.setShowBottomBar(true)
                UserDetailScreen(userId = userId?.toLongOrNull().toString())
            }
        }
    }
}