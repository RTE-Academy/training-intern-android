package com.app.imagerandom.presentation.view.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

fun NavController.navigateToHome() {
    navigate(Screen.HOME)
}

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(
        route = Screen.HOME,
    ) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val pageAndMovieList by viewModel.pageAndListMovie.collectAsState()
        val recentMovieList = viewModel.movies
        HomeScreen(
            navController = navController,
            isAutoSignIn = viewModel.checkAutoSignIn(),
            pageAndMovieList = pageAndMovieList,
            recentMovieList = recentMovieList,
            loadMoreMovies = { viewModel.loadMoreMovies() }
        )
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    isAutoSignIn: Boolean,
    pageAndMovieList: GetMovieListResponse? = null,
    recentMovieList: SnapshotStateList<MovieItem>,
    loadMoreMovies: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    // Auto sign in
    LaunchedEffect(isAutoSignIn) {
        if (!isAutoSignIn) {
            navController.navigateToSignIn("", "")
        }
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
            .padding(16.dp)
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
                Text(
                    text = "Khám Phá Phim Hot Nhất 🔥",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = AppColors.TextPrimary
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(Modifier.align(Alignment.BottomEnd)) {
                            Image(
                                painter = painterResource(id = R.drawable.img_background_categories_left),
                                contentDescription = "Image 1",
                                modifier = Modifier
                                    .fillMaxWidth(0.9f),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.img_categories_spiderman),
                            contentDescription = "Image 2",
                            modifier = Modifier
                                .fillMaxSize(0.8f)
                                .align(Alignment.BottomStart),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 10.dp, bottom = 20.dp),
                            text = "Movies",
                            color = AppColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_background_categories_right),
                            contentDescription = "Image 1",
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(Alignment.BottomStart),
                            contentScale = ContentScale.Crop
                        )

                        Image(
                            painter = painterResource(id = R.drawable.img_categories_deku),
                            contentDescription = "Image 2",
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.BottomEnd),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 10.dp, bottom = 20.dp),
                            text = "Animes",
                            color = AppColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

                Text(
                    text = "Phim Mới Cập Nhật",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AppColors.TextPrimary
                    )
                )

                if (pageAndMovieList != null && pageAndMovieList.results.isNotEmpty()) {
                    // List movies
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(5.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recentMovieList.size) { index ->
                            val movie = recentMovieList[index]
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .shadow(4.dp, RoundedCornerShape(12.dp))
                                    .clickable {
                                        Log.d("HomeScreen", "Clicked movie ID: ${movie.id}")
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = AppColors.Primary
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                        contentDescription = movie.title,
                                        modifier = Modifier
                                            .height(120.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = movie.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = AppColors.TextSecondary
                                        ),
                                        textAlign = TextAlign.Center,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                } else {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val sampleMovies = remember { mutableStateListOf<MovieItem>() }
    HomeScreen(
        navController = NavController(LocalContext.current),
        isAutoSignIn = true,
        pageAndMovieList = GetMovieListResponse(page = 1, results = emptyList()),
        recentMovieList = sampleMovies,
        loadMoreMovies = {}
    )
}