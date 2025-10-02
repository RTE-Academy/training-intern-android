package com.app.imagerandom.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.imagerandom.presentation.view.auth.signInScreen
import com.app.imagerandom.presentation.view.auth.signUpScreen
import com.app.imagerandom.presentation.view.home.homeScreen

object Screen {
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"
    const val HOME = "home"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME
    ) {
        signUpScreen(navController)
        signInScreen(navController)
        homeScreen(navController)
    }
}