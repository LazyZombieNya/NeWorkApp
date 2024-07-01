package ru.netology.neworkapp.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.netology.neworkapp.viewmodel.SharedViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Feed : Screen("feed")
    object CreatePost : Screen("create_post")
    object Profile : Screen("profile")
    object Register : Screen("register")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: SharedViewModel = hiltViewModel(),
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
                    sharedViewModel.setToken(token)
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Feed.route) {
            FeedScreen(
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
                    sharedViewModel.setToken(token)
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
    }
}