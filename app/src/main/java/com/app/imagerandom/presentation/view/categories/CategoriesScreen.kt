package com.app.imagerandom.presentation.view.categories

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.app.imagerandom.common.NetworkConstants
import com.app.imagerandom.data.app_const.Genres
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.custom_view.AutoSlidingBanner
import com.app.imagerandom.presentation.view.custom_view.CategoriesMoviesItemCard
import com.app.imagerandom.presentation.view.home.navigateToHome
import com.app.imagerandom.presentation.viewmodel.CategoriesViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

fun NavController.navigateToCategories(categories: Int) {
    navigate("${Screen.CATEGORIES}?categories=${categories}")
}

fun NavGraphBuilder.categoriesScreen(navController: NavController) {
    composable(
        route = "${Screen.CATEGORIES}?categories={categories}",
        arguments = listOf(
            navArgument("categories") { defaultValue = Genres.MOVIES }
        )
    ) {
        val viewModel = hiltViewModel<CategoriesViewModel>()
        val movieListForSlideShow by viewModel.movieListForSlideShow.collectAsState()
        val movieList by viewModel.movies.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val categories = it.arguments?.getInt("categories") ?: Genres.MOVIES
        LaunchedEffect(Unit) { viewModel.loadMoviesByGenres(genres = categories) }
        CategoriesScreen(
            navController = navController,
            movieListForSlideShow = movieListForSlideShow,
            movieList = movieList,
            isLoading = isLoading,
            categories = categories,
            loadMoreMovies = { viewModel.loadMoviesByGenres(isLoadMore = true) }
        )
    }
}

@Composable
fun CategoriesScreen(
    navController: NavController,
    movieListForSlideShow: List<MovieItem>,
    movieList: List<MovieItem>,
    isLoading: Boolean,
    categories: Int,
    loadMoreMovies: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    // Observe grid scroll to load more movies
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemIndex + gridState.layoutInfo.visibleItemsInfo.size }
            .distinctUntilChanged()
            .collect { lastVisible ->
                val total = gridState.layoutInfo.totalItemsCount
                if (lastVisible >= total - 12 && total > 0) {
                    loadMoreMovies()
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Primary)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    // Home btn
                    IconButton(
                        onClick = { navController.navigateToHome() },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(48.dp)
                            .background(AppColors.Secondary, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home button",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Title
                    Text(
                        text = "Thể loại: " + if (categories == Genres.MOVIES) "Movies" else "Toons",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = AppColors.TextPrimary
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                    )
                }

                if (movieListForSlideShow.isNotEmpty()) {
                    // Pager for slide movies
                    val pagerState = rememberPagerState(pageCount = { movieListForSlideShow.size })
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        AutoSlidingBanner(
                            movies = movieListForSlideShow,
                            pagerState = pagerState
                        )
                    }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = AppColors.TextPrimary,
                            trackColor = AppColors.Error
                        )
                    }
                }

                if (movieList.isNotEmpty()) {
                    // List movies
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(5.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(movieList.size) { index ->
                            val movie = movieList[index]
                            CategoriesMoviesItemCard(movie = movie) {
                                // TODO: Navigate to movie detail
                            }
                        }
                    }
                } else if (!isLoading) {
                    // Empty state
                    Text(
                        text = "Không có phim nào để hiển thị",
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppColors.TextSecondary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}