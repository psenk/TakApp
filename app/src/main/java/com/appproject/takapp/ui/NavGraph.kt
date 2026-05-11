package com.appproject.takapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appproject.takapp.ui.login.LoginScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Game : Screen("game")
    data object Create : Screen("create")
    data object History : Screen("history")
    data object Settings : Screen("settings")
}

@Composable
fun NavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onResumeGameClick = {
                    navController.navigate(Screen.Game.route)
                },
                onCreateGameClick = {
                    navController.navigate(Screen.Create.route)
                },
                onGameHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Game.route) {
            GameScreen()
        }
        composable(Screen.Create.route) {
            CreateScreen()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}