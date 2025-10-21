package com.app.imagerandom.presentation.view.person

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.imagerandom.domain.model.PersonResponse
import com.app.imagerandom.presentation.navigation.AppDrawer
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.view.custom_view.ErrorMessage
import com.app.imagerandom.presentation.view.custom_view.PersonItemCard
import com.app.imagerandom.presentation.view.custom_view.ShimmerPersonGrid
import com.app.imagerandom.presentation.viewmodel.PersonViewModel
import com.app.imagerandom.domain.model.Response
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun NavGraphBuilder.personScreen(navController: NavController) {
    composable(route = Screen.PERSON) {
        val viewModel = hiltViewModel<PersonViewModel>()

        val personState = viewModel.person
        val isLoading = viewModel::isLoading
        val loadNextPage = viewModel::loadNextPage

        PersonScreen(
            navController = navController,
            personState = personState,
            isLoading = isLoading,
            loadNextPage = loadNextPage
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    personState: StateFlow<Response<PersonResponse>>,
    isLoading: () -> Boolean,
    loadNextPage: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val scope = rememberCoroutineScope()
    val state by personState.collectAsState()

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
                            text = "Diễn viên tiêu biểu",
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
                    .background(
                        Brush.verticalGradient(
                            listOf(AppColors.Primary, AppColors.Secondary)
                        )
                    )
            ) {
                when (state) {
                    is Response.Loading -> ShimmerPersonGrid()
                    is Response.Error -> ErrorMessage((state as Response.Error).message ?: "Đã xảy ra lỗi")
                    is Response.Success -> {
                        val data = (state as Response.Success).data
                        val people = data?.results ?: emptyList()

                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(160.dp),
                            contentPadding = PaddingValues(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(people) { person ->
                                PersonItemCard(
                                    person = person,
                                    onClickItem = {
                                        navController.navigateToPersonDetail(person.id)
                                    }
                                )
                            }

                            item {
                                if (isLoading()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = AppColors.Accent)
                                    }
                                } else {
                                    LaunchedEffect(Unit) {
                                        loadNextPage()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}