package com.app.imagerandom.presentation.view.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.custom_view.AutoSlidingBanner
import com.app.imagerandom.presentation.view.custom_view.CategoryHeader
import com.app.imagerandom.presentation.view.custom_view.GenreSelectionPopup
import com.app.imagerandom.presentation.view.custom_view.MoviesItemCard
import com.app.imagerandom.presentation.view.home.navigateToHome
import com.app.imagerandom.presentation.view.movie.navigateToMovieDetail
import com.app.imagerandom.presentation.viewmodel.CategoriesViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

fun NavController.navigateToCategories(categories: Int) {
    navigate("${Screen.CATEGORIES}?categories=${categories}")
}

fun NavGraphBuilder.categoriesScreen(navController: NavController) {
    composable(
        route = "${Screen.CATEGORIES}?categories={categories}",
        arguments = listOf(
            navArgument("categories") { defaultValue = 10768 }
        )
    ) {
        val viewModel = hiltViewModel<CategoriesViewModel>()
        val movieListForSlideShow by viewModel.movieListForSlideShow.collectAsState()
        val movieList by viewModel.movies.collectAsState()
        val genresList by viewModel.genres.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val categories = it.arguments?.getInt("categories") ?: 10768
        LaunchedEffect(Unit) { viewModel.loadMoviesByGenres(genres = categories) }
        CategoriesScreen(
            navController = navController,
            movieListForSlideShow = movieListForSlideShow,
            movieList = movieList,
            genresList = genresList,
            isLoading = isLoading,
            categories = categories,
            loadMoreMovies = { viewModel.loadMoviesByGenres(isLoadMore = true) },
            onCategoryChanged = { newGenre ->
                viewModel.loadMoviesByGenres(genres = newGenre)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavController,
    movieListForSlideShow: List<MovieItem>,
    movieList: List<MovieItem>,
    genresList: List<Genre>,
    isLoading: Boolean,
    categories: Int,
    loadMoreMovies: () -> Unit,
    onCategoryChanged: (Int) -> Unit
) {
    val gridState = rememberLazyGridState()
    var showPopup by remember { mutableStateOf(false) }
    var selectedGenre by remember { mutableStateOf<Genre?>(null) }

    LaunchedEffect(genresList) {
        if (selectedGenre == null && genresList.isNotEmpty()) {
            selectedGenre = genresList.firstOrNull { it.id == categories } ?: genresList.first()
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

    GenreSelectionPopup(
        showPopup = showPopup,
        selectedGenre = selectedGenre,
        genreList = genresList,
        onDismiss = { showPopup = false },
        onCategorySelected = { genre ->
            selectedGenre = genre
            onCategoryChanged(genre.id)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    selectedGenre?.let {
                        CategoryHeader(
                            selectedGenre = it,
                            onClickShowPopup = { showPopup = true },
                            onNavigateToHome = { navController.navigateToHome() }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.Primary)
            )
        },
        containerColor = AppColors.Primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppColors.Primary),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                        pagerState = pagerState,
                        onClickItem = { movie ->
                            navController.navigateToMovieDetail(movie.id)
                        }
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
                    contentPadding = PaddingValues(15.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movieList.size) { index ->
                        val movie = movieList[index]
                        MoviesItemCard(
                            movie = movie,
                            onClick = {
                                navController.navigateToMovieDetail(movie.id)
                            },
                        )
                    }
                }
            } else if (!isLoading) {
                // Empty state
                Text(
                    text = stringResource(R.string.lable_empty_movie),
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.TextSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    val mockItem = MovieItem(
        id = 1,
        title = "Inception",
        overview = "A thief who steals corporate secrets through dream-sharing technology.",
        posterPath = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
        backdropPath = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
        voteAverage = 8.8,
        releaseDate = "2010-07-16",
        adult = true,
        genreIds = emptyList(),
        originalLanguage = "TODO()",
        originalTitle = "TODO()",
        popularity = 1.0,
        video = false,
        voteCount = 1
    )

    val mockMovies = List(30) { index ->
        mockItem
    }

    val mockGenres = listOf(
        Genre(id = 28, name = "Action"),
        Genre(id = 12, name = "Adventure"),
        Genre(id = 878, name = "Science Fiction"),
        Genre(id = 18, name = "Drama"),
        Genre(id = 35, name = "Comedy")
    )

    CategoriesScreen(
        navController = NavController(LocalContext.current),
        movieListForSlideShow = emptyList(),
        movieList = mockMovies,
        genresList = mockGenres,
        isLoading = false,
        categories = 35,
        loadMoreMovies = { },
        onCategoryChanged = { }
    )
}