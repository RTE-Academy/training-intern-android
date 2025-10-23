package com.app.imagerandom.presentation.view.tv_show

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShowDetail
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.custom_view.ErrorStateView
import com.app.imagerandom.presentation.view.custom_view.TVShowDetailContent
import com.app.imagerandom.presentation.view.custom_view.TVShowDetailShimmer
import com.app.imagerandom.presentation.viewmodel.TVShowDetailViewModel

fun NavController.navigateToTvDetail(tvId: Int) {
    navigate(Screen.TV_SHOW_DETAIL + "/$tvId")
}

fun NavGraphBuilder.tvDetailScreen(navController: NavController) {
    composable(
        route = Screen.TV_SHOW_DETAIL + "/{tvId}",
        arguments = listOf(navArgument("tvId") { type = NavType.IntType })
    ) { backStackEntry ->
        val tvId = backStackEntry.arguments?.getInt("tvId") ?: return@composable
        val viewModel = hiltViewModel<TVShowDetailViewModel>()
        val tvShowDetail by viewModel.tvShowDetail.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadTVShowDetail(tvId)
        }

        TVShowDetailScreen(
            navController = navController,
            tvShowDetailState = tvShowDetail
        )
    }
}

@Composable
fun TVShowDetailScreen(
    navController: NavController,
    tvShowDetailState: Response<TVShowDetail>,
) {
    val scrollState = rememberScrollState()
    val gradient = Brush.verticalGradient(
        colors = listOf(AppColors.Primary, AppColors.Secondary)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        // Close button
        Box(
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 40.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(AppColors.Primary)
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = AppColors.TextPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Main content
        when (tvShowDetailState) {
            is Response.Loading -> TVShowDetailShimmer()
            is Response.Error -> ErrorStateView(
                message = tvShowDetailState.message
                    ?: stringResource(R.string.error_unspecified_error)
            ) { }

            is Response.Success -> {
                val tvShow = tvShowDetailState.data ?: TVShowDetail()
                TVShowDetailContent(
                    tvShow = tvShow,
                    scrollState = scrollState,
                    navController = navController
                )
            }
        }
    }
}