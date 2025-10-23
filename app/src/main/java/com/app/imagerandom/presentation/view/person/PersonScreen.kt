package com.app.imagerandom.presentation.view.person

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.imagerandom.R
import com.app.imagerandom.domain.model.PersonResponse
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.presentation.navigation.AppDrawer
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.util.animateDpValue
import com.app.imagerandom.presentation.util.animateFloatValue
import com.app.imagerandom.presentation.view.auth.navigateToSignIn
import com.app.imagerandom.presentation.view.custom_view.ErrorMessage
import com.app.imagerandom.presentation.view.custom_view.PersonItemCard
import com.app.imagerandom.presentation.view.custom_view.ShimmerPersonGrid
import com.app.imagerandom.presentation.viewmodel.PersonViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun NavGraphBuilder.personScreen(navController: NavController) {
    composable(route = Screen.PERSON) {
        val viewModel = hiltViewModel<PersonViewModel>()

        val personState = viewModel.person
        val isLoading = viewModel::isLoading
        val searchQuery by viewModel.currentQuery.collectAsState()
        val loadNextPage = viewModel::loadNextPage
        val onClearQuery = viewModel::clearQuery
        val onUpdateQuery = viewModel::updateQuery
        val onSearch = viewModel::searchPerson

        PersonScreen(
            navController = navController,
            personState = personState,
            searchQuery = searchQuery,
            isLoading = isLoading,
            loadNextPage = loadNextPage,
            onClearQuery = onClearQuery,
            onUpdateQuery = onUpdateQuery,
            onSearch = onSearch
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    navController: NavController,
    personState: StateFlow<Response<PersonResponse>>,
    searchQuery: String,
    isLoading: () -> Boolean,
    loadNextPage: () -> Unit,
    onClearQuery: () -> Unit,
    onUpdateQuery: (String) -> Unit,
    onSearch: (Boolean) -> Unit,
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
        var isSearchActive by remember { mutableStateOf(false) }
        // Animation
        val transition = updateTransition(targetState = isSearchActive, label = "SearchTransition")
        val titleOpacity by transition.animateFloatValue { if (!it) 1f else 0f }
        val searchFieldOpacity by transition.animateFloatValue { if (it) 1f else 0f }
        val searchFieldOffset by transition.animateDpValue { if (it) 0.dp else 50.dp }
        val navIconOpacity by transition.animateFloatValue { if (!it) 1f else 0f }
        val actionIconOpacity by transition.animateFloatValue { if (!it) 1f else 0f }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!isSearchActive) {
                                // Title
                                Text(
                                    text = stringResource(R.string.title_popular_actor),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp,
                                        color = AppColors.TextPrimary
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .alpha(titleOpacity)
                                )
                            } else {
                                // Search field
                                TextField(
                                    value = searchQuery,
                                    onValueChange = {
                                        onUpdateQuery(it)
                                        onSearch(true)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(AppColors.Primary)
                                        .padding(end = 10.dp)
                                        .border(
                                            width = 1.dp,
                                            color = AppColors.TextPrimary,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .alpha(searchFieldOpacity)
                                        .offset(y = searchFieldOffset),
                                    textStyle = TextStyle(color = AppColors.TextPrimary),
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.search_label),
                                            color = AppColors.TextPrimary.copy(alpha = 0.6f)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_search),
                                            contentDescription = "Search Icon",
                                            tint = AppColors.TextPrimary,
                                            modifier = Modifier.size(25.dp)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            onClearQuery()
                                            isSearchActive = false
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Clear Search",
                                                tint = AppColors.TextPrimary,
                                                modifier = Modifier.size(25.dp)
                                            )
                                        }
                                    },
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = AppColors.Primary,
                                        unfocusedContainerColor = AppColors.Primary,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = AppColors.TextPrimary,
                                        focusedLabelColor = AppColors.TextPrimary,
                                        unfocusedLabelColor = Color.Gray
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        onSearch(true)
                                    })
                                )
                            }
                        }
                    },
                    actions = {
                        if (!isSearchActive) {
                            IconButton(
                                onClick = { isSearchActive = true },
                                modifier = Modifier.alpha(actionIconOpacity)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_search),
                                    modifier = Modifier.size(25.dp),
                                    contentDescription = "Search",
                                    tint = AppColors.TextPrimary
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        if (!isSearchActive) {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier.alpha(navIconOpacity)
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = AppColors.TextPrimary
                                )
                            }
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
                    is Response.Error -> ErrorMessage(
                        (state as Response.Error).message ?: stringResource(
                            R.string.error_something_went_wrong
                        )
                    )

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
                                        onClearQuery()
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