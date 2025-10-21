package com.app.imagerandom.presentation.view.person

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.imagerandom.domain.model.PersonDetail
import com.app.imagerandom.presentation.navigation.Screen
import com.app.imagerandom.presentation.ui.AppColors
import com.app.imagerandom.presentation.viewmodel.PersonDetailViewModel
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.MovieCast
import com.app.imagerandom.domain.model.MovieCredits
import com.app.imagerandom.domain.model.TvCast
import com.app.imagerandom.domain.model.TvCredits
import com.app.imagerandom.presentation.view.custom_view.ErrorState
import com.app.imagerandom.presentation.view.custom_view.PersonDetailContent
import com.app.imagerandom.presentation.view.custom_view.PersonDetailShimmer
import com.app.imagerandom.presentation.view.home.navigateToHome

fun NavController.navigateToPersonDetail(personId: Int) {
    navigate("${Screen.PERSON_DETAIL}?person_id=$personId")
}

fun NavGraphBuilder.personDetailScreen(navController: NavController) {
    composable(
        route = "${Screen.PERSON_DETAIL}?person_id={person_id}",
        arguments = listOf(
            navArgument("person_id") { defaultValue = 2049994 }
        )
    ) {
        val viewModel = hiltViewModel<PersonDetailViewModel>()
        val personState by viewModel.personDetail.collectAsState()
        val personId = it.arguments?.getInt("person_id") ?: 2049994

        LaunchedEffect(personId) {
            viewModel.loadPersonDetail(personId)
        }

        PersonDetailScreen(
            navController = navController,
            personState = personState,
            onRetry = { viewModel.loadPersonDetail(personId) }
        )
    }
}

@Composable
fun PersonDetailScreen(
    navController: NavController,
    personState: Response<PersonDetail>,
    onRetry: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = AppColors.Primary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppColors.Primary)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppColors.Primary)
                    .border(1.dp, AppColors.Accent, CircleShape)
                    .clickable { navController.navigateToHome() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Đóng",
                    tint = AppColors.TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            when (personState) {
                is Response.Loading -> PersonDetailShimmer()
                is Response.Error -> ErrorState(
                    message = personState.message ?: "Lỗi không xác định", onRetry
                )

                is Response.Success -> {
                    val data = personState.data ?: PersonDetail()
                    PersonDetailContent(navController, data, scrollState)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonDetailScreenPreview() {
    val navController = rememberNavController()

    val mockPerson = PersonDetail(
        id = 1,
        name = "Nguyen Duc Thanh Nhan",
        profilePath = null,
        knownForDepartment = "Acting",
        birthday = "2003-07-15",
        placeOfBirth = "Hue, Vietnam",
        popularity = 98.5,
        biography = "Nguyen Duc Thanh Nhan is an Vietnamese actor and producer. Known for his roles in blockbuster films...",
        movieCredits = MovieCredits(
            cast = listOf(
                MovieCast(title = "Iron Man", posterPath = null, voteAverage = 8.5),
                MovieCast(title = "Sherlock Holmes", posterPath = null, voteAverage = 7.9)
            )
        ),
        tvCredits = TvCredits(
            cast = listOf(
                TvCast(name = "Ally McBeal", posterPath = null, voteAverage = 7.0)
            )
        )
    )

    PersonDetailScreen(
        navController = navController,
        personState = Response.Success(mockPerson),
        onRetry = {}
    )
}