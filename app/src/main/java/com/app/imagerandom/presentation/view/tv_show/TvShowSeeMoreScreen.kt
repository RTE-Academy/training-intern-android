package com.app.imagerandom.presentation.view.tv_show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShow
import com.app.imagerandom.domain.util.TvShowType
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.custom_view.MoviesItemCard
import com.app.imagerandom.presentation.viewmodel.TvShowSeeMoreViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

fun NavController.navigateToTvShowSeeMore(type: String) {
    navigate(Screen.TV_SHOW_SEE_MORE + "/$type")
}

fun NavGraphBuilder.tvShowSeeMoreScreen(navController: NavController) {
    composable(route = Screen.TV_SHOW_SEE_MORE + "/{type}",
        arguments = listOf(navArgument("type") { type = NavType.StringType })
    ) { backStackEntry ->
        val type = backStackEntry.arguments?.getString("type") ?: TvShowType.TOP_RATED
        val viewModel = hiltViewModel<TvShowSeeMoreViewModel>()
        val tvShowList by viewModel.tvShows.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadTVShowType(type)
        }

        TvShowSeeMoreScreen(
            navController = navController,
            tvShowList = tvShowList,
            type = type,
            loadMoreTvShows = viewModel::loadNextPage
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowSeeMoreScreen(
    navController: NavController,
    tvShowList: Response<List<TVShow>>,
    type: String,
    loadMoreTvShows: () -> Unit,
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemIndex + gridState.layoutInfo.visibleItemsInfo.size }
            .distinctUntilChanged()
            .collect { lastVisible ->
                val total = gridState.layoutInfo.totalItemsCount
                if (lastVisible >= total - 12 && total > 0) {
                    loadMoreTvShows()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                        when (type) {
                                TvShowType.AIRING_TODAY -> stringResource(R.string.title_airing_today)
                                TvShowType.ON_THE_AIR -> stringResource(R.string.title_on_the_air)
                                TvShowType.POPULAR -> stringResource(R.string.title_popular)
                                TvShowType.TOP_RATED -> stringResource(R.string.title_top_rated)
                                else -> stringResource(R.string.tv_show_screen_title) },
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = AppColors.TextPrimary
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Back",
                            tint = AppColors.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.Primary)
            )
        },
        containerColor = AppColors.Primary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppColors.Primary)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            when (tvShowList) {
                is Response.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = AppColors.TextPrimary,
                        trackColor = AppColors.Error
                    )
                }

                is Response.Success -> {
                    val data = tvShowList.data ?: emptyList()

                    if (data.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.empty_tv_show),
                                style = MaterialTheme.typography.bodyLarge,
                                color = AppColors.TextSecondary
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(5.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(data) { tvShow ->
                                MoviesItemCard(
                                    movie = tvShow.toMovieItem(),
                                    onClick = {
                                        navController.navigateToTvDetail(tvShow.id)
                                    }
                                )
                            }
                        }
                    }
                }

                is Response.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lỗi: ${tvShowList.message ?: stringResource(R.string.error_unspecified_error)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AppColors.Error
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TvShowSeeMorePreview() {
    val mockItem = TVShow(
        name = "Sample TV Show"
    )
    val mockList = List(10) { mockItem }
    TvShowSeeMoreScreen(
        navController = rememberNavController(),
        tvShowList = Response.Success(mockList),
        type = TvShowType.TOP_RATED,
        loadMoreTvShows = {}
    )
}