package ru.netology.neworkapp.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Feed : Screen("feed/{token}") {
        fun createRoute(token: String) = "feed/${Uri.encode(token)}"
    }
    object CreatePost : Screen("create_post")
    object Profile : Screen("profile")
    object Register : Screen("register")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { token ->
                    navController.navigate(Screen.Feed.createRoute(token)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Feed.route) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            FeedScreen(
                token = token,
                onCreatePost = {
                    navController.navigate(Screen.CreatePost.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        composable(Screen.CreatePost.route) {
            CreatePostScreen(
                onPostCreated = {
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Feed.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
//                onProfileEdit = {
//                    // Реализуйте редактирование профиля
//                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { token ->
                    navController.navigate(Screen.Feed.createRoute(token)) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
    }
}