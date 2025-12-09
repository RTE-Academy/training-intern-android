package com.app.imagerandom.presentation.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.util.MovieType
import com.app.imagerandom.presentation.navigation.AppDrawer
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.view.categories.navigateToCategories
import com.app.imagerandom.presentation.view.custom_view.MovieSection
import com.app.imagerandom.presentation.view.search.navigateToSearch
import com.app.imagerandom.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

fun NavController.navigateToHome(
    clearBackStack: Boolean = false
) {
    navigate(Screen.HOME) {
        if (clearBackStack) {
            popUpTo(0) { inclusive = true }
        }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(
        route = Screen.HOME,
    ) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val movieListPopular by viewModel.popular.collectAsState()
        val movieListNowPlaying by viewModel.nowPlaying.collectAsState()
        val movieListTopRated by viewModel.topRated.collectAsState()
        val movieListUpcoming by viewModel.upcoming.collectAsState()
        val genresList by viewModel.genres.collectAsState()
        HomeScreen(
            navController = navController,
            movieListPopular = movieListPopular,
            movieListNowPlaying = movieListNowPlaying,
            movieListTopRated = movieListTopRated,
            movieListUpcoming = movieListUpcoming,
            isAutoSignIn = viewModel.checkAutoSignIn(),
            genresList = genresList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isAutoSignIn: Boolean,
    genresList: List<Genre>,
    movieListPopular: List<MovieItem>,
    movieListNowPlaying: List<MovieItem>,
    movieListTopRated: List<MovieItem>,
    movieListUpcoming: List<MovieItem>
) {
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Auto sign in
    LaunchedEffect(isAutoSignIn) {
        if (!isAutoSignIn) {
            navController.navigateToSignIn("", "")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedRoute = currentRoute,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(Screen.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    navController.navigateToSignIn("", "")
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.home_screen_title),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = AppColors.TextPrimary
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigateToSearch() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                modifier = Modifier.size(25.dp),
                                contentDescription = "Search",
                                tint = AppColors.TextPrimary
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
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
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigateToCategories(
                                        genresList.getOrNull(17)?.id ?: 10768
                                    )
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
                                text = genresList.getOrNull(17)?.name ?: "Phim Chiến Tranh",
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
                                    navController.navigateToCategories(
                                        genresList.getOrNull(3)?.id ?: 35
                                    )
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
                                text = genresList.getOrNull(3)?.name ?: "Phim Hài",
                                color = AppColors.TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        MovieSection(
                            navController = navController,
                            title = "Phim phổ biến",
                            movies = movieListPopular,
                            type = MovieType.POPULAR
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        MovieSection(
                            navController = navController,
                            title = "Phim đang chiếu",
                            movies = movieListNowPlaying,
                            type = MovieType.NOW_PLAYING
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        MovieSection(
                            navController = navController,
                            title = "Phim được đánh giá cao",
                            movies = movieListTopRated,
                            type = MovieType.TOP_RATED
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        MovieSection(
                            navController = navController,
                            title = "Phim sắp ra mắt",
                            movies = movieListUpcoming,
                            type = MovieType.UPCOMING
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val mockMovie = MovieItem(
        id = 1,
        title = "Mock Movie",
        overview = "This is a mock movie used for preview purposes.",
        posterPath = "/path/to/poster.jpg",
        backdropPath = "/path/to/backdrop.jpg",
        releaseDate = "2024-01-01",
        voteAverage = 8.5,
        genreIds = listOf(28, 12),
        adult = false,
        originalLanguage = "",
        originalTitle = "",
        popularity = 2.0,
        video = false,
        voteCount = 1,
    )
    val mockMovies = List(10) { mockMovie }
    HomeScreen(
        navController = rememberNavController(),
        isAutoSignIn = true,
        genresList = emptyList(),
        movieListPopular = mockMovies,
        movieListNowPlaying = mockMovies,
        movieListTopRated = mockMovies,
        movieListUpcoming = mockMovies,
    )
}