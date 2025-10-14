package com.app.imagerandom.presentation.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.snapshotFlow
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.presentation.navigation.AppDrawer
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.view.categories.navigateToCategories
import com.app.imagerandom.presentation.view.custom_view.MoviesItemCard
import com.app.imagerandom.presentation.view.custom_view.MovieDetailPopup
import com.app.imagerandom.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

fun NavController.navigateToHome() {
    navigate(Screen.HOME)
}

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(
        route = Screen.HOME,
    ) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val recentMovieList by viewModel.movies.collectAsState()
        val genresList by viewModel.genres.collectAsState()
        val isLoading by viewModel::isLoading
        HomeScreen(
            navController = navController,
            isAutoSignIn = viewModel.checkAutoSignIn(),
            isLoading = isLoading,
            recentMovieList = recentMovieList,
            genresList = genresList,
            loadMoreMovies = viewModel::loadMovies
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isAutoSignIn: Boolean,
    isLoading: Boolean,
    recentMovieList: List<MovieItem>,
    genresList: List<Genre>,
    loadMoreMovies: () -> Unit
) {
    val gridState = rememberLazyGridState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Bien dung de luu phim duoc chon de hien thi detail
    var selectedMovie by remember { mutableStateOf<MovieItem?>(null) }

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
                            text = "Khám Phá Phim Hot Nhất",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = AppColors.TextPrimary
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
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

                // Movie detail popup
                if (selectedMovie != null) {
                    MovieDetailPopup(
                        movie = selectedMovie!!,
                        onDismiss = { selectedMovie = null }
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
        isLoading = true,
        recentMovieList = sampleMovies,
        loadMoreMovies = {},
        genresList = emptyList()
    )
}