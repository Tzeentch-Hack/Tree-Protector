package com.tzeench.treeprotectorfrontend

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.decapitalize
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tzeench.treeprotectormobile.android.screens.CameraScreen
import com.tzeench.treeprotectormobile.android.screens.MenuScreen
import com.tzeench.treeprotectormobile.android.screens.RegistryScreen
import com.tzeench.treeprotectormobile.android.screens.SatelliteMapScreen

sealed class MenuSections(val destination: String) {

    object MenuScreenSection: MenuSections(MenuSection.MENU_ROUTE)
    object RegistrySection: MenuSections(MenuSection.REGISTRY_ROUTE)
    object CameraSection: MenuSections(MenuSection.CAMERA_ROUTE)
    object SatelliteSection: MenuSections(MenuSection.SATELLITE_ROUTE)
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavGraph(
    modifier: Modifier,
    startDestination: String =  MenuSections.MenuScreenSection.destination
) {

    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(300)) },
        exitTransition = { fadeOut(tween(300)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = { fadeOut(tween(300)) }
    ) {
        composable(route = MenuSections.MenuScreenSection.destination) {
            MenuScreen(navController = navController)
        }

        composable(route = MenuSections.RegistrySection.destination) {
            RegistryScreen(navController = navController)
        }

        composable(route = MenuSections.CameraSection.destination) {
            CameraScreen(navController = navController)
        }

        composable(route = MenuSections.SatelliteSection.destination) {
            SatelliteMapScreen(navController = navController)
        }
    }
}