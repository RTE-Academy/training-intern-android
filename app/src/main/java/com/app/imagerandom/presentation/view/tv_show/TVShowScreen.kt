package com.app.imagerandom.presentation.view.tv_show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShow
import com.app.imagerandom.domain.util.TvShowType
import com.app.imagerandom.presentation.navigation.AppDrawer
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.viewmodel.TvShowViewModel
import com.app.imagerandom.presentation.view.custom_view.TVShowSection
import com.app.imagerandom.presentation.view.search.navigateToSearch
import kotlinx.coroutines.launch

fun NavGraphBuilder.tvShowScreen(navController: NavController) {
    composable(
        route = Screen.TV_SHOW
    ) {
        val viewModel = hiltViewModel<TvShowViewModel>()
        val airingTodayState by viewModel.airingToday.collectAsState()
        val onTheAirState by viewModel.onTheAir.collectAsState()
        val popularState by viewModel.popular.collectAsState()
        val topRatedState by viewModel.topRated.collectAsState()

        TVShowScreen(
            navController = navController,
            airingTodayState = airingTodayState,
            onTheAirState = onTheAirState,
            popularState = popularState,
            topRatedState = topRatedState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TVShowScreen(
    navController: NavController,
    airingTodayState: Response<List<TVShow>>,
    onTheAirState: Response<List<TVShow>>,
    popularState: Response<List<TVShow>>,
    topRatedState: Response<List<TVShow>>,
) {
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val scope = rememberCoroutineScope()
    val gradient = Brush.verticalGradient(
        colors = listOf(AppColors.Primary, AppColors.Secondary)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    selectedRoute = currentRoute,
                    onNavigate = { route ->
                        scope.launch { drawerState.close() }
                        if (route != currentRoute) {
                            navController.navigate(route)
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
                                text = stringResource(R.string.tv_show_screen_title),
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
                containerColor = Color.Transparent
            ) { innerPadding ->
                // Main content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                ) {
                    TVShowSection(
                        navControler = navController,
                        title = stringResource(R.string.airing_today),
                        state = airingTodayState,
                        type = TvShowType.AIRING_TODAY
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TVShowSection(
                        navControler = navController,
                        title = stringResource(R.string.on_the_air),
                        state = onTheAirState,
                        type = TvShowType.ON_THE_AIR
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TVShowSection(
                        navControler = navController,
                        title = stringResource(R.string.popular),
                        state = popularState,
                        type = TvShowType.POPULAR
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TVShowSection(
                        navControler = navController,
                        title = stringResource(R.string.top_rated),
                        state = topRatedState,
                        type = TvShowType.TOP_RATED
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}