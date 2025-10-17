package com.app.imagerandom.presentation.view.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.model.MovieSearchResult
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.util.MediaType
import com.app.imagerandom.presentation.navigation.AppDrawer
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.view.custom_view.MoviesItemCard
import com.app.imagerandom.presentation.view.custom_view.SearchTabs
import com.app.imagerandom.presentation.viewmodel.SearchViewModel
import com.app.imagerandom.presentation.view.home.navigateToHome
import com.app.imagerandom.presentation.view.movie.navigateToMovieDetail
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

fun NavController.navigateToSearch() {
    navigate(Screen.SEARCH)
}

fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(
        route = Screen.SEARCH
    ) { backStackEntry ->
        val viewModel = hiltViewModel<SearchViewModel>()
        val searchResult by viewModel.searchResults.collectAsState()
        val currentQuery by viewModel.currentQuery.collectAsState()

        SearchScreen(
            navController = navController,
            movieList = searchResult,
            query = currentQuery,
            loadMoreMovies = viewModel::loadNextPage,
            onSearch = { viewModel.searchMovies(true) },
            onUpdateQuery = viewModel::onUpdateQuery,
            onClear = viewModel::onClearQuery
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    movieList: Response<List<MovieSearchResult>>,
    query: String,
    loadMoreMovies: () -> Unit,
    onSearch: () -> Unit,
    onUpdateQuery: (String) -> Unit,
    onClear: () -> Unit
) {
    val gridState = rememberLazyGridState()
    var mediaType by remember { mutableStateOf(MediaType.MOVIE) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val scope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val focusManager = LocalFocusManager.current

    // Scroll to load more
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

    // Scroll to top when change tab
    LaunchedEffect(selectedTabIndex) {
        gridState.scrollToItem(0)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedRoute = currentRoute,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    if (route != currentRoute) {
                        navController.navigateToHome()
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
                            text = "Tìm kiếm",
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
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(AppColors.Primary)
            ) {

                // Search field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = query,
                        onValueChange = { onUpdateQuery(it) },
                        singleLine = true,
                        placeholder = { Text("Tìm kiếm") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(onClick = {
                                onClear()
                            }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = AppColors.Primary
                                )
                            }
                        },
                        leadingIcon = {
                            IconButton(onClick = {
                                if (query.isNotEmpty()) onSearch()
                                focusManager.clearFocus()
                            }) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = AppColors.Primary
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = AppColors.Primary,
                            focusedLabelColor = AppColors.Primary,
                            unfocusedLabelColor = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (query.isNotEmpty()) {
                                onSearch()
                                focusManager.clearFocus()
                            }
                        })
                    )
                }

                SearchTabs(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        selectedTabIndex = index
                        when (index) {
                            0 -> mediaType = MediaType.MOVIE
                            1 -> mediaType = MediaType.TV
                            2 -> mediaType = MediaType.PERSON
                        }
                    }
                )

                when (movieList) {
                    is Response.Loading -> {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = AppColors.TextPrimary,
                            trackColor = AppColors.Error
                        )
                    }

                    is Response.Success -> {
                        val movies = movieList.data ?: emptyList()
                        if (movies.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(AppColors.Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Không có phim nào để hiển thị",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AppColors.TextSecondary
                                )
                            }
                        } else {
                            val filteredMovies = movies.filter { it.mediaType == mediaType }

                            if (filteredMovies.isEmpty()) {
                                loadMoreMovies()
                            }

                            LazyVerticalGrid(
                                state = gridState,
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(15.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredMovies) { movie ->
                                    val movieItem = with(movie) {
                                        MovieItem(
                                            adult = true,
                                            backdropPath = backdropUrl,
                                            genreIds = emptyList(),
                                            id = id,
                                            originalLanguage = "",
                                            originalTitle = title,
                                            overview = overview ?: "Không có mô tả.",
                                            popularity = 0.0,
                                            posterPath = posterUrl,
                                            releaseDate = "",
                                            title = title,
                                            video = false,
                                            voteAverage = rating,
                                            voteCount = 0
                                        )
                                    }

                                    MoviesItemCard(movie = movieItem) {
                                        if (movie.mediaType == MediaType.MOVIE) {
                                            navController.navigateToMovieDetail(movieItem.id)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is Response.Error -> {
                        Text(
                            text = "Lỗi: ${movieList.message ?: "Không xác định"}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AppColors.Error,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchScreenPreview() {
    val mockSearchResult = MovieSearchResult(
        id = 1,
        title = "Inception",
        overview = "A thief who steals corporate secrets through dream-sharing technology.",
        posterUrl = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
        backdropUrl = "/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
        rating = 8.8,
        mediaType = MediaType.MOVIE,
        totalPage = 5
    )

    val mockMovies = List(30) { mockSearchResult }

    SearchScreen(
        movieList = Response.Success(mockMovies),
        navController = rememberNavController(),
        loadMoreMovies = { },
        query = "",
        onSearch = { },
        onUpdateQuery = { },
        onClear = { }
    )
}