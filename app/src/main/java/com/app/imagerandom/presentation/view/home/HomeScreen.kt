package com.app.imagerandom.presentation.view.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.app.imagerandom.R
import com.app.imagerandom.data.app_const.Genres
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.view.categories.navigateToCategories
import com.app.imagerandom.presentation.view.custom_view.MoviesItemCard
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
        val recentMovieList by viewModel.movies.collectAsState()
        val isLoading by viewModel::isLoading
        HomeScreen(
            navController = navController,
            isAutoSignIn = viewModel.checkAutoSignIn(),
            isLoading = isLoading,
            recentMovieList = recentMovieList,
            loadMoreMovies = viewModel::loadMovies
        )
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    isAutoSignIn: Boolean,
    isLoading: Boolean,
    recentMovieList: List<MovieItem>,
    loadMoreMovies: () -> Unit,
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Khám Phá Phim Hot Nhất",
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
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigateToCategories(Genres.WAR.id)
                            }
                    ) {
                        // Create references for the composables to constrain
                        val (background, image, text) = createRefs()
                        Image(
                            painter = painterResource(id = R.drawable.img_background_categories_left),
                            contentDescription = "Left Background",
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(background) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            contentScale = ContentScale.FillWidth
                        )
                        Image(
                            painter = painterResource(id = R.drawable.img_categories_sparta),
                            contentDescription = "Left Image",
                            modifier = Modifier
                                .constrainAs(image) {
                                    start.linkTo(background.start)
                                    end.linkTo(background.end)
                                },
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 20.dp, end = 8.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .constrainAs(text) {
                                    top.linkTo(background.top)
                                    end.linkTo(background.end)
                                },
                            text = Genres.WAR.displayName,
                            color = AppColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    ConstraintLayout(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigateToCategories(Genres.COMEDY.id)
                            }
                    ) {
                        val (background, image, text) = createRefs()

                        Image(
                            painter = painterResource(id = R.drawable.img_background_categories_right),
                            contentDescription = "Right Background",
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(background) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            contentScale = ContentScale.FillWidth
                        )

                        Image(
                            painter = painterResource(id = R.drawable.img_categories_comedy),
                            contentDescription = "Right Image",
                            modifier = Modifier
                                .aspectRatio(604f / 644f)
                                .offset(x = 30.dp)
                                .constrainAs(image) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                },
                            contentScale = ContentScale.Fit
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 20.dp, start = 8.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .constrainAs(text) {
                                    top.linkTo(background.top)
                                    start.linkTo(parent.start)
                                },
                            text = Genres.COMEDY.displayName,
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

                if (recentMovieList.isNotEmpty()) {
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
                            MoviesItemCard(movie = movie) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val sampleMovies = remember { mutableStateListOf<MovieItem>() }
    HomeScreen(
        navController = NavController(LocalContext.current),
        isAutoSignIn = true,
        recentMovieList = sampleMovies,
        loadMoreMovies = {},
        isLoading = true
    )
}