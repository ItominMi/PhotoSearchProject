package com.example.photosearchproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.photosearchproject.presentation.mainscreen.MainScreen
import com.example.photosearchproject.presentation.photodetail.PhotoDetailScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreen) {
        composable<MainScreen> {
            MainScreen(
                onNavigateToPhotoScreen = { url ->
                    navController.navigate(PhotoDetailScreen(url))
                }
            )
        }

        composable<PhotoDetailScreen> {
            val args = it.toRoute<PhotoDetailScreen>()
            PhotoDetailScreen(args.photoUrl, onBack = {
                navController.popBackStack()
            })
        }
    }
}