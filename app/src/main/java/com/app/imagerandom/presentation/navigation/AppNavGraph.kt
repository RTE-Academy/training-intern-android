package com.app.imagerandom.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.imagerandom.presentation.view.auth.signInScreen
import com.app.imagerandom.presentation.view.auth.signUpScreen
import com.app.imagerandom.presentation.view.categories.categoriesScreen
import com.app.imagerandom.presentation.view.movie.trailerScreen
import com.app.imagerandom.presentation.view.home.homeScreen
import com.app.imagerandom.presentation.view.movie.movieDetailScreen
import com.app.imagerandom.presentation.view.search.searchScreen
import com.app.imagerandom.presentation.view.tv_show.tvDetailScreen
import com.app.imagerandom.presentation.view.tv_show.tvShowScreen
import com.app.imagerandom.presentation.view.tv_show.tvShowSeeMoreScreen
import com.app.imagerandom.presentation.view.person.personScreen
import com.app.imagerandom.presentation.view.person.personDetailScreen

object Screen {
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"
    const val HOME = "home"
    const val MOVIE_DETAIL = "movie_detail"
    const val MOVIE_TRAILER = "movie_trailer"
    const val CATEGORIES = "categories"
    const val TV_SHOW = "tv_show"
    const val TV_SHOW_DETAIL = "tv_show_detail"
    const val TV_SHOW_SEE_MORE = "tv_show_see_more"
    const val SEARCH = "search"
    const val PERSON = "person"
    const val PERSON_DETAIL = "person_detail"
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
        movieDetailScreen(navController)
        categoriesScreen(navController)
        searchScreen(navController)
        tvShowScreen(navController)
        tvDetailScreen(navController)
        tvShowSeeMoreScreen(navController)
        trailerScreen(navController)
        personScreen(navController)
        personDetailScreen(navController)
    }
}