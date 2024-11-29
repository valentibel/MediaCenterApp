package com.valentibel.mediacenterapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.valentibel.mediacenterapp.screens.details.DetailsScreen
import com.valentibel.mediacenterapp.screens.details.DetailsScreenViewModel
import com.valentibel.mediacenterapp.screens.home.HomeScreen
import com.valentibel.mediacenterapp.screens.home.HomeScreenViewModel

@Composable
fun MediaNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MediaScreens.HomeScreen.name) {

        composable(MediaScreens.HomeScreen.name) {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        val detailsName = MediaScreens.DetailsScreen.name
        composable("$detailsName/{path}", arguments = listOf(navArgument("path") {
            type = NavType.StringType
        })) { backStackEntry ->
            val viewModel = hiltViewModel<DetailsScreenViewModel>()

            backStackEntry.arguments?.getString("path").let { path ->
                DetailsScreen(path = path ?:run { "" }, navController = navController, viewModel = viewModel)
            }

        }
    }

}