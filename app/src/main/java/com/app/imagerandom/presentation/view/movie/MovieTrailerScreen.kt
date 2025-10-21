package com.app.imagerandom.presentation.view.movie

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.viewmodel.MovieTrailerViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

// Navigation Extensions
fun NavController.navigateToMovieTrailer(movieId: Int) {
    navigate(Screen.MOVIE_TRAILER + "/" + movieId)
}

fun NavGraphBuilder.trailerScreen(navController: NavController) {
    composable(
        route = Screen.MOVIE_TRAILER + "/{movie_id}",
        arguments = listOf(navArgument("movie_id") { type = NavType.IntType })
    ) { backStackEntry ->
        val movieId = backStackEntry.arguments?.getInt("movie_id") ?: return@composable
        val viewModel = hiltViewModel<MovieTrailerViewModel>()
        val trailerKey by viewModel.trailerKey.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadTrailer(movieId)
        }

        MovieTrailerScreen(
            trailerKey = trailerKey,
            onDismiss = {
                navController.popBackStack()
            }
        )
    }
}

// Movie Trailer Screen Composable
@Composable
fun MovieTrailerScreen(
    trailerKey: Response<String?>,
    onDismiss: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val activity = context.findActivity()

    LaunchedEffect(trailerKey) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    BackHandler(onBack = onDismiss)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Primary),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (trailerKey) {
                    is Response.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AppColors.Primary),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is Response.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AppColors.Primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.trailer_unavailable),
                                color = AppColors.TextSecondary
                            )
                        }
                    }
                    is Response.Success -> {
                        // YouTube Player
                        AndroidView(
                            factory = { context ->
                                YouTubePlayerView(context).apply {
                                    layoutParams = FrameLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    setBackgroundColor(android.graphics.Color.BLACK)
                                    lifecycleOwner.lifecycle.addObserver(this)
                                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                        override fun onReady(youTubePlayer: YouTubePlayer) {
                                            youTubePlayer.loadVideo(trailerKey.data!!, 0f)
                                        }
                                    })
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f)
                                .background(AppColors.Primary)
                        )
                    }
                }

                IconButton(
                    onClick = {
                        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, end = 16.dp, top = 40.dp)
                        .zIndex(2f)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// Utility Function
fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}